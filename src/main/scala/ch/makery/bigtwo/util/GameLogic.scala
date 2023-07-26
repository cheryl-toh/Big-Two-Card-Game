package ch.makery.bigtwo.util

import ch.makery.bigtwo.entities.{Card, Game}
import scalafx.animation.AnimationTimer


object GameLogic {
  var game:Game = _
  var gameInProgress = false

  def initializeGame(game: Game): Unit = {
    this.game = game
    gameInProgress = true

    game.startGame()
    startGameLoop()
    println("loop started")
  }

  def handlePlayerTurns(): Unit = {
    val currentPlayer = game.getCurrentPlayer()
    if(!currentPlayer.getTurn()){
      game.moveToNextTurn()
    }
  }

  def checkCombo(selectedCards: List[Card]): String = {
    var comboType: String = ""

    if (selectedCards.length == 1) {
      comboType = "single"
    } else if (selectedCards.length == 2) {
      val firstRank = selectedCards.head.getRank()
      if (selectedCards.forall(card => card.getRank() == firstRank)) {
        comboType = "double"
      } else {
        comboType = "invalid"
      }
    } else if (selectedCards.length == 3) {
      val firstRank = selectedCards.head.getRank()
      if (selectedCards.forall(card => card.getRank() == firstRank)) {
        comboType = "triple"
      } else {
        comboType = "invalid"
      }
    } else if (selectedCards.length == 5) {
      if (isFlush(selectedCards)) {
        if (isStraight(selectedCards)) {
          comboType = "straight flush"
        } else {
          comboType = "flush"
        }
      } else if (isStraight(selectedCards)) {
        comboType = "straight"
      } else if (isFullHouse(selectedCards)) {
        comboType = "full house"
      } else {
        comboType = "invalid"
      }
    } else {
      comboType = "invalid"
    }

    comboType
  }

  def isFlush(cards: List[Card]): Boolean = {
    // Check if all cards have the same suit
    val firstSuit = cards.head.getSuit()
    cards.forall(card => card.getSuit() == firstSuit)
  }

  def isStraight(cards: List[Card]): Boolean = {
    // Sort the cards by rank
    val sortedCards = cards.sortBy(_.getRank())

    // Check if the ranks form a consecutive sequence
    sortedCards.indices.drop(1).forall(i => sortedCards(i).getRank() == sortedCards(i - 1).getRank() + 1)
  }

  def isFullHouse(cards: List[Card]): Boolean = {
    // Group cards by rank and check if there are two groups: one with size 2 and one with size 3
    val rankGroups = cards.groupBy(_.getRank())
    rankGroups.values.exists(group => group.length == 2) && rankGroups.values.exists(group => group.length == 3)
  }
  def getThreeOfAKindRank(cards: List[Card]): Int = {
    // Group cards by rank and filter out the three-of-a-kind group
    val threeOfAKindGroup = cards.groupBy(_.getRank()).values.find(_.size == 3)

    // Get the rank of the three-of-a-kind cards
    threeOfAKindGroup match {
      case Some(threeOfAKind) => threeOfAKind.head.getRank()
      case None => throw new IllegalArgumentException("Selected cards do not form a full house.")
    }
  }

  def checkDealable(selectedCards: List[Card]): Boolean = {
    var dealable: Boolean = false
    val previousDealtCards = game.previousDealtCards

    val thisRoundCombo = checkCombo(selectedCards)
    val lastRoundCombo = checkCombo(previousDealtCards)

    if(previousDealtCards.isEmpty){
      dealable = true
    }else{
      dealable = (lastRoundCombo, thisRoundCombo) match {
        case ("single", "single") =>
          selectedCards.last.getRank() > previousDealtCards.last.getRank() ||
            (selectedCards.last.getRank() == previousDealtCards.last.getRank() &&
              selectedCards.last.getSuit() > previousDealtCards.last.getSuit())

        case ("single", _) => false

        case ("double", "double") =>
          selectedCards.last.getRank() > previousDealtCards.last.getRank() ||
            (selectedCards.last.getRank() == previousDealtCards.last.getRank() &&
              selectedCards.last.getSuit() > previousDealtCards.last.getSuit())

        case ("double", _) => false

        case ("triple", "triple") =>
          selectedCards.last.getRank() > previousDealtCards.last.getRank() ||
            (selectedCards.last.getRank() == previousDealtCards.last.getRank() &&
              selectedCards.last.getSuit() > previousDealtCards.last.getSuit())

        case ("triple", _) => false

        case ("flush", "flush") =>
          selectedCards.head.getSuit() >= previousDealtCards.head.getSuit()

        case ("flush", ("straight" | "full house" | "straight flush")) => true
        case ("flush", _) => false

        case ("straight", ("full house" | "straight flush")) => true
        case ("straight", "straight") =>
          (selectedCards.last.getRank() == previousDealtCards.last.getRank() &&
            selectedCards.last.getSuit() > previousDealtCards.last.getSuit()) ||
            selectedCards.last.getRank() > previousDealtCards.last.getRank()

        case ("straight", _) => false

        case ("full house", "straight flush") => true
        case ("full house", "full house") =>
          val selectedThreeOfAKindRank = getThreeOfAKindRank(selectedCards)
          val previousThreeOfAKindRank = getThreeOfAKindRank(previousDealtCards)
          selectedThreeOfAKindRank > previousThreeOfAKindRank

        case ("full house", _) => false
        case ("straight flush", "straight flush") =>
          (selectedCards.head.getSuit() > previousDealtCards.head.getSuit() &&
            selectedCards.head.getRank() >= previousDealtCards.head.getRank())||
            (selectedCards.head.getSuit() == previousDealtCards.head.getSuit() &&
              selectedCards.last.getRank() > previousDealtCards.last.getRank())

        case ("straight flush", _) => false

        case _ => false
      }
    }


    dealable
  }

  def startGameLoop(): Unit = {
    val gameLoop: AnimationTimer = AnimationTimer { deltaTime =>
      if (gameInProgress) {

        handlePlayerTurns()

        if(game.gamefinished){
          gameInProgress = false
          println("loop stopped")
        }

      }
    }
    gameLoop.start()
  }

}

