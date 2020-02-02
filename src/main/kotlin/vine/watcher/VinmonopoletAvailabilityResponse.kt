package vine.watcher

data class VinmonopoletAvailabilityResponse(
    val stores: List<Store>
) {

    fun prettyPrintNearest(maxNumber: Int, maxTravelKms: Int): String {
        return stores.filter { it.distance() != null && it.distance()!! < maxTravelKms }.take(maxNumber).joinToString("\n") { it.friendlyPrint() }
    }

    data class Store(
        val displayName: String,
        val formattedDistance: String,
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
