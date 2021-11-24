# munro-data-parser

The goal of the `munro-data-parser` library is to filter and sort CSV data of the Munro tops within Scotland.

## build

to build library run
```
gradlew build
```

 compiled `JAR` can be found under `lib/build/libs/lib.jar`

## usage

to use the library first you need to create an instance of the parser object providing `csv` file as an `InputStream`

```Kotlin
val parser = MunroDataParser(inputStream = yourInputStream)
```

by default parser will use `,` as the delimiter, if you want to use different one then provide it via `delimiter` param

```Kotlin
val parser = MunroDataParser(inputStream = inputStream, delimiter = ".")
```

you can get list of parsed results using `getResults()`, method which returns list of `MunroDataModel` objects

```Kotlin
parser.getResults(): List<MunroDataModel>

data class MunroDataModel(
    val name: String,
    val heightInMeters: Double,
    val hillCategory: String,
    val gridRef: String
)
```

if any of the required fields within provided `csv` file is empty, parser will default its value to either empty `String` or `0.0` for `Double` values

### filtering and sorting data

you can add multiple filters and define sorting behaviour using `MunroDataQuery.Builder()`

```Kotlin
     val munroDataQuery = MunroDataQuery.Builder()
                            .filterByHillCategory(MunroDataQuery.MunroDataHillCategory.MUNRO) //MUNRO, TOP or DEFAULT
                            .setMinHeightInMeters(945.7) //double
                            .setMaxHeightInMeters(948.0) //double
                            .setResultsLimit(3) //int
                            .setSortingRule(MunroDataQuerySortingRules.SortByHeightInMeters(ascending = true) // or SortAlphabeticallyByName(ascending:Boolean)
                            .build()

    parser.getResults(query = munroDataQuery)

```

available filters are defined in the `MunroDataQueryFilters` class

```Kotlin
enum class MunroDataHillCategory(val value: String) {
    DEFAULT(value = ""), //user in parser by default and returns either Munro or Top hills
    MUNRO(value = "MUN"),
    TOP(value = "TOP")
}
```
data can be also sorted using one of the sorting rules available

```Kotlin
sealed class MunroDataQuerySortingRules {
    object NoSorting : MunroDataQuerySortingRules() //used in parser by default
    data class SortByHeightInMeters(val ascending: Boolean = false) : MunroDataQuerySortingRules()
    data class SortAlphabeticallyByName(val ascending: Boolean = false) : MunroDataQuerySortingRules()
}
```
## csv parsing rules

Included parser applies following rules when to parse cvs files:

- detects quoted fields and parses them as a single value, including special characters like:
    - delimiter (by default:`,`)
    - double `"` characters
- ignores empty lines
- includes empty values

## known limitiations

- inside a quote field, a multiple occurences of `"` character are transformed into single `"` character

## useful links

- https://en.wikipedia.org/wiki/Comma-separated_values
- https://en.wikipedia.org/wiki/Munro
- http://www.hills-database.co.uk/downloads.html
