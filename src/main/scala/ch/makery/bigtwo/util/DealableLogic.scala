package ch.makery.bigtwo.util

import ch.makery.bigtwo.entities.Card

sealed trait DealableLogic {
  // Abstract method to check if the selected cards can be dealt in the current round
  def isDealable(thisRoundCombo: String, previousRoundCombo: String, selectedCards: List[Card], previousDealtCards: List[Card]): Boolean
}

object DealableLogic {

  // Object representing a single card combo dealable logic
  case object SingleDealable extends DealableLogic {
    override def isDealable(thisRoundCombo: String, previousRoundCombo: String, selectedCards: List[Card], previousDealtCards: List[Card]): Boolean =
      thisRoundCombo == "single" && previousRoundCombo == "single" &&
        selectedCards.last.getRank() > previousDealtCards.last.getRank() ||
        (selectedCards.last.getRank() == previousDealtCards.last.getRank() &&
          selectedCards.last.getSuit() > previousDealtCards.last.getSuit())
  }

  // Object representing a double card combo dealable logic
  case object DoubleDealable extends DealableLogic {
    override def isDealable(thisRoundCombo: String, previousRoundCombo: String, selectedCards: List[Card], previousDealtCards: List[Card]): Boolean =
      thisRoundCombo == "double" && previousRoundCombo == "double" &&
        selectedCards.last.getRank() > previousDealtCards.last.getRank() ||
        (selectedCards.last.getRank() == previousDealtCards.last.getRank() &&
          selectedCards.last.getSuit() > previousDealtCards.last.getSuit())
  }

  // Object representing a triple card combo dealable logic
  case object TripleDealable extends DealableLogic {
    override def isDealable(thisRoundCombo: String, previousRoundCombo: String, selectedCards: List[Card], previousDealtCards: List[Card]): Boolean =
      thisRoundCombo == "triple" && previousRoundCombo == "triple" &&
        selectedCards.last.getRank() > previousDealtCards.last.getRank() ||
        (selectedCards.last.getRank() == previousDealtCards.last.getRank() &&
          selectedCards.last.getSuit() > previousDealtCards.last.getSuit())
  }

  // Object representing a flush combo dealable logic
  case object FlushDealable extends DealableLogic {
    override def isDealable(thisRoundCombo: String, previousRoundCombo: String, selectedCards: List[Card], previousDealtCards: List[Card]): Boolean =
      thisRoundCombo == "flush" && previousRoundCombo == "flush" &&
        selectedCards.head.getSuit() >= previousDealtCards.head.getSuit()
  }

  // Object representing a straight combo dealable logic
  case object StraightDealable extends DealableLogic {
    override def isDealable(thisRoundCombo: String, previousRoundCombo: String, selectedCards: List[Card], previousDealtCards: List[Card]): Boolean =
      thisRoundCombo == "straight" && previousRoundCombo == "straight" &&
        (selectedCards.last.getRank() == previousDealtCards.last.getRank() &&
          selectedCards.last.getSuit() > previousDealtCards.last.getSuit()) ||
        selectedCards.last.getRank() > previousDealtCards.last.getRank()
  }

  // Object representing a full house combo dealable logic
  case object FullHouseDealable extends DealableLogic {
    override def isDealable(thisRoundCombo: String, previousRoundCombo: String, selectedCards: List[Card], previousDealtCards: List[Card]): Boolean =
      thisRoundCombo == "full house" && previousRoundCombo == "full house" &&
        getThreeOfAKindRank(selectedCards) > getThreeOfAKindRank(previousDealtCards)

    // Method to get three of a kind
    private def getThreeOfAKindRank(cards: List[Card]): Int = {
      // Group cards by rank and filter out the three-of-a-kind group
      val threeOfAKindGroup = cards.groupBy(_.getRank()).values.find(_.size == 3)

      // Get the rank of the three-of-a-kind cards
      threeOfAKindGroup match {
        case Some(threeOfAKind) => threeOfAKind.head.getRank()
        case None => throw new IllegalArgumentException("Selected cards do not form a full house.")
      }
    }
  }

  // Object representing a straight flush combo dealable logic
  case object StraightFlushDealable extends DealableLogic {
    override def isDealable(thisRoundCombo: String, previousRoundCombo: String, selectedCards: List[Card], previousDealtCards: List[Card]): Boolean =
      thisRoundCombo == "straight flush" && previousRoundCombo == "straight flush" &&
        (selectedCards.head.getSuit() > previousDealtCards.head.getSuit() &&
          selectedCards.head.getRank() >= previousDealtCards.head.getRank()) ||
        (selectedCards.head.getSuit() == previousDealtCards.head.getSuit() &&
          selectedCards.last.getRank() > previousDealtCards.last.getRank())
  }
}
