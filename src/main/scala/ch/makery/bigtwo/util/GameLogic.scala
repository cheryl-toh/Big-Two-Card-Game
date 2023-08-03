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
    import ComboLogic._ // Import the ComboLogic abstract class here to access its members

    val comboType: String = selectedCards match {
      case _ if Single.checkCombo(selectedCards) => Single.comboType
      case _ if Double.checkCombo(selectedCards) => Double.comboType
      case _ if Triple.checkCombo(selectedCards) => Triple.comboType
      case _ if Flush.checkCombo(selectedCards) => Flush.comboType
      case _ if Straight.checkCombo(selectedCards) => Straight.comboType
      case _ if FullHouse.checkCombo(selectedCards) => FullHouse.comboType
      case _ if StraightFlush.checkCombo(selectedCards) => StraightFlush.comboType
      case _ => "invalid"
    }
    println(comboType)
    comboType
  }


  // Method to check if cards is deal-able
  def checkDealable(selectedCards: List[Card]): Boolean = {

    import DealableLogic._

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
        case ("single", "single") => SingleDealable.isDealable(thisRoundCombo, lastRoundCombo, selectedCards, previousDealtCards)
        case ("single", _) => false
        case ("double", "double") => DoubleDealable.isDealable(thisRoundCombo,lastRoundCombo,selectedCards, previousDealtCards)
        case ("double", _) => false
        case ("triple", "triple") => TripleDealable.isDealable(thisRoundCombo,lastRoundCombo,selectedCards, previousDealtCards)
        case ("triple", _) => false
        case ("flush", "flush") => FlushDealable.isDealable(thisRoundCombo,lastRoundCombo,selectedCards, previousDealtCards)
        case ("flush", "straight" | "full house" | "straight flush") => true
        case ("flush", _) => false
        case ("straight", "full house" | "straight flush") => true
        case ("straight", "straight") => StraightDealable.isDealable(thisRoundCombo,lastRoundCombo,selectedCards, previousDealtCards)
        case ("straight", _) => false
        case ("full house", "straight flush") => true
        case ("full house", "full house") => FullHouseDealable.isDealable(thisRoundCombo,lastRoundCombo,selectedCards, previousDealtCards)
        case ("full house", _) => false
        case ("straight flush", "straight flush") => StraightFlushDealable.isDealable(thisRoundCombo,lastRoundCombo,selectedCards, previousDealtCards)
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

