package com.mobilkontrol.remote

import com.mobilkontrol.data.repository.ParentalRepository
import com.mobilkontrol.security.PinHasher
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import fi.iki.elonen.NanoHTTPD

/**
 * Ebeveynin telefonundan aynı Wi-Fi üzerinden komut alabilmesi için
 * yerel HTTP sunucusu. PIN doğrulaması X-Pin başlığı ile yapılır.
 * Örnek: POST /addtime?minutes=15  (Header: X-Pin: 2580)
Aynı ağdaki tarayıcıdan http://<cihaz-ip>:8765/ ile basit kontrol paneli açılır.
 */
class RemoteControlServer(
    private val repository: ParentalRepository,
    port: Int = 8765
) : NanoHTTPD(port) {

    override fun serve(session: IHTTPSession): Response {
        return try {
            when (session.uri) {
                "/", "/index.html" -> htmlPage()
                "/status" -> json(statusJson())
                "/addtime", "/lock", "/unlock" -> {
                    val pin = session.headers["x-pin"] ?: ""
                    val hash = runBlocking { repository.settings.pinHash.firstOrNull() } ?: ""
                    if (!PinHasher.verify(pin, hash)) {
                        return newFixedLengthResponse(Response.Status.UNAUTHORIZED, "text/plain", "unauthorized")
                    }
                    when (session.uri) {
                        "/addtime" -> {
                            val minutes = session.parms["minutes"]?.toIntOrNull() ?: 0
                            runBlocking { repository.addBonusMinutes(minutes) }
                        }
                        "/lock" -> runBlocking { repository.setManualLock(true) }
                        "/unlock" -> runBlocking { repository.setManualLock(false) }
                    }
                    json("ok")
                }
                else -> newFixedLengthResponse(Response.Status.NOT_FOUND, "text/plain", "not found")
            }
        } catch (e: Exception) {
            newFixedLengthResponse(Response.Status.INTERNAL_ERROR, "text/plain", e.message)
        }
    }

    private fun json(body: String): Response =
        newFixedLengthResponse(Response.Status.OK, "application/json", body)

    private fun htmlPage(): Response = newFixedLengthResponse(
        Response.Status.OK, "text/html",
        """
        <html><head><meta charset="utf-8"><title>MobilKontrol</title></head>
        <body style="font-family:sans-serif;max-width:420px;margin:auto;padding:16px">
        <h2>MobilKontrol Uzaktan Kontrol</h2>
        <input id="pin" placeholder="Ebeveyn PIN" type="password" style="width:100%;padding:8px">
        <p id="out"></p>
        <button onclick="call('/status')">Durumu Getir</button>
        <button onclick="call('/addtime?minutes=5')">+5 dk</button>
        <button onclick="call('/addtime?minutes=15')">+15 dk</button>
        <button onclick="call('/addtime?minutes=30')">+30 dk</button>
        <button onclick="call('/lock')">Kilitle</button>
        <button onclick="call('/unlock')">Aç</button>
        <script>
        async function call(p){
          const r = await fetch(p, {method:'POST', headers:{'X-Pin': document.getElementById('pin').value}});
          document.getElementById('out').innerText = await r.text();
        }
        </script></body></html>
        """.trimIndent()
    )

    private fun statusJson(): String {
        val usage = runBlocking { repository.getTodayUsage() }
        val limit = runBlocking { repository.settings.getDailyLimitNow() }
        return """{"date":"${usage.date}","usedMinutes":${usage.usedMinutes},"bonusMinutes":${usage.bonusMinutes},"dailyLimitMinutes":$limit}"""
    }
}
