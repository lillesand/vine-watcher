package vine.watcher

import com.github.seratch.jslack.Slack
import com.github.seratch.jslack.api.webhook.Payload

class SlackPoster {

    private val slack = Slack.getInstance()
    private val url = System.getenv("SLACK_WEBHOOK")

    fun postMessage(message: String) {
        val payload = Payload.builder()
                .text(message)
                .channel("#vinolini")
                .iconEmoji(":wine_glass:")
                .username("Vinolini")
                .build()
        slack.send(url, payload)
    }

    fun postWine(wineName: String, nearbyWines: String) {
        postMessage("*$wineName*:\n" + nearbyWines)
    }

}
