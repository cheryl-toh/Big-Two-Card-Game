package ch.makery.bigtwo.controllers

import ch.makery.bigtwo.Main
import ch.makery.bigtwo.entities.{Card, Deck, Game, Player}
import ch.makery.bigtwo.util.{GameLogic, PlaySound}
import javafx.fxml.FXML
import scalafx.animation.{AnimationTimer, KeyFrame, KeyValue, Timeline}
import scalafx.scene.image.{Image, ImageView}
import scalafxml.core.macros.sfxml
import scalafx.event.ActionEvent
import scalafx.scene.input.{MouseButton, MouseEvent}
import scalafx.util.Duration
import scalafx.Includes._
import javafx.event.EventHandler

@sfxml
class GameController (@FXML var leftPlayer: ImageView,
                      @FXML var rightPlayer: ImageView,
                      @FXML var middlePlayer: ImageView,
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
                      @FXML var card13: ImageView){

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

  //initialize new deck and game
  private val deck: Deck = new Deck()
  private var game: Game = new Game(List(
    new Player(1),
    new Player(2),
    new Player(3),
    new Player(4)
  ), deck)
  private var dragStartX: Double = 0
  private var dragStartY: Double = 0
  private var isDragging: Boolean = false

  // initialize animation images for each player
  private var playerAnimations: Map[Int, List[Image]] = Map(
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

  def startGame(): Unit = {
    GameLogic.initializeGame(game)
    startPlayerAnimations()

    distributeCardAnimation()
  }

  // Method to start player animations
  def startPlayerAnimations(): Unit = {

    var lastFrameTime: Long = 0

    // Initialize animation timer
    animationTimer = AnimationTimer { time =>
      val currentTime = System.nanoTime()

      if(currentTime - lastFrameTime >= 400000000){
        lastFrameTime = currentTime

        val currentPlayer = game.getCurrentPlayer()
        val currentPlayerID = currentPlayer.getPlayerID()

        // Update the ImageView nodes with the corresponding animation frame
        leftPlayer.image = playerAnimations((currentPlayerID) % 4 + 1)(currentFrameIndices((currentPlayerID) % 4 + 1))
        middlePlayer.image = playerAnimations((currentPlayerID + 1) % 4 + 1)(currentFrameIndices((currentPlayerID + 1) % 4 + 1))
        rightPlayer.image = playerAnimations((currentPlayerID + 2) % 4 + 1)(currentFrameIndices((currentPlayerID + 2) % 4 + 1))

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

  def showCardAtIndex(cardImageViews: List[ImageView], index: Int, card: Card): Unit = {
    if (index < cardImageViews.length) {
      cardImageViews(index).image = card.getImage()
      cardImageViews(index).visible = true
    }
  }

  def distributeCardAnimation(): Unit = {
    val currentPlayer = game.getCurrentPlayer()
    val currentPlayerHand = currentPlayer.showHand()

    val cardImageViews = List(card1, card2, card3, card4, card5, card6, card7, card8, card9, card10, card11, card12, card13)

    // Create a Timeline for the card animation
    val timeline = new Timeline {
      cycleCount = 1 // Only one cycle
    }

    val durationPerCard: Duration = Duration(100)

    // Create the KeyFrames for each card
    for ((card, index) <- currentPlayerHand.zipWithIndex) {
      // Calculate the time delay for each card based on its index
      val delay: Duration = durationPerCard * (index + 1)

      // Create the KeyFrame with the EventHandler and time delay
      val keyFrame = KeyFrame(delay, onFinished = _ => showCardAtIndex(cardImageViews, index, card))

      // Add the KeyFrame to the Timeline
      timeline.keyFrames.add(keyFrame)
    }

    // Play the card animation
    timeline.play()
    val file = getClass.getResource("/sounds/spreadCard.wav")
    PlaySound.playSoundEffect(file)
  }

  def updateShownHand(): Unit = {
    val currentPlayer = game.getNextPlayer()
    println("Player updated to: " + currentPlayer.getPlayerID())
    val currentPlayerHand = currentPlayer.showHand()

    val cardImageViews = List(card1,card2,card3,card4,card5,card6,card7,card8,card9,card10,card11,card12,card13)

    for ((card, index) <- currentPlayerHand.zipWithIndex) {

      if (index < cardImageViews.length){
        cardImageViews(index).image = card.getImage()
        cardImageViews(index).visible = true // Show the card ImageView node
      }
    }
  }

  def resetCardTranslations(): Unit = {
    val cardImageViews = List(card1, card2, card3, card4, card5, card6, card7, card8, card9, card10, card11, card12, card13)

    // Reset the translateY property of all card ImageViews
    cardImageViews.foreach(_.translateY = 0)
  }

  def handleCardPress(action: MouseEvent): Unit = {
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
      // If it's not, add it to the list and move the card up a bit
      currentPlayer.addSelectedCard(card)
      cardImageViews(cardIndex).translateY = -30
    }
  }

  def handleMouseDrag(action: MouseEvent): Unit = {

  }

  def handleMouseRelease(action: MouseEvent): Unit = {

  }

  def handlePassButton(action: ActionEvent): Unit = {
    val currentPlayer = game.getCurrentPlayer()
    currentPlayer.clearSelectedCard()
    currentPlayer.setTurn(false)

    resetCardTranslations()
    updateShownHand()

  }

  def handleDealButton(action: ActionEvent): Unit = {
    val currentPlayer = game.getCurrentPlayer()
    currentPlayer.clearSelectedCard()
    currentPlayer.setTurn(false)

    resetCardTranslations()
    updateShownHand()
  }

  def handleEndGame(action: ActionEvent): Unit = {
    game.gamefinished = true
    Main.showMainMenuScene()
  }

  def handleBack(action: ActionEvent): Unit = {
    Main.showMainMenuScene()
  }

}
