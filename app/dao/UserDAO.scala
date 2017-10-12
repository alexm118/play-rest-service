package dao

import javax.inject.Inject

import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}

import scala.concurrent.{ExecutionContext, Future}
import slick.jdbc.JdbcProfile
import models.User

/**
  * Created by alexmartin on 10/4/17.
  */
class UserDAO @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)
                       (implicit executionContext: ExecutionContext) extends HasDatabaseConfigProvider[JdbcProfile]{
  import profile.api._

  private val Users = TableQuery[UserTable]

  def create(): Future[Unit] = db.run(Users.schema.create)

  def all(): Future[Seq[User]] = db.run(Users.result)

  def insert(user: User): Future[Unit] = db.run(Users += user).map{_ => ()}

  private class UserTable(tag: Tag) extends Table[User](tag, "USERS") {
    def username = column[String]("username")
    def password = column[String]("password")
    def firstname = column[String]("firstname")
    def lastname = column[String]("lastname")
    def email = column[String]("email")

    def * = (username, password, firstname, lastname, email) <> (User.tupled, User.unapply)
  }

}
