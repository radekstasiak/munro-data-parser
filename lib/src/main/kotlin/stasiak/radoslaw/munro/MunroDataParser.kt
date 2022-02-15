package stasiak.radoslaw.munro

import stasiak.radoslaw.munro.model.MunroDataModel
import java.io.InputStream
import java.util.*
import kotlin.collections.ArrayList


class MunroDataParser(
    private val inputStream: InputStream,
    private val delimiter: Char = ",".single()
) {
    private val munroDataRecordList: ArrayList<MunroDataRecord> = arrayListOf()
    private var headerList: List<String> = listOf()

    init {
        val reader = inputStream.bufferedReader()
        val iterator = reader.lineSequence().iterator()
        var lineNumber = 0
        while (iterator.hasNext()) {
            var line = iterator.next()
            while (line.isNullOrBlank()) {
                line = iterator.next()
            }

            if (lineNumber == 0) {
                headerList = line.split(delimiter).map { it.trim() }

            } else {
                val csvRecordParser = CSVRecordParser(line, delimiter)
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

        reader.close()
    }

    fun getResults(query: MunroDataQuery = MunroDataQuery.Builder().build()): List<MunroDataModel> {
        val munroDataModelList = munroDataRecordList.filter { munroDataRecord ->
            filterMunroDataRecords(munroDataRecord.fieldsMap, query.filterParamsMap)
        }.map { munroDataRecord ->
            MunroDataModel(
                name = munroDataRecord.fieldsMap[RequiredHeader.REQUIRED_HEADER_NAME.value] ?: "",
                hillCategory = when (munroDataRecord.fieldsMap[RequiredHeader.REQUIRED_HEADER_HILL_CATEGORY.value]) {
                    "MUN" -> "Munro"
                    "TOP" -> "Munro Top"
                    else -> ""
                },
                heightInMeters = munroDataRecord.fieldsMap[RequiredHeader.REQUIRED_HEADER_HEIGHT_IN_METERS.value]?.toDoubleOrNull()
                    ?: 0.0,
                gridRef = munroDataRecord.fieldsMap[RequiredHeader.REQUIRED_HEADER_GRID_REF.value] ?: "",
            )
        }

        val sortedMunroDataModelList = when (val sortingRule = query.sortingRule) {
            MunroDataQuerySortingRules.NoSorting -> munroDataModelList
            is MunroDataQuerySortingRules.SortAlphabeticallyByName -> if (sortingRule.ascending) munroDataModelList.sortedBy { it.name } else munroDataModelList.sortedByDescending { it.name }
            is MunroDataQuerySortingRules.SortByHeightInMeters -> if (sortingRule.ascending) munroDataModelList.sortedBy { it.heightInMeters } else munroDataModelList.sortedByDescending { it.heightInMeters }
        }

        return if (query.resultsLimit != null) sortedMunroDataModelList.take(query.resultsLimit) else sortedMunroDataModelList
    }

    private fun filterMunroDataRecords(
        fieldsMap: Map<String, String>,
        queryParams: Map<String, MunroDataQueryFilters>
    ): Boolean {
        val hillCatField = fieldsMap[RequiredHeader.REQUIRED_HEADER_HILL_CATEGORY.value] ?: ""
        val heightInMetersField = fieldsMap[RequiredHeader.REQUIRED_HEADER_HEIGHT_IN_METERS.value]?.toDoubleOrNull()

        return !queryParams.entries.map { entry ->
            when (val query = entry.value) {
                is MunroDataQueryFilters.FilterByHilLCategory -> {
                    when (val hillCategory = query.hilLCategory) {
                        MunroDataHillCategory.EITHER -> hillCatField == MunroDataHillCategory.MUNRO.value || hillCatField == MunroDataHillCategory.TOP.value
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

    private data class MunroDataRecord(
        val fieldsMap: HashMap<String, String>
    )
}
