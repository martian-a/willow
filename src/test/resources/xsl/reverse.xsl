<?xml version="1.0" encoding="UTF-8" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="2.0">
    
    <xsl:template match="/">
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