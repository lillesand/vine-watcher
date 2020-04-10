package vine.watcher

import vine.watcher.vinmonopolet.VMPClient

class App(private val vmpClient: VMPClient = VMPClient(),
          private val slackPoster: SlackPoster = SlackPoster(),
          private val winesRepository: WinesRepository = WinesRepository()) {

    private val maxTravelKms = 10

    fun postStatus() {
        val previousWineStatus = winesRepository.previousWineStatus()

        val newWineStatus = previousWineStatus.map {
            it.copy(status = vmpClient.getStoreStatus(it.articleId).prettyPrintNearest(3, maxTravelKms))
        }

        val winesToPost = newWineStatus.filter {
            previousWineStatus.find { previousIt -> it.articleId == previousIt.articleId }?.status != it.status
        }

        println("Posting ${winesToPost.size} updated wines to Slack")

        winesToPost.forEach { slackPoster.postWine(it.name, it.status!!) }

        println("Saving updated wine status: ${newWineStatus.joinToString("\n")}")
        winesRepository.updateWineStatus(winesToPost)
    }

}

fun main() {
    App().postStatus()
}
