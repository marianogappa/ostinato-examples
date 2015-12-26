import org.scalajs.dom.html

import scala.scalajs.js.JSApp
import scala.scalajs.js.annotation.JSExport
import ostinato.chess.core._

@JSExport
object OstinatoParserExampleApp extends JSApp {
  def main(): Unit = {
    render()
  }

  @JSExport
  var currentPosition = -1

  @JSExport
  var positions: Array[String] = null

  lazy val buttons = """<button id="previous" onclick="javascript:OstinatoParserExampleApp().previous()">&lt</button>
                       |<button id="next" onclick="javascript:OstinatoParserExampleApp().next()">&gt</button>
                       |""".stripMargin

  @JSExport
  def refresh(input: html.Input, target: html.Div) = {
    Notation.parseMatchString(input.value) match {
      case Right(states: List[(ChessAction, ChessBoard)]) ⇒
        currentPosition = -1
        render()
        positions = states.map(_._2.toFen).toArray

        target.innerHTML = buttons +
          states.zipWithIndex.grouped(2).zipWithIndex.map {
            case (states: List[((ChessAction, ChessBoard), Int)], index: Int) ⇒
              s"""<div class="line">${index + 1}. ${renderLine(states)}</div>"""
          }.mkString

      case Left(states: List[(String, Option[(ChessAction, ChessBoard)])]) ⇒
        currentPosition = -1
        render()
        positions = states.filter(_._2.nonEmpty).map(_._2.get._2.toFen).toArray

        target.innerHTML = buttons +
          states.zipWithIndex.grouped(2).zipWithIndex.map {
            case (states: List[((String, Option[(ChessAction, ChessBoard)]), Int)], index: Int) ⇒
              s"""<div class="line">${index + 1}. ${renderLineWithErrors(states)}</div>"""
          }.mkString
    }
  }

  @JSExport
  def render() = {
    if (currentPosition == -1)
      Board.position(ChessGame.defaultGame.board.toFen)
    else
      Board.position(positions(currentPosition))
  }

  @JSExport
  def next() = {
    if (currentPosition < positions.length - 1) {
      currentPosition += 1
      render()
    }
  }

  @JSExport
  def previous() = {
    if (currentPosition > -1) {
      currentPosition -= 1
      render()
    }
  }

  def renderLine(line: List[((ChessAction, ChessBoard), Int)]) = line.map {
    case ((action: ChessAction, board: ChessBoard), halfIndex: Int) ⇒
      s"""<a href="javascript:OstinatoParserExampleApp().currentPosition=$halfIndex; javascript:OstinatoParserExampleApp().render()">${action.toAn.toString}</a>"""
  }.mkString(" ")

  def renderLineWithErrors(line: List[((String, Option[(ChessAction, ChessBoard)]), Int)]) = line.map {
    case ((rawAction: String, Some((action: ChessAction, board: ChessBoard))), halfIndex: Int) ⇒
      s"""<a href="javascript:OstinatoParserExampleApp().currentPosition=$halfIndex; javascript:OstinatoParserExampleApp().render()">${action.toAn.toString}</a>"""
    case ((rawAction: String, None), halfIndex: Int) ⇒
      s"""<b>$rawAction</b>"""
  }.mkString(" ")

}
