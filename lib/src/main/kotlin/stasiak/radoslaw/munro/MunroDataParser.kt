package stasiak.radoslaw.munro

import stasiak.radoslaw.munro.model.MunroDataModel
import stasiak.radoslaw.munro.model.MunroDataRecord
import java.io.FileInputStream
import java.util.*
import kotlin.collections.ArrayList


class MunroDataParser(
    private val inputStream: FileInputStream,
    private val delimiter: Char = ",".single()
) {
    private val munroDataRecordList: ArrayList<MunroDataRecord> = arrayListOf()
    private var headerList: List<String> = listOf()

    init {
        val scanner = Scanner(inputStream, "UTF-8")
        var lineNumber = 0
        while (scanner.hasNextLine()) {
            var line = scanner.nextLine()
            while (line.isNullOrBlank()) {
                line = scanner.nextLine()
            }

            if (lineNumber == 0) {
                headerList = line.split(delimiter).map { it.trim() }

            } else {
                val csvRecordParser = CSVRecordParser.instantiate(line, delimiter)
                val row = csvRecordParser.result
                //row needs to have at least entry id, this way we can drop non munro data records
                if (row.size > 0 && row[0].isNotBlank()) {
                    val fieldsMap = hashMapOf<String, String>()
                    headerList.forEachIndexed { index, column ->
                        fieldsMap[column] = if (row.size > index) row[index] else ""
                    }
                    munroDataRecordList.add(
                        MunroDataRecord(fieldsMap = fieldsMap)
                    )
                }
                val fieldsMap = hashMapOf<String, String>()
                headerList.forEachIndexed { index, column ->
                    fieldsMap[column] = if (row.size > index) row[index] else ""
                }
            }
            lineNumber++

        }

        if (scanner.ioException() != null) {
            throw scanner.ioException()
        }

        scanner.close()
    }

    fun getResults(query: MunroDataQuery): List<MunroDataModel> {
        val munroDataModelList = munroDataRecordList.filter { munroDataRecord ->
            filterMunroDataRecords(munroDataRecord.fieldsMap, query.filterParamsMap)
        }.map { munroDataRecord ->
            MunroDataModel(
                name = munroDataRecord.fieldsMap[RequiredHeader.REQUIRED_HEADER_NAME.value] ?: "",
                hillCategory = munroDataRecord.fieldsMap[RequiredHeader.REQUIRED_HEADER_HILL_CATEGORY.value] ?: "",
                heightInMeters = munroDataRecord.fieldsMap[RequiredHeader.REQUIRED_HEADER_HEIGHT_IN_METERS.value] ?: "",
                gridRef = munroDataRecord.fieldsMap[RequiredHeader.REQUIRED_HEADER_GRID_REF.value] ?: "",
            )
        }

        val sortedMunroDataModelList = when (val sortingRule = query.sortingRule) {
            MunroDataQuerySortingRules.NoSorting -> munroDataModelList
            is MunroDataQuerySortingRules.SortAlphabeticallyByName -> if (sortingRule.ascending) munroDataModelList.sortedBy { it.name } else munroDataModelList.sortedByDescending { it.name }
            is MunroDataQuerySortingRules.SortByHeightInMeters -> if (sortingRule.ascending) munroDataModelList.sortedBy { it.heightInMeters.toDoubleOrNull() } else munroDataModelList.sortedByDescending { it.heightInMeters.toDoubleOrNull() }
        }

        return if (query.resultsLimit != null) sortedMunroDataModelList.take(query.resultsLimit) else sortedMunroDataModelList
    }

    private fun filterMunroDataRecords(
        fieldsMap: Map<String, String>,
        queryParams: Map<MunroDataQuery.MunroDataQueryParamName, MunroDataQueryFilters>
    ): Boolean {
        val hillCatField = fieldsMap[RequiredHeader.REQUIRED_HEADER_HILL_CATEGORY.value] ?: ""
        val heightInMetersField = fieldsMap[RequiredHeader.REQUIRED_HEADER_HEIGHT_IN_METERS.value]?.toDoubleOrNull()

        return !queryParams.entries.map { entry ->
            when (val query = entry.value) {
                is MunroDataQueryFilters.FilterByHilLCategory -> {
                    when (val hillCategory = query.hilLCategory) {
                        MunroDataQuery.MunroDataHillCategory.DEFAULT -> hillCatField == MunroDataQuery.MunroDataHillCategory.MUNRO.value || hillCatField == MunroDataQuery.MunroDataHillCategory.TOP.value
                        else -> hillCatField == hillCategory.value
                    }
                }
                is MunroDataQueryFilters.SetMaxHeightInMeters -> heightInMetersField != null && heightInMetersField <= query.maxHeight

                is MunroDataQueryFilters.SetMinHeightInMeters -> heightInMetersField != null && heightInMetersField >= query.minHeight

            }
        }.contains(false)
    }

    @JvmSynthetic
    internal fun getHeaders(): List<String> = headerList

    private enum class RequiredHeader(val value: String) {
        REQUIRED_HEADER_NAME(value = "Name"),
        REQUIRED_HEADER_HEIGHT_IN_METERS(value = "Height (m)"),
        REQUIRED_HEADER_HILL_CATEGORY(value = "Post 1997"),
        REQUIRED_HEADER_GRID_REF(value = "Grid Ref")
    }
}
