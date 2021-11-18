package stasiak.radoslaw.munro

import java.io.FileInputStream
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class MunroDataParser(
    private val inputStream: FileInputStream,
    private val delimiter: String
) {
    private val requiredColumnList = listOf("Name", "Height (m)", "Post 1997", "Grid Ref")
    private val list: ArrayList<String> = arrayListOf()
    private var headerList: List<String> = listOf()
    private var headerListMap: HashMap<String, Int> = hashMapOf()

    init {
        try {
            val scanner = Scanner(inputStream, "UTF-8")
            var lineNumber = 0
            while (scanner.hasNextLine()) {
                val line = scanner.nextLine()
                if (lineNumber == 0) {
                    headerList = line.split(delimiter).map { it.trim() }
                    line.split(delimiter)
                        .forEachIndexed { index, value ->
                            if (requiredColumnList.contains(value.trim())) headerListMap[value.trim()] = index
                        }

                } else {
                    list.add(line)
                }
                lineNumber++

            }

            if (scanner.ioException() != null) {
                throw scanner.ioException()
            }

            //validate header data and throw an error whenever any of the fields are missing
            //go through the map and see which one is missing and add an error
            //throw an error?

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
    fun getHeaderListMap(): Map<String, Int> = headerListMap
}

//enum class MunroDataParserConfig(val value: String) {
//    DEFAULT(value = ",")
//}