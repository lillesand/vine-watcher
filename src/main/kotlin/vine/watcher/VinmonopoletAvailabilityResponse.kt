package vine.watcher

data class VinmonopoletAvailabilityResponse(
    val pagination: Pagination,
    val stores: List<Store>
) {
    data class Pagination(
        val currentPage: Int,
        val pageSize: Int,
        val totalPages: Int,
        val totalResults: Int
    )

    data class Store(
        val clickAndCollect: Boolean,
        val displayName: String,
        val formattedDistance: String,
        val name: String,
        val stockInfo: StockInfo
    ) {
        data class StockInfo(
            val stockLevel: Int
        )

        fun distance(): Double? {
            return formattedDistance.substringBefore("km").replace(",", ".").toDoubleOrNull()
        }

        fun friendlyPrint(): String {
            return "$displayName - ${stockInfo.stockLevel} flasker ($formattedDistance unna)"
        }
    }
}
