import java.io.{FileWriter, BufferedWriter, File}
import org.scalatest._

import play.api.libs.json.{Json, Reads}
import scala.io.Source
import scala.util.parsing.combinator._

abstract class Ast
case class SourceFile(ast: List[Ast]) extends Ast
case class ClassFile(name: String, properties: List[Property] = List(), extend: Option[String]) extends Ast
case class HeaderImport(file: String) extends Ast
case class Dummy(name: String) extends Ast
case class Property(name: String, value: String, typeDef: String, const: Option[String] = None)  extends Ast
case class Enum(name: String, values: List[EnumValue])  extends Ast
case class EnumValue(name: String, value: String)  extends Ast
case class Enums(enums: List[Enum]) extends Ast
case class Classes(enums: List[ClassFile]) extends Ast

class SteamLanguageParser extends JavaTokenParsers {

  lazy val expr = export

  lazy val word = "([a-zA-Z_0-9<>]+)".r

  lazy val export: Parser[Ast] = rep(header | cls | enum) ^^ {
    case ts => SourceFile(ts)
  }

  lazy val header = """#import \"([a-zA-Z]+[.])steamd\"""".r ^^ { x => HeaderImport(x) }

  lazy val className = "class" ~> "([a-zA-Z0-9]+)".r ~ (("<" ~> "([a-zA-Z0-9:]+)".r <~ ">")?)

  lazy val cls = className ~ ("{" ~> rep(classProperty) <~ "};") ~ (("obsolete" ~ (("\"" ~ "([a-zA-Z ])" ~ "\"")?) )?) ^^ {
    case x ~ headerName ~ properties ~ obsolete => ClassFile(x, properties, headerName)
  }

  lazy val enumProp = "([a-zA-Z]+)".r ~ ("=" ~> "([0-9]+)".r <~ ";") ^^ {
    case name ~ value => EnumValue(name, value)
  }

  lazy val enumName = "enum" ~> "([a-zA-Z0-9]+)".r
  lazy val enum = enumName ~ ("{" ~> rep(enumProp) <~ "};") ^^ {
    case name ~ props =>
      println(s"$name => $props")
      Enum(name, props)
  }

  lazy val enums = rep(enum) ^^ {
    case x =>
      println(x)
      Enums(x)
  }

  lazy val classes = rep(cls) ^^ {
    case x => Classes(x)
  }

  lazy val propDef = "=" ~> """([a-zA-Z:0-9_\"]+)""".r

  lazy val marshals = "steamidmarshal" | "boolmarshal" | "gameidmarshal"

  lazy val classProperty = (("const" | marshals)?) ~ word ~ word ~ ( propDef ? ) <~ ";" ^^ {
    case marshal ~ typeDef ~ name ~ prop => Property(name, prop.getOrElse(""), typeDef, marshal)
  }
}

case class GithubFile(path: String, name: String)

class ParserTest extends FlatSpec with Matchers {

  // https://raw.github.com/SteamRE/SteamKit/master/Resources/SteamLanguage/steammsg.steamd

  implicit val reader = Json.reads[GithubFile]


  def parseFile(contents: String) = {
    val parser = new SteamLanguageParser
    val result = parser.parseAll(parser.expr, contents)
    //println(result)

    result.get match {
      case SourceFile(ast) =>
        var fileContents = ""
        ast.foreach {
        case HeaderImport(header) =>
          fileContents += "header: " + header
        case ClassFile(name, properties, typeName) =>

        fileContents +=  s"class $name extends SteamSerializableMessage {"
        typeName.map { x =>
          fileContents += s"  var msg = $x"
        }

        var streamWrite = ""
        var streamRead = ""

        properties.foreach { x =>
          var prop = s"  var ${x.name}"
          if (x.value.isEmpty) prop += s": ${x.typeDef}"
          else prop += s" = ${x.value}"

          streamWrite += s"    stream.write(${x.name})\n"
          streamRead += s"    ${x.name} = stream.read(${x.name})\n"

          fileContents += prop
        }



        fileContents += s"""
          |  def serialize(stream: BinaryWriter) {
          |$streamWrite
          |  }
          |
          |  def deserialize(stream: BinaryReader) {
          |$streamRead
          |  }
        """.stripMargin

          fileContents += "}"


        case Enum(name, enumValues) =>
        //println(s"enum $name")
        //enumValues.foreach(x => println(s"${x.name} => ${x.value}"))
      }

      fileContents
    }
  }

  "my parser" should "read header imports" in {

    val steamlanguageFiles = Source.fromURL("https://api.github.com/repos/steamre/steamkit/contents/Resources/SteamLanguage").mkString
    val source = Source.fromURL("https://raw.github.com/SteamRE/SteamKit/master/Resources/SteamLanguage/steammsg.steamd").mkString

    val githubFiles = Json.parse(steamlanguageFiles).as[List[GithubFile]]
    githubFiles.foreach { file =>

      try {
        val sourceFile = Source.fromURL(s"https://raw.github.com/SteamRE/SteamKit/master/${file.path}").mkString
        val classContents = parseFile(sourceFile)

        // FileWriter
        val classFile = new File("/home/rovak/steam/" + file.name)

        val bw = new BufferedWriter(new FileWriter(classFile))
        bw.write(classContents)
        bw.close()
      }
      catch {
        case e: Exception => println("failed: " + file.name)
      }

    }

    "test" should be ("test")
  }
}