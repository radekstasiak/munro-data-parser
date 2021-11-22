package stasiak.radoslaw.munro

import org.junit.Test
import kotlin.test.assertEquals

class MunroDataQueryTest {


    @Test
    fun `builder creates correct munro data query`() {
        val munroDataQuery = MunroDataQuery.Builder()
            .filterByHillCategory(hilLCategory = MunroDataQuery.MunroDataHillCategory.MUNRO)
            .sortByHeightInMeters(ascending = false)
            .sortAlphabeticallyByName(ascending = true)
            .sortByHeightInMeters(ascending = true)
            .setMaxHeightInMeters(maxHeight = 23.12)
            .setMinHeightInMeters(minHeight = 12.12)
            .setResultsLimit(resultsLimit = 10)
            .build()

        assertEquals(
            MunroDataQuery.MunroDataHillCategory.MUNRO,
            (munroDataQuery.paramsMap[MunroDataQuery.MunroDataQueryParamName.FILTER_BY_HILL_CAT] as MunroDataQueryParams.FilterByHilLCategory).hilLCategory
        )

        assertEquals(
            true,
            (munroDataQuery.paramsMap[MunroDataQuery.MunroDataQueryParamName.SORT_BY_HEIGHT_IN_M] as MunroDataQueryParams.SortByHeightInMeters).ascending
        )

        assertEquals(
            true,
            (munroDataQuery.paramsMap[MunroDataQuery.MunroDataQueryParamName.SORT_ALPHABETICALLY] as MunroDataQueryParams.SortAlphabeticallyByName).ascending
        )

        assertEquals(
            12.12,
            (munroDataQuery.paramsMap[MunroDataQuery.MunroDataQueryParamName.SET_MIN_HEIGHT_IN_M] as MunroDataQueryParams.SetMinHeightInMeters).minHeight
        )

        assertEquals(
            23.12,
            (munroDataQuery.paramsMap[MunroDataQuery.MunroDataQueryParamName.SET_MAX_HEIGHT_IN_M] as MunroDataQueryParams.SetMaxHeightInMeters).maxHeight
        )

        assertEquals(
            10,
            (munroDataQuery.paramsMap[MunroDataQuery.MunroDataQueryParamName.SET_RESULTS_LIMIT] as MunroDataQueryParams.SetResultsLimit).resultsLimit
        )

    }
}