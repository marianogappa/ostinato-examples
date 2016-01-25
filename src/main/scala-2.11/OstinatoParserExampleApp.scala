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

  @JSExport
  def currentFenBoard() =
    if (currentPosition == -1)
      ChessGame.defaultGame.board.toFen
    else
      positions(currentPosition)

  lazy val buttons = """<button id="previous" onclick="javascript:OstinatoParserExampleApp().previous()">&lt</button>
                       |<button id="next" onclick="javascript:OstinatoParserExampleApp().next()">&gt</button>
                       |""".stripMargin

  @JSExport
  def refresh(input: html.Input, target: html.Div) = {
    val results = NotationParser.parseMatchString(input.value).results

    results.head match {
      case (steps, notationRules) ⇒
        currentPosition = -1
        render()
        positions = steps.filter(_._2.nonEmpty).map(_._2.get._2.toFen).toArray

        target.innerHTML = buttons +
          steps.zipWithIndex.grouped(2).zipWithIndex.map {
            case (states: List[((String, Option[(ChessAction, ChessBoard)]), Int)], index: Int) ⇒
              s"""<div class="line">${index + 1}. ${renderLineWithErrors(states)}</div>"""
          }.mkString + s"""<br/><div class="line" style="width: 100px">""" + showNotation(notationRules) + "</div>"
    }
  }

  private def showNotation(notationRules: Either[Option[NotationRules], NotationRules]) = notationRules match {
    case Left(None) ⇒
      "What is this?"
    case Left(Some(r: NotationRules)) ⇒
      "<b>Most likely:</b><br/> " + r.fullName
    case Right(r: NotationRules) ⇒
      "<b>Parsed as:</b><br/> " + r.fullName
  }

  @JSExport
  def render() = Board.position(currentFenBoard())

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

  def renderLineWithErrors(line: List[((String, Option[(ChessAction, ChessBoard)]), Int)]) = line.map {
    case ((rawAction: String, Some((action: ChessAction, board: ChessBoard))), halfIndex: Int) ⇒
      s"""<a href="javascript:OstinatoParserExampleApp().currentPosition=$halfIndex; javascript:OstinatoParserExampleApp().render()">${rawAction}</a>"""
    case ((rawAction: String, None), halfIndex: Int) ⇒
      s"""<i>$rawAction</i>"""
  }.mkString(" ")

}
