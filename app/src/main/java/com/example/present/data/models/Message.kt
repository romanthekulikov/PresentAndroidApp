package com.example.present.data.models

data class Message(
    var text: String? = null,
    var time: Long? = null,
    var userId: Int? = null,
    var messageId: String? = null, //TODO: Сменить String на Int
    var replayId: String? = null
)
