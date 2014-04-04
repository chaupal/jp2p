/*******************************************************************************
 * Copyright (c) 2013 Condast and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Kees Pieters - initial API and implementation
 *******************************************************************************/
package net.jp2p.chaupal.jxta;

import net.jp2p.container.factory.IJp2pComponents;
import net.jp2p.container.utils.StringStyler;

public interface IChaupalComponents{

	public enum ChaupalComponents implements IJp2pComponents{
		PERSISTENCE_SERVICE,
		NET_PEERGROUP_SERVICE,
		PEERGROUP_SERVICE,
		DISCOVERY_SERVICE,
		REGISTRATION_SERVICE,
		RENDEZVOUS_SERVICE,
		PIPE_SERVICE,
		JXSE_SOCKET,
		JXSE_SERVER_SOCKET,
		ADVERTISEMENT_SERVICE,
		HTTP_SERVICE;

		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString() );
		}	
		
		public static boolean isComponent( String str ){
			str = StringStyler.styleToEnum(str);
			if(( str == null ) || ( str.length() == 0 ))
				return false;
			for( ChaupalComponents comp: values()){
				if( comp.name().equals( str.toUpperCase() ))
					return true;
			}
			return false;
		}
	}
}