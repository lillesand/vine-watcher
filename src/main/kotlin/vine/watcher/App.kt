package vine.watcher

class App {

    private val vmpClient = VMPClient()
    private val slackPoster = SlackPoster()
    private val winesRepository = WinesRepository()

    private val maxTravelKms = 10
    private val wines = mapOf(
            "8278501" to "Zind-Humbrecht Riesling Brand Grand Cru 2017",
            "10802701" to "Faiveley Mercurey Premier Cru Clos du Roy 2017",
            "10802801" to "Faiveley Mercurey La Framboisère 2017",
            "11416405" to "Penfolds St. Henri Shiraz 2006"
    )

    fun postStatus() {
        val previousWineStatus = winesRepository.previousWineStatus()

        val newWineStatus = wines.map { entry ->
            WineStatus(entry.key, vmpClient.getStoreStatus(entry.key).prettyPrintNearest(3, maxTravelKms))
        }

        newWineStatus.filter {
            previousWineStatus.find { previousIt -> it.articleId == previousIt.articleId }?.status != it.status
        }.forEach{ slackPoster.post(wines[it.articleId] ?: error("Fant ikke ${it.articleId}. Ikke bra."), it.status) }

        winesRepository.saveWineStatus(newWineStatus)
    }

}

fun main() {
    App().postStatus()
}
