package dev.borowiecki.astar

import dev.borowiecki.core.Reader

object App {

  def main(arg: Array[String]): Unit = {
    arg.toList match {
      case graphFile :: locationFile :: from :: to :: Nil =>
        val map = Reader.readGraphFile(graphFile)
        val locationMap = Reader.readLocationFile(locationFile)
        println(Algorithm.buildMap(map, from, locationMap)(to).fold(s"No route from $from to $to")(km => s"$from -> $to: $km km"))
      case _ =>
        System.err.println("Invalid arguments, expected 4: graphFile, locationFile, from, to")
    }
  }

}
