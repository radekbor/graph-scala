package dev.borowiecki.dijkstra

import dev.borowiecki.core.{Connection, Reader}

import scala.util.{Try, Using}

object App {
  

  def main(arg: Array[String]): Unit = {
    arg.toList match {
      case file :: from :: to :: Nil =>
        val map = Reader.readGraphFile(file)
        
        println(Algorithm.buildMap(map, from)(to).fold(s"No route from $from to $to")(km => s"$from -> $to: $km km"))
      case _ =>
        System.err.println("Invalid arguments, expected 3 , file, from , to")
    }
  }

}

