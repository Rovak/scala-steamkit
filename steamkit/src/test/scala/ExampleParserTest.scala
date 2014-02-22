


import org.scalatest.{Matchers, FlatSpec}
import scala.util.parsing.combinator.JavaTokenParsers

abstract class Tree
case class Add(t1: Tree, t2: Tree) extends Tree
case class Sub(t1: Tree, t2: Tree) extends Tree
case class Mul(t1: Tree, t2: Tree) extends Tree
case class Div(t1: Tree, t2: Tree) extends Tree
case class Num(t: Double) extends Tree

class ExprParsers2 extends JavaTokenParsers {

  def eval(t: Tree): Double = t match {
    case Add(t1, t2) => eval(t1)+eval(t2)
    case Sub(t1, t2) => eval(t1)-eval(t2)
    case Mul(t1, t2) => eval(t1)*eval(t2)
    case Div(t1, t2) => eval(t1)/eval(t2)
    case Num(t) => t
  }

  lazy val expr: Parser[Tree] = term ~ rep("[+-]".r ~ term) ^^ {
    case t ~ ts => ts.foldLeft(t) {
      case (t1, "+" ~ t2) => Add(t1, t2)
      case (t1, "-" ~ t2) => Sub(t1, t2)
    }
  }

  lazy val term = factor ~ rep("[*/]".r ~ factor) ^^ {
    case t ~ ts => ts.foldLeft(t) {
      case (t1, "*" ~ t2) => Mul(t1, t2)
      case (t1, "/" ~ t2) => Div(t1, t2)
    }
  }

  lazy val factor = "(" ~> expr <~ ")" | num

  lazy val num = floatingPointNumber ^^ { t => Num(t.toDouble) }
}


class ExampleParserTest extends FlatSpec with Matchers  {
  "a parser" should "should" in {
    val parser = new ExprParsers2
    val result =  parser.parseAll(parser.expr, "(1 + 1 - 10) + 100").get
    println(parser.eval(result))

    "test" should be ("test")
  }
}
