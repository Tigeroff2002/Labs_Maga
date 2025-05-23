<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
    xmlns:ns="http://example.com/schema"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:exsl="http://exslt.org/common"
    extension-element-prefixes="exsl">

    <xsl:output method="html" encoding="UTF-8" doctype-system="http://www.w3.org/TR/html4/strict.dtd"/>

    <xsl:template match="/">
        <html>
        <head>
            <meta charset="UTF-8"/>
            <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
            <title>XSLT преобразование</title>
        </head>
        <body>
            <h1>Данные о дорогах города</h1>
            
            <h2>Пользовательская информация</h2>
            <xsl:apply-templates select="//ns:user"/>
            
            <h2>Информация о запросе</h2>
            <xsl:apply-templates select="//ns:request"/>
            
            <h2>Сегменты построенного маршрута</h2>
            <table border="1">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Start Coord</th>
                        <th>End Coord</th>
                        <th>Length (m)</th>
                        <th>Quality Factor</th>
                        <th>User Intensity</th>
                        <th>Efficiency</th>
                    </tr>
                </thead>
                <tbody>
                    <xsl:apply-templates select="//ns:roadSegment">
                        <xsl:sort select="ns:length" data-type="number" order="descending"/>
                    </xsl:apply-templates>
                </tbody>
                <tfoot>
                    <tr>
                        <td colspan="3">Total Length</td>
                        <td>
                            <xsl:value-of select="sum(//ns:roadSegment/ns:length)"/>
                        </td>
                        <td colspan="2">Average Efficiency</td>
                        <td>
                            <xsl:variable name="efficiency-sum">
                                <xsl:call-template name="calculate-efficiency-sum"/>
                            </xsl:variable>
                            <xsl:variable name="efficiency-count" select="count(//ns:roadSegment)"/>
                            <xsl:value-of select="format-number($efficiency-sum div $efficiency-count, '0%')"/>
                        </td>
                    </tr>
                </tfoot>
            </table>
        </body>
        </html>
    </xsl:template>
    
    <xsl:template name="calculate-efficiency-sum">
        <xsl:param name="nodes" select="//ns:roadSegment"/>
        <xsl:param name="sum" select="0"/>
        
        <xsl:choose>
            <xsl:when test="$nodes">
                <xsl:variable name="current-efficiency">
                    <xsl:apply-templates select="$nodes[1]" mode="calculate-efficiency"/>
                </xsl:variable>
                <xsl:call-template name="calculate-efficiency-sum">
                    <xsl:with-param name="nodes" select="$nodes[position() > 1]"/>
                    <xsl:with-param name="sum" select="$sum + $current-efficiency"/>
                </xsl:call-template>
            </xsl:when>
            <xsl:otherwise>
                <xsl:value-of select="$sum"/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    
    <xsl:template match="ns:roadSegment" mode="calculate-efficiency">
        <xsl:choose>
            <xsl:when test="ns:user_intensity &lt; 0.5">
                <xsl:value-of select="ns:quality_factor * 0.5"/>
            </xsl:when>
            <xsl:otherwise>
                <xsl:value-of select="ns:quality_factor * ns:user_intensity"/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    
    <xsl:template match="ns:user">
        <p><strong>Email:</strong> <xsl:value-of select="ns:email"/></p>
        <p><strong>Name:</strong> <xsl:value-of select="ns:full_name"/></p>
    </xsl:template>
    
    <xsl:template match="ns:request">
        <p><strong>Start Point:</strong> <xsl:value-of select="ns:start_point"/></p>
        <p><strong>End Point:</strong> <xsl:value-of select="ns:end_point"/></p>
        <p><strong>Status:</strong> <xsl:value-of select="ns:status"/></p>
    </xsl:template>
    
    <xsl:template match="ns:roadSegment">
        <tr>
            <td><xsl:value-of select="ns:id"/></td>
            <td><xsl:value-of select="ns:start_coord"/></td>
            <td><xsl:value-of select="ns:end_coord"/></td>
            <td><xsl:value-of select="ns:length"/></td>
            <td><xsl:value-of select="ns:quality_factor"/></td>
            <td><xsl:value-of select="ns:user_intensity"/></td>
            <td>
                <xsl:choose>
                    <xsl:when test="ns:user_intensity &lt; 0.5">
                        <xsl:value-of select="format-number(ns:quality_factor * 0.5, '0%')"/>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:value-of select="format-number(ns:quality_factor * ns:user_intensity, '0%')"/>
                    </xsl:otherwise>
                </xsl:choose>
            </td>
        </tr>
    </xsl:template>

</xsl:stylesheet>