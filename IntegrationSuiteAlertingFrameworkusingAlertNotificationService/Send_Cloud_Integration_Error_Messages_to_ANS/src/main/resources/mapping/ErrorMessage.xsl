<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:atom="http://www.w3.org/2005/Atom"
    xmlns:m="http://schemas.microsoft.com/ado/2007/08/dataservices/metadata"
    xmlns:d="http://schemas.microsoft.com/ado/2007/08/dataservices"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:multimap="http://sap.com/xi/XI/SplitAndMerge"
    exclude-result-prefixes="atom m d xsl multimap">
    
    <xsl:strip-space elements="*"/>
    <xsl:output indent="yes" omit-xml-declaration="no"
        media-type="application/xml" encoding="UTF-8" />
    
    <xsl:template match="multimap:Message1">            
            <!-- Creating Inidividual Error Message Groups by grouping on same interface ID --> 
        <MessageProcessingLog> 
             <xsl:for-each-group select="MessageProcessingLogs"
                 group-by="MessageProcessingLog/IntegrationFlowName">               
                    <Record>
                        <IntegrationFlowName>
                            <xsl:value-of select="MessageProcessingLog/IntegrationFlowName"/>
                        </IntegrationFlowName>
                        <IntegrationFlowID>
                            <xsl:value-of select="MessageProcessingLog/IntegrationArtifact/Id"/>
                        </IntegrationFlowID>
                        <IntegrationFlowPackageName>
                            <xsl:value-of select="MessageProcessingLog/IntegrationArtifact/PackageName"/>
                        </IntegrationFlowPackageName>
                        <Type>
                            <xsl:value-of select="MessageProcessingLog/IntegrationArtifact/Type" />
                        </Type>                     
                        <xsl:for-each select="//MessageProcessingLogs/MessageProcessingLog[IntegrationFlowName = current-grouping-key()]">
                    <error>
                        <MessageGuid>
                            <xsl:value-of select="MessageGuid" />
                        </MessageGuid>
                        <Status>
                            <xsl:value-of select="Status" />
                        </Status>
                        <LogStart>
                            <xsl:value-of select="LogStart" />
                        </LogStart>   
                        <LogEnd>
                            <xsl:value-of select="LogEnd" />
                        </LogEnd>
                        <AlternateWebLink>
                            <xsl:value-of select="AlternateWebLink" />
                        </AlternateWebLink>   
                        <OriginComponentName>
                            <xsl:value-of select="OriginComponentName" />
                        </OriginComponentName>   
                        <CorrelationId>
                            <xsl:value-of select="CorrelationId" />
                        </CorrelationId>  
                        <ErrorText>
                            <xsl:value-of select="../ErrorText" />
                        </ErrorText>
                    </error>                       
                    </xsl:for-each> 
                    </Record>
                  
              </xsl:for-each-group> 
         </MessageProcessingLog>  
    </xsl:template>
    
    
    <!-- Default templates to do nothing -->
    <!-- ########## -->
    <xsl:template match="text()" priority="-100"/>
    
</xsl:stylesheet>