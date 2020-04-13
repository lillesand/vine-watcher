package vine.watcher.slackhook

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler
import com.amazonaws.services.sqs.AmazonSQS
import com.amazonaws.services.sqs.AmazonSQSClientBuilder
import com.github.seratch.jslack.app_backend.vendor.aws.lambda.request.ApiGatewayRequest
import com.github.seratch.jslack.app_backend.vendor.aws.lambda.response.ApiGatewayResponse
import com.github.seratch.jslack.app_backend.vendor.aws.lambda.util.SlackSignatureVerifier

class SlackCommandRequestHandler(
        private val signatureVerifier: SlackSignatureVerifier = SlackSignatureVerifier(),
        private val queueUrl: String? = System.getenv("QUEUE_URL"),
        private val sqsClient: AmazonSQS = AmazonSQSClientBuilder.defaultClient()
): RequestHandler<ApiGatewayRequest, ApiGatewayResponse> {

    override fun handleRequest(request: ApiGatewayRequest?, context: Context?): ApiGatewayResponse {
        if (!signatureVerifier.isValid(request)) {
            return ApiGatewayResponse.builder().statusCode(401).build()
        }

        val command = readParam(request, "command")
        val args = readParam(request, "text")

        if (command != "vinolini") {
            return ApiGatewayResponse.builder()
                    .rawBody("Wat. Ukjent commando: $command")
                    .build()
        }

        sqsClient.sendMessage(queueUrl, args)

        return ApiGatewayResponse.builder()
                .rawBody("Da har vi putta `$args` på køa!")
                .build()
    }

    private fun readParam(request: ApiGatewayRequest?, param: String): String? {
        return request?.body?.substringAfter("$param=")?.substringBefore("&")?.removePrefix("%2F")?.replace("+", " ")
    }


}
