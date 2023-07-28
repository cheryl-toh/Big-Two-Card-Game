package ch.makery.bigtwo.entities

import scala.collection.mutable.ListBuffer

class Player (playerIDS: Int){

  private val playerID = playerIDS
  val hand: ListBuffer[Card] = ListBuffer.empty[Card]
  private var selectCard: ListBuffer[Card] = ListBuffer.empty[Card]
  private var isTurn: Boolean = false
  private var hasPassed: Boolean = false
  private var dealtCards: ListBuffer[Card] = ListBuffer.empty[Card]

  def getPlayerID(): Int = {
    playerID
  }

  def addCardToHand(cardToAdd: Card): Unit = {
    hand += cardToAdd
  }

  def showHand(): List[Card] = {
    hand.toList.sortWith((card1, card2) => {
      if (card1.getRank() < card2.getRank()) {
        true
      } else if (card1.getRank() == card2.getRank()) {
        card1.getSuit() < card2.getSuit()
      } else {
        false
      }
    })
  }

  def addSelectedCard(selectedCard: Card): Unit = {
    selectCard += selectedCard
  }

  def deselectCard(card: Card): Unit = {
    selectCard -= card
  }

  def getSelectCard(): List[Card] = {
    selectCard.toList
  }

  def clearSelectedCard(): Unit = {
    selectCard = ListBuffer.empty[Card]
  }

  def setTurn(turn: Boolean): Unit = {
    isTurn = turn
  }

  def getTurn(): Boolean = {
    isTurn
  }

  def getHasPassed(): Boolean = {
    hasPassed
  }

  def setHasPassed(passed: Boolean): Unit = {
    hasPassed = passed
  }

  def getDealtCards(): List[Card] = {
    dealtCards.toList
  }

  def setDealtCard(cards: ListBuffer[Card]): Unit = {
    dealtCards = cards
  }

  def clearDealtCard(): Unit = {
    dealtCards = ListBuffer.empty[Card]
  }
}
