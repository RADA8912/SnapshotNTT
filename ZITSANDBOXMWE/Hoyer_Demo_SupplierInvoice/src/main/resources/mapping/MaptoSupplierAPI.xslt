<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  
  <xsl:output method="xml" indent="yes" encoding="UTF-8"/>
  
  <!-- Variablen -->
  <xsl:variable name="year" select="substring(/root/DocumentDate,1,4)"/>
  <xsl:variable name="month" select="substring(/root/DocumentDate,6,2)"/>
  <xsl:variable name="day" select="substring(/root/DocumentDate,9,2)"/>
  
  <!-- Erzeuge 10-stelligen Dummy (z. B. 2025101601 → 10 Zeichen) -->
  <xsl:variable name="dynInvoiceId" select="substring(concat($year,$month,$day,'01'),1,10)"/>
  
  <!-- Root Template -->
  <xsl:template match="/root">
    <A_SupplierInvoice>
      <A_SupplierInvoiceType>
        <!-- Kopf -->
        <SupplierInvoice><xsl:value-of select="$dynInvoiceId"/></SupplierInvoice>
        <FiscalYear><xsl:value-of select="$year"/></FiscalYear>
        <CompanyCode><xsl:value-of select="CompanyCode"/></CompanyCode>
        <DocumentDate><xsl:value-of select="DocumentDate"/></DocumentDate>
        <PostingDate><xsl:value-of select="PostingDate"/></PostingDate>
        
        <!-- NEU: Steuerermittlungsdatum (Pflicht bei zeitabhängigen Steuern) -->
        <TaxDeterminationDate><xsl:value-of select="DocumentDate"/></TaxDeterminationDate>
        
        <SupplierInvoiceIDByInvcgParty>
          <xsl:value-of select="SupplierInvoiceIDByInvcgParty"/>
        </SupplierInvoiceIDByInvcgParty>
        <InvoicingParty><xsl:value-of select="InvoicingParty"/></InvoicingParty>
        <DocumentCurrency><xsl:value-of select="DocumentCurrency"/></DocumentCurrency>
        <InvoiceGrossAmount><xsl:value-of select="InvoiceGrossAmount"/></InvoiceGrossAmount>
        <TaxIsCalculatedAutomatically>
          <xsl:value-of select="TaxIsCalculatedAutomatically"/>
        </TaxIsCalculatedAutomatically>
        
        <!-- G/L Account Items -->
        <to_SupplierInvoiceItemGLAcct>
          <A_SupplierInvoiceItemGLAcctType>
            <SupplierInvoice><xsl:value-of select="$dynInvoiceId"/></SupplierInvoice>
            <FiscalYear><xsl:value-of select="$year"/></FiscalYear>
            <SupplierInvoiceItem><xsl:value-of select="to_SupplierInvoiceItemGLAcct/SupplierInvoiceItem"/></SupplierInvoiceItem>
            <CompanyCode><xsl:value-of select="CompanyCode"/></CompanyCode>
            <CostCenter><xsl:value-of select="to_SupplierInvoiceItemGLAcct/CostCenter"/></CostCenter>
            <GLAccount><xsl:value-of select="to_SupplierInvoiceItemGLAcct/GLAccount"/></GLAccount>
            <DocumentCurrency><xsl:value-of select="to_SupplierInvoiceItemGLAcct/DocumentCurrency"/></DocumentCurrency>
            <SupplierInvoiceItemAmount><xsl:value-of select="to_SupplierInvoiceItemGLAcct/SupplierInvoiceItemAmount"/></SupplierInvoiceItemAmount>
            <TaxCode><xsl:value-of select="to_SupplierInvoiceItemGLAcct/TaxCode"/></TaxCode>
            <DebitCreditCode><xsl:value-of select="to_SupplierInvoiceItemGLAcct/DebitCreditCode"/></DebitCreditCode>
          </A_SupplierInvoiceItemGLAcctType>
        </to_SupplierInvoiceItemGLAcct>
        
        <!-- Steuerdaten
        <to_SupplierInvoiceTax>
          <A_SupplierInvoiceTaxType>
            <SupplierInvoice><xsl:value-of select="$dynInvoiceId"/></SupplierInvoice>
            <FiscalYear><xsl:value-of select="$year"/></FiscalYear>
            <TaxCode><xsl:value-of select="to_SupplierInvoiceTax/TaxCode"/></TaxCode>
            <SupplierInvoiceTaxCounter>000001</SupplierInvoiceTaxCounter>
            <DocumentCurrency><xsl:value-of select="to_SupplierInvoiceTax/DocumentCurrency"/></DocumentCurrency>
            <TaxAmount><xsl:value-of select="to_SupplierInvoiceTax/TaxAmount"/></TaxAmount>
          </A_SupplierInvoiceTaxType>
        </to_SupplierInvoiceTax>
         --> 
         
      </A_SupplierInvoiceType>
    </A_SupplierInvoice>
  </xsl:template>
  
</xsl:stylesheet>
