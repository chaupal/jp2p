/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.container.xml;

import net.jp2p.container.properties.IJp2pProperties;
import net.jp2p.container.properties.ManagedProperty;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public interface IJp2pHandler {

	public boolean startElement( String qName,	Attributes attributes) ;
	
	public void endElement( String qName);

	/**
	 * Parse the properties
	 * @param ch
	 * @param start
	 * @param length
	 * @throws SAXException
	 */
	public void parseProperties( ManagedProperty<IJp2pProperties, Object> property, String value );

}
