package stasiak.radoslaw.munro

import org.junit.Assert.assertThrows
import org.junit.Test
import java.security.InvalidParameterException
import kotlin.test.assertEquals

class MunroDataQueryTest {

    @Test
    fun `builder creates MunroDataQuery successfully`() {
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
            (munroDataQuery.filterParamsMap[MunroDataQuery.MunroDataQueryParamName.FILTER_BY_HILL_CAT] as MunroDataQueryFilters.FilterByHilLCategory).hilLCategory
        )

        assertEquals(
            12.12,
            (munroDataQuery.filterParamsMap[MunroDataQuery.MunroDataQueryParamName.SET_MIN_HEIGHT_IN_M] as MunroDataQueryFilters.SetMinHeightInMeters).minHeight
        )

        assertEquals(
            23.12,
            (munroDataQuery.filterParamsMap[MunroDataQuery.MunroDataQueryParamName.SET_MAX_HEIGHT_IN_M] as MunroDataQueryFilters.SetMaxHeightInMeters).maxHeight
        )

        assertEquals(
            true,
            (munroDataQuery.sortParamsMap[MunroDataQuery.MunroDataQueryParamName.SORT_BY_HEIGHT_IN_M] as MunroDataQuerySortingRules.SortByHeightInMeters).ascending
        )

        assertEquals(
            true,
            (munroDataQuery.sortParamsMap[MunroDataQuery.MunroDataQueryParamName.SORT_ALPHABETICALLY] as MunroDataQuerySortingRules.SortAlphabeticallyByName).ascending
        )

        assertEquals(10, munroDataQuery.resultsLimit)
    }

    @Test
    fun `builder throws invalid params exception MunroDataQuery successfully`() {

        val exception = assertThrows(InvalidParameterException::class.java) {
            MunroDataQuery.Builder()
                .filterByHillCategory(hilLCategory = MunroDataQuery.MunroDataHillCategory.MUNRO)
                .sortByHeightInMeters(ascending = false)
                .sortAlphabeticallyByName(ascending = true)
                .sortByHeightInMeters(ascending = true)
                .setMaxHeightInMeters(maxHeight = 12.12)
                .setMinHeightInMeters(minHeight = 23.12)
                .setResultsLimit(resultsLimit = -10)
                .build()
        }

        assertEquals("Results limit must be greater then 0, Max height must be greater then min height", exception.message)


    }
}