package controllers

import javax.inject.Singleton

import com.google.inject.Inject
import models.User
import play.api.libs.json.{JsError, Json}
import play.api.mvc._
import dao.UserDAO

import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by alexmartin on 10/4/17.
  */
@Singleton
class UserController @Inject()(cc: ControllerComponents, userDAO: UserDAO)
                              (implicit executionContext: ExecutionContext) extends AbstractController(cc) {

  import models.JsonConverters._

  def saveUser = Action(parse.json) { implicit request =>
    request.body.validate[User].map {
      case user => {
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

  def createUserTable = Action {
    userDAO.create()
    Ok(Json.toJson("Done Creating Schema"))
  }

}
