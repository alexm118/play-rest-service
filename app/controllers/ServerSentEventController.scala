package controllers

import javax.inject.{Inject, Singleton}

import akka.stream.scaladsl.Source
import dao.TaskDAO
import models.Task
import play.api.mvc.{AbstractController, Action, ControllerComponents}
import models.JsonConverters._
import play.api.http.ContentTypes
import play.api.libs.EventSource
import play.api.libs.json.{JsValue, Json}


@Singleton
class ServerSentEventController @Inject()(cc: ControllerComponents, taskDao: TaskDAO) extends AbstractController(cc){

  def taskSource: Source[JsValue, _] = {
    val source = Source.fromFuture(taskDao.all());
    source.map(task => Json.toJson(task))
  }

  def taskStream() = Action {
    Ok.chunked(taskSource via EventSource.flow).as(ContentTypes.EVENT_STREAM)
  }

}
