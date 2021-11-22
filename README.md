# munro-data-parser

The goal of `munro-data-parser` library is to filter and sort CSV data of the Munro tops within Scotland.

## build
to build library run `gradlew build` from the root directory, compiled `JAR` can be found under following path`lib/build/libs/lib.jar`

## usage
TBC

## csv parsing rules
Included parser applies following rules when to parse cvs files:
- requires file to include following headers: `Name, Height (m), Grid Ref, Post 1997`
- detects quoted fields and parses them as a single value, including special characters like:
    -  delimiter (by default:`,`)
    -  double `"` characters
- ignores empty lines
- includes empty values


## known limitiations

- inside a quote field, a multiple occurences of `"` character are transformed into single `"` character

## useful links
- https://en.wikipedia.org/wiki/Comma-separated_values
- https://en.wikipedia.org/wiki/Munro
- http://www.hills-database.co.uk/downloads.html