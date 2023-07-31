package ch.makery.bigtwo.controllers

import ch.makery.bigtwo.Main
import ch.makery.bigtwo.util.PlaySound
import javafx.animation.Interpolator
import javafx.fxml.FXML
import scalafx.animation.{RotateTransition, ScaleTransition}
import scalafx.event.ActionEvent
import scalafx.scene.image.ImageView
import scalafx.scene.transform.Rotate
import scalafx.util.Duration
import scalafxml.core.macros.sfxml

@sfxml
class MainMenuController(@FXML private val MainMenuBackground: ImageView,
                         @FXML private val logo: ImageView) {

  //starting animation
  def initialize(): Unit = {

    // start background rotation and logo zoom in
    startBackgroundRotation()
    zoomInLogo()

  }

  // Method to set zoom in transition to logo
  private def zoomInLogo(): Unit = {

    // initialize scale transition effect
    val zoomIn = new ScaleTransition()

    // zoom in details
    zoomIn.setNode(logo)
    zoomIn.setDuration(Duration(500))
    zoomIn.cycleCount = 1
    zoomIn.setFromX(2.5)
    zoomIn.setFromY(2.5)
    zoomIn.setToX(1.0)
    zoomIn.setToY(1.0)
    zoomIn.setInterpolator(Interpolator.LINEAR)
    zoomIn.play()

    // play zoom in sound effect
    val file = getClass.getResource("/sounds/LogoIn.wav")
    PlaySound.playSoundEffect(file)

  }

  // Method to rotate background image
  private def startBackgroundRotation(): Unit = {

    // initialize rotating effect
    val rotate = new Rotate(0, MainMenuBackground.boundsInLocal().getWidth / 2, MainMenuBackground.boundsInLocal().getHeight / 2)

    // add transformation to background
    MainMenuBackground.getTransforms.add(rotate)

    // initialize rotate transition
    val rotateTransition = new RotateTransition()

    // rotate transition details
    rotateTransition.setNode(MainMenuBackground)
    rotateTransition.setDuration(Duration(10000))
    rotateTransition.setInterpolator(Interpolator.LINEAR)
    rotateTransition.byAngle = 360 // Rotate by 360 degrees
    rotateTransition.cycleCount = javafx.animation.Animation.INDEFINITE

    // start rotation
    rotateTransition.play()

  }

  // Method to handle start button clicked
  def handleStartGame(action: ActionEvent): Unit = {

    // play button click sound
    val file = getClass.getResource("/sounds/Click.wav")
    PlaySound.playSoundEffect(file)

    //start new game
    Main.showGameScene()

  }

  // Method to handle how to play button clicked
  def handleTutorialButton(action: ActionEvent): Unit = {

    //play button clicked sound
    val file = getClass.getResource("/sounds/Click.wav")
    PlaySound.playSoundEffect(file)

    // stop background music
    PlaySound.stopBackgroundMusic()

    // show video dialog
    Main.showHowToPlayDialog()

  }
}
