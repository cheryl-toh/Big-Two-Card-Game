package ch.makery.bigtwo.entities

import scalafx.scene.image.Image

import scala.util.Random

class Deck {

  //list to store all 52 cards
  private var cards: List[Card] = Nil

  //initialize card objects
  def initializeDeck(): Unit = {
    val ranks = 1 to 13
    val suits = 1 to 4

    cards = (for {
      suit <- suits
      rank <- ranks
      cardImage = new Image(s"/Images/cards/${rank}_${suit}.png")
    } yield new Card(rank, suit, cardImage)).toList


  }

  //shuffle deck
  def shuffle(): Unit = {
    cards = Random.shuffle(cards)
  }

  //draw top card of the deck
  def drawCard(): Option[Card] = {
    cards match {
      case Nil => None
      case head :: tail =>
        cards = tail
        Some(head)
    }
  }

  def getCards(): List[Card] = {
    cards
  }
}
