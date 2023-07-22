package ch.makery.bigtwo.controllers

import ch.makery.bigtwo.Main
import ch.makery.bigtwo.entities.Game
import scalafx.event.ActionEvent
import scalafxml.core.macros.sfxml

@sfxml
class GameController {
  // Define a variable to hold the Game instance
  private var game: Game = _

  // Method to initialize the Game instance
  def initGame(game: Game): Unit = {
    this.game = game
    // Here you can do any setup or display logic related to the game
    // For example, you might display the current player's hand, etc.
  }

  def handleBack(action: ActionEvent): Unit = {
    Main.showMainMenuScene()
  }

}
