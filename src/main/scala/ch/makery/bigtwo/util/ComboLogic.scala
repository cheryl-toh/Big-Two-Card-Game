package ch.makery.bigtwo.util

import ch.makery.bigtwo.entities.Card

abstract class ComboLogic {
  // Abstract method to check if the selected cards form a specific combo
  def checkCombo(selectedCards: List[Card]): Boolean

  // Abstract method to get the name of the combo
  def comboType: String
}

object ComboLogic {

  // Object representing a single card combo
  case object Single extends ComboLogic {
    override def checkCombo(selectedCards: List[Card]): Boolean = selectedCards.length == 1
    override def comboType: String = "single"
  }

  // Object representing a double card combo
  case object Double extends ComboLogic {
    override def checkCombo(selectedCards: List[Card]): Boolean =
      selectedCards.length == 2 && selectedCards.forall(card => card.getRank() == selectedCards.head.getRank())
    override def comboType: String = "double"
  }

  // Object representing a triple card combo
  case object Triple extends ComboLogic {
    override def checkCombo(selectedCards: List[Card]): Boolean =
      selectedCards.length == 3 && selectedCards.forall(card => card.getRank() == selectedCards.head.getRank())
    override def comboType: String = "triple"
  }

  // Object representing a flush combo
  case object Flush extends ComboLogic {
    override def checkCombo(selectedCards: List[Card]): Boolean =
      selectedCards.length == 5 && isFlush(selectedCards)

    override def comboType: String = "flush"
  }

  // Object representing a straight combo
  case object Straight extends ComboLogic {
    override def checkCombo(selectedCards: List[Card]): Boolean =
      selectedCards.length == 5 && isStraight(selectedCards)

    override def comboType: String = "straight"
  }

  // Object representing a full house combo
  case object FullHouse extends ComboLogic {
    override def checkCombo(selectedCards: List[Card]): Boolean =
      selectedCards.length == 5 && isFullHouse(selectedCards)

    override def comboType: String = "full house"
  }

  // Object representing a straight flush combo
  case object StraightFlush extends ComboLogic {
    override def checkCombo(selectedCards: List[Card]): Boolean =
      selectedCards.length == 5 && isFlush(selectedCards) && isStraight(selectedCards)

    override def comboType: String = "straight flush"
  }

  // Method to check if cards form a flush combo
  private def isFlush(cards: List[Card]): Boolean = {

    // Check if all cards have the same suit
    val firstSuit = cards.head.getSuit()
    cards.forall(card => card.getSuit() == firstSuit)

  }

  // Method to check if cards form a straight combo
  private def isStraight(cards: List[Card]): Boolean = {

    // Sort the cards by rank
    val sortedCards = cards.sortBy(_.getRank())

    // Check if the ranks form a consecutive sequence
    sortedCards.indices.drop(1).forall(i => sortedCards(i).getRank() == sortedCards(i - 1).getRank() + 1)

  }

  // Method to check if cards form a full house combo
  private def isFullHouse(cards: List[Card]): Boolean = {

    // Group cards by rank and check if there are two groups: one with size 2 and one with size 3
    val rankGroups = cards.groupBy(_.getRank())
    rankGroups.values.exists(group => group.length == 2) && rankGroups.values.exists(group => group.length == 3)

  }
}