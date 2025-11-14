/*
 * Copyright (c) 2022 Miele & Cie. KG - All rights reserved.
 */

import com.sap.gateway.ip.core.customdev.util.Message

Message processData(Message message) {

    boolean doRetry = message.getProperty('DoRetry').toBoolean()
    String errorMessage = message.getProperty('ErrorMessage')
    String errorTrace = message.getProperty('ErrorTrace')

    if (doRetry &&
            (errorMessage.contains('com.sap.gateway.core.ip.component.exceptions.OData4Exception') ||
                    errorTrace.contains('org.apache.olingo.client.api.http.HttpClientException') ||
                    errorMessage.contains('java.io.IOException: Broken pipe') ||
                    errorMessage.contains('Failed to start a new transaction') ||
                    errorMessage.contains('Unexpected exception during cancelation of member') ||
                    errorMessage.contains('com.sap.esb.security.TrustManagerFactory'))) {
        message.setProperty('PerformRetry', 'Yes')
    } else {
        message.setProperty('PerformRetry', 'No')
    }
    return message
}