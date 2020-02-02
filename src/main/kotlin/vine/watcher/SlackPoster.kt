package vine.watcher

import com.github.seratch.jslack.Slack
import com.github.seratch.jslack.api.webhook.Payload

class SlackPoster {

    private val slack = Slack.getInstance()
    private val url = System.getenv("SLACK_WEBHOOK")

    fun post(wineName: String, nearbyWines: String) {
        val payload = Payload.builder()
                .text("*$wineName*:\n$nearbyWines")
                .channel("#vinolini")
                .iconEmoji(":wine_glass:")
                .username("Vinolini")
                .build()
        slack.send(url, payload)
    }

}
