package stasiak.radoslaw.munro

import java.security.InvalidParameterException

class MunroDataQuery private constructor(
    @JvmSynthetic internal val filterParamsMap: HashMap<String, MunroDataQueryFilters>,
    @JvmSynthetic internal var sortingRule: MunroDataQuerySortingRules,
    @JvmSynthetic internal val resultsLimit: Int? = null,
) {

    companion object {
        @JvmSynthetic
        internal const val FILTER_BY_HILL_CAT = "filterByHillCat"

        @JvmSynthetic
        internal const val SET_MIN_HEIGHT_IN_M = "setMinHeightInMeters"

        @JvmSynthetic
        internal const val SET_MAX_HEIGHT_IN_M = "setMaxHeightInMeters"
    }


data class Builder(
    private val filterParams: HashMap<String, MunroDataQueryFilters> = hashMapOf(
        FILTER_BY_HILL_CAT to MunroDataQueryFilters.FilterByHilLCategory(
            MunroDataHillCategory.DEFAULT
        )
    ),
    private var sortingRule: MunroDataQuerySortingRules = MunroDataQuerySortingRules.NoSorting,
    private var resultsLimit: Int? = null,

    ) {

    fun filterByHillCategory(hilLCategory: MunroDataHillCategory) = apply {
        this.filterParams[FILTER_BY_HILL_CAT] =
            MunroDataQueryFilters.FilterByHilLCategory(hilLCategory)
    }


    fun setMinHeightInMeters(minHeight: Double) = apply {
        this.filterParams[SET_MIN_HEIGHT_IN_M] =
            MunroDataQueryFilters.SetMinHeightInMeters(minHeight)
    }

    fun setMaxHeightInMeters(maxHeight: Double) = apply {
        this.filterParams[SET_MAX_HEIGHT_IN_M] =
            MunroDataQueryFilters.SetMaxHeightInMeters(maxHeight)
    }

    fun setSortingRule(sortingRule: MunroDataQuerySortingRules) = apply {
        this.sortingRule = sortingRule
    }

    fun setResultsLimit(resultsLimit: Int) = apply {
        this.resultsLimit = resultsLimit
    }

    fun build(): MunroDataQuery {
        val paramErrors = validateParams()
        if (paramErrors.isNotEmpty()) {
            val errorMessage = StringBuffer()
            paramErrors.forEachIndexed { index, error ->
                errorMessage.append(error)
                if (index < paramErrors.size - 1) errorMessage.append(", ")
            }

            throw InvalidParameterException(errorMessage.toString())
        }
        return MunroDataQuery(
            filterParamsMap = this.filterParams,
            sortingRule = this.sortingRule,
            resultsLimit = resultsLimit
        )
    }

    @JvmSynthetic
    internal fun validateParams(): List<String> {
        val results = arrayListOf<String>()
        resultsLimit?.let { limit ->
            if (limit <= 0)
                results.add("Results limit must be greater then 0")
        }
        if (this.filterParams.containsKey(SET_MIN_HEIGHT_IN_M) &&
            this.filterParams.containsKey(SET_MAX_HEIGHT_IN_M)
        ) {
            val minHeight =
                (this.filterParams[SET_MIN_HEIGHT_IN_M] as MunroDataQueryFilters.SetMinHeightInMeters).minHeight
            val maxHeight =
                (this.filterParams[SET_MAX_HEIGHT_IN_M] as MunroDataQueryFilters.SetMaxHeightInMeters).maxHeight

            if (maxHeight < minHeight) results.add("Max height must be greater then min height")
        }

        return results
    }
}


enum class MunroDataHillCategory(val value: String) {
    DEFAULT(value = ""), MUNRO(value = "MUN"), TOP(value = "TOP")
}

}

sealed class MunroDataQueryFilters {
    data class FilterByHilLCategory(val hilLCategory: MunroDataQuery.MunroDataHillCategory = MunroDataQuery.MunroDataHillCategory.DEFAULT) :
        MunroDataQueryFilters()

    data class SetMinHeightInMeters(val minHeight: Double) : MunroDataQueryFilters()
    data class SetMaxHeightInMeters(val maxHeight: Double) : MunroDataQueryFilters()
}

sealed class MunroDataQuerySortingRules {
    object NoSorting : MunroDataQuerySortingRules()
    data class SortByHeightInMeters(val ascending: Boolean = false) : MunroDataQuerySortingRules()
    data class SortAlphabeticallyByName(val ascending: Boolean = false) : MunroDataQuerySortingRules()
}
