import scala.scalajs.js
import scala.scalajs.js.JSApp
import scala.scalajs.js.annotation.JSName
import scala.scalajs.js.timers._
import scala.concurrent.duration._
import org.scalajs.dom
import dom.document
import boardgame.chess.core._
import scala.util.Random

object OstinatoExampleApp extends JSApp {
  def main(): Unit = {
    val initialBoard = ChessGame.fromString("""♜♞♝♛.♝♞♜
                                              |♟♟♟♟♟♟♟♟
                                              |........
                                              |........
                                              |........
                                              |........
                                              |♙♙♙♙♙♙♙♙
                                              |♖♘♗♕.♗♘♖""".stripMargin).board

    var board: ChessBoard = null
    board = initialBoard
    Board.position(board.toFen)

    setInterval(280.millis) {
      val movements = board.movements

      if (movements.nonEmpty) {
        board = board.move(movements.toList(Random.nextInt(movements.size)))
        Board.position(board.toFen)
      }
      else {
        board = initialBoard
        Board.position(board.toFen)
      }
    }
  }

}

@js.native
object DOMGlobalScope extends js.GlobalScope {
  def alert(message: String): Unit = js.native
}

@JSName("board")
@js.native
object Board extends js.Object {
  def position(a: String): Unit = js.native
}