package stasiak.radoslaw.munro

import stasiak.radoslaw.munro.model.MunroDataRecord
import java.io.FileInputStream
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class MunroDataParser(
    private val inputStream: FileInputStream,
    private val delimiter: String
) {
    private val requiredColumnKeyName = "Name"
    private val requiredColumnKeyHeightInMeters = "Height (m)"
    private val requiredColumnKeyHillCategory = "Post 1997"
    private val requiredColumnKeyGridRef = "Grid Ref"

    private val requiredColumnList = listOf(
        requiredColumnKeyName,
        requiredColumnKeyHeightInMeters,
        requiredColumnKeyHillCategory,
        requiredColumnKeyGridRef
    )
    private val munroDataRecordList: ArrayList<MunroDataRecord> = arrayListOf()
    private var headerList: List<String> = listOf()
    private var requiredHeadersWithPosMap: HashMap<String, Int> = hashMapOf()

    init {
        try {
            val scanner = Scanner(inputStream, "UTF-8")
            var lineNumber = 0
            while (scanner.hasNextLine()) {
                var line = scanner.nextLine()
//                if(line.isNullOrBlank()) break
                //test for the header existing on the first line and first line not being blank
                if (lineNumber == 0) {
                    headerList = line.split(delimiter).map { it.trim() }
                    line.split(delimiter)
                        .forEachIndexed { index, value ->
                            if (requiredColumnList.contains(value.trim())) requiredHeadersWithPosMap[value.trim()] = index
                        }
                    //if requiredHeadersMap.size <requiredColumn == throw an error

                } else {
                    //ignore empty lines
                    while (line.isNullOrBlank()) {
                        line = scanner.nextLine()
                    }
                    val row = line.split(delimiter).map { it.trim() }
                    row.get(5)
                    munroDataRecordList.add(
                        MunroDataRecord(
                            name = row[requiredHeadersWithPosMap[requiredColumnKeyName]!!],
                            heightInMeters = row[requiredHeadersWithPosMap[requiredColumnKeyHeightInMeters]!!],
                            hillCategory = row[requiredHeadersWithPosMap[requiredColumnKeyHillCategory]!!],
                            gridRef = row[requiredHeadersWithPosMap[requiredColumnKeyGridRef]!!]
                        )
                    )
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

    fun getResults(): List<MunroDataRecord> = munroDataRecordList
    fun getHeaders(): List<String> = headerList
    fun getHeaderListMap(): Map<String, Int> = requiredHeadersWithPosMap
}

//enum class MunroDataParserConfig(val value: String) {
//    DEFAULT(value = ",")
//}