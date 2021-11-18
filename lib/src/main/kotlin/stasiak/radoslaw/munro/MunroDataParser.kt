package stasiak.radoslaw.munro

import java.io.FileInputStream
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList


class MunroDataParser(
    private val inputStream: FileInputStream,
    private val delimeter: String
) {
    private val list: ArrayList<String> = arrayListOf()
    private var headerList: List<String> = listOf()

    init {
        try {
            val scanner = Scanner(inputStream, "UTF-8")
            var lineNumber = 0
            while (scanner.hasNextLine()) {
                if (lineNumber == 0) {
                    headerList = scanner.nextLine().split(delimeter)
                } else {
                    val line: String = scanner.nextLine()
                    list.add(line)
                }
                lineNumber++

            }

            if (scanner.ioException() != null) {
                throw scanner.ioException()
            }

            scanner.close()
        } catch (exception: IOException) {

//        } finally {
//            inputStream.close()
//            if (sca != null) {
//                sc.close()
//            }
//        }
        }


    }

    fun getResults(): List<String> = list
    fun getHeaders(): List<String> = headerList
}

//enum class MunroDataParserConfig(val value: String) {
//    DEFAULT(value = ",")
//}