package ch.makery.bigtwo.controllers

import ch.makery.bigtwo.Main
import ch.makery.bigtwo.entities.{Deck, Game, Player}
import ch.makery.bigtwo.util.GameLogic
import javafx.fxml.FXML
import scalafx.animation.AnimationTimer
import scalafx.event.ActionEvent
import scalafx.scene.image.{Image, ImageView}
import scalafxml.core.macros.sfxml

@sfxml
class GameController (@FXML var leftPlayer: ImageView,
                      @FXML var rightPlayer: ImageView,
                      @FXML var middlePlayer: ImageView){

  private val deck: Deck = new Deck()
  private var game: Game = new Game(List(
    new Player(1),
    new Player(2),
    new Player(3),
    new Player(4)
  ), deck)
  // Animation images for each player
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

  // Additional variable to store the current frame index for each player
  private var currentFrameIndices: Map[Int, Int] = Map(
    1 -> 0,
    2 -> 0,
    3 -> 0,
    4 -> 0
  )

  // Animation timer for player animations
  private var animationTimer: AnimationTimer = _

  def startGame(): Unit = {
    GameLogic.initializeGame(game)
    startPlayerAnimations()
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

  def handlePassButton(action: ActionEvent): Unit = {
    val currentPlayer = game.getCurrentPlayer()
    currentPlayer.setTurn(false)
  }

  def handleDealButton(action: ActionEvent): Unit = {
    val currentPlayer = game.getCurrentPlayer()
    currentPlayer.setTurn(false)
  }

  def handleEndGame(action: ActionEvent): Unit = {
    game.gamefinished = true
    Main.showMainMenuScene()
  }

  def handleBack(action: ActionEvent): Unit = {
    Main.showMainMenuScene()
  }

}
