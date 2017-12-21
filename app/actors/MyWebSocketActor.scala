package actors

import akka.actor._

object MyWebSocketActor {
  def props(out: ActorRef) = {
    println("Object")
    Props(new MyWebSocketActor(out))
  }
}

class MyWebSocketActor(out: ActorRef) extends Actor {
  def receive = {
    case msg: String => {
      println("String")
      out ! ("I received your message: " + msg)
    }
    case _ => println("Wildcard")
  }
}
