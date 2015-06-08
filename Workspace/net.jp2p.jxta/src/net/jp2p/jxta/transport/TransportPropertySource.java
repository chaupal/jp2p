/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.jxta.transport;

import net.jp2p.container.properties.AbstractJp2pWritePropertySource;
import net.jp2p.container.properties.IJp2pProperties;
import net.jp2p.container.properties.IJp2pPropertySource;
import net.jp2p.container.properties.IJp2pWritePropertySource;
import net.jp2p.container.properties.IPropertyConvertor;
import net.jp2p.container.utils.StringStyler;

public class TransportPropertySource extends AbstractJp2pWritePropertySource
	implements IJp2pWritePropertySource<IJp2pProperties>

{	
	public static final int DEF_MIN_PORT = 1000;
	public static final int DEF_MAX_PORT = 9999;
	public static final int DEF_PORT = 9715;

	/**
	 * Supported default network types for transport
	 * 
	 */
	public enum NetworkTypes{
		UDP,
		TCP,
		HTTP;

		@Override
		public String toString() {
			return super.toString().toLowerCase();
		}	
	}

	/**
	 * Supported default properties for transport
	 * 
	 */
	public enum TransportProperties implements IJp2pProperties{
		INCOMING_STATUS,
		INTERFACE_ADDRESS,
		OUTGOING_STATUS,
		PORT,
		START_PORT,
		END_PORT,
		PUBLIC_ADDRESS,
		PUBLIC_ADDRESS_EXCLUSIVE;

		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString() );
		}	

		/**
		 * Returns true if the given property is valid for this enumeration
		 * @param property
		 * @return
		 */
		public static boolean isValidProperty( IJp2pProperties property ){
			if( property == null )
				return false;
			for( TransportProperties prop: values() ){
				if( prop.equals( property ))
					return true;
				if( prop.name().equals(property.name() ))
					return true;
			}
			return false;
		}

		public static TransportProperties convertTo( String str ){
			String enumStr = StringStyler.styleToEnum( str );
			return valueOf( enumStr );
		}
	}

	protected TransportPropertySource( String bundleID, String transport ) {
		super( bundleID, transport );
	}

	public TransportPropertySource( String transport, IJp2pPropertySource<IJp2pProperties> parent ) {
		super( transport, parent );
	}

	/**
	 * Get the start ort
	 * @return
	 */
	public int getStartPort(){
		return (Integer) super.getProperty( TransportProperties.START_PORT );
	}

	/**
	 * Get the end port
	 * @return
	 */
	public int getEndPort(){
		return (Integer) super.getProperty( TransportProperties.END_PORT );
	}

	@Override
	public boolean validate(IJp2pProperties id, Object value) {
		return false;
	}

	@Override
	public IPropertyConvertor<IJp2pProperties, String, Object> getConvertor() {
		return new Convertor( this );
	}

	private static class Convertor extends SimplePropertyConvertor{

		public Convertor(IJp2pPropertySource<IJp2pProperties> source) {
			super(source);
		}

		@Override
		public TransportProperties getIdFromString(String key) {
			return TransportProperties.valueOf( key );
		}
	}
}
