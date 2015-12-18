import ostinato.chess.ai.ChessRandomAi

import scala.scalajs.js
import scala.scalajs.js.JSApp
import scala.scalajs.js.annotation.JSName
import scala.scalajs.js.timers._
import scala.concurrent.duration._
import ostinato.chess.core._

object OstinatoExampleApp extends JSApp {
  def main(): Unit = {
    val initialBoard = ChessGame.defaultGame.board
    var board: ChessBoard = initialBoard
    val ai: Map[ChessPlayer, ChessRandomAi] = Map(
      WhiteChessPlayer -> ChessRandomAi(WhiteChessPlayer),
      BlackChessPlayer -> ChessRandomAi(BlackChessPlayer)
    )

    def move() = {
      board = ai(board.turn).move(board.game) match {
        case m: DrawMovement ⇒ initialBoard
        case m               ⇒ board.move(m)
      }
      Board.position(board.toFen)
    }

    move()
    setInterval(300.millis) {
      move()
    }
  }
}

@JSName("board")
@js.native
object Board extends js.Object {
  def position(a: String): Unit = js.native
}
