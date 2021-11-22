package stasiak.radoslaw.munro

class MunroDataQuery private constructor(@JvmSynthetic internal val paramsMap: HashMap<MunroDataQueryParamName, MunroDataQueryParams>) {

    data class Builder(
         val paramsMap: HashMap<MunroDataQueryParamName, MunroDataQueryParams> = hashMapOf()

    ) {
        fun filterByHillCategory(hilLCategory: MunroDataHillCategory) = apply {
            paramsMap[MunroDataQueryParamName.FILTER_BY_HILL_CAT] =
                MunroDataQueryParams.FilterByHilLCategory(hilLCategory)
        }

        fun sortByHeightInMeters(ascending: Boolean) = apply {
            paramsMap[MunroDataQueryParamName.SORT_BY_HEIGHT_IN_M] =
                MunroDataQueryParams.SortByHeightInMeters(ascending)
        }

        fun sortAlphabeticallyByName(ascending: Boolean) = apply {
            paramsMap[MunroDataQueryParamName.SORT_ALPHABETICALLY] =
                MunroDataQueryParams.SortAlphabeticallyByName(ascending)
        }

        fun setMinHeightInMeters(minHeight: Double) = apply {
            paramsMap[MunroDataQueryParamName.SET_MIN_HEIGHT_IN_M] =
                MunroDataQueryParams.SetMinHeightInMeters(minHeight)
        }

        fun setMaxHeightInMeters(maxHeight: Double) = apply {
            paramsMap[MunroDataQueryParamName.SET_MAX_HEIGHT_IN_M] =
                MunroDataQueryParams.SetMaxHeightInMeters(maxHeight)
        }

        fun setResultsLimit(resultsLimit: Int) = apply {
            paramsMap[MunroDataQueryParamName.SET_RESULTS_LIMIT] =
                MunroDataQueryParams.SetResultsLimit(resultsLimit)
        }


        fun build(): MunroDataQuery = MunroDataQuery(paramsMap = this.paramsMap)
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
        SET_RESULTS_LIMIT
    }

}

sealed class MunroDataQueryParams {
    data class FilterByHilLCategory(val hilLCategory: MunroDataQuery.MunroDataHillCategory = MunroDataQuery.MunroDataHillCategory.DEFAULT) :
        MunroDataQueryParams()

    data class SortByHeightInMeters(val ascending: Boolean) : MunroDataQueryParams()

    data class SortAlphabeticallyByName(val ascending: Boolean) : MunroDataQueryParams()

    data class SetMinHeightInMeters(val minHeight: Double) : MunroDataQueryParams()
    data class SetMaxHeightInMeters(val maxHeight: Double) : MunroDataQueryParams()
    data class SetResultsLimit(val resultsLimit: Int) : MunroDataQueryParams()
}
