package stasiak.radoslaw.munro

import org.junit.Assert.assertThrows
import org.junit.Test
import stasiak.radoslaw.munro.MunroDataQuery.Companion.FILTER_BY_HILL_CAT
import stasiak.radoslaw.munro.MunroDataQuery.Companion.SET_MAX_HEIGHT_IN_M
import stasiak.radoslaw.munro.MunroDataQuery.Companion.SET_MIN_HEIGHT_IN_M
import java.security.InvalidParameterException
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class MunroDataQueryTest {

    @Test
    fun `test default values in MunroDataQuery`() {
        val munroDataQuery = MunroDataQuery.Builder().build()

        assertEquals(1, munroDataQuery.filterParamsMap.size)
        assertEquals(
            MunroDataQuery.MunroDataHillCategory.DEFAULT,
            (munroDataQuery.filterParamsMap[FILTER_BY_HILL_CAT] as MunroDataQueryFilters.FilterByHilLCategory).hilLCategory
        )
        assertTrue(munroDataQuery.sortingRule is MunroDataQuerySortingRules.NoSorting)
        assertTrue(munroDataQuery.resultsLimit == null)
    }

    @Test
    fun `test builder creates MunroDataQuery successfully`() {
        val munroDataQuery = MunroDataQuery.Builder()
            .filterByHillCategory(hilLCategory = MunroDataQuery.MunroDataHillCategory.MUNRO)
            .setSortingRule(MunroDataQuerySortingRules.SortByHeightInMeters())
            .setMaxHeightInMeters(maxHeight = 23.12)
            .setMinHeightInMeters(minHeight = 12.12)
            .setResultsLimit(resultsLimit = 10)
            .build()

        assertEquals(
            MunroDataQuery.MunroDataHillCategory.MUNRO,
            (munroDataQuery.filterParamsMap[FILTER_BY_HILL_CAT] as MunroDataQueryFilters.FilterByHilLCategory).hilLCategory
        )

        assertEquals(
            12.12,
            (munroDataQuery.filterParamsMap[SET_MIN_HEIGHT_IN_M] as MunroDataQueryFilters.SetMinHeightInMeters).minHeight
        )

        assertEquals(
            23.12,
            (munroDataQuery.filterParamsMap[SET_MAX_HEIGHT_IN_M] as MunroDataQueryFilters.SetMaxHeightInMeters).maxHeight
        )

        assertEquals(
            false,
            (munroDataQuery.sortingRule as MunroDataQuerySortingRules.SortByHeightInMeters).ascending
        )

        assertEquals(10, munroDataQuery.resultsLimit)
    }

    @Test
    fun `test builder throws invalid params exception MunroDataQuery successfully`() {

        val exception = assertThrows(InvalidParameterException::class.java) {
            MunroDataQuery.Builder()
                .filterByHillCategory(hilLCategory = MunroDataQuery.MunroDataHillCategory.MUNRO)
                .setSortingRule(MunroDataQuerySortingRules.SortByHeightInMeters())
                .setMaxHeightInMeters(maxHeight = 12.12)
                .setMinHeightInMeters(minHeight = 23.12)
                .setResultsLimit(resultsLimit = -10)
                .build()
        }

        assertEquals(
            "Results limit must be greater then 0, Max height must be greater then min height",
            exception.message
        )


    }
}