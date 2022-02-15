package stasiak.radoslaw.munro

internal class CSVRecordParser constructor(private val csvRecord: String, private val delimiter: Char) {

    private val quoteChar = "\"".single()
    val result: ArrayList<String> = arrayListOf()
    private var currentPosition = 0

    init {
        val token = StringBuffer()
        while (currentPosition < csvRecord.length) {
            val currentChar = csvRecord[currentPosition]
            //check if first or last element of the row is an empty field
            if ((csvRecord.isSOL(currentPosition) || csvRecord.isEOL(currentPosition)) && currentChar == delimiter) {
                //to cover case of a record containing only 2 empty values i.e.","
                if (csvRecord == delimiter.toString()) result.add(token.toString())
                result.add(token.toString())
                token.setLength(0)
            }

            if (!csvRecord.isEOL(currentPosition)) {
                //check if column contains quoted value
                val value =
                    if (csvRecord[currentPosition].isQuoteChar() || csvRecord[currentPosition + 1].isQuoteChar()) {
                        readQuoteColumn()
                    } else {
                        val startIndex = if (currentChar == delimiter) currentPosition + 1 else currentPosition
                        readRegularColumn(startIndex)
                    }
                result.add(value)
            }
            currentPosition++
        }

    }

    private fun readRegularColumn(startIndex: Int): String {
        val columnEndPosition = csvRecord.indexOf(delimiter, startIndex)
        //get the value between current and next delimiters or end of the line
        val result =
            csvRecord.substring(startIndex, if (columnEndPosition != -1) columnEndPosition else csvRecord.length)
        //update current position to the next delimiter position or end of the line
        currentPosition = if (columnEndPosition != -1) columnEndPosition - 1 else csvRecord.length
        return result
    }

    private fun readQuoteColumn(): String {
        val token = StringBuffer()
        val currentColumn = csvRecord.substring(currentPosition + 1, csvRecord.length)
        var columnPosition = 0
        while (true) {
            val currentChar = currentColumn[columnPosition]
            var appendChar = true
            if (currentChar.isQuoteChar()) {

                //don't append first opening quote
                if (currentColumn.isSOL(columnPosition)) appendChar = false

                //if next char is a quote skip current one
                if (!currentColumn.isEOL(columnPosition) && currentColumn[columnPosition + 1].isQuoteChar()) {
                    appendChar = false
                }

                //check if it's the end of the column or end of the line
                if ((!currentColumn.isEOL(columnPosition) && currentColumn[columnPosition + 1] == delimiter)
                    || currentColumn.isEOL(columnPosition)
                ) {
                    break
                }
            }
            if (appendChar) token.append(currentChar)
            columnPosition++
        }
        currentColumn.substring(0, columnPosition)
        currentPosition += columnPosition + 1
        return token.toString()
    }

    private fun Char?.isQuoteChar() = this == quoteChar
    private fun String.isEOL(currentPosition: Int): Boolean = currentPosition == this.length - 1
    private fun String.isSOL(currentPosition: Int): Boolean = currentPosition == 0
}