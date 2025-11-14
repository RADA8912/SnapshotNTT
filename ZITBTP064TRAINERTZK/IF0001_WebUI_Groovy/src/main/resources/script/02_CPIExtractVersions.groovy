package btp064_Groovy_fuer_CPI

import com.sap.gateway.ip.core.customdev.util.Message
import org.apache.camel.CamelContext
import org.apache.camel.Exchange

import java.lang.management.ManagementFactory
import java.lang.management.RuntimeMXBean;


def Message processData(Message message) {

    Exchange ex = message.exchange
    CamelContext ctx = ex.getContext()

    // builder für das Verarbeiten von Zeichenketten und für die spätere Ausgabe
    // builder2 für das Verarbeiten von Zeichenketten und für die spätere Ausgabe als Anhang
    StringBuilder builder = new StringBuilder()
    StringBuilder builder2 = new StringBuilder()

    // Für die Identifizierung von Groovy und Java Versionen auf der CPI
    // Hier werden die Left-Shift-Operatoren '<<' verwendet, um die Variable builder mit der Zeichenkette zu befüllen
    // Alternativ kann mit builder.append() gearbeitet werden
    // Die Interpolation ${} dient als Platzhalter, um während der Laufzeit dynamische Werte zu instanziieren
    builder << "Groovy: ${GroovySystem.getVersion()}\r\n"
    builder << "Java: ${System.getProperty('java.version')}\r\n"

    // Auslesen von CamelContext-Informationen
    builder << "CamelContext Name/ID: ${ctx.getName()}\r\n"
    builder << "CamelContext Version: ${ctx.getVersion()}\r\n"
    message.setBody(builder.toString())

    // Folgende Ausgaben werden als Anhang hinzugefügt
    // JVM system properties
    System.properties.each {key, value -> builder2 << "${key}=${value}\n"}
    def messageLog = messageLogFactory.getMessageLog(message);
    messageLog.addAttachmentAsString("JVM system properties", builder2.toString(), "text/plain")

    // JVM startup arguments
    RuntimeMXBean runtimeMXBean = ManagementFactory.runtimeMXBean
    runtimeMXBean.inputArguments.each { builder2 << "${it}\n" }
    messageLog.addAttachmentAsString("JVM startup arguments", builder2.toString(), "text/plain")

    // Class methods
    String className = "com.sap.gateway.ip.core.customdev.util.Message"
    Class cls = Class.forName(className)
    cls.methods.each { builder2 << "${it}\n" }
    messageLog.addAttachmentAsString("Class methods", builder2.toString(), "text/plain")
    return message;
}

