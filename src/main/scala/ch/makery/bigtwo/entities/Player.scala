package ch.makery.bigtwo.entities

import scala.collection.mutable.ListBuffer

class Player (playerIDS: Int){

  // Initialize attributes for player
  private val playerID = playerIDS
  val hand: ListBuffer[Card] = ListBuffer.empty[Card]
  private var selectCard: ListBuffer[Card] = ListBuffer.empty[Card]
  private var isTurn: Boolean = false
  private var hasPassed: Boolean = false
  private var dealtCards: ListBuffer[Card] = ListBuffer.empty[Card]

  // Method to get player ID
  def getPlayerID(): Int = {
    playerID
  }

  // Method to add a card to player's hand
  def addCardToHand(cardToAdd: Card): Unit = {
    hand += cardToAdd
  }

  // Method to get player's hand
  def showHand(): List[Card] = {

    // sort cards of hand in ascending order
    hand.toList.sortWith((card1, card2) => {
      if (card1.getRank < card2.getRank()) {
        true
      } else if (card1.getRank() == card2.getRank()) {
        card1.getSuit() < card2.getSuit()
      } else {
        false
      }
    })
  }

  // Method to add card to selected card list
  def addSelectedCard(selectedCard: Card): Unit = {
    selectCard += selectedCard
  }

  // method to remove a card from selected card list
  def deselectCard(card: Card): Unit = {
    selectCard -= card
  }

  // getter for selected card list
  def getSelectCard(): List[Card] = {
    selectCard.toList
  }

  // empty selected card list
  def clearSelectedCard(): Unit = {
    selectCard = ListBuffer.empty[Card]
  }

  // set player's turn
  def setTurn(turn: Boolean): Unit = {
    isTurn = turn
  }

  // get player's turn boolean
  def getTurn(): Boolean = {
    isTurn
  }

  // get player passed status
  def getHasPassed(): Boolean = {
    hasPassed
  }

  // set player's passed status
  def setHasPassed(passed: Boolean): Unit = {
    hasPassed = passed
  }

  // get dealt cards of player
  def getDealtCards(): List[Card] = {
    dealtCards.toList
  }

  // set player's dealt cards
  def setDealtCard(cards: ListBuffer[Card]): Unit = {
    dealtCards = cards
  }

  // empty dealt card list
  def clearDealtCard(): Unit = {
    dealtCards = ListBuffer.empty[Card]
  }
}
