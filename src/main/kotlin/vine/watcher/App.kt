package vine.watcher

import com.github.seratch.jslack.Slack
import com.github.seratch.jslack.api.webhook.Payload

class App {

    private val slack = Slack.getInstance()
    private val url = System.getenv("SLACK_WEBHOOK")

    private val vmpClient = VMPClient()

    private val maxTravelKms = 10
    private val wines = mapOf(
            "8278501" to "Zind-Humbrecht Riesling Brand Grand Cru 2017",
            "11416405" to "Penfolds St. Henri Shiraz 2006"
    )

    fun postStatus() {
        for ((articleId, wineName) in wines) {
            val status = vmpClient.getStoreStatus(articleId)
            val nearbyWines = status?.stores?.filter { it.distance() != null && it.distance()!! < maxTravelKms }?.take(3)?.joinToString("\n") { it.friendlyPrint() }

            val payload = Payload.builder()
                    .text("*$wineName*:\n$nearbyWines")
                    .channel("#vinolini")
                    .iconEmoji(":wine_glass:")
                    .username("Vinolini")
                    .build()
            slack.send(url, payload)
        }
    }

}


fun main() {
    App().postStatus()
}
