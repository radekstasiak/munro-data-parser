package stasiak.radoslaw.munro

import java.security.InvalidParameterException

class MunroDataQuery private constructor(@JvmSynthetic internal val paramsMap: HashMap<MunroDataQueryParamName, MunroDataQueryParams>) {

    data class Builder(
        val params: HashMap<MunroDataQueryParamName, MunroDataQueryParams> = hashMapOf()

    ) {
        fun filterByHillCategory(hilLCategory: MunroDataHillCategory) = apply {
            params[MunroDataQueryParamName.FILTER_BY_HILL_CAT] =
                MunroDataQueryParams.FilterByHilLCategory(hilLCategory)
        }

        fun sortByHeightInMeters(ascending: Boolean) = apply {
            params[MunroDataQueryParamName.SORT_BY_HEIGHT_IN_M] =
                MunroDataQueryParams.SortByHeightInMeters(ascending)
        }

        fun sortAlphabeticallyByName(ascending: Boolean) = apply {
            params[MunroDataQueryParamName.SORT_ALPHABETICALLY] =
                MunroDataQueryParams.SortAlphabeticallyByName(ascending)
        }

        fun setMinHeightInMeters(minHeight: Double) = apply {
            params[MunroDataQueryParamName.SET_MIN_HEIGHT_IN_M] =
                MunroDataQueryParams.SetMinHeightInMeters(minHeight)
        }

        fun setMaxHeightInMeters(maxHeight: Double) = apply {
            params[MunroDataQueryParamName.SET_MAX_HEIGHT_IN_M] =
                MunroDataQueryParams.SetMaxHeightInMeters(maxHeight)
        }

        fun setResultsLimit(resultsLimit: Int) = apply {
            params[MunroDataQueryParamName.SET_RESULTS_LIMIT] =
                MunroDataQueryParams.SetResultsLimit(resultsLimit)
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
            return MunroDataQuery(paramsMap = this.params)
        }

        @JvmSynthetic
        internal fun validateParams(): List<String> {
            val results = arrayListOf<String>()
            if (this.params.containsKey(MunroDataQueryParamName.SET_RESULTS_LIMIT)) {
                val resultsLimit =
                    (this.params[MunroDataQueryParamName.SET_RESULTS_LIMIT] as MunroDataQueryParams.SetResultsLimit).resultsLimit
                if (resultsLimit <= 0) results.add("Results limit must be greater then 0")
            }

            if (this.params.containsKey(MunroDataQueryParamName.SET_MIN_HEIGHT_IN_M) &&
                this.params.containsKey(MunroDataQueryParamName.SET_MAX_HEIGHT_IN_M)
            ) {
                val minHeight =
                    (this.params[MunroDataQueryParamName.SET_MIN_HEIGHT_IN_M] as MunroDataQueryParams.SetMinHeightInMeters).minHeight
                val maxHeight =
                    (this.params[MunroDataQueryParamName.SET_MAX_HEIGHT_IN_M] as MunroDataQueryParams.SetMaxHeightInMeters).maxHeight

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
