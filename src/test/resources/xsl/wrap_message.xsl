<?xml version="1.0" encoding="UTF-8" ?>
<xsl:stylesheet 
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema"
    version="2.0"
	exclude-result-prefixes="#all">
    
    <xsl:output
        encoding="UTF-8"
        method="xml"        
        indent="yes"
        omit-xml-declaration="no"
        doctype-public="-//KAIKODA//DTD DOCUMENT 1.0//EN"
        doctype-system="../../schema/document.dtd"
    />
    
    <xsl:param name="wrapper" select="'document'" as="xs:string" />
    <xsl:param name="message" select="''" as="xs:string?" />
    
    <xsl:template match="/">
        <!--xsl:processing-instruction name="xml-model">href="../../schema/document.dtd" type="application/xml-dtd"</xsl:processing-instruction-->
        <xsl:element name="{$wrapper}">            
        	<xsl:comment><xsl:value-of select="concat(' ', $message, ' ')" /></xsl:comment>
        	<p><xsl:value-of select="$message" /></p>
        </xsl:element>    	
    </xsl:template>      
    
</xsl:stylesheet>