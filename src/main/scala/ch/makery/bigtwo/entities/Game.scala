package ch.makery.bigtwo.entities

class Game (players: List[Player], deck: Deck){

  var currentPlayerIndex: Int = 0
  var gamefinished:Boolean = false

  def startGame(): Unit = {
    deck.initializeDeck()
    deck.shuffle()

    distributeCards()
    startNextTurn()
  }

  def distributeCards(): Unit = {
    val totalPlayers = players.length
    val totalCards = deck.getCards()

    for (i <- 0 until totalCards.length by totalPlayers){
      for (j <- 0 until totalPlayers){
        players(j).addCardToHand(totalCards(i + j))
      }
    }
  }

  def getCurrentPlayer(): Player = {
    players(currentPlayerIndex)
  }

  def startNextTurn(): Unit = {
    val currentPlayer = players(currentPlayerIndex)
    currentPlayer.setTurn(true)
    println("Player " + currentPlayer.getPlayerID())

    val currentHand = currentPlayer.showHand()
    currentHand.foreach(x => println(x.getRank() + "_" + x.getSuit()))
  }

  def moveToNextTurn(): Unit = {

    // Move to the next player's turn
    currentPlayerIndex = (currentPlayerIndex + 1) % players.length

    // Start the next player's turn
    startNextTurn()
  }

}
