package ch.makery.bigtwo.entities


class Game (playersS: List[Player], deck: Deck){

  // initialize game attributes
  var players: List[Player] =  playersS
  private var currentPlayerIndex: Int = 0
  var gameFinished:Boolean = false
  var previousDealtCards: List[Card] = List.empty[Card]
  var kingPlayerID: Int = 1
  var playersPassed: Set[Player] = Set.empty

  // Method to start game
  def startGame(): Unit = {

    // start new deck and shuffle deck
    deck.initializeDeck()
    deck.shuffle()

    // set first king to player 1
    setKingPlayerID(1)

    // distribute cards to players and start game
    distributeCards()
    startNextTurn()

  }

  // Method to distribute cards
  private def distributeCards(): Unit = {

    // get number of players and number of cards
    val totalPlayers = players.length
    val totalCards = deck.getCards()

    // distribute cards to all players
    for (i <- totalCards.indices by totalPlayers){
      for (j <- 0 until totalPlayers){
        players(j).addCardToHand(totalCards(i + j))
      }
    }
  }

  // Method to get current player of game
  def getCurrentPlayer(): Player = {

    // get player by index
    players(currentPlayerIndex)

  }

  // Method to get next player of game
  def getNextPlayer():Player = {

    // calculate next player index
    val nextPlayerIndex = (currentPlayerIndex + 1) % players.length

    // get player by index
    players(nextPlayerIndex)

  }

  // Method to start next turn of game
  private def startNextTurn(): Unit = {

    // set current player of game
    val currentPlayer = players(currentPlayerIndex)

    // set player turn to true
    currentPlayer.setTurn(true)

    // test line to print current player
    println("Player " + currentPlayer.getPlayerID())

    // set current player hand
    val currentHand = currentPlayer.showHand()

    // test line to print cards in current player's hand
    currentHand.foreach(x => println(x.getRank + "_" + x.getSuit))

  }


  // Method to move to next turn
  def moveToNextTurn(): Unit = {

    // Move to the next player's turn
    currentPlayerIndex = (currentPlayerIndex + 1) % players.length

    // Start the next player's turn
    startNextTurn()

  }

  // Method to set player as king
  def setKingPlayerID(playerID: Int): Unit = {

    // new player as king
    kingPlayerID = playerID

    // test line for setting new king
    println("King set to: " + kingPlayerID)

  }

  // Method to reset player passed status
  def resetPlayersPassedStatus(): Unit = {

    // set playersPassed to empty
    playersPassed = Set.empty

    // set all players passed boolean to false
    players.foreach(_.setHasPassed(false))

  }

  // Method to set player as passed
  def markPlayerAsPassed(player: Player): Unit = {

    // add player into passed players set
    playersPassed += player

    // set passed boolean of player to true
    player.setHasPassed(true)

  }

}
