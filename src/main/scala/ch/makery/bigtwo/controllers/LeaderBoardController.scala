package ch.makery.bigtwo.controllers

import ch.makery.bigtwo.entities.{Game, Player}
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

  def updateLeaderboard(players: List[Player]): Unit = {
    // Sort the players based on hand length in descending order
    val sortedPlayers = players.sortBy(_.showHand().length)

    // Create an observable list to store the player names and hand lengths
    val leaderboardData: ObservableList[String] = FXCollections.observableArrayList()

    // Add player names and hand lengths to the observable list in descending order
    for (player <- sortedPlayers) {
      leaderboardData.add(s"Player ${player.getPlayerID()}")
    }

    // Update the labels with the leaderboard data
    numberOne.text = leaderboardData.get(0)
    numberTwo.text = leaderboardData.get(1)
    numberThree.text = leaderboardData.get(2)
    numberFour.text = leaderboardData.get(3)
  }

  def handleAgain(action: ActionEvent): Unit = {
    val file = getClass.getResource("/sounds/Click.wav")
    PlaySound.playSoundEffect(file)

    Main.showGameScene()
  }


  def handleQuit(action: ActionEvent): Unit = {
    val file = getClass.getResource("/sounds/Click.wav")
    PlaySound.playSoundEffect(file)

    Main.showMainMenuScene()
  }

}
