<?xml version="1.0" encoding="UTF-8" ?>
<xsl:stylesheet 
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema"
    version="2.0">
    
    <xsl:output
        encoding="UTF-8"
        method="xml"        
        indent="yes"
        omit-xml-declaration="no"
        doctype-public="-//KAIKODA//DTD DOCUMENT 1.0//EN"
        doctype-system="../../schema/document.dtd"
    />
    
    <xsl:template match="/">
        <!-- xsl:processing-instruction name="xml-model">href="../../schema/document.dtd" type="application/xml-dtd"</xsl:processing-instruction-->         
        
        <xsl:apply-templates>
            <xsl:sort select="position()" order="descending" />
        </xsl:apply-templates>
    </xsl:template>
    
    <xsl:template match="*">
        <xsl:element name="{name()}">
            <xsl:copy-of select="@*" />
            <xsl:apply-templates>
                <xsl:sort select="position()" order="descending" />
            </xsl:apply-templates>
        </xsl:element>
    </xsl:template>
    
    <xsl:template match="comment()">
        <xsl:copy-of select="." />
    </xsl:template>
    
    <xsl:template match="text()">
        <xsl:value-of select="." /> 
    </xsl:template>
    
</xsl:stylesheet>