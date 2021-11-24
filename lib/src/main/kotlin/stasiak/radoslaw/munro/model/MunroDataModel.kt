package stasiak.radoslaw.munro.model

data class MunroDataModel(
    val name: String,
    val heightInMeters: Double,
    val hillCategory: String,
    val gridRef: String)