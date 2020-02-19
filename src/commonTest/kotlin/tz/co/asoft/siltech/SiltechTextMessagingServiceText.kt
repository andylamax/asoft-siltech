package tz.co.asoft.siltech

import tz.co.asoft.test.AsyncTest
import kotlin.test.Test

class SiltechTextMessagingServiceText : AsyncTest() {
    private val service = SiltechTextMessagingService("35E4C86A0C1A31")

    private val longMessage = """
        This is a very long test message to see if you Eng. Andy Lamax have implemented correctly the sms part of this application
        This makes it can be confirmed that all is well and happily acquired. This will be the last and final typing
    """.trimIndent()

    @Test
    fun should_send_sms_to_255620296750() = asyncTest {
        service.send("15076", listOf("255620296750"), longMessage, false)
    }
}