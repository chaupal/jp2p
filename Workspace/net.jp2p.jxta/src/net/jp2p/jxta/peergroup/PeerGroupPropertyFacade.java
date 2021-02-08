/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.jxta.peergroup;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import net.jp2p.container.properties.AbstractPropertyFacade;
import net.jp2p.container.properties.IJp2pProperties;
import net.jp2p.jxta.peergroup.PeerGroupPropertySource.PeerGroupProperties;
import net.jxta.peergroup.PeerGroup;

public class PeerGroupPropertyFacade extends AbstractPropertyFacade<PeerGroup> {

	public PeerGroupPropertyFacade( String bundleId, PeerGroup module) {
		super( bundleId, module);
	}

	@Override
	public Object getProperty( IJp2pProperties id) {
		PeerGroup peergroup = super.getModule();
		if(!( id instanceof PeerGroupProperties))
			return null;
		PeerGroupProperties property  = ( PeerGroupProperties )id;
		switch( property ){
		case PEERGROUP_ID:
			return peergroup.getPeerGroupID();
		case NAME:
			return peergroup.getPeerGroupName();
		case PEER_ID:
			return peergroup.getPeerID();
		case PEER_NAME:
			return peergroup.getPeerName();
		case STORE_HOME:
			return peergroup.getStoreHome();
		default:
			break;
		}
		return null;
	}

	@Override
	public Iterator<IJp2pProperties> propertyIterator() {
		Collection<IJp2pProperties> results = new ArrayList<IJp2pProperties>();
		for( PeerGroupProperties nmp: PeerGroupProperties.values())
			results.add( nmp );
		return results.iterator();
	}
}
