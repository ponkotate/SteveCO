package com.steveco.urgency

object ClientUrgencyData {
    var urgency: Int = 0
        private set

    fun update(value: Int) {
        urgency = value.coerceIn(0, 100)
    }

    fun reset() {
        urgency = 0
    }
}
