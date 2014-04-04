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
package net.jp2p.chaupal.jxta.root.network;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.jp2p.container.component.IJp2pComponent;
import net.jxta.refplatform.platform.NetworkConfigurator;

public class RelayConfiguration {

	private NetworkConfigurator configurator;
	
	public RelayConfiguration( NetworkConfigurator configurator ) {
		this.configurator = configurator;
	}

	public RelayConfiguration( IJp2pComponent<NetworkConfigurator> component ) {
		this( component.getModule() );
	}

	public int getMaxClients(){
		return this.configurator.getRelayMaxClients();
	}

	public void setMaxClients( int maxClients ){
		this.configurator.setRelayMaxClients( maxClients);
	}

	public URI[] getSeedingURIs(){
		return this.configurator.getRelaySeedingURIs();
	}

	public void setSeedingURIs( URI[] uris ){
		List<String> list = new ArrayList<String>();
		for( URI uri: uris )
			list.add( uri.getPath() );
		this.configurator.setRelaySeedURIs( list );
	}

	public void setSeedingURIs( List<String> uris ){
		this.configurator.setRelaySeedURIs( uris );
	}

	public URI[] getSeedURIs(){
		return this.configurator.getRelaySeedURIs();
	}

	public void setSeedURIs( URI[] uris ){
		Set<String> list = new HashSet<String>();
		for( URI uri: uris )
			list.add( uri.getPath() );
		this.configurator.setRelaySeedingURIs( list );
	}

	public void setSeedURIs( Set<String> uris ){
		this.configurator.setRelaySeedingURIs( uris );
	}
	
}
