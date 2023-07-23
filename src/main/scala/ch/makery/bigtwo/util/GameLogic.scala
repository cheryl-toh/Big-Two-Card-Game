package ch.makery.bigtwo.util

import ch.makery.bigtwo.entities.Game
import scalafx.animation.AnimationTimer

object GameLogic {
  var game:Game = _
  var gameInProgress = false

  def initializeGame(game: Game): Unit = {
    this.game = game
    gameInProgress = true

    game.startGame()
    startGameLoop()
    println("loop started")
  }

  def handlePlayerTurns(): Unit = {
    val currentPlayer = game.getCurrentPlayer()

    if(!currentPlayer.getTurn()){
      game.moveToNextTurn()
    }
  }

  def endGame() = ???

  def startGameLoop(): Unit = {
    val gameLoop: AnimationTimer = AnimationTimer { deltaTime =>
      if (gameInProgress) {

        handlePlayerTurns()

        if(game.gamefinished){
          gameInProgress = false
          println("loop stopped")
        }

      }
    }
    gameLoop.start()
  }

}
