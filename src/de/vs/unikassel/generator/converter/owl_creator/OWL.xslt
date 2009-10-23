<?xml version="1.0" encoding="UTF-8"?>
<!-- This XSLT-document is used by the "OWL_Creator"-class to transform a tanonomy-description-document into an OWL-document. -->
<!-- @author Marc Kirchhoff -->

<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:owl="http://www.w3.org/2002/07/owl#"
xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#" xmlns:rdfs="http://www.w3.org/2000/01/rdf-schema#">
	<xsl:output method="xml" version="1.0" encoding="UTF-8" indent="yes"/>
  	<xsl:key name="depthMapConcept" match="concept" use="count(ancestor::*)"/>
  	<xsl:key name="depthMapInstance" match="instance" use="count(ancestor::*)"/>
 
	<xsl:template match="/">
    <!-- Create the basic-owl-document-structure. -->
		<rdf:RDF 
			xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#"
			xmlns:owl="http://www.w3.org/2002/07/owl#"
			xmlns="http://www.ws-challenge.org/wsc08.owl#"
			xmlns:xsd="http://www.w3.org/2001/XMLSchema#"
			xmlns:rdfs="http://www.w3.org/2000/01/rdf-schema#"
			xml:base="http://www.ws-challenge.org/wsc08.owl">
			<owl:Ontology rdf:about=""/>
      
      	<!-- Create a <owl:Class>-element for each <concept>-elements. -->
      	<!-- The <owl:Class>-elements are ordered by the derivation depth of the corresponding <concept>-elements. -->
			<xsl:call-template name="concepts"/>				
      
      	<!-- Create an OWL-Instance for each <instance>-element. -->
     	<!-- The OWL-Instance-elements are ordered by the derivation depth of the corresponding <concept>-elements. -->
     	<xsl:call-template name="instances"/>        
      
		</rdf:RDF>
	</xsl:template>
	
  <!-- Traverse the <concept>-elements in a breadth-first-way.  -->
	<xsl:template name="concepts">		
		<xsl:param name="depth" select="1"/>
		<xsl:variable name="concept_nodes_at_this_depth" select="key('depthMapConcept', $depth)"/>
		<xsl:apply-templates select="$concept_nodes_at_this_depth"/>
		<xsl:if test="count($concept_nodes_at_this_depth)>0">
			<xsl:call-template name="concepts">			 
			  <xsl:with-param name="depth" select="$depth + 1"/>       
			</xsl:call-template>
		</xsl:if>    
  </xsl:template>

  <!-- Traverse the <instance>-elements in a breadth-first-way.  -->
  <xsl:template name="instances">    
    <xsl:param name="depth" select="1"/>
    <xsl:variable name="concept_nodes_at_this_depth" select="key('depthMapConcept', $depth)"/>
    <xsl:variable name="instance_nodes_at_this_depth" select="key('depthMapInstance', $depth)"/>
    <xsl:apply-templates select="$instance_nodes_at_this_depth"/>
    <xsl:if test="count($concept_nodes_at_this_depth)>0">
      <xsl:call-template name="instances">        
        <xsl:with-param name="depth" select="$depth + 1"/>
      </xsl:call-template>
    </xsl:if>
  </xsl:template>

  <!-- Create an <owl:Class>-element. -->
	<xsl:template match="concept">
    <owl:Class rdf:ID="{@name}">
      <xsl:if test="name(..)='concept'">
        <rdfs:subClassOf rdf:resource="#{../@name}"/>
      </xsl:if>
    </owl:Class>
	</xsl:template>

  <!-- Create an OWL-instance-element. -->
  <xsl:template match="instance">
    <xsl:if test="name(..)='concept'">
      <owl:Thing rdf:ID="{@name}">
        <rdf:type rdf:resource="#{../@name}"/>
      </owl:Thing>    
    </xsl:if>    
  </xsl:template>

	
</xsl:stylesheet>
