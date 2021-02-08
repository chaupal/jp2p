/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.container.xml;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Logger;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;

import net.jp2p.container.builder.ICompositeBuilder;
import net.jp2p.container.builder.ICompositeBuilderListener;
import net.jp2p.container.builder.IContainerBuilder;
import net.jp2p.container.builder.IFactoryBuilder;
import net.jp2p.container.context.IJp2pServiceManager;
import net.jp2p.container.factory.ContainerFactory;
import net.jp2p.container.properties.IJp2pDirectives.DeveloperModes;
import net.jp2p.container.sequencer.Jp2pBundleSequencer;
import net.jp2p.container.utils.Utils;
import net.jp2p.container.utils.io.IOUtils;

public class XMLFactoryBuilder implements ICompositeBuilder<ContainerFactory>, IFactoryBuilder {

	protected static final String JAXP_SCHEMA_SOURCE =
		    "http://java.sun.com/xml/jaxp/properties/schemaSource";
	protected static final String JP2P_XSD_SCHEMA = "http://www.condast.com/saight/jp2p-schema.xsd";

	private static final String S_ERR_NO_SCHEMA_FOUND = "The XML Schema was not found";
	private static final String S_WRN_NOT_NAMESPACE_AWARE = "The parser is not validating or is not namespace aware";
	
	static final String S_DOCUMENT_ROOT = "DOCUMENT_ROOT";

	static final String JAXP_SCHEMA_LANGUAGE = "http://java.sun.com/xml/jaxp/properties/schemaLanguage";
	static final String W3C_XML_SCHEMA = "http://www.w3.org/2001/XMLSchema";	
		
	private boolean completed, failed;
	private URL url;
	private IJp2pServiceManager manager;
	private Jp2pBundleSequencer<Object> sequencer;
	private IContainerBuilder<Object> builder;
	
	private String bundleId;
	private Class<?> clss;
	private DeveloperModes mode;
	
	private Collection<ICompositeBuilderListener<Object>> listeners;
		
	private Logger logger = Logger.getLogger( XMLFactoryBuilder.class.getName() );
	
	public XMLFactoryBuilder( String bundleId, Class<?> clss, DeveloperModes mode, IContainerBuilder<Object> builder, IJp2pServiceManager manager, Jp2pBundleSequencer<Object> sequencer ) {
		this( bundleId, clss.getResource( S_DEFAULT_LOCATION ), clss, mode, builder, manager, sequencer );
	}

	/**
	 * Build the factories from the given resource in the class file and add them to the container
	 * @param bundleId
	 * @param clss
	 * @param location
	 * @param builder
	 */
	public XMLFactoryBuilder( String bundleId, URL url, Class<?> clss, DeveloperModes mode, IContainerBuilder<Object> builder, IJp2pServiceManager manager, Jp2pBundleSequencer<Object> sequencer ) {
		this.bundleId = bundleId;
		this.manager = manager;
		this.sequencer = sequencer;
		this.builder = builder;
		this.url = url;
		this.clss = clss;
		this.mode = mode;
		this.completed = false;
		this.failed = false;
		this.listeners = new ArrayList<ICompositeBuilderListener<Object>>();
	}

	/* (non-Javadoc)
	 * @see net.osgi.jxta.factory.ICompositeFactory#addListener(net.osgi.jxta.factory.ICompositeFactoryListener)
	 */
	/* (non-Javadoc)
	 * @see net.osgi.jp2p.chaupal.xml.IFactoryBuilder#addListener(net.jp2p.container.builder.ICompositeBuilderListener)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void addListener( ICompositeBuilderListener<?> listener ){
		this.listeners.add((ICompositeBuilderListener<Object>) listener);
	}

	/* (non-Javadoc)
	 * @see net.osgi.jxta.factory.ICompositeFactory#removeListener(net.osgi.jxta.factory.ICompositeFactoryListener)
	 */
	/* (non-Javadoc)
	 * @see net.osgi.jp2p.chaupal.xml.IFactoryBuilder#removeListener(net.jp2p.container.builder.ICompositeBuilderListener)
	 */
	@Override
	public void removeListener( ICompositeBuilderListener<?> listener ){
		this.listeners.remove( listener);
	}

	/**
	 * Returns true if the url points to a valid resource
	 * @return
	 */
	public boolean canCreate(){
		if( url == null )
			return false;
		try {
			return ( url.openConnection().getContentLengthLong() > 0 );
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	
	/* (non-Javadoc)
	 * @see net.osgi.jp2p.chaupal.xml.IFactoryBuilder#build()
	 */
	@Override
	public void build() {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		URL schema_in = XMLFactoryBuilder.class.getResource( S_SCHEMA_LOCATION); 
		if( schema_in == null )
			throw new RuntimeException( S_ERR_NO_SCHEMA_FOUND );
		
		//factory.setNamespaceAware( true );
		//SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

		// note that if your XML already declares the XSD to which it has to conform, then there's no need to create a validator from a Schema object
		//Source schemaFile = new StreamSource( Jp2pHandler.class.getResourceAsStream( S_SCHEMA_LOCATION ));
		InputStream in;
		try {
			in = url.openStream();
		} catch (IOException e1) {
			e1.printStackTrace();
			return;
		}
		
		//First parse the XML file
		try {
			logger.info("Parsing JP2P Bundle: " + this.bundleId );
			//Schema schema = schemaFactory.newSchema(schemaFile);
			//factory.setSchema(schema);//saxParser.
			
			SAXParser saxParser = factory.newSAXParser();
			if( !saxParser.isNamespaceAware() )
				logger.warning( S_WRN_NOT_NAMESPACE_AWARE );
			
			//saxParser.setProperty(JAXP_SCHEMA_LANGUAGE, W3C_XML_SCHEMA); 
			//saxParser.setProperty(JAXP_SCHEMA_SOURCE, new File(JP2P_XSD_SCHEMA)); 
			logger.info("\n\nParsing JP2P Bundle: " + this.bundleId );
			Jp2pHandler handler = new Jp2pHandler( this.bundleId, mode, builder, manager, sequencer, clss );
			saxParser.parse( in, handler);
			logger.info("JP2P Bundle Parsed: " + this.bundleId + "\n\n");
		} catch( SAXNotRecognizedException e ){
			failed = true;
			e.printStackTrace();			
		} catch (ParserConfigurationException e) {
			failed = true;
			e.printStackTrace();
		} catch (SAXException e) {
			failed = true;
			e.printStackTrace();
		} catch (IOException e) {
			failed = true;
			e.printStackTrace();
		}
		finally{
			IOUtils.closeInputStream(in);
		}
		
		this.completed = true;
	}

	public boolean complete() {
		this.completed = true;
		return completed;
	}

	/* (non-Javadoc)
	 * @see net.osgi.jp2p.chaupal.xml.IFactoryBuilder#isCompleted()
	 */
	@Override
	public boolean isCompleted() {
		return completed;
	}

	public boolean hasFailed() {
		return failed;
	}

	public static String getLocation( String defaultLocation ){
		if( !Utils.isNull( defaultLocation ))
			return defaultLocation;
		return defaultLocation;
	}
}