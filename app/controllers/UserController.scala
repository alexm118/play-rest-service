package controllers

import javax.inject.Singleton

import com.google.inject.Inject
import models.{Login, User}
import play.api.libs.json.{JsError, JsSuccess, Json}
import play.api.mvc._
import dao.UserDAO
import play.api.Logger

import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by alexmartin on 10/4/17.
  */
@Singleton
class UserController @Inject()(cc: ControllerComponents, userDAO: UserDAO)
                              (implicit executionContext: ExecutionContext) extends AbstractController(cc) {

  import models.JsonConverters._

  def saveUser = Action(parse.json) { implicit request =>
    Logger.debug("Attempting to save user")
    request.body.validate[User].map {
      case user => {
        Logger.debug(user.toString)
        userDAO.insert(user)
        Ok(Json.toJson(user))
      }
    }.recoverTotal{
      e => BadRequest(JsError.toJson(e))
    }
  }

  def getUsers = Action.async {
    val response: Future[Seq[User]] = userDAO.all()
    response.map {
      results => {
        Ok(Json.toJson(results))
      }
    }
  }

  def login = Action.async(parse.json) { implicit request =>
    Logger.debug("Attempting to Login")
    val loginResult = request.body.validate[Login]
    loginResult match {
      case login: JsSuccess[Login] => {
        Logger.debug(login.get.toString())
        userDAO.login(login.get).map {
          user => {
            if (user.nonEmpty) {
              Ok(Json.toJson(user))
            } else {
              BadRequest("Authentication Failed")
            }
          }
        }
      }
      case error: JsError => {
        Future(BadRequest(JsError.toJson(error)))
      }
    }
  }
}
