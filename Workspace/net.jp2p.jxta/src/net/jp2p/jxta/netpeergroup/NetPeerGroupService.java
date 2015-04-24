/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.jxta.netpeergroup;

import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Logger;

import net.jp2p.container.component.AbstractJp2pService;
import net.jp2p.container.component.IJp2pComponent;
import net.jp2p.container.component.IJp2pComponentNode;
import net.jp2p.container.properties.IJp2pProperties;
import net.jp2p.container.properties.IJp2pWritePropertySource;
import net.jp2p.jxta.netpeergroup.NetPeerGroupFactory;
import net.jp2p.jxta.peergroup.PeerGroupPreferences;
import net.jxta.peergroup.PeerGroup;
import net.jxta.platform.NetworkManager;

public class NetPeerGroupService extends AbstractJp2pService<PeerGroup> implements IJp2pComponentNode<PeerGroup>{

	private NetworkManager manager;
	
	private Collection<IJp2pComponent<?>> modules;
	
	private Logger logger = Logger.getLogger( NetPeerGroupService.class.getName());

	public NetPeerGroupService( NetPeerGroupFactory factory, NetworkManager manager ) {
		super(( IJp2pWritePropertySource<IJp2pProperties> ) factory.getPropertySource(), null );
		this.manager = manager;
		modules = new ArrayList<IJp2pComponent<?>>();
	}

	@Override
	protected void activate() {
		try {
			PeerGroupPreferences preferences = new PeerGroupPreferences(( IJp2pWritePropertySource<IJp2pProperties> )super.getPropertySource() );
			manager.setPeerID( preferences.getPeerID());
			PeerGroup peergroup = manager.startNetwork();
			logger.info("\n\nNETWORK STARTED: " + manager.getInstanceName() + "\n\n");
			
			super.setModule( peergroup );
			super.activate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void deactivate() {
		manager.stopNetwork();
	}

	@Override
	public boolean isRoot() {
		return false;
	}

	@Override
	public boolean addChild(IJp2pComponent<?> child) {
		return modules.add( child );
	}

	@Override
	public void removeChild(IJp2pComponent<?> child) {
		modules.remove( child );
	}

	@Override
	public IJp2pComponent<?>[] getChildren() {
		return (IJp2pComponent<?>[]) modules.toArray( new IJp2pComponent[ modules.size()]);
	}

	@Override
	public boolean hasChildren() {
		return !modules.isEmpty();
	}	
}