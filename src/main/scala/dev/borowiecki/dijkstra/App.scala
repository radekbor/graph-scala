package dev.borowiecki.dijkstra

import scala.util.{Try, Using}

object App {

  private def readFile(filePath: String): Map[Connection[String], Int] = {
    val lines = Using.resource(io.Source.fromFile(filePath))(raw => raw.getLines().toList)
    lines.map(_.split(" ").toList).flatMap {
      case a :: b :: l :: Nil =>
        Try(l.toInt)
          .toOption
          .filter(_ >= 0)
          .toList
          .flatMap(
            len => Seq(
              Connection(a, b) -> len,
              Connection(b, a) -> len
            )
          )
      case _ =>
        None
    }.toMap
  }

  def main(arg: Array[String]): Unit = {
    arg.toList match {
      case file :: from :: to :: Nil =>
        val map = readFile(file)
        println(Algorithm.buildMap(map, from)(to).fold(s"No route from $from to $to")(km => s"$from -> $to: $km km"))
      case _ =>
        System.err.println("Invalid arguments, expected 3 , file, from , to")
    }
  }

}

