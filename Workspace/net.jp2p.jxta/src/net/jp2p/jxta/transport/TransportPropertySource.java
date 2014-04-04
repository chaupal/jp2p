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
import net.jp2p.container.utils.StringStyler;

public class TransportPropertySource extends AbstractJp2pWritePropertySource
	implements IJp2pWritePropertySource<IJp2pProperties>

{	
	public static final int DEF_MIN_PORT = 1000;
	public static final int DEF_MAX_PORT = 9999;
	public static final int DEF_PORT = 9715;

	/**
	 * Supported default properties for transport
	 * 
	 */
	public enum TransportProperties implements IJp2pProperties{
		INCOMING_STATUS,
		INTERFACE_ADDRESS,
		OUTGOING_STATUS,
		PORT,
		PUBLIC_ADDRESS,
		TO_PUBLIC_ADDRESS_EXCLUSIVE,
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


	@Override
	public TransportProperties getIdFromString(String key) {
		return TransportProperties.valueOf( key );
	}

	@Override
	public boolean validate(IJp2pProperties id, Object value) {
		return false;
	}
}
