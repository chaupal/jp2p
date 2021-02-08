/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.jxta.filter;

import net.jp2p.container.builder.ICompositeBuilderListener.BuilderEvents;
import net.jp2p.container.factory.AbstractComponentFactory;
import net.jp2p.container.factory.ComponentBuilderEvent;
import net.jp2p.container.factory.IComponentFactory;
import net.jp2p.container.factory.filter.AbstractComponentFactoryFilter;
import net.jp2p.container.properties.IJp2pPropertySource;
import net.jp2p.container.utils.Utils;
import net.jp2p.jxta.discovery.DiscoveryPropertySource;
import net.jp2p.jxta.factory.IJxtaComponents.JxtaComponents;
import net.jp2p.jxta.peergroup.PeerGroupFactory;
import net.jp2p.jxta.peergroup.PeerGroupPropertySource;
import net.jp2p.jxta.peergroup.PeerGroupPropertySource.PeerGroupDirectives;
import net.jxta.peergroup.PeerGroup;

public class PeerGroupFilter<T extends Object> extends AbstractComponentFactoryFilter<T> {

	private String peergroupName;
	
	private PeerGroup peergroup;
	
	public PeerGroupFilter( IComponentFactory<T> factory, String peergroupName ) {
		super( factory );
		this.peergroupName = peergroupName;
	}

	public PeerGroupFilter( IComponentFactory<T> factory ) {
		super( factory );
		IJp2pPropertySource<?> ancestor = DiscoveryPropertySource.findPropertySource( factory.getPropertySource(), PeerGroupDirectives.PEERGROUP );
		if( ancestor != null ){
			peergroupName = ancestor.getDirective(PeerGroupDirectives.PEERGROUP);
		}
		if( Utils.isNull( peergroupName ))
			peergroupName = PeerGroupPropertySource.S_NET_PEER_GROUP;
	}


	public PeerGroup getPeergroup() {
		return peergroup;
	}

	@Override
	public boolean onAccept(ComponentBuilderEvent event) {
		if( !BuilderEvents.COMPONENT_STARTED.equals( event.getBuilderEvent()))
			return false;
		PeerGroup pg = PeerGroupFactory.getPeerGroup((IComponentFactory<?>) event.getFactory());
		if( pg == null )
			return false;
		peergroup = pg;
		return ( peergroupName.toLowerCase().equals( peergroup.getPeerGroupName().toLowerCase()));	
	}

	/**
	 * Returns true if the factory contains the correct peergroup
	 * @param factory
	 * @return
	 */
	public static boolean isCorrectPeerGroup( IJp2pPropertySource<?> current, IComponentFactory<?> factory ){
		if( !AbstractComponentFactory.isComponentFactory( JxtaComponents.PEERGROUP_SERVICE, factory ) && 
				!AbstractComponentFactory.isComponentFactory( JxtaComponents.NET_PEERGROUP_SERVICE, factory ))
			return false;
		IJp2pPropertySource<?> ancestor = DiscoveryPropertySource.findPropertySource( current, PeerGroupDirectives.PEERGROUP );
		String peergroupName = null;
		if( ancestor != null ){
			peergroupName = ancestor.getDirective(PeerGroupDirectives.PEERGROUP);
		}
		if( Utils.isNull( peergroupName ))
			peergroupName = PeerGroupPropertySource.S_NET_PEER_GROUP;
		PeerGroup peergroup = PeerGroupFactory.getPeerGroup(factory);
		if( peergroup == null )
			return false;
		return ( peergroupName.toLowerCase().equals( peergroup.getPeerGroupName().toLowerCase()));	
	}

}
