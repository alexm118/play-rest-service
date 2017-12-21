package controllers
import play.api.mvc._
import javax.inject.Inject

import actors.MyWebSocketActor
import akka.actor.{ActorRef, ActorSystem}
import akka.stream.Materializer
import akka.stream.scaladsl.{Flow, Sink, Source}
import dao.TaskDAO
import models.Task
import play.api.libs.json.{JsSuccess, JsValue, Json}
import play.api.libs.streams.ActorFlow

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.concurrent.{ExecutionContext, Future}

class WebSocketController @Inject()(cc:ControllerComponents,
                                    taskDao: TaskDAO)
                                   (implicit system: ActorSystem,
                                    mat: Materializer,
                                    executionContext: ExecutionContext) extends AbstractController(cc) {

  import models.JsonConverters._

  def socket = WebSocket.accept[JsValue, JsValue] { request =>

    // Log events to the console
    val in = Sink.foreach[JsValue](task => {
      task.validate[Task] match {
        case validTask: JsSuccess[Task] => {
          taskDao.createTask(validTask.get)
        }
      }
    })

//    val out = Source.single(Json.toJson(Await.result(taskDao.all(), 10 seconds )))
    val out = Source.fromFuture(taskDao.all().map(tasks => Json.toJson(tasks)))

    // Send a single 'Hello!' message and then leave the socket open
//    val out = Source.single("Hello!").concat(Source.maybe)

    Flow.fromSinkAndSource(in, out)
  }

}
