/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.jxta.factory;

import net.jp2p.container.factory.IJp2pComponents;
import net.jp2p.container.utils.StringStyler;

public interface IJxtaComponents{

	public enum JxtaComponents implements IJp2pComponents{
		NET_PEERGROUP_SERVICE,
		DISCOVERY_SERVICE,
		PEERGROUP_SERVICE,
		REGISTRATION_SERVICE,
		RENDEZVOUS_SERVICE,
		ADVERTISEMENT,
		PIPE_SERVICE,
		JXSE_SOCKET_SERVICE;

		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString() );
		}	
		
		public static boolean isComponent( String str ){
			str = StringStyler.styleToEnum(str);
			if(( str == null ) || ( str.length() == 0 ))
				return false;
			for( JxtaComponents comp: values()){
				if( comp.name().equals( str ))
					return true;
			}
			return false;
		}
	}

	public enum JxtaPlatformComponents implements IJp2pComponents{
		PLATFORM,
		NETWORK_MANAGER,
		NETWORK_CONFIGURATOR,
		SEED_LIST,
		SECURITY,
		TCP,
		HTTP,
		HTTP2,
		MULTICAST;

		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString() );
		}	
		
		public static boolean isComponent( String str ){
			str = StringStyler.styleToEnum(str);
			if(( str == null ) || ( str.length() == 0 ))
				return false;
			for( JxtaPlatformComponents comp: values()){
				if( comp.name().equals( str ))
					return true;
			}
			return false;
		}
	}

}