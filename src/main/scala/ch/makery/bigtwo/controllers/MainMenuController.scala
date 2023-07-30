package ch.makery.bigtwo.controllers

import ch.makery.bigtwo.Main
import ch.makery.bigtwo.entities.Game
import ch.makery.bigtwo.util.PlaySound
import javafx.animation.{Interpolator, TranslateTransition}
import javafx.fxml.FXML
import scalafx.animation.{RotateTransition, ScaleTransition}
import scalafx.event.ActionEvent
import scalafx.scene.Scene
import scalafx.scene.image.ImageView
import scalafx.scene.transform.Rotate
import scalafx.util.Duration
import scalafxml.core.macros.sfxml
import scalafx.Includes._
import scalafx.scene.control.Button

@sfxml
class MainMenuController(@FXML private val MainMenuBackground: ImageView,
                         @FXML private val logo: ImageView,
                         @FXML private val startGameButton: Button) {

  //starting animation
  def initialize(): Unit = {
    startBackgroundRotation()
    zoomInLogo()
  }

  //zoom in logo
  def zoomInLogo(): Unit = {
    val zoomIn = new ScaleTransition()
    zoomIn.setNode(logo)
    zoomIn.setDuration(Duration(500))
    zoomIn.cycleCount = 1
    zoomIn.setFromX(2.5)
    zoomIn.setFromY(2.5)
    zoomIn.setToX(1.0)
    zoomIn.setToY(1.0)
    zoomIn.setInterpolator(Interpolator.LINEAR)
    zoomIn.play()
    val file = getClass.getResource("/sounds/LogoIn.wav")
    PlaySound.playSoundEffect(file)
  }

  //rotate background
  def startBackgroundRotation(): Unit = {
    val rotate = new Rotate(0, MainMenuBackground.boundsInLocal().getWidth / 2, MainMenuBackground.boundsInLocal().getHeight / 2)
    MainMenuBackground.getTransforms.add(rotate)

    val rotateTransition = new RotateTransition()
    rotateTransition.setNode(MainMenuBackground)
    rotateTransition.setDuration(Duration(10000))
    rotateTransition.setInterpolator(Interpolator.LINEAR)
    rotateTransition.byAngle = 360 // Rotate by 360 degrees
    rotateTransition.cycleCount = javafx.animation.Animation.INDEFINITE
    rotateTransition.play()
  }

  def handleStartGame(action: ActionEvent): Unit = {
    val file = getClass.getResource("/sounds/Click.wav")
    PlaySound.playSoundEffect(file)
    Main.showGameScene()
  }

  def handleTutorialButton(action: ActionEvent): Unit = {
    val file = getClass.getResource("/sounds/Click.wav")
    PlaySound.playSoundEffect(file)
    PlaySound.stopBackgroundMusic()
    Main.showHowToPlayDialog()
  }


}
