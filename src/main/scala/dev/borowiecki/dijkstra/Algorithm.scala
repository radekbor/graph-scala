package dev.borowiecki.dijkstra

import scala.annotation.tailrec


object Algorithm {

  def buildMap[K](map: Map[Connection[K], Int], a: K)(b: K): Option[Int] = {

    def fromNode(k: K): List[(Connection[K], Int)] = {
      map.collect {
        case (value, i) if value.a == k =>
          value -> i
      }.toList
    }

    // only reached nodes are present; a missing key means "not reached yet" (no infinity sentinel)
    val distancesFromA: Map[K, Int] = Map(a -> 0)

    // Dijkstra: always relax the edge that gives the shortest candidate distance first.
    // Every queued edge starts at an already reached node, so distances(connection.a) always exists.
    def byCandidate(distances: Map[K, Int])(queue: List[(Connection[K], Int)]): List[(Connection[K], Int)] =
      queue.sortBy { case (connection, l) => distances(connection.a) + l }

    @tailrec
    def process(queue: List[(Connection[K], Int)], distances: Map[K, Int]): Map[K, Int] = {
      queue match {
        case Nil =>
          distances
        case (connection, l) :: tail =>
          val candidate = distances(connection.a) + l
          val currentLen = distances.getOrElse(connection.b, Int.MaxValue) // not reached yet

          if (candidate < currentLen) {
            val newDistances = distances + (connection.b -> candidate)
            process(byCandidate(newDistances)(tail ::: fromNode(connection.b)), newDistances)
          } else {
            process(tail, distances)
          }
      }
    }

    val result = process(queue = byCandidate(distancesFromA)(fromNode(a)), distances = distancesFromA)
    result.get(b)
  }
}
