package dev.borowiecki.core

import scala.util.Using

object Reader {

  // CSV with a header line; values are trimmed so names may contain spaces ("Zielona Góra")
  private def readCsv(filePath: String): List[List[String]] =
    Using.resource(io.Source.fromFile(filePath))(_.getLines().toList)
      .drop(1)
      .filter(_.trim.nonEmpty)
      .map(_.split(",").map(_.trim).toList)

  private def skip(filePath: String, row: List[String]): Nil.type = {
    System.err.println(s"$filePath: skipping invalid line: ${row.mkString(",")}")
    Nil
  }

  // City,Latitude,Longitude
  def readLocationFile(filePath: String): Map[String, (Double, Double)] =
    readCsv(filePath).flatMap {
      case city :: lat :: lon :: Nil if lat.toDoubleOption.isDefined && lon.toDoubleOption.isDefined =>
        List(city -> (lat.toDouble, lon.toDouble))
      case row =>
        skip(filePath, row)
    }.toMap

  // From,To,Km - roads go both ways; negative km is rejected because Dijkstra / A* are wrong with it
  def readGraphFile(filePath: String): Map[Connection[String], Int] =
    readCsv(filePath).flatMap {
      case a :: b :: km :: Nil if km.toIntOption.exists(_ >= 0) =>
        List(Connection(a, b) -> km.toInt, Connection(b, a) -> km.toInt)
      case row =>
        skip(filePath, row)
    }.toMap
}
