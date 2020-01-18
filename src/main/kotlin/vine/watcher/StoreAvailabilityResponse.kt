package vine.watcher

data class StoreAvailabilityResponse(
    val `data`: List<Data>
) {
    data class Data(
        val country: String,
        val displayName: String,
        val formattedDistance: String,
        val line1: String,
        val line2: String,
        val name: String,
        val postalCode: String,
        val productcode: String,
        val status: String,
        val stockLevel: String,
        val stockPickup: String,
        val storeLatitude: String,
        val storeLongitude: String,
        val town: String,
        val url: String
    )
}
