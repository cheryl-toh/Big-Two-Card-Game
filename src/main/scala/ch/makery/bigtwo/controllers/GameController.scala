package ch.makery.bigtwo.controllers

import ch.makery.bigtwo.Main
import ch.makery.bigtwo.entities.{Card, Deck, Game, Player}
import ch.makery.bigtwo.util.{GameLogic, PlaySound, TimerLogic}
import javafx.fxml.FXML
import scalafx.animation.{AnimationTimer, KeyFrame, Timeline}
import scalafx.application.Platform
import scalafx.scene.image.{Image, ImageView}
import scalafxml.core.macros.sfxml
import scalafx.event.ActionEvent
import scalafx.scene.control.{Alert, Label}
import scalafx.scene.control.Alert.AlertType
import scalafx.scene.input.MouseEvent
import scalafx.scene.layout.AnchorPane
import scalafx.util.Duration
import scala.collection.mutable.ListBuffer

@sfxml
class GameController (@FXML var leftPlayer: ImageView,
                      @FXML var rightPlayer: ImageView,
                      @FXML var middlePlayer: ImageView,
                      @FXML var currentPlayer: ImageView,
                      @FXML var leftPlayerID: Label,
                      @FXML var middlePlayerID: Label,
                      @FXML var rightPlayerID: Label,
                      @FXML var currentPlayerID: Label,
                      @FXML var leftPlayerLength: Label,
                      @FXML var middlePlayerLength: Label,
                      @FXML var rightPlayerLength: Label,
                      @FXML var leftPlayerCrown: ImageView,
                      @FXML var rightPlayerCrown: ImageView,
                      @FXML var middlePlayerCrown: ImageView,
                      @FXML var currentPlayerCrown: ImageView,
                      @FXML var card1: ImageView,
                      @FXML var card2: ImageView,
                      @FXML var card3: ImageView,
                      @FXML var card4: ImageView,
                      @FXML var card5: ImageView,
                      @FXML var card6: ImageView,
                      @FXML var card7: ImageView,
                      @FXML var card8: ImageView,
                      @FXML var card9: ImageView,
                      @FXML var card10: ImageView,
                      @FXML var card11: ImageView,
                      @FXML var card12: ImageView,
                      @FXML var card13: ImageView,
                      @FXML var lpCard1: ImageView,
                      @FXML var lpCard2: ImageView,
                      @FXML var lpCard3: ImageView,
                      @FXML var lpCard4: ImageView,
                      @FXML var lpCard5: ImageView,
                      @FXML var mpCard1: ImageView,
                      @FXML var mpCard2: ImageView,
                      @FXML var mpCard3: ImageView,
                      @FXML var mpCard4: ImageView,
                      @FXML var mpCard5: ImageView,
                      @FXML var rpCard1: ImageView,
                      @FXML var rpCard2: ImageView,
                      @FXML var rpCard3: ImageView,
                      @FXML var rpCard4: ImageView,
                      @FXML var rpCard5: ImageView,
                      @FXML var lpPassed: Label,
                      @FXML var mpPassed: Label,
                      @FXML var rpPassed: Label,
                      @FXML var mainGamePane: AnchorPane,
                      @FXML var transitionPane: AnchorPane,
                      @FXML var nextPlayer: Label,
                      @FXML var countDown: Label){

  //assign user data to hand cards
  card1.userData = "card1"
  card2.userData = "card2"
  card3.userData = "card3"
  card4.userData = "card4"
  card5.userData = "card5"
  card6.userData = "card6"
  card7.userData = "card7"
  card8.userData = "card8"
  card9.userData = "card9"
  card10.userData = "card10"
  card11.userData = "card11"
  card12.userData = "card12"
  card13.userData = "card13"

  // Maximum number of cards that can be displayed on the screen
  private val maxDisplayedCards = 13

  //initialize new deck and game
  private val deck: Deck = new Deck()
  private val game: Game = new Game(List(
    new Player(1),
    new Player(2),
    new Player(3),
    new Player(4)
  ), deck)

  // initialize animation images for each player
  private val playerAnimations: Map[Int, List[Image]] = Map(
    1 -> List(
      new Image(getClass.getResourceAsStream("/Images/player 1/1.png")),
      new Image(getClass.getResourceAsStream("/Images/player 1/2.png")),
      new Image(getClass.getResourceAsStream("/Images/player 1/3.png")),
      new Image(getClass.getResourceAsStream("/Images/player 1/4.png")),
      new Image(getClass.getResourceAsStream("/Images/player 1/5.png")),
      new Image(getClass.getResourceAsStream("/Images/player 1/6.png"))
    ),
    2 -> List(
      new Image(getClass.getResourceAsStream("/Images/player 2/1.png")),
      new Image(getClass.getResourceAsStream("/Images/player 2/2.png")),
      new Image(getClass.getResourceAsStream("/Images/player 2/3.png")),
      new Image(getClass.getResourceAsStream("/Images/player 2/4.png")),
      new Image(getClass.getResourceAsStream("/Images/player 2/5.png")),
      new Image(getClass.getResourceAsStream("/Images/player 2/6.png"))
    ),
    3 -> List(
      new Image(getClass.getResourceAsStream("/Images/player 3/1.png")),
      new Image(getClass.getResourceAsStream("/Images/player 3/2.png")),
      new Image(getClass.getResourceAsStream("/Images/player 3/3.png")),
      new Image(getClass.getResourceAsStream("/Images/player 3/4.png")),
      new Image(getClass.getResourceAsStream("/Images/player 3/5.png")),
      new Image(getClass.getResourceAsStream("/Images/player 3/6.png"))
    ),
    4 -> List(
      new Image(getClass.getResourceAsStream("/Images/player 4/1.png")),
      new Image(getClass.getResourceAsStream("/Images/player 4/2.png")),
      new Image(getClass.getResourceAsStream("/Images/player 4/3.png")),
      new Image(getClass.getResourceAsStream("/Images/player 4/4.png")),
      new Image(getClass.getResourceAsStream("/Images/player 4/5.png")),
      new Image(getClass.getResourceAsStream("/Images/player 4/6.png"))
    )
  )

  // initialize current frame index for each player
  private var currentFrameIndices: Map[Int, Int] = Map(
    1 -> 0,
    2 -> 0,
    3 -> 0,
    4 -> 0
  )

  // initialize animation timer for player animations
  private var animationTimer: AnimationTimer = _


  // Method to start game
  def startGame(): Unit = {
    GameLogic.initializeGame(game)
    startPlayerAnimations()

    transitionPane.visible = false

    distributeCardAnimation()
    updateCrowns()
  }

  // Method to start player animations
  private def startPlayerAnimations(): Unit = {

    var lastFrameTime: Long = 0

    // Initialize animation timer
    animationTimer = AnimationTimer { time =>
      val currentTime = System.nanoTime()

      if(currentTime - lastFrameTime >= 400000000){
        lastFrameTime = currentTime

        val turnPlayer = game.getCurrentPlayer()
        val turnPlayerID = turnPlayer.getPlayerID()

        // Update the ImageView nodes with the corresponding animation frame
        currentPlayer.image = playerAnimations(turnPlayerID)(currentFrameIndices(turnPlayerID))
        leftPlayer.image = playerAnimations(turnPlayerID % 4 + 1)(currentFrameIndices(turnPlayerID % 4 + 1))
        middlePlayer.image = playerAnimations((turnPlayerID + 1) % 4 + 1)(currentFrameIndices((turnPlayerID + 1) % 4 + 1))
        rightPlayer.image = playerAnimations((turnPlayerID + 2) % 4 + 1)(currentFrameIndices((turnPlayerID + 2) % 4 + 1))

        // Update the player ID labels
        currentPlayerID.text = s"Player $turnPlayerID"
        leftPlayerID.text = s"Player ${(turnPlayerID % 4) + 1}"
        middlePlayerID.text = s"Player ${((turnPlayerID + 1) % 4) + 1}"
        rightPlayerID.text = s"Player ${((turnPlayerID + 2) % 4) + 1}"
        updateCrowns()
        updateHandLength()
        updateDealtCards()

        // Increment the frame index for each player
        currentFrameIndices = currentFrameIndices.map { case (playerID, frameIndex) =>
          val nextFrameIndex = (frameIndex + 1) % playerAnimations(playerID).length
          (playerID, nextFrameIndex)
        }
      }
    }
    // Start the animation timer
    animationTimer.start()
  }

  // Method to update dealt cards of players
  private def updateDealtCards(): Unit = {

    //initialize players and list of cards image views
    val leftPlayer = game.getNextPlayer()
    val middlePlayer = getNextPlayer(game.getNextPlayer())
    val rightPlayer = getNextPlayer(getNextPlayer(game.getNextPlayer()))
    val lpCards: List[ImageView] = List(lpCard1, lpCard2, lpCard3, lpCard4, lpCard5)
    val mpCards: List[ImageView] = List(mpCard1, mpCard2, mpCard3, mpCard4, mpCard5)
    val rpCards: List[ImageView] = List(rpCard1, rpCard2, rpCard3, rpCard4, rpCard5)

    // Show dealt cards for left player
    if (leftPlayer.getDealtCards().nonEmpty) {
      showDealtCardsForPlayer(leftPlayer, lpCards)
      lpPassed.visible = false
    } else if (leftPlayer.getHasPassed()) {
      // Show "Passed" label if player passed their turn
      lpCards.foreach(_.visible = false)
      lpPassed.visible = true
    } else {
      // Hide "Passed" label if player has not passed their turn and has no dealt cards
      lpCards.foreach(_.visible = false)
      lpPassed.visible = false
    }

    // Show dealt cards for middle player
    if (middlePlayer.getDealtCards().nonEmpty) {
      showDealtCardsForPlayer(middlePlayer, mpCards)
      mpPassed.visible = false
    } else if (middlePlayer.getHasPassed()) {
      // Show "Passed" label if player passed their turn
      mpCards.foreach(_.visible = false)
      mpPassed.visible = true
    } else {
      // Hide "Passed" label if player has not passed their turn and has no dealt cards
      mpCards.foreach(_.visible = false)
      mpPassed.visible = false
    }

    // Show dealt cards for right player
    if (rightPlayer.getDealtCards().nonEmpty) {
      showDealtCardsForPlayer(rightPlayer, rpCards)
      rpPassed.visible = false
    } else if (rightPlayer.getHasPassed()) {
      // Show "Passed" label if player passed their turn
      rpCards.foreach(_.visible = false)
      rpPassed.visible = true
    } else {
      // Hide "Passed" label if player has not passed their turn and has no dealt cards
      rpCards.foreach(_.visible = false)
      rpPassed.visible = false
    }
  }

  // Method to show dealt cards for player
  private def showDealtCardsForPlayer(player: Player, cardImageViews: List[ImageView]): Unit = {

    // get player's dealt cards
    val playerDealtCards = player.getDealtCards()

    // Clear the images in all card ImageView nodes first
    cardImageViews.foreach(_.image = null)

    // Hide all card ImageView nodes first
    cardImageViews.foreach(_.visible = false)

    // Show the cards in the middle of the ImageView nodes
    for ((card, index) <- playerDealtCards.zipWithIndex) {
      // Get the corresponding ImageView for the current card
      val cardImageView = cardImageViews(index)
      showCardAtIndex(cardImageView, card, index)
    }
  }


  // Method to reset dealt cards for all players
  private def resetDealtCardsForAllPlayers(): Unit = {
    val lpCards: List[ImageView] = List(lpCard1, lpCard2, lpCard3, lpCard4, lpCard5)
    val mpCards: List[ImageView] = List(mpCard1, mpCard2, mpCard3, mpCard4, mpCard5)
    val rpCards: List[ImageView] = List(rpCard1, rpCard2, rpCard3, rpCard4, rpCard5)

    lpCards.foreach(_.image = null)
    mpCards.foreach(_.image = null)
    rpCards.foreach(_.image = null)

    lpCards.foreach(_.visible = false)
    mpCards.foreach(_.visible = false)
    rpCards.foreach(_.visible = false)

    lpPassed.visible = false
    mpPassed.visible = false
    rpPassed.visible = false
  }


  // Method to set new king
  private def setNewKing(player: Player): Unit = {
    game.setKingPlayerID(player.getPlayerID())

    // Play reset king sound
    val file = getClass.getResource("/sounds/setKing.wav")
    PlaySound.playSoundEffect(file)

    // Reset the previousDealtCards list to be empty
    game.previousDealtCards = List.empty[Card]

    // Reset dealt cards for all players
    resetDealtCardsForAllPlayers()
  }

  // Method to update crown position
  private def updateCrowns(): Unit = {

    //initialize current player
    val currentPlayer = game.getCurrentPlayer()

    //show crown in correct position
    showCrownForPlayer(currentPlayer, currentPlayerCrown)
    showCrownForPlayer(getNextPlayer(currentPlayer), leftPlayerCrown)
    showCrownForPlayer(getNextPlayer(getNextPlayer(currentPlayer)), middlePlayerCrown)
    showCrownForPlayer(getNextPlayer(getNextPlayer(getNextPlayer(currentPlayer))), rightPlayerCrown)
  }

  // Method to show crown for player
  private def showCrownForPlayer(player: Player, crownImageView: ImageView): Unit = {

    //show crown if player is king
    crownImageView.visible = player.getPlayerID() == game.kingPlayerID

    //show crown image in the image view
    if (crownImageView.visible.value) {
      crownImageView.image = new Image(getClass.getResourceAsStream("/Images/crown.png"))
    }
  }

  // Method to update hand length of opponent players
  private def updateHandLength(): Unit = {

    //initialize current player
    val currentPlayer = game.getCurrentPlayer()

    //show hand length of each opponent players
    showHandForPlayers(getNextPlayer(currentPlayer), leftPlayerLength)
    showHandForPlayers(getNextPlayer(getNextPlayer(currentPlayer)), middlePlayerLength)
    showHandForPlayers(getNextPlayer(getNextPlayer(getNextPlayer(currentPlayer))), rightPlayerLength)
  }

  // Method to show hand length for player
  private def showHandForPlayers(player: Player, handLabel: Label): Unit = {

    //set hand length to label text of player
    handLabel.text = player.showHand().length.toString
  }

  // Method to get next player
  private def getNextPlayer(player: Player): Player = {

    //calculate index of next player
    val nextPlayerIndex = (game.players.indexOf(player) + 1) % game.players.length

    //get next player using index calculated
    game.players(nextPlayerIndex)
  }

  // Method to show card at given index
  private def showCardAtIndex(cardImageView: ImageView, card: Card, index: Int): Unit = {

    //index is valid and not null
    if (index < maxDisplayedCards && card != null) {

      //set image to the image view node and display the image
      cardImageView.image = card.getImage()
      cardImageView.visible = true

    } else {

      //make image view null and invisible
      cardImageView.image = null
      cardImageView.visible = false
    }
  }

  // Method to play distribute card animation
  private def distributeCardAnimation(): Unit = {
    val currentPlayer = game.getCurrentPlayer()
    val currentPlayerHand = currentPlayer.showHand()
    val cardImageViews = List(card1, card2, card3, card4, card5, card6, card7, card8, card9, card10, card11, card12, card13)

    // Create a Timeline for the card animation
    val timeline = new Timeline {
      cycleCount = 1 // Only one cycle
    }

    // Create duration for a card to display
    val durationPerCard: Duration = Duration(100)

    // Create the KeyFrames for each card
    for ((card, index) <- currentPlayerHand.zipWithIndex) {
      // Calculate the time delay for each card based on its index
      val delay: Duration = durationPerCard * (index + 1)

      // Create the KeyFrame with the EventHandler and time delay
      val keyFrame = KeyFrame(delay, onFinished = _ => showCardAtIndex(cardImageViews(index), card, index))

      // Add the KeyFrame to the Timeline
      timeline.keyFrames.add(keyFrame)
    }

    // Play the card animation
    timeline.play()

    // Play sound for distributing cards
    val file = getClass.getResource("/sounds/spreadCard.wav")
    PlaySound.playSoundEffect(file)
  }

  // Method for showing hand of current player
  private def updateShownHand(): Unit = {

    //get current player
    val currentPlayer = game.getNextPlayer()

    //test line for player update
    println("Player updated to: " + currentPlayer.getPlayerID())
    //get current player hand
    val currentPlayerHand = currentPlayer.showHand()

    // List of all card ImageView nodes
    val cardImageViews = List(card1, card2, card3, card4, card5, card6, card7, card8, card9, card10, card11, card12, card13)

    // Clear the images in all card ImageView nodes first
    cardImageViews.foreach(_.image = null)

    // Hide all card ImageView nodes first
    cardImageViews.foreach(_.visible = false)

    // Show the cards in the middle of the ImageView nodes
    for ((card, index) <- currentPlayerHand.zipWithIndex) {

      // Get the corresponding ImageView for the current card
      val cardImageView = cardImageViews(index)

      //show card at corresponding index
      showCardAtIndex(cardImageView, card, index)
    }
  }

  // Method for resetting card translation
  private def resetCardTranslations(): Unit = {

    // List of all card ImageView nodes
    val cardImageViews = List(card1, card2, card3, card4, card5, card6, card7, card8, card9, card10, card11, card12, card13)

    // Reset the translateY property of all card ImageViews
    cardImageViews.foreach(_.translateY = 0)
  }

  // Method to show transition scene
  private def showTransitionPane(nextPlayerID: Int, countdownDuration: Int, onTransitionFinish: () => Unit): Unit = {

    //test line for transition scene
    println("transition method called")

    //get next player and countDown duration in seconds
    nextPlayer.text = s"Player $nextPlayerID"
    countDown.text = countdownDuration.toString

    //make main game pane invisible
    mainGamePane.visible = false

    // Show the transition pane
    transitionPane.visible = true

    //play transition sound effect
    val file = getClass.getResource("/sounds/Whoosh.wav")
    PlaySound.playSoundEffect(file)

    //play timer sound effect
    val file2 = getClass.getResource("/sounds/Clock.wav")
    PlaySound.playSoundEffect(file2)

    // Start the countdown timer
    val startTime = System.currentTimeMillis()

    // start timer
    TimerLogic.startCountdown(countdownDuration, () => {
      // After the countdown timer finishes, hide the transition pane and execute the callback
      transitionPane.visible = false
      mainGamePane.visible = true
      onTransitionFinish()
    })

    // Update the countdown label in real-time
    val animationTimer = AnimationTimer { _ =>
      val currentTime = System.currentTimeMillis()
      val elapsedTime = currentTime - startTime
      val remainingTime = countdownDuration * 1000 - elapsedTime

      if (remainingTime <= 0) {
        countDown.text = "0" // Ensure the label shows 0 when the countdown finishes
      } else {
        countDown.text = (remainingTime / 1000).toString
      }
    }

    //start animation
    animationTimer.start()
  }


  // Method to handle selecting card
  def handleCardPress(action: MouseEvent): Unit = {

    // get current player and list of hand card image view
    val currentPlayer = game.getCurrentPlayer()
    val cardImageViews = List(card1, card2, card3, card4, card5, card6, card7, card8, card9, card10, card11, card12, card13)

    // Get the fx:id of the clicked ImageView from its userData property
    val clickedFxId = action.getSource.asInstanceOf[javafx.scene.image.ImageView].getUserData.toString

    // Find the index of the clicked ImageView using the fx:id
    val cardIndex = cardImageViews.indexWhere(_.getUserData.toString == clickedFxId)

    // Retrieve the card associated with the clicked ImageView
    val card = currentPlayer.showHand()(cardIndex)

    // Check if the card is already in the selectedCards list
    if (currentPlayer.getSelectCard().contains(card)) {
      // If it is, remove it from the list and move the card back to the hand
      currentPlayer.deselectCard(card)
      cardImageViews(cardIndex).translateY = 0
    } else {
      //play select card sound effect
      val file = getClass.getResource("/sounds/Card.wav")
      PlaySound.playSoundEffect(file)

      // If it's not, add it to the list and move the card up a bit
      currentPlayer.addSelectedCard(card)
      cardImageViews(cardIndex).translateY = -30
    }
  }

  // Method to handle pass button clicked
  def handlePassButton(action: ActionEvent): Unit = {

    //play button clicked sound effect
    val file = getClass.getResource("/sounds/Click.wav")
    PlaySound.playSoundEffect(file)

    //get current and next player
    val currentPlayer = game.getCurrentPlayer()
    val nextPlayer = getNextPlayer(currentPlayer)

    // Mark the current player as dealt (made a move) in this round
    game.markPlayerAsPassed(currentPlayer)

    // Check if all other players except the next player have dealt and passed in this round
    val otherPlayersDealtAndPassed = game.players.filterNot(p => p == nextPlayer).forall(p => game.playersPassed.contains(p))

    if (otherPlayersDealtAndPassed) {
      // Reset the previousDealtCards list to be empty
      game.previousDealtCards = List.empty[Card]

      // Find the player who has not dealt and passed in this round and set them as the king
      val nonDealtAndPassedPlayer = game.players.find(p => !p.getHasPassed()).get
      setNewKing(nonDealtAndPassedPlayer)

      //reset players passed status
      game.resetPlayersPassedStatus()
    }

    // clear dealt cards and selected card list
    currentPlayer.clearDealtCard()
    currentPlayer.clearSelectedCard()
    currentPlayer.setTurn(false)

    //reset all card translations and update UI of scene
    resetCardTranslations()
    updateShownHand()
    updateCrowns()
    updateHandLength()
    updateDealtCards()

    // Show the transition pane with a countdown of 5 seconds
    val countdownDuration = 4
    showTransitionPane(nextPlayer.getPlayerID(), countdownDuration, () => {})
  }

  // Method to handle deal button clicked
  def handleDealButton(action: ActionEvent): Unit = {

    //Play button clicked sound effect
    val file = getClass.getResource("/sounds/Click.wav")
    PlaySound.playSoundEffect(file)

    //get current player and its selected cards
    val currentPlayer = game.getCurrentPlayer()
    val selectedCards = currentPlayer.getSelectCard()

    //check if selected cards is valid combo
    val validSelect = GameLogic.checkCombo(selectedCards)

    if (validSelect == "invalid") {

      // Play error sound
      val file = getClass.getResource("/sounds/error.wav")
      PlaySound.playSoundEffect(file)

      // Show an alert indicating invalid combo
      val alert = new Alert(AlertType.Warning)
      alert.title = "Invalid Combo"
      alert.headerText = "Invalid combo selected."
      alert.contentText = "Please select a valid combo."
      alert.showAndWait()

    } else {

      // Check if the selected cards can be dealt
      val validDeal = GameLogic.checkDealable(selectedCards)

      if (validDeal) {

        // Save the selected cards as previous dealt cards
        game.previousDealtCards = selectedCards
        currentPlayer.setDealtCard(ListBuffer(selectedCards: _*))

        // remove dealt cards from hand
        currentPlayer.hand --= selectedCards

        //test lines for selected cards
        println("selected cards: ")
        selectedCards.foreach(x => println(x.getRank() + "_" + x.getSuit()))

        // Clear the selected cards from the current player's hand
        currentPlayer.clearSelectedCard()

        if(currentPlayer.showHand().isEmpty){

          //game finish if current player hand is empty
          game.gameFinished = true

          //show leaderboard scene
          Platform.runLater(() => Main.showLeaderBoardScene(game.players))

        }else{

          //reset players passed status
          game.resetPlayersPassedStatus()

          // Set the current player's turn to false and move to the next turn
          currentPlayer.setTurn(false)
          currentPlayer.setHasPassed(false)

          // Reset card translations and update shown hand for the next player
          resetCardTranslations()
          updateShownHand()
          updateHandLength()
          updateDealtCards()

          // Show the transition pane with a countdown of 4 seconds
          val countdownDuration = 4
          showTransitionPane(getNextPlayer(currentPlayer).getPlayerID(), countdownDuration, () => {})
        }

      } else {

        // Play error sound
        val file = getClass.getResource("/sounds/error.wav")
        PlaySound.playSoundEffect(file)

        // Show an alert indicating invalid deal
        val alert = new Alert(AlertType.Warning)
        alert.title = "Invalid Deal"
        alert.headerText = "Invalid deal: The selected cards cannot be dealt."
        alert.contentText = "Please select a valid deal."
        alert.showAndWait()
      }
    }
  }
}
