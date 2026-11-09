# Testing graphs algorithms  in scala

Inputs (CSV with a header line):
- `polish-roads.csv` - `From,To,Km`, roads between major Polish cities (both directions)
- `polish-cities-geo.csv` - `City,Latitude,Longitude`, used by A* to estimate the remaining distance

Quote city names with spaces: `\"Zielona Góra\"`.

## 1) Dijkstra algorithm

```bash
sbt "runMain dev.borowiecki.dijkstra.App polish-roads.csv Szczecin Lublin"
```

## 2) A* algorithm

```bash
sbt "runMain dev.borowiecki.astar.App polish-roads.csv polish-cities-geo.csv \"Zielona Góra\" Białystok"
```
