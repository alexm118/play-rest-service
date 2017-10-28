package dao

import javax.inject.Inject

import models.Task
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile
import play.api.Logger
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

/**
  * Created by alexmartin on 10/28/17.
  */
class TaskDAO @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)
                       (implicit executionContext: ExecutionContext) extends HasDatabaseConfigProvider[JdbcProfile]{
  import profile.api._

  private val Tasks = TableQuery[TaskTable]

  def all(): Future[Seq[Task]] = db.run(Tasks.result)

  def createTask(task: Task): Future[Either[String, Task]] = {
    db.run((Tasks += task).asTry).map {
      case Success(_) => Right(task)
      case Failure(ex) => {
        Logger.error(ex.toString)
        Left("DB ERROR")
      }
    }
  }

  def getTasksForUser(username: String): Future[Seq[Task]] = {
    db.run(Tasks.filter(task => task.username === username).result)
  }

  private class TaskTable(tag: Tag) extends Table[Task](tag, "TASKS") {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def username = column[String]("username")
    def task = column[String]("task")
    def description = column[String]("description")
    def status = column[String]("status")
    def pk = primaryKey("id", id)

    def * = (id.?, username, task, description, status) <> (Task.tupled, Task.unapply)
  }

}
