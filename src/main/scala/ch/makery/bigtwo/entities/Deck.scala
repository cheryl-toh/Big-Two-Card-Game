package ch.makery.bigtwo.entities

import scalafx.scene.image.Image

import scala.util.Random

class Deck {

  //list to store all 52 cards
  private var cards: List[Card] = Nil

  // Method to initialize card objects
  def initializeDeck(): Unit = {

    // initialize rank and suit range
    val ranks = 1 to 13
    val suits = 1 to 4

    // add all cards to deck
    cards = (for {
      suit <- suits
      rank <- ranks
      cardImage = new Image(s"/Images/cards/${rank}_${suit}.png")
    } yield new Card(rank, suit, cardImage)).toList


  }

  // Method to shuffle deck
  def shuffle(): Unit = {
    // Separate the card "1_1" from the rest of the deck
    val card1 = cards.head
    val otherCards = cards.tail

    // Shuffle the remaining cards
    val shuffledOtherCards = Random.shuffle(otherCards)

    // Combine the card "1_1" and the shuffled remaining cards
    cards = card1 :: shuffledOtherCards
  }

  // Method to draw top card of the deck
  def drawCard(): Option[Card] = {
    cards match {
      case Nil => None
      case head :: tail =>
        cards = tail
        Some(head)
    }
  }

  // getter
  def getCards(): List[Card] = {
    cards
  }
}
