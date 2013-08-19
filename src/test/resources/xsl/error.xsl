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

    
    <xsl:template match="/">
        <xsl:apply-templates />  	
    </xsl:template>      
    
    <xsl:template match="document">
        <xsl:value-of select="." />
    </xsl:template>
    
</xsl:stylesheet>