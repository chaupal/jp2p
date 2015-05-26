/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.chaupal.context;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Stack;
import java.util.logging.Logger;

import net.jp2p.chaupal.xml.IContextEntities;
import net.jp2p.container.context.IJp2pServiceBuilder;
import net.jp2p.container.context.IJp2pServiceBuilder.Components;
import net.jp2p.container.context.Jp2pServiceDescriptor;
import net.jp2p.container.factory.IJp2pComponents;
import net.jp2p.container.properties.ManagedProperty;
import net.jp2p.container.utils.StringStyler;
import net.jp2p.container.utils.Utils;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

class ServiceHandler extends DefaultHandler implements IContextEntities{

	public static final int MAX_COUNT = 200;
		
	private Stack<String> stack;
	private Collection<Jp2pServiceDescriptor> services;
	
	private boolean skip;

	public ServiceHandler() {
		this.skip = false;
		this.stack = new Stack<String>();
		this.services = new ArrayList<Jp2pServiceDescriptor>();
	}

	/**
	 * Get the root factory 
	 * @return
	 */
	Collection<Jp2pServiceDescriptor> getServices() {
		return services;
	}
	
	@Override
	public void startElement(String uri, String localName, String qName, 
			Attributes attributes) throws SAXException {
		if(skip)
			return;
		
		//First check for groups
		if( Groups.isGroup( qName )){
			stack.push( qName );
			skip = true;
			return;
		}
		if( IJp2pServiceBuilder.Components.isComponent( qName )){
			IJp2pComponents current = IJp2pServiceBuilder.Components.valueOf( StringStyler.styleToEnum( qName ));
			switch(( IJp2pServiceBuilder.Components )current ){
			case CONTEXT:
				stack.push( qName );
				skip = true;
				return;				
			default:
				break;
			}
		}
		String serviceName = StringStyler.styleToEnum(qName);
		String name = StringStyler.prettyStringFromXml( qName );
		boolean optional = Components.JP2P_CONTAINER.toString().equals( name );
		Jp2pServiceDescriptor info = new Jp2pServiceDescriptor( StringStyler.prettyString( serviceName ), optional );
		services.add( info );
		stack.push( qName );
	}
		
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		if( !stack.peek().equals( qName ))
			return;
		this.stack.pop();
		skip = false;
	}

	@Override
	public void characters(char ch[], int start, int length) throws SAXException {
	}

	@Override
	public void error(SAXParseException e) throws SAXException {
		print(e);
		super.error(e);
	}

	@Override
	public void fatalError(SAXParseException arg0) throws SAXException {
		print(arg0);
		super.fatalError(arg0);
	}

	@Override
	public void warning(SAXParseException arg0) throws SAXException {
		print(arg0);
		super.warning(arg0);
	}


	private MessageFormat message =
		      new MessageFormat("({0}: {1}, {2}): {3}");
	
	private void print(SAXParseException x)
	{
		String msg = message.format(new Object[]
				{
				x.getSystemId(),
				new Integer(x.getLineNumber()),
				new Integer(x.getColumnNumber()),
				x.getMessage()
				});
		Logger.getLogger( this.getClass().getName()).info(msg);
	}
	
	/**
	 * Returns true if one of the attributes allows creation of values
	 * @param attributes
	 * @return
	 */
	protected static boolean isCreated( Attributes attributes ){
		String attr = attributes.getValue( ManagedProperty.Attributes.PERSIST.name().toLowerCase() );
		if( Utils.isNull(attr))
			return false;
		return Boolean.parseBoolean( attr );
	}
}