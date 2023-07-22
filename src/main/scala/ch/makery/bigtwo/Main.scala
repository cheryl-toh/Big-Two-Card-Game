package ch.makery.bigtwo

import ch.makery.bigtwo.controllers.{GameController, MainMenuController}
import ch.makery.bigtwo.util.PlaySound
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafxml.core.{FXMLLoader, NoDependencyResolver}
import scalafx.Includes._
import scalafx.scene.Scene
import javafx.{scene => jfxs}
import scalafx.scene.layout.AnchorPane
import scalafx.stage.{Modality, Stage}

import java.io.File

object Main extends JFXApp{


  stage = new PrimaryStage{
    title = "Big Two"
    maximized = true
  }
  showMainMenuScene()

  //function for switching to game scene
  def showGameScene(): Unit = {
    val resource = getClass.getResourceAsStream("view/GamePage.fxml")
    val loader = new FXMLLoader(null, NoDependencyResolver)
    loader.load(resource);
    val roots2 = loader.getRoot[jfxs.Parent]
    val control = loader.getController[GameController#Controller]


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
    loader.load();
    val roots = loader.getRoot[jfxs.Parent]
    val control2 = loader.getController[MainMenuController#Controller]
    control2.initialize()

    // Create a new Scene object with the gameRoot as the root node
    val gameScene = new Scene(roots)


    // Set the game scene to the primary stage to display it
    stage.setScene(gameScene)

    val file = getClass.getResource("/sounds/MenuMusic.wav")
    PlaySound.playBackgroundMusic(file)
  }


}
