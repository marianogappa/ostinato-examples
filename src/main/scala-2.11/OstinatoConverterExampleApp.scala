import org.scalajs.dom.html
import ostinato.chess.core.NotationParser.{GameStep, ParseStep, ParsedMatch}
import ostinato.chess.core._

import scala.scalajs.js.JSApp
import scala.scalajs.js.annotation.JSExport

@JSExport
object OstinatoConverterExampleApp extends JSApp {
  def main(): Unit = {
  }

  @JSExport
  def convert(input: html.Input, target: html.Div, notation: String) = {
    val results = NotationParser.parseMatchString(input.value).results
    val actionParser = getActionParser(notation)

    results.head match {
      case ParsedMatch(steps, notationRules) ⇒

        target.innerHTML =
          steps.zipWithIndex.grouped(2).zipWithIndex.map {
            case (states: List[(ParseStep, Int)], index: Int) ⇒
              s"""<div class="line">${index + 1}. ${renderLineWithErrors(states, actionParser)}</div>"""
          }.mkString
    }
  }

  private def getActionParser(notation: String) = notation match {
    case "Algebraic Notation" =>
      AlgebraicNotationActionParser(
        AlgebraicNotationRules(
          lowerCaseLetters = true, figurine = false, distinguishCaptures = true, colonForCaptures = false, castlingNotation = "zeroes"
        )
      )
    case "Figurine Algebraic Notation" => AlgebraicNotationActionParser(AlgebraicNotation.allPossibleRules.head.copy(figurine = true))
    case "Descriptive Notation" => DescriptiveNotationActionParser(DescriptiveNotation.allPossibleRules.head)
    case "Coordinate Notation" => CoordinateNotationActionParser(CoordinateNotation.allPossibleRules.head)
    case "ICCF Notation" => IccfNotationActionParser(IccfNotation.allPossibleRules.head)
    case "Smith Notation" => SmithNotationActionParser(SmithNotation.allPossibleRules.head)
    case _ => AlgebraicNotationActionParser(
      AlgebraicNotationRules(
        lowerCaseLetters = true, figurine = false, distinguishCaptures = true, colonForCaptures = false, castlingNotation = "zeroes"
      )
    )
  }

  private def showNotation(notationRules: Either[Option[NotationRules], NotationRules]) = notationRules match {
    case Left(None) ⇒
      "What is this?"
    case Left(Some(r: NotationRules)) ⇒
      "<b>Most likely:</b><br/> " + r.fullName
    case Right(r: NotationRules) ⇒
      "<b>Parsed as:</b><br/> " + r.fullName
  }


  def renderLineWithErrors(line: List[(ParseStep, Int)], actionParser: ActionParser) = line.map {
    case (ParseStep(rawAction: String, Some(GameStep(action: ChessAction, board: ChessBoard))), halfIndex: Int) ⇒
      s"""${actionParser.parseAction(action).head._1}"""
    case (ParseStep(rawAction: String, None), halfIndex: Int) ⇒
      s"""<i>$rawAction</i>"""
  }.mkString(" ")

}
