package controllers

import javax.inject._

import play.api.mvc._
import de.htwg.se.reversi.Reversi
import de.htwg.se.reversi.controller.controllerComponent.GameStatus
import play.api.libs.json.{JsNumber, JsValue, Json, Writes}

@Singleton
class ReversiController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {
  val gameController = Reversi.controller
  def message = GameStatus.message(gameController.gameStatus)
  def reversiAsText =  gameController.gridToString + message

  def about= Action {
    Ok(views.html.index())
  }

  def reversi = Action {
    Ok(views.html.reversi(gameController, message))
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
    Json.obj(
      "grid" -> Json.obj(
        "size" -> JsNumber(size),
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
}