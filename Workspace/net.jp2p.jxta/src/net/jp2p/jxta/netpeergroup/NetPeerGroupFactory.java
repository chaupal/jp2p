/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.jxta.netpeergroup;

import java.util.HashMap;

import net.jp2p.container.builder.IContainerBuilder;
import net.jp2p.container.component.IJp2pComponent;
import net.jp2p.container.factory.AbstractComponentDependencyFactory;
import net.jp2p.container.factory.filter.IComponentFactoryFilter;
import net.jp2p.container.properties.IJp2pProperties;
import net.jp2p.container.properties.IJp2pPropertySource;
import net.jp2p.container.properties.IJp2pDirectives.Directives;
import net.jp2p.jxta.factory.JxtaFactoryUtils;
import net.jp2p.jxta.factory.IJxtaComponents.JxtaComponents;
import net.jp2p.jxta.filter.NetworkManagerFilter;
import net.jp2p.jxta.netpeergroup.NetPeerGroupService;
import net.jp2p.jxta.peergroup.PeerGroupPropertySource;
import net.jp2p.jxta.rendezvous.RendezVousPropertySource;
import net.jxta.peergroup.PeerGroup;
import net.jxta.platform.NetworkManager;

public class NetPeerGroupFactory extends AbstractComponentDependencyFactory<PeerGroup, IJp2pComponent<NetworkManager>>{

	public NetPeerGroupFactory() {
		super( JxtaComponents.NET_PEERGROUP_SERVICE.toString());
	}	

	@Override
	public void extendContainer() {
		IContainerBuilder<?> builder = super.getBuilder();
		RendezVousPropertySource rdvps = (RendezVousPropertySource) JxtaFactoryUtils.getOrCreateChildFactory( builder, new HashMap<String, String>(), super.getPropertySource(), JxtaComponents.RENDEZVOUS_SERVICE.toString(), true ).getPropertySource();
		rdvps.setDirective( Directives.AUTO_START, this.getPropertySource().getDirective( Directives.AUTO_START ));
		super.extendContainer();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	protected IComponentFactoryFilter createFilter() {
		return new NetworkManagerFilter( this );
	}

	@Override
	public PeerGroupPropertySource onCreatePropertySource() {
		PeerGroupPropertySource source = new PeerGroupPropertySource( JxtaComponents.NET_PEERGROUP_SERVICE.toString(), super.getParentSource());
		return source;
	}
	
	@Override
	protected IJp2pComponent<PeerGroup> onCreateComponent( IJp2pPropertySource<IJp2pProperties> properties) {
		NetworkManagerFilter<?> filter = (NetworkManagerFilter<?>) super.getFilter();
		return new NetPeerGroupService( this, filter.getManager() );
	}
}