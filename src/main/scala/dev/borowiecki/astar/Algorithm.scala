package dev.borowiecki.astar

import dev.borowiecki.core.Connection

import scala.annotation.tailrec

object Algorithm {


  def heuristic[K](geoLocation: Map[K, (Double, Double)])(a: K, b: K): Option[Double] =
    for {
      (lat1, lon1) <- geoLocation.get(a)
      (lat2, lon2) <- geoLocation.get(b)
    } yield Math.sqrt(Math.pow(latToKm(lat2 - lat1), 2) + Math.pow(lonToKm(lon2 - lon1), 2))

  // shortcut: fixed values for Poland (lat 49-55°N); a longitude degree is shorter further north, so use the northern value
  private def latToKm(degrees: Double): Double = degrees * 111
  private def lonToKm(degrees: Double): Double = degrees * 64

  def buildMap[K](map: Map[Connection[K], Int], a: K, geoLocation: Map[K, (Double, Double)])(b: K): Option[Int] = {
    def fromNode(k: K): List[(Connection[K], Int)] = {
      map.collect {
        case (value, i) if value.a == k =>
          value -> i
      }.toList
    }

    def estimateToB(k: K): Double = heuristic(geoLocation)(k, b).getOrElse(0.0)

    def byEstimate(distances: Map[K, Int])(queue: List[(Connection[K], Int)]): List[(Connection[K], Int)] =
      queue.sortBy { case (connection, l) => distances(connection.a) + l + estimateToB(connection.b) }

    @tailrec
    def process(queue: List[(Connection[K], Int)], distances: Map[K, Int]): Option[Int] = {
      queue match {
        case Nil =>
          None
        case (connection, l) :: tail =>
          val candidate = distances(connection.a) + l

          if (connection.b == b) {
            // first road into the target taken from the sorted queue is the shortest one, so stop early
            Some(candidate)
          } else if (candidate < distances.getOrElse(connection.b, Int.MaxValue)) {
            val newDistances = distances + (connection.b -> candidate)
            process(byEstimate(newDistances)(tail ::: fromNode(connection.b)), newDistances)
          } else {
            process(tail, distances)
          }
      }
    }

    val distancesFromA: Map[K, Int] = Map(a -> 0)
    if (a == b) Some(0)
    else process(byEstimate(distancesFromA)(fromNode(a)), distancesFromA)
  }

}
