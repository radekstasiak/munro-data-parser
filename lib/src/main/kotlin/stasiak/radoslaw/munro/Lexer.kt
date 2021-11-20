package stasiak.radoslaw.munro

class Lexer(val csvRecord: String, val delimiter: Char) {

    val result: ArrayList<String> = arrayListOf()
    var previousChar: Char? = null
    var currentPosition = 0

    private fun readRegularColumn(): String {
        val columnEndPosition = csvRecord.indexOf(delimiter, currentPosition + 1)
        //get the value between current and next delimiters
        val result = csvRecord.substring(currentPosition + 1, columnEndPosition)
        //update current position to the next delimiter position
        currentPosition = columnEndPosition - 1
        return result
    }

    //"this, is"" test",
    private fun readQuoteColumn(): String {
        val token = StringBuffer()
        var previousChar: Char? = null
        //get to the next position after delimiter
        currentPosition++
        while (true) {
            val currentChar = csvRecord[currentPosition]
            var appendChar = true
            if (currentChar.isQuoteChar()) {

                //don't append first opening quote
                if (csvRecord.isSOL(currentPosition)) appendChar = false

                //if next char is a quote skip current one
                //also check if previous one was a quote to cover multiple quotes characters in a row
                if (!csvRecord.isEOL(currentPosition) && csvRecord[currentPosition + 1].isQuoteChar() && previousChar.isQuoteChar()) {
                    appendChar = false
                }

                if (!csvRecord.isEOL(currentPosition) && csvRecord[currentPosition + 1] == ",".single()) {
                    break
                }
            }
            if (appendChar) token.append(currentChar)
            previousChar = currentChar
            currentPosition++
        }
        return token.toString()
    }


    init {
        val token = StringBuffer()

        var isQuoteColumn = false

        while (currentPosition < csvRecord.length) {
            val currentChar = csvRecord[currentPosition]
            //check if first or last element of the row is empty field
            if ((csvRecord.isSOL(currentPosition) || csvRecord.isEOL(currentPosition)) && currentChar == delimiter) {
                result.add(token.toString())
                token.setLength(0)
            }


            //we reach next column
            if (!csvRecord.isEOL(currentPosition)) {
                //check if next column contains quoted value
                val value = if (csvRecord[currentPosition + 1].isQuoteChar()) {
                    readQuoteColumn()
                } else {
                    readRegularColumn()
                }

                result.add(value)
            }

            previousChar = currentChar
            currentPosition++
        }
        //use for ?
//        csvRecord.toCharArray().forEachIndexed { index, char ->
//            //delimiter detected
//            if (char == delimiter && !isQuoteColumn) {
//                if (currentPosition == 0 || token.toString().isNotBlank() || previousChar == delimiter) {
//                    result.add(token.toString())
//                    token.setLength(0)
//                }
//                //check if first value is a field containing delimiter
//            } else if (char.isDoubleQuote()) {
//                //check if we've already begun parsing field with delimiter
//                if (!isQuoteColumn) {
//                    isQuoteColumn = true
//                } else {
//                    if (isEOL(currentPosition)) {
//                        result.add(token.toString())
//                        token.setLength(0)
//                        isQuoteColumn = false
//                    } else if (currentPosition < csvRecord.length && csvRecord[currentPosition + 1] != "\"".single() && previousChar != "\"".single()) {
//                        result.add(token.toString())
//                        token.setLength(0)
//                        isQuoteColumn = false
//                    }else if(previousChar == "\"".single()){
//                        token.append("\"")
//                    }
//                }
//            } else {
//                token.append(char)
//            }
//
//            previousChar = char
//            currentPosition++
//        }

    }

    private fun Char?.isQuoteChar() = this == "\"".single()
    private fun String.isEOL(currentPosition: Int): Boolean = currentPosition == this.length - 1
    private fun String.isSOL(currentPosition: Int): Boolean = currentPosition == 0
}