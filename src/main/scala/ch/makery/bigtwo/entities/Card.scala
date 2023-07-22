package ch.makery.bigtwo.entities
import scalafx.beans.property.ObjectProperty
import scalafx.scene.image.Image


class Card (rankS: Int, suitS: Int, image: Image){
  private val rank = rankS
  private val suit = suitS
  private val cardImage = image

  def getRank(): Int = {
    rank
  }

  def getSuit(): Int = {
    suit
  }

  def getImage(): Unit = {
    cardImage
  }
}
