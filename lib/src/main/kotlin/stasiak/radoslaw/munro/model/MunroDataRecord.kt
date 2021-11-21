package stasiak.radoslaw.munro.model

data class MunroDataRecord(
    val name: String,
    val heightInMeters: String,
    val hillCategory: String,
    val gridRef: String
)