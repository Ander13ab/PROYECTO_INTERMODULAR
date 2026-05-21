package com.hazelgym.mobile.data.model

private val NUMERIC_ID_PATTERN = Regex("""^(\d+)$""")
private val HAZELGYM_URI_PATTERN = Regex("""hazelgym://qr/(\d+)""", RegexOption.IGNORE_CASE)
private val QUERY_ID_PATTERN = Regex("""[?&]qrId=(\d+)""", RegexOption.IGNORE_CASE)
private val JSON_ID_PATTERN = Regex("""\"id\"\s*:\s*(\d+)""", RegexOption.IGNORE_CASE)
private val FALLBACK_DIGIT_PATTERN = Regex("""\d+""")

fun buildQrPayload(qrCode: QrCodeResponse): String {
    val params = buildList {
        add("type=${qrCode.tipo}")
        if (qrCode.esEntradaGimnasio) {
            add("entry=true")
        }
        qrCode.maquinaId?.let { add("machineId=$it") }
        qrCode.sesionClaseId?.let { add("sessionId=$it") }
    }

    val query = if (params.isEmpty()) "" else "?${params.joinToString("&")}"
    return "hazelgym://qr/${qrCode.id}$query"
}

fun extractQrCodeId(rawValue: String): Long? {
    val normalized = rawValue.trim()

    val patterns = listOf(
        NUMERIC_ID_PATTERN,
        HAZELGYM_URI_PATTERN,
        QUERY_ID_PATTERN,
        JSON_ID_PATTERN
    )

    for (pattern in patterns) {
        val id = pattern.find(normalized)?.groupValues?.getOrNull(1)?.toLongOrNull()
        if (id != null) return id
    }

    return FALLBACK_DIGIT_PATTERN.find(normalized)?.value?.toLongOrNull()
}

fun describeQrDestination(qrCode: QrCodeResponse): String {
    return when {
        qrCode.esEntradaGimnasio -> "Entrada general del gimnasio"
        qrCode.tipo.equals("MACHINE", ignoreCase = true) ->
            qrCode.maquinaNombre?.let { "Maquina: $it" } ?: "Maquina del gimnasio"
        qrCode.tipo.equals("CLASS_SESSION", ignoreCase = true) ->
            qrCode.sesionClaseResumen?.let { "Sesion: $it" } ?: "Sesion de clase"
        else -> "QR asociado"
    }
}
