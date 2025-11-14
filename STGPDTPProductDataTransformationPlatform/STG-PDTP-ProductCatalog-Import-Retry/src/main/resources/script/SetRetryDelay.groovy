/*
 * Copyright (c) 2022 Miele & Cie. KG - All rights reserved.
 */

import com.sap.gateway.ip.core.customdev.util.Message

import java.util.concurrent.TimeUnit

def Message processData(Message message) {
    String delayProperty = message.getProperty('RetryDelay')
    if (delayProperty) {
        try {
            def delayInterval = delayProperty as long
            if (delayInterval) {
                TimeUnit.SECONDS.sleep(delayInterval)
            }
        } catch (ignored) {
        }
    }
    return message
}