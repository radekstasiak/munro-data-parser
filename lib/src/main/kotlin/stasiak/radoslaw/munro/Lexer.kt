package stasiak.radoslaw.munro

class Lexer(val csvRecord: String, val delimiter: Char) {

    val result: ArrayList<String> = arrayListOf()
//    var previousChar = "".single()

    init {
        val token = StringBuffer()
        var currentPosition = 0
        var doubleQuoteDetected = false

        //use for ?
        csvRecord.toCharArray().forEachIndexed { index, char ->
            if (currentPosition == 0) {
                //check if first value is empty
                if (char == delimiter) {
                    token.append("")
                    result.add(token.toString())
                    token.setLength(0)
                    //check if first value is a field containing delimiter
                } else if (char.isDoubleQuote()) {
                    //check if we've already begun parsing field with delimiter
                    if (!doubleQuoteDetected) {
                        token.append("\"")
                        doubleQuoteDetected = true
                    } else {
                        token.append("\"")
                        result.add(token.toString())
                        token.setLength(0)
                        doubleQuoteDetected = false
                    }
                } else{
                    token.append(char)
                }

            }else{
                if (char == delimiter && !doubleQuoteDetected) {
                    //TODO fix empty field case
                    if(token.toString().isNotBlank())result.add(token.toString())
                    if(token.toString().isNotBlank())token.setLength(0)
                    //check if first value is a field containing delimiter
                } else if (char.isDoubleQuote()) {
                    //check if we've already begun parsing field with delimiter
                    if (!doubleQuoteDetected) {
                        token.append("\"")
                        doubleQuoteDetected = true
                    } else {
                        token.append("\"")
                        result.add(token.toString())
                        token.setLength(0)
                        doubleQuoteDetected = false
                    }
                } else{
                    token.append(char)
                }
            }
//            previousChar = char
            currentPosition++
        }
//        while (currentPosition <= csvRecord.length) {
//            val currentChar = csvRecord.get(currentPosition)
//            if (currentPosition == 0) {
//
//            }
//        }
//        while (currentPosition <= csvRecord.length) {
//            val currentChar = csvRecord.get(currentPosition)
//            if (currentPosition == 0) {
//                if (currentChar == delimiter) {
//                    token.append("")
//                    result.add(token.toString())
//                    token.r
//                }
//            }
//            currentPosition++
//        }
    }

    private fun Char.isDoubleQuote() = this == "\"".single()
}