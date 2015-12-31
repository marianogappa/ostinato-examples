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

    def doAction(): SetTimeoutHandle = {
      board = ai(board.turn).nextNonFinalAction(board.game) match {
        case Some(a) ⇒ board.doAction(a).get
        case None    ⇒ initialBoard
      }
      Board.position(board.toFen)

      setTimeout(600.millis) { doAction() }
    }

    doAction()
  }
}

@JSName("board")
@js.native
object Board extends js.Object {
  def position(a: String): Unit = js.native
}
