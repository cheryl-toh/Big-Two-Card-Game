package ch.makery.bigtwo.util

import ch.makery.bigtwo.entities.{Card, Game}
import scalafx.animation.AnimationTimer

object GameLogic {

  // initialize variables
  var game:Game = _
  private var gameInProgress = false

  // Method to initialize game
  def initializeGame(game: Game): Unit = {

    // set game and game status
    this.game = game
    gameInProgress = true

    //start game loop
    game.startGame()
    startGameLoop()

    // test line for game loop
    println("loop started")

  }

  // Method to handle player's turns
  private def handlePlayerTurns(): Unit = {

    // get current player
    val currentPlayer = game.getCurrentPlayer()

    // if current player's turn has passed, move to next turn
    if(!currentPlayer.getTurn()){
      game.moveToNextTurn()
    }
  }

  // Method to check combo of selected cards
  def checkCombo(selectedCards: List[Card]): String = {

    // initialize combo type string
    var comboType: String = ""

    // set single card combo
    if (selectedCards.length == 1) {
      comboType = "single"
    } else if (selectedCards.length == 2) {

      // check double cards and set combo type
      val firstRank = selectedCards.head.getRank()
      if (selectedCards.forall(card => card.getRank() == firstRank)) {
        comboType = "double"
      } else {
        comboType = "invalid"
      }

    } else if (selectedCards.length == 3) {

      // check triple cards and set combo type
      val firstRank = selectedCards.head.getRank()
      if (selectedCards.forall(card => card.getRank() == firstRank)) {
        comboType = "triple"
      } else {
        comboType = "invalid"
      }

    } else if (selectedCards.length == 5) {

      // check if cards is flush combo
      if (isFlush(selectedCards)) {

        // check if cards is straight flush
        if (isStraight(selectedCards)) {
          comboType = "straight flush"
        } else {
          comboType = "flush"
        }

      } else if (isStraight(selectedCards)) {

        // if cards is straight
        comboType = "straight"

      } else if (isFullHouse(selectedCards)) {

        // check if cards is full house
        comboType = "full house"

      } else {
        comboType = "invalid"
      }
    } else {
      // invalid combo type
      comboType = "invalid"
    }

    // return combo type of cards
    comboType

  }


  // Method to check if cards is flush
  private def isFlush(cards: List[Card]): Boolean = {

    // Check if all cards have the same suit
    val firstSuit = cards.head.getSuit()
    cards.forall(card => card.getSuit() == firstSuit)

  }


  // Method to check if cards is straight
  private def isStraight(cards: List[Card]): Boolean = {

    // Sort the cards by rank
    val sortedCards = cards.sortBy(_.getRank())

    // Check if the ranks form a consecutive sequence
    sortedCards.indices.drop(1).forall(i => sortedCards(i).getRank() == sortedCards(i - 1).getRank() + 1)

  }


  // Method to check if cards is full house
  private def isFullHouse(cards: List[Card]): Boolean = {

    // Group cards by rank and check if there are two groups: one with size 2 and one with size 3
    val rankGroups = cards.groupBy(_.getRank())
    rankGroups.values.exists(group => group.length == 2) && rankGroups.values.exists(group => group.length == 3)

  }


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


  // Method to check if cards is deal-able
  def checkDealable(selectedCards: List[Card]): Boolean = {

    // initialize boolean and dealt cards
    var dealable: Boolean = false
    val previousDealtCards = game.previousDealtCards

    // check combo for this round and last round dealt cards
    val thisRoundCombo = checkCombo(selectedCards)
    val lastRoundCombo = checkCombo(previousDealtCards)

    // compare combos of this round and last round and set deal-able boolean
    if(previousDealtCards.isEmpty){
      dealable = true
    }else{

      // comparison cases
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

        case ("flush", "straight" | "full house" | "straight flush") => true

        case ("flush", _) => false

        case ("straight", "full house" | "straight flush") => true

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

    // return deal-able boolean
    dealable

  }


  // Method of main game loop
  private def startGameLoop(): Unit = {

    // game loop
    val gameLoop: AnimationTimer = AnimationTimer { deltaTime =>

      // check statement for game loop
      if (gameInProgress) {

        // handle players turn and updates graphics and state correspondingly
        handlePlayerTurns()

        // stop game loop if game is finished
        if(game.gameFinished){
          gameInProgress = false

          // test line for loop stopping
          println("loop stopped")
        }
      }
    }

    // start game loop
    gameLoop.start()

  }
}

