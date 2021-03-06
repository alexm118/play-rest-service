package models

import play.api.libs.json.{Format, Json}

case class User(username: String, password: String, firstName: String, lastName: String, email: String)
case class Login(username: String, password: String)
case class Task(id: Option[Int] = None, username: String, task: String, description: String, status: String)

object JsonConverters {
  implicit val userFormat: Format[User] = Json.format[User]
  implicit val loginFormat: Format[Login] = Json.format[Login]
  implicit val taskWrite: Format[Task] = Json.format[Task]
}