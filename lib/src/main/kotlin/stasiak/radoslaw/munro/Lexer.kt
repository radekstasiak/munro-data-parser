package stasiak.radoslaw.munro

class Lexer(val csvRecord: String, val delimiter: Char) {

    val result: ArrayList<String> = arrayListOf()
    var previousChar: Char? = null
    var currentPosition = 0

    private fun readRegularColumn(startIndex: Int): String {
        val columnEndPosition = csvRecord.indexOf(delimiter, startIndex)
        //get the value between current and next delimiters
        val result = csvRecord.substring(startIndex, columnEndPosition)
        //update current position to the next delimiter position
        currentPosition = columnEndPosition - 1
        return result
    }

    //"this, is"" test",
    private fun readQuoteColumn(): String {
        val token = StringBuffer()
        var previousChar: Char? = null
        val currentColumn = csvRecord.substring(currentPosition + 1, csvRecord.length)
        var columnPosition = 0
        while (true) {
            val currentChar = currentColumn[columnPosition]
            var appendChar = true
            if (currentChar.isQuoteChar()) {

                //don't append first opening quote
                if (currentColumn.isSOL(columnPosition)) appendChar = false

                //if next char is a quote skip current one
                //also check if previous one was a quote to cover multiple quotes characters in a row
                if (!currentColumn.isEOL(columnPosition) && currentColumn[columnPosition + 1].isQuoteChar()) {
                    appendChar = false
                }

                //check if it's the end of the column or end of the line
                if ((!currentColumn.isEOL(columnPosition) && currentColumn[columnPosition + 1] == ",".single())
                    || currentColumn.isEOL(columnPosition)
                ) {
                    break
                }
            }
            if (appendChar) token.append(currentChar)
            previousChar = currentChar
            columnPosition++
        }
        currentColumn.substring(0, columnPosition)
        currentPosition = currentPosition + columnPosition+1
        return token.toString()
    }


    init {
        val token = StringBuffer()
        while (currentPosition < csvRecord.length) {
            val currentChar = csvRecord[currentPosition]
            //check if first or last element of the row is empty field
            if ((csvRecord.isSOL(currentPosition) || csvRecord.isEOL(currentPosition)) && currentChar == delimiter) {
                result.add(token.toString())
                token.setLength(0)
            }

            if (!csvRecord.isEOL(currentPosition)) {
                //check if next column contains quoted value
                val value = if (csvRecord[currentPosition + 1].isQuoteChar()) {
                    readQuoteColumn()
                } else {
                    val startIndex = if (currentChar == ",".single()) currentPosition + 1 else currentPosition
                    readRegularColumn(startIndex)
                }
                println(value)
                result.add(value)
            }
            previousChar = currentChar
            currentPosition++
        }

    }

    private fun Char?.isQuoteChar() = this == "\"".single()
    private fun String.isEOL(currentPosition: Int): Boolean = currentPosition == this.length - 1
    private fun String.isSOL(currentPosition: Int): Boolean = currentPosition == 0
}