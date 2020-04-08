package vine.watcher

import vine.watcher.vinmonopolet.VMPClient

class App(private val vmpClient: VMPClient = VMPClient(),
          private val slackPoster: SlackPoster = SlackPoster(),
          private val winesRepository: WinesRepository = WinesRepository()) {

    private val maxTravelKms = 10
    private val wines = mapOf(
            "8278501" to "Zind-Humbrecht Riesling Brand Grand Cru 2017",
            "10802701" to "Faiveley Mercurey Premier Cru Clos du Roy 2017",
            "10802801" to "Faiveley Mercurey La Framboisère 2017",
            "11416405" to "Penfolds St. Henri Shiraz 2006",
            "5089801" to "Viña Tondonia Gran Reserva Rosado 2009",
            "7879101" to "Dom. de la Côte Memorious Pinot Noir 2017",
            "7879201" to "Dom. de la Côte Bloom's Field Pinot Noir 2017",
            "7879001" to "Dom. de la Côte Sta. Rita Hills Pinot Noir 2016",
            "7879301" to "La Côte Pinot Noir 2017",
            "5089801" to "Viña Tondonia Gran Reserva Rosado 2009"
    )

    fun postStatus() {
        val previousWineStatus = winesRepository.previousWineStatus()

        val newWineStatus = wines.map { entry ->
            WineStatus(entry.key, vmpClient.getStoreStatus(entry.key).prettyPrintNearest(3, maxTravelKms))
        }

        val winesToPost = newWineStatus.filter {
            previousWineStatus.find { previousIt -> it.articleId == previousIt.articleId }?.status != it.status
        }

        println("Posting ${winesToPost.size} updated wines to Slack")

        winesToPost.forEach{ slackPoster.postWine(wines[it.articleId] ?: error("Fant ikke ${it.articleId}. Ikke bra."), it.status) }

        println("Saving updated wine status: ${newWineStatus.joinToString("\n")}")
        winesRepository.saveWineStatus(newWineStatus)
    }

}

fun main() {
    App().postStatus()
}
