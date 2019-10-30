package controllers

import javax.inject._

import play.api.mvc._
import de.htwg.se.reversi.Reversi
import de.htwg.se.reversi.controller.controllerComponent.GameStatus

@Singleton
class ReversiController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {
  val gameController = Reversi.controller
  def reversiAsText =  gameController.gridToString + GameStatus.message(gameController.gameStatus)

  def about= Action {
    Ok(views.html.index())
  }

  def reversi = Action {
    Ok(views.html.reversi(gameController))
  }

  def newGrid = Action {
    gameController.createNewGrid
    Ok(views.html.reversi(gameController))
  }

  def resize(size:Int)= Action {
    gameController.resize(size)
    Ok(views.html.reversi(gameController))
  }

  def set(row: Int, col: Int) = Action {
    gameController.set(row,col,gameController.getActivePlayer())
    Ok(views.html.reversi(gameController))
  }

  def enableBot() = Action {
    gameController.enableBot()
    Ok(views.html.reversi(gameController))
  }

  def disableBot() = Action {
    gameController.disableBot()
    Ok(views.html.reversi(gameController))
  }

  def save() = Action {
    gameController.save()
    Ok(views.html.reversi(gameController))
  }

  def load() = Action {
    gameController.load()
    Ok(views.html.reversi(gameController))
  }

  def undo() = Action {
    gameController.undo
    Ok(views.html.reversi(gameController))
  }

  def redo() = Action {
    gameController.redo
    Ok(views.html.reversi(gameController))
  }

  def quit() = Action {
    System.exit(0)
    Ok(views.html.reversi(gameController))
  }

}