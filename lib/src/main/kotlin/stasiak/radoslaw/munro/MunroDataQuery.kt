package stasiak.radoslaw.munro

import java.security.InvalidParameterException

class MunroDataQuery private constructor(
    @JvmSynthetic internal val filterParamsMap: HashMap<MunroDataQueryParamName, MunroDataQueryFilters>,
    @JvmSynthetic internal var sortingRule: MunroDataQuerySortingRules,
    @JvmSynthetic internal val resultsLimit: Int? = null,
) {

    data class Builder(
        private val filterParams: HashMap<MunroDataQueryParamName, MunroDataQueryFilters> = hashMapOf(
            MunroDataQueryParamName.FILTER_BY_HILL_CAT to MunroDataQueryFilters.FilterByHilLCategory(
                MunroDataHillCategory.DEFAULT
            )
        ),
        private var sortingRule: MunroDataQuerySortingRules = MunroDataQuerySortingRules.NoSorting,
        private var resultsLimit: Int? = null,

        ) {

        fun filterByHillCategory(hilLCategory: MunroDataHillCategory) = apply {
            this.filterParams[MunroDataQueryParamName.FILTER_BY_HILL_CAT] =
                MunroDataQueryFilters.FilterByHilLCategory(hilLCategory)
        }


        fun setMinHeightInMeters(minHeight: Double) = apply {
            this.filterParams[MunroDataQueryParamName.SET_MIN_HEIGHT_IN_M] =
                MunroDataQueryFilters.SetMinHeightInMeters(minHeight)
        }

        fun setMaxHeightInMeters(maxHeight: Double) = apply {
            this.filterParams[MunroDataQueryParamName.SET_MAX_HEIGHT_IN_M] =
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
//            if (this.filterParams.containsKey(MunroDataQueryParamName.SET_RESULTS_LIMIT)) {
//                val resultsLimit =
//                    (this.filterParams[MunroDataQueryParamName.SET_RESULTS_LIMIT] as MunroDataQueryParams.SetResultsLimit).resultsLimit
            resultsLimit?.let { limit ->
                if (limit <= 0)
                    results.add("Results limit must be greater then 0")
            }
//            }

            if (this.filterParams.containsKey(MunroDataQueryParamName.SET_MIN_HEIGHT_IN_M) &&
                this.filterParams.containsKey(MunroDataQueryParamName.SET_MAX_HEIGHT_IN_M)
            ) {
                val minHeight =
                    (this.filterParams[MunroDataQueryParamName.SET_MIN_HEIGHT_IN_M] as MunroDataQueryFilters.SetMinHeightInMeters).minHeight
                val maxHeight =
                    (this.filterParams[MunroDataQueryParamName.SET_MAX_HEIGHT_IN_M] as MunroDataQueryFilters.SetMaxHeightInMeters).maxHeight

                if (maxHeight < minHeight) results.add("Max height must be greater then min height")
            }

            return results
        }
    }


    enum class MunroDataHillCategory(val value: String) {
        DEFAULT(value = ""), MUNRO(value = "MUN"), TOP(value = "TOP")
    }

    enum class MunroDataQueryParamName {
        FILTER_BY_HILL_CAT,
        SORT_BY_HEIGHT_IN_M,
        SORT_ALPHABETICALLY,
        SET_MIN_HEIGHT_IN_M,
        SET_MAX_HEIGHT_IN_M,
//        SET_RESULTS_LIMIT
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
