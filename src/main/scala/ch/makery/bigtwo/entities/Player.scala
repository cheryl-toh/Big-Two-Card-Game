package ch.makery.bigtwo.entities

import scala.collection.mutable.ListBuffer

class Player (playerIDS: Int){

  private val playerID = playerIDS
  private val hand: ListBuffer[Card] = ListBuffer.empty[Card]
  private var selectCard: ListBuffer[Card] = ListBuffer.empty[Card]
  private var isTurn: Boolean = false

  def getPlayerID(): Int = {
    playerID
  }

  def addCardToHand(cardToAdd: Card): Unit = {
    hand += cardToAdd
  }

  def showHand(): List[Card] = {
    hand.toList
  }

  def addSelectedCard(selectedCard: Card): Unit = {
    selectCard += selectedCard
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
}
