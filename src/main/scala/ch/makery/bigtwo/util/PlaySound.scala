package ch.makery.bigtwo.util

import scalafx.scene.media.{Media, MediaPlayer}

import java.io.File
import java.net.URL
import javax.sound.sampled.{AudioSystem, Clip}


object PlaySound {
  private var backgroundMusicClip: Option[Clip] = None

  def playBackgroundMusic(filePath: URL, volume: Double = 0.5, loop: Boolean = true): Unit = {
    stopBackgroundMusic()

    try {
      val audioInput = AudioSystem.getAudioInputStream(filePath)
      val clip = AudioSystem.getClip
      clip.open(audioInput)
      clip.start
      clip.loop(Clip.LOOP_CONTINUOUSLY)
      backgroundMusicClip = Some(clip)
    } catch {
      case ex: Exception =>
        ex.printStackTrace()
    }
  }

  def stopBackgroundMusic(): Unit = {
    backgroundMusicClip.foreach { clip =>
      if (clip.isRunning) {
        clip.stop()
      }
      clip.close()
    }
    backgroundMusicClip = None
  }


  def playSoundEffect(filePath: URL): Unit = {
    try {
      val audioInput = AudioSystem.getAudioInputStream(filePath)
      val clip = AudioSystem.getClip
      clip.open(audioInput)
      clip.start
    } catch {
      case ex: Exception =>
        ex.printStackTrace()
    }
  }

}
