package ch.makery.bigtwo.controllers

import ch.makery.bigtwo.Main
import ch.makery.bigtwo.util.PlaySound
import javafx.fxml.FXML
import scalafx.event.ActionEvent
import scalafx.scene.layout.AnchorPane
import scalafx.scene.media.{Media, MediaPlayer, MediaView}
import scalafxml.core.macros.sfxml

@sfxml
class HowToPlayController(@FXML var tutorial: MediaView,
                          @FXML var root: AnchorPane) {

  // Initialize media and media player
  var media: Media = _
  private var mediaPlayer: MediaPlayer = _

  // Method to play video
  def initialize(): Unit = {

    // get video to be played
    val resourceUrl = getClass.getResource("/Video/tutorial.mp4")

    // test line for video source
    println("Resource URL: " + resourceUrl)

    if (resourceUrl != null) {

      // set video as media
      media = new Media(resourceUrl.toExternalForm)

      // set media to media player
      mediaPlayer = new MediaPlayer(media)

      // set media player to scene media player element
      tutorial.setMediaPlayer(mediaPlayer)

      // play video
      mediaPlayer.play()

    } else {
      //print error message if video not found
      println("Resource not found: Video/tutorial.mp4")

    }
  }

  // Method to handle pause button clicked
  def handlePause(action: ActionEvent): Unit = {

    //play button clicked sound
    val file = getClass.getResource("/sounds/Click.wav")
    PlaySound.playSoundEffect(file)

    // pause video
    val mediaPlayer = tutorial.mediaPlayer.value
    mediaPlayer.pause()

  }

  // Method to handle play button clicked
  def handlePlay(action: ActionEvent): Unit = {

    //play button clicked sound
    val file = getClass.getResource("/sounds/Click.wav")
    PlaySound.playSoundEffect(file)

    // Play video
    val mediaPlayer = tutorial.mediaPlayer.value
    mediaPlayer.play()

  }

  // Method to handle quit button clicked
  def handleQuit(action: ActionEvent): Unit = {

    //play button clicked sound
    val file = getClass.getResource("/sounds/Click.wav")
    PlaySound.playSoundEffect(file)

    // show main menu scene and hide how to play window
    Main.showMainMenuScene()
    root.getScene.getWindow.hide()

  }
}
