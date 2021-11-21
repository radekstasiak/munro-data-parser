package stasiak.radoslaw.munro

import stasiak.radoslaw.munro.model.MunroDataRecord
import java.io.FileInputStream
import java.io.IOException
import java.lang.IllegalArgumentException
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class MunroDataParser(
    private val inputStream: FileInputStream,
    private val delimiter: String
) {

    private val requiredColumnList = listOf(
        RequiredHeader.REQUIRED_HEADER_NAME,
        RequiredHeader.REQUIRED_HEADER_HEIGHT_IN_METERS,
        RequiredHeader.REQUIRED_HEADER_HILL_CATEGORY,
        RequiredHeader.REQUIRED_HEADER_GRID_REF,
    )
    private val munroDataRecordList: ArrayList<MunroDataRecord> = arrayListOf()
    private var headerList: List<String> = listOf()
    private var requiredHeadersWithPosMap: HashMap<String, Int> = hashMapOf()

    init {
            val scanner = Scanner(inputStream, "UTF-8")
            var lineNumber = 0
            while (scanner.hasNextLine()) {
                var line = scanner.nextLine()
                //ignore empty lines
                while (line.isNullOrBlank()) {
                    line = scanner.nextLine()
                }

                if (lineNumber == 0) {
                    headerList = line.split(delimiter).map { it.trim() }
                    line.split(delimiter)
                        .forEachIndexed { index, value ->
                            if (requiredColumnList.map { it.value }
                                    .contains(value.trim())) requiredHeadersWithPosMap[value.trim()] =
                                index
                        }

                    //validate whether all the required headers exist
                    val headersValidation = validateHeaders()
                    if (headersValidation.isNotEmpty()) {
                        throw IllegalArgumentException(headersValidation.toErrorMessage())
                        break
                    }

                } else {
                    val csvRecordParser = CSVRecordParser.instantiate(line, delimiter.single())
                    val row = csvRecordParser.result
                    val requiredHeaderNameColumnPos = requiredHeadersWithPosMap[RequiredHeader.REQUIRED_HEADER_NAME.value]!!
                    val requiredColumnKeyHeightInMetersPos =
                        requiredHeadersWithPosMap[RequiredHeader.REQUIRED_HEADER_HEIGHT_IN_METERS.value]!!
                    val requiredColumnKeyHillCategoryPos =
                        requiredHeadersWithPosMap[RequiredHeader.REQUIRED_HEADER_HILL_CATEGORY.value]!!
                    val requiredColumnKeyGridRefPos =
                        requiredHeadersWithPosMap[RequiredHeader.REQUIRED_HEADER_GRID_REF.value]!!

                    munroDataRecordList.add(
                        MunroDataRecord(
                            name = if (row.size > requiredHeaderNameColumnPos) row[requiredHeaderNameColumnPos] else "",
                            heightInMeters = if (row.size > requiredColumnKeyHeightInMetersPos) row[requiredColumnKeyHeightInMetersPos] else "",
                            hillCategory = if (row.size > requiredColumnKeyHillCategoryPos) row[requiredColumnKeyHillCategoryPos] else "",
                            gridRef = if (row.size > requiredColumnKeyGridRefPos) row[requiredColumnKeyGridRefPos] else ""
                        )
                    )
                }
                lineNumber++

            }

            if (scanner.ioException() != null) {
                throw scanner.ioException()
            }

            scanner.close()
    }

    private fun validateHeaders(): List<RequiredHeaderValidationError> {
        val errorList = arrayListOf<RequiredHeaderValidationError>()
        if (!requiredHeadersWithPosMap.containsKey(RequiredHeader.REQUIRED_HEADER_NAME.value)) errorList.add(RequiredHeaderValidationError.MISSING_NAME_HEADER)
        if (!requiredHeadersWithPosMap.containsKey(RequiredHeader.REQUIRED_HEADER_HEIGHT_IN_METERS.value)) errorList.add(
            RequiredHeaderValidationError.MISSING_HEIGHT_IN_METERS_HEADER
        )
        if (!requiredHeadersWithPosMap.containsKey(RequiredHeader.REQUIRED_HEADER_HILL_CATEGORY.value)) errorList.add(
            RequiredHeaderValidationError.MISSING_HILL_CATEGORY_HEADER
        )
        if (!requiredHeadersWithPosMap.containsKey(RequiredHeader.REQUIRED_HEADER_GRID_REF.value)) errorList.add(
            RequiredHeaderValidationError.MISSING_GRID_REF_HEADER
        )

        return errorList
    }

    private fun List<RequiredHeaderValidationError>.toErrorMessage(): String {
        val errorMessage = StringBuffer("Required headers are missing:")
        this.forEachIndexed { index, error ->
            errorMessage.append("`${error.value}`")
            if (index < this.size - 1) errorMessage.append(",")
        }
        return errorMessage.toString()
    }

    @JvmSynthetic
    internal fun getResults(): List<MunroDataRecord> = munroDataRecordList

    @JvmSynthetic
    internal fun getHeaders(): List<String> = headerList

    @JvmSynthetic
    internal fun getHeaderListMap(): Map<String, Int> = requiredHeadersWithPosMap

    private enum class RequiredHeaderValidationError(val value: String) {
        MISSING_NAME_HEADER(value = "Name"),
        MISSING_HEIGHT_IN_METERS_HEADER(value = "Height (m)"),
        MISSING_HILL_CATEGORY_HEADER(value = "Post 1997"),
        MISSING_GRID_REF_HEADER(value = "Grid Ref")
    }

    private enum class RequiredHeader(val value: String) {
        REQUIRED_HEADER_NAME(value = "Name"),
        REQUIRED_HEADER_HEIGHT_IN_METERS(value = "Height (m)"),
        REQUIRED_HEADER_HILL_CATEGORY(value = "Post 1997"),
        REQUIRED_HEADER_GRID_REF(value = "Grid Ref")
    }
}
