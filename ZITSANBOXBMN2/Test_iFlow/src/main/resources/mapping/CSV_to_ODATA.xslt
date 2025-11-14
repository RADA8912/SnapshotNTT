<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template match="/">
		<WarehouseTask>
			<WarehouseTaskType>
				<EWMWarehouse>
					<xsl:value-of select="'1050'" />
				</EWMWarehouse>
				<WarehouseProcessType>
					<xsl:value-of select="root/WarehouseTask/Scanner" />
				</WarehouseProcessType>
				<SourceHandlingUnit>
					<xsl:value-of select="normalize-space(root/WarehouseTask/HU)" />
				</SourceHandlingUnit>
				<SourceStorageBin>
					<xsl:value-of select="normalize-space(root/WarehouseTask/KommNrVormaterial)" />
				</SourceStorageBin>
				<DestinationHandlingUnit>
					<xsl:value-of select="normalize-space(root/WarehouseTask/HU)" />
				</DestinationHandlingUnit>
				<DestinationStorageBin>
					<xsl:value-of select="normalize-space(root/WarehouseTask/KommNrVormaterial)" />
				</DestinationStorageBin>
			</WarehouseTaskType>
		</WarehouseTask>
	</xsl:template>
</xsl:stylesheet>