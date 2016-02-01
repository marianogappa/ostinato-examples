import org.scalajs.dom.html
import ostinato.chess.core.NotationParser._

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
  def currentFenBoard() = {
    if (currentPosition == -1)
      ChessGame.defaultGame.board.toFen
    else
      positions(currentPosition)
  }

  @JSExport
  def currentHumanPlayer() = {
    if (currentPosition % 2 == 0) "black" else "white"
  }

  lazy val buttons = """<button id="previous" onclick="javascript:OstinatoParserExampleApp().previous()">&lt</button>
                       |<button id="next" onclick="javascript:OstinatoParserExampleApp().next()">&gt</button>
                       |""".stripMargin

  @JSExport
  def refresh(input: html.Input, target: html.Div) = {
    val results = NotationParser.parseMatchString(input.value).results

    results.head match {
      case ParsedMatch(steps, notationRules) ⇒
        currentPosition = -1
        render()
        positions = steps.filter(_.maybeGameStep.nonEmpty).map(_.maybeGameStep.get.board.toFen).toArray

        target.innerHTML = buttons +
          steps.zipWithIndex.grouped(2).zipWithIndex.map {
            case (states: List[(ParseStep, Int)], index: Int) ⇒
              s"""<div class="line">${index + 1}. ${renderLineWithErrors(states)}</div>"""
          }.mkString + s"""<br/><div class="line" style="width: 100px">""" + showNotation(notationRules) + "</div>"
    }
  }

  private def showNotation(notationRules: ParsingResult) = notationRules match {
    case FailedParse(None) ⇒
      "What is this?"
    case FailedParse(Some(r: NotationRules)) ⇒
      "<b>Most likely:</b><br/> " + r.fullName
    case SuccessfulParse(r: NotationRules) ⇒
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

  def renderLineWithErrors(line: List[(ParseStep, Int)]) = line.map {
    case (ParseStep(rawAction: String, Some(GameStep(action: ChessAction, board: ChessBoard))), halfIndex: Int) ⇒
      s"""<a href="javascript:OstinatoParserExampleApp().currentPosition=$halfIndex; javascript:OstinatoParserExampleApp().render()">${rawAction}</a>"""
    case (ParseStep(rawAction: String, None), halfIndex: Int) ⇒
      s"""<i>$rawAction</i>"""
  }.mkString(" ")

}
