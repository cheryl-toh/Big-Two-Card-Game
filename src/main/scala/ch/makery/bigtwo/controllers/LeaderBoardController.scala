package ch.makery.bigtwo.controllers

import ch.makery.bigtwo.entities.Player
import javafx.collections.{FXCollections, ObservableList}
import javafx.fxml.FXML
import scalafx.event.ActionEvent
import scalafx.scene.control.Label
import scalafxml.core.macros.sfxml
import ch.makery.bigtwo.Main
import ch.makery.bigtwo.util.PlaySound

@sfxml
class LeaderBoardController (@FXML var numberOne: Label,
                             @FXML var numberTwo: Label,
                             @FXML var numberThree: Label,
                             @FXML var numberFour: Label) {


  // Method to update leaderboard
  def updateLeaderboard(players: List[Player]): Unit = {

    // Sort the players based on hand length in descending order
    val sortedPlayers = players.sortBy(_.showHand().length)

    // Create an observable list to store the player names and hand lengths
    val leaderboardData: ObservableList[String] = FXCollections.observableArrayList()

    for (player <- sortedPlayers) {
      // Add player names and hand lengths to the observable list in descending order
      leaderboardData.add(s"Player ${player.getPlayerID()}")

    }

    // Update the labels with the leaderboard data
    numberOne.text = leaderboardData.get(0)
    numberTwo.text = leaderboardData.get(1)
    numberThree.text = leaderboardData.get(2)
    numberFour.text = leaderboardData.get(3)

  }

  // Method to handle again buttun pressed
  def handleAgain(action: ActionEvent): Unit = {

    //play button clicked sound
    val file = getClass.getResource("/sounds/Click.wav")
    PlaySound.playSoundEffect(file)

    // start new game
    Main.showGameScene()

  }

  //Method to handle quit button clicked
  def handleQuit(action: ActionEvent): Unit = {

    //play button clicked sound
    val file = getClass.getResource("/sounds/Click.wav")
    PlaySound.playSoundEffect(file)

    // show main menu
    Main.showMainMenuScene()

  }
}
