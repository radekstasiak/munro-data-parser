package stasiak.radoslaw.munro

class Lexer(val csvRecord: String, val delimiter: Char) {

    val result: ArrayList<String> = arrayListOf()
    var previousChar: Char? = null

    init {
        val token = StringBuffer()
        var currentPosition = 0
        var doubleQuoteDetected = false

        //use for ?
        csvRecord.toCharArray().forEachIndexed { index, char ->
            //delimiter detected
            if (char == delimiter && !doubleQuoteDetected) {
                //TODO fix empty field case
                if (currentPosition == 0 || token.toString().isNotBlank() || previousChar == delimiter) {
                    result.add(token.toString())
                    token.setLength(0)
                }
                //check if first value is a field containing delimiter
            } else if (char.isDoubleQuote()) {
                //check if we've already begun parsing field with delimiter
                if (!doubleQuoteDetected) {
                    token.append("\"")
                    doubleQuoteDetected = true
                } else {
                    token.append("\"")
                    if (isEOL(currentPosition)) {
//                    if (currentPosition < csvRecord.length && csvRecord[currentPosition + 1] != "\"".single()) {
                        result.add(token.toString())
                        token.setLength(0)
                        doubleQuoteDetected = false
                    } else if (currentPosition < csvRecord.length && csvRecord[currentPosition + 1] != "\"".single() && previousChar != "\"".single()) {
                        result.add(token.toString())
                        token.setLength(0)
                        doubleQuoteDetected = false
                    }

                }
            } else {
                token.append(char)
            }
//            }

            previousChar = char
            currentPosition++
        }

    }

    private fun Char.isDoubleQuote() = this == "\"".single()
    private fun isEOL(currentPosition: Int): Boolean = currentPosition == csvRecord.length - 1
}