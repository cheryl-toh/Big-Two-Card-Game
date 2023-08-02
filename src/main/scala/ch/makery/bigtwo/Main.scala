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
import scalafx.stage.{Modality, Stage}

object Main extends JFXApp{

  // set primary stage
  stage = new PrimaryStage{
    title = "Big Two"
    maximized = true
  }

  // show main menu scene
  showMainMenuScene()


  // Method for switching to game scene
  def showGameScene(): Unit = {

    // initialize resource and loader
    val resource = getClass.getResource("view/GamePage.fxml")
    val loader = new FXMLLoader(resource, NoDependencyResolver)

    // load resources
    loader.load()

    // initialize root and controller
    val roots2 = loader.getRoot[jfxs.Parent]
    val control = loader.getController[GameController#Controller]

    // call start game method of controller
    control.startGame()

    // Create a new Scene object with the gameRoot as the root node
    val gameScene = new Scene(roots2)

    // Set the game scene to the primary stage to display it
    stage.setScene(gameScene)

    // play game background music
    val file = getClass.getResource("/sounds/GameMusic.wav")
    PlaySound.playBackgroundMusic(file)

  }


  // Method for switching to main menu scene
  def showMainMenuScene(): Unit = {

    // initialize resource and loader
    val resource = getClass.getResource("view/GameMenuPage.fxml")
    val loader = new FXMLLoader(resource, NoDependencyResolver)

    // load resources
    loader.load()

    // initialize root and controller
    val roots = loader.getRoot[jfxs.Parent]
    val control2 = loader.getController[MainMenuController#Controller]

    // call initialize method of controller
    control2.initialize()

    // Create a new Scene object with the gameRoot as the root node
    val gameScene = new Scene(roots)

    // Set the game scene to the primary stage to display it
    stage.setScene(gameScene)

    //play background music
    val file = getClass.getResource("/sounds/MenuMusic.wav")
    PlaySound.playBackgroundMusic(file)

  }


  // Method for switching to leaderboard scene
  def showLeaderBoardScene(players: List[Player]): Unit = {

    // initialize resource and loader
    val resource = getClass.getResource("view/LeaderboardPage.fxml")
    val loader = new FXMLLoader(resource, NoDependencyResolver)

    // load resources
    loader.load()

    // initialize root and controller
    val roots = loader.getRoot[jfxs.Parent]
    val control2 = loader.getController[LeaderBoardController#Controller]

    // call updateLeaderBoard method of controller
    control2.updateLeaderboard(players)


    // Create a new Scene object with the gameRoot as the root node
    val gameScene = new Scene(roots)


    // Set the game scene to the primary stage to display it
    stage.setScene(gameScene)
    val file = getClass.getResource("/sounds/Win.wav")
    PlaySound.playSoundEffect(file)

    // play background music
    val file2 = getClass.getResource("/sounds/MenuMusic.wav")
    PlaySound.playBackgroundMusic(file2)

  }


  // Method to show how to play dialog
  def showHowToPlayDialog(): Unit = {

    // initialize resource and loader
    val resource = getClass.getResource("view/HowToPlayDialog.fxml")
    val loader = new FXMLLoader(resource, NoDependencyResolver)

    // load resources
    loader.load();

    // initialize roots and controller
    val roots2 = loader.getRoot[jfxs.Parent]
    val control = loader.getController[HowToPlayController#Controller]

    // call initialize method of controller
    control.initialize()

    // set up stage for dialog
    val dialog = new Stage() {
      initModality(Modality.ApplicationModal)
      initOwner(stage)

      // Disable the standard window decorations (close button, etc.)
      initStyle(javafx.stage.StageStyle.UNDECORATED)

      // set up scene
      scene = new Scene {
        root = roots2
      }
    }

    // show dialog
    dialog.showAndWait()
  }
}
