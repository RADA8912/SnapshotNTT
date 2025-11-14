package src.main.resources.script

import com.sap.gateway.ip.core.customdev.util.Message
import org.apache.camel.Exchange
import org.apache.camel.builder.SimpleBuilder

class PipelineLogger {
    final List entries
    final String logLevel
	final String iFlow

    static PipelineLogger newLogger(Message message) {
        return new PipelineLogger(message)
    }

    private PipelineLogger() {}

    private PipelineLogger(Message message) {
        this.entries = []
		def headers = message.getHeaders()
		if (headers.find { it.toString().contains('auditLogHeader') }) {
			this.entries.add(headers.get('auditLogHeader'))
		}		
	//	this.logLevel = message.properties.SAP_MessageProcessingLogConfiguration.logLevel.toString()
		Exchange ex = message.exchange
		this.iFlow = SimpleBuilder.simple('${camelId}').evaluate(ex, String)
    }

    void addEntry(String location, String entry) {
    //    if (this.logLevel in ['DEBUG', 'TRACE'] || this.entries.size()) {
			def timeNow=new Date().format("yyyy-MM-dd'T'HH:mm:ss.SSSXXX",TimeZone.getTimeZone('GMT'))
	//		this.entries.add("[" + this.logLevel + "]" + timeNow + "|" + this.iFlow + "|" + location + "|" + entry)
			this.entries.add(timeNow + "|" + this.iFlow + "|" + location + "|" + entry)
	//	}
    }
    
    String getAuditLog() {
        StringBuilder sb = new StringBuilder()
		if (this.entries.size()) {
            this.entries.each { sb << it + ";;" }
        }
		sb.toString()
    }
	
    String getAuditLog4Print() {
        this.getAuditLog().replaceAll(';;', '\n')
    }
}
