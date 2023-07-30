package ch.makery.bigtwo

import ch.makery.bigtwo.controllers.{GameController, HowToPlayController, LeaderBoardController, MainMenuController}
import ch.makery.bigtwo.entities.Player
import ch.makery.bigtwo.util.PlaySound
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafxml.core.{FXMLLoader, NoDependencyResolver}
import scalafx.Includes._
import scalafx.scene.Scene
import javafx.{scene => jfxs}
import scalafx.scene.layout.AnchorPane
import scalafx.scene.text.Font
import scalafx.stage.{Modality, Stage}

import java.io.File

object Main extends JFXApp{

  // Load the custom font
  Font.loadFont(getClass.getResource("/fonts/Warungasem-rgO1O.ttf").toExternalForm, 24)

  stage = new PrimaryStage{
    title = "Big Two"
    maximized = true
  }
  showMainMenuScene()

  //function for switching to game scene
  def showGameScene(): Unit = {
    val resource = getClass.getResource("view/GamePage.fxml")
    val loader = new FXMLLoader(resource, NoDependencyResolver)
    loader.load()
    val roots2 = loader.getRoot[jfxs.Parent]
    val control = loader.getController[GameController#Controller]

    control.startGame()

    // Create a new Scene object with the gameRoot as the root node
    val gameScene = new Scene(roots2)

    // Set the game scene to the primary stage to display it
    stage.setScene(gameScene)
    val file = getClass.getResource("/sounds/GameMusic.wav")
    PlaySound.playBackgroundMusic(file)
  }

  //function for switching to main menu scene
  def showMainMenuScene(): Unit = {

    val resource = getClass.getResource("view/GameMenuPage.fxml")
    val loader = new FXMLLoader(resource, NoDependencyResolver)
    loader.load()
    val roots = loader.getRoot[jfxs.Parent]
    val control2 = loader.getController[MainMenuController#Controller]
    control2.initialize()

    // Create a new Scene object with the gameRoot as the root node
    val gameScene = new Scene(roots)
    gameScene.getStylesheets.add(getClass.getResource("/style/style.css").toExternalForm())

    // Set the game scene to the primary stage to display it
    stage.setScene(gameScene)

    val file = getClass.getResource("/sounds/MenuMusic.wav")
    PlaySound.playBackgroundMusic(file)
  }

  //function for switching to main menu scene
  def showLeaderBoardScene(players: List[Player]): Unit = {

    val resource = getClass.getResource("view/LeaderboardPage.fxml")
    val loader = new FXMLLoader(resource, NoDependencyResolver)
    loader.load()
    val roots = loader.getRoot[jfxs.Parent]
    val control2 = loader.getController[LeaderBoardController#Controller]
    control2.updateLeaderboard(players)


    // Create a new Scene object with the gameRoot as the root node
    val gameScene = new Scene(roots)


    // Set the game scene to the primary stage to display it
    stage.setScene(gameScene)
    val file = getClass.getResource("/sounds/Win.wav")
    PlaySound.playSoundEffect(file)

    val file2 = getClass.getResource("/sounds/MenuMusic.wav")
    PlaySound.playBackgroundMusic(file2)
  }

  def showHowToPlayDialog(): Unit = {
    val resource = getClass.getResource("view/HowToPlayDialog.fxml")
    val loader = new FXMLLoader(resource, NoDependencyResolver)
    loader.load();
    val roots2 = loader.getRoot[jfxs.Parent]
    val control = loader.getController[HowToPlayController#Controller]
    control.initialize()

    val dialog = new Stage() {
      initModality(Modality.ApplicationModal)
      initOwner(stage)
      scene = new Scene {
        root = roots2
      }
    }

    dialog.showAndWait()
    //control.okClicked
  }


}
