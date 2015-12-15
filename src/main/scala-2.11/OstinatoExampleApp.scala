import scala.scalajs.js
import scala.scalajs.js.JSApp
import scala.scalajs.js.annotation.JSName
import scala.scalajs.js.timers._
import scala.concurrent.duration._
import org.scalajs.dom
import dom.document
import ostinato.chess.core._
import scala.util.Random

object OstinatoExampleApp extends JSApp {
  def main(): Unit = {
    val initialBoard = ChessGame.defaultGame.board
    var board: ChessBoard = initialBoard

    def move() = {
      val movements = board.movements
      board = if (movements.isEmpty) initialBoard else board.move(movements.toList(Random.nextInt(movements.size)))
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
