package ch.makery.bigtwo.entities

import scalafx.scene.image.Image


class Card (rankS: Int, suitS: Int, image: Image){

  // initialize attributes of a card
  private val rank = rankS
  private val suit = suitS
  private val cardImage = image

  // getters
  def getRank(): Int = {
    rank
  }

  def getSuit(): Int = {
    suit
  }

  def getImage(): Image = {
    cardImage
  }
}
