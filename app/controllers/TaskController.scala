package controllers

import com.google.inject.Inject
import dao.TaskDAO
import models.Task
import play.api.libs.json.{JsError, JsSuccess, Json}
import play.api.mvc.{AbstractController, ControllerComponents}
import play.api.Logger

import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by alexmartin on 10/28/17.
  */
class TaskController @Inject()(cc: ControllerComponents, taskDao: TaskDAO)
                             (implicit executionContext: ExecutionContext) extends AbstractController(cc) {

  import models.JsonConverters._

  def getAllTasks = Action.async { implicit request =>
    Logger.debug("Retrieving all tasks")
    val response: Future[Seq[Task]] = taskDao.all()
    response.map {
      results => {
        Ok(Json.toJson(results))
      }
    }
  }

  def getTaskForUser(username: String) = Action.async(parse.default) { implicit request =>
    Logger.debug("Retrieving tasks for " + username)
    taskDao.getTasksForUser(username).map {
      result => {
        Ok(Json.toJson(result))
      }
    }
  }

  def createTask = Action.async(parse.json) { implicit request =>
    Logger.debug("Creating a new task")
    request.body.validate[Task] match {
      case task: JsSuccess[Task] => {
        taskDao.createTask(task.get).map{
          response => {
            if(response.isLeft){
              BadRequest(Json.toJson(response.left.get))
            } else {
              Ok(Json.toJson(response.right.get))
            }
          }
        }
      }
      case JsError(e) => {
        Future(BadRequest(JsError.toJson(e)))
      }
    }
  }

}
