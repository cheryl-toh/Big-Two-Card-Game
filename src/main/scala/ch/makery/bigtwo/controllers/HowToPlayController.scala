package ch.makery.bigtwo.controllers

import ch.makery.bigtwo.Main
import javafx.fxml.FXML
import scalafx.event.ActionEvent
import scalafx.scene.control.Slider
import scalafx.Includes._
import scalafx.scene.layout.AnchorPane
import scalafx.scene.media.{Media, MediaPlayer, MediaView}
import scalafx.stage.Stage
import scalafxml.core.macros.sfxml

import scala.reflect.io.File

@sfxml
class HowToPlayController(@FXML var tutorial: MediaView,
                          @FXML var root: AnchorPane) {

  private var stage: Stage = _
  var media: Media = _
  var mediaPlayer: MediaPlayer = _

  def setStage(stage: Stage): Unit = {
    this.stage = stage
  }

  def initialize(): Unit = {
    val resourceUrl = getClass.getResource("/Video/temp.mp4")
    println("Resource URL: " + resourceUrl)
    if (resourceUrl != null) {
      media = new Media(resourceUrl.toExternalForm)
      println("Media: " + media)
      mediaPlayer = new MediaPlayer(media)
      tutorial.setMediaPlayer(mediaPlayer)

      mediaPlayer.play()
    } else {
      println("Resource not found: Video/temp.mp4")
    }
  }

  def handlePause(action: ActionEvent): Unit = {
    // This method will be called when the Pause button is clicked
    val mediaPlayer = tutorial.mediaPlayer.value
    mediaPlayer.pause()
  }

  def handlePlay(action: ActionEvent): Unit = {
    // This method will be called when the Play button is clicked
    val mediaPlayer = tutorial.mediaPlayer.value
    mediaPlayer.play()
  }

  def handleQuit(action: ActionEvent): Unit = {
    Main.showMainMenuScene()
    root.getScene.getWindow.hide()
  }

}
