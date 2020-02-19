package tz.co.asoft.siltech

import io.ktor.client.HttpClient
import io.ktor.client.request.forms.FormPart
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.http.encodeURLParameter
import tz.co.asoft.comms.TextMessagingService as TextService

class SiltechTextMessagingService(private val key: String) : TextService {
    private val client = HttpClient { }

    private fun getData(sender: String, receivers: List<String>, body: String, hasUnicodeChars: Boolean) = mutableListOf<FormPart<Any>>().apply {
        add(FormPart("key", key))
        add(FormPart("campaign", 1))
        add(FormPart("routeid", 10))
        add(FormPart("type", if (hasUnicodeChars) "unicode" else "text"))
        add(FormPart("contacts", receivers.joinToString(",")))
        add(FormPart("senderid", sender))
        add(FormPart("msg", body))
    }

    private fun getFormData(sender: String, receivers: List<String>, body: String, hasUnicodeChars: Boolean) = formData {
        getData(sender, receivers, body, hasUnicodeChars).forEach { append(it) }
    }

    private suspend fun postRequest(sender: String, receivers: List<String>, body: String, hasUnicodeChars: Boolean) {
        client.post<String>("https://siltechtz.com/app/smsapi/index.php") {
            this.body = MultiPartFormDataContent(getFormData(sender, receivers, body, hasUnicodeChars))
        }
    }

    private suspend fun getRequest(sender: String, receivers: List<String>, body: String, hasUnicodeChars: Boolean) {
        val data = getData(sender, receivers, body, hasUnicodeChars).joinToString("&") { "${it.key}=${it.value.toString().encodeURLParameter(true)}" }
        val response = client.get<String>("https://siltechtz.com/app/smsapi/index.php?$data")
        if (!response.contains("SMS-SHOOT-ID")) {
            throw Throwable("Failed to send sms to ${receivers.joinToString(",", limit = 2)}: $response")
        }
    }

    override suspend fun send(sender: String, receivers: List<String>, body: String, hasUnicodeChars: Boolean) {
        getRequest(sender, receivers, body, hasUnicodeChars)
    }
}