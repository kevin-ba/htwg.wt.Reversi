package controllers

import javax.inject._

import play.api.mvc._
import de.htwg.se.reversi.Reversi
import de.htwg.se.reversi.controller.controllerComponent.{GameStatus, CellChanged, GridSizeChanged}
import play.api.libs.json.{JsNumber, Json}
import play.api.libs.streams.ActorFlow
import akka.actor.ActorSystem
import akka.stream.Materializer
import akka.actor._

import scala.swing.Reactor

@Singleton
class ReversiController @Inject()(cc: ControllerComponents) (implicit system: ActorSystem, mat: Materializer) extends AbstractController(cc) {
  val gameController = Reversi.controller
  def message = GameStatus.message(gameController.gameStatus)
  def reversiAsText =  gameController.gridToString + message

  def about= Action {
    Ok(views.html.index())
  }

  def reversi = Action {
    Ok(views.html.reversi(gameController, message))
  }

  def reversiPolymer = Action {
    Ok(views.html.reversiPolymer())
  }

  def newGrid = Action {
    gameController.createNewGrid
    Ok(views.html.reversi(gameController, message))
  }

  def resize(size:Int)= Action {
    gameController.resize(size)
    Ok(views.html.reversi(gameController, message))
  }

  def set(row: Int, col: Int) = Action {
    gameController.set(row,col,gameController.getActivePlayer())
    Ok(views.html.reversi(gameController, message))
  }

  def save() = Action {
    gameController.save()
    Ok(views.html.reversi(gameController, message))
  }

  def load() = Action {
    gameController.load()
    Ok(views.html.reversi(gameController, message))
  }

  def undo() = Action {
    gameController.undo
    Ok(views.html.reversi(gameController, message))
  }

  def redo() = Action {
    gameController.redo
    Ok(views.html.reversi(gameController, message))
  }

  def gridToJson = Action {
    Ok(toJson)
  }

  def quit = Action {
    System.exit(0)
    Ok(views.html.index())
  }

  def toJson = {
    val size = gameController.gridSize;
    val activePlayer = gameController.getActivePlayer();
    Json.obj(
      "grid" -> Json.obj(
        "size" -> JsNumber(size),
        "activePlayer" -> JsNumber(activePlayer),
        "cells" -> Json.toJson(
          for {row <- 0 until size;
               col <- 0 until size} yield {
            Json.obj(
              "row" -> row,
              "col" -> col,
              "cell" -> Json.toJson(gameController.cell(row, col).value))
          }
        )
      )
    )
  }

  def socket = WebSocket.accept[String, String] { request =>
    ActorFlow.actorRef { out =>
      println("Connect received")
      ReversiWebSocketActorFactory.create(out)
    }
  }

  object ReversiWebSocketActorFactory {
    def create(out: ActorRef) = {
      Props(new ReversiWebSocketActor(out))
    }
  }

  class ReversiWebSocketActor(out: ActorRef) extends Actor with Reactor{
    listenTo(gameController)

    def receive = {
      case msg: String =>
        out ! (toJson.toString)
        println("Sent Json to Client"+ msg)
    }

    reactions += {
      case event: GridSizeChanged => sendJsonToClient
      case event: CellChanged     => sendJsonToClient
    }

    def sendJsonToClient = {
      println("Received event from Controller")
      out ! (toJson.toString)
    }

  }

}