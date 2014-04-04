/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.jxta.discovery;

import net.jp2p.container.Jp2pContainerPropertySource;
import net.jp2p.container.component.IJp2pComponent;
import net.jp2p.container.component.Jp2pComponent;
import net.jp2p.container.properties.IJp2pProperties;
import net.jp2p.container.properties.IJp2pPropertySource;
import net.jp2p.jxta.discovery.DiscoveryPropertySource;
import net.jp2p.jxta.factory.AbstractPeerGroupDependencyFactory;
import net.jp2p.jxta.factory.IJxtaComponents.JxtaComponents;
import net.jp2p.jxta.netpeergroup.NetPeerGroupFactory;
import net.jp2p.jxta.peergroup.PeerGroupPropertySource;
import net.jp2p.jxta.peergroup.PeerGroupPropertySource.PeerGroupDirectives;
import net.jxta.discovery.DiscoveryService;

public class DiscoveryServiceFactory extends
		AbstractPeerGroupDependencyFactory<DiscoveryService> {

	@Override
	public String getComponentName() {
		return JxtaComponents.DISCOVERY_SERVICE.toString();
	}

	@Override
	protected DiscoveryPropertySource onCreatePropertySource() {
		return new DiscoveryPropertySource( super.getParentSource() );
	}

	@SuppressWarnings("unchecked")
	@Override
	public void extendContainer() {
		
		//ALWAYS expect a peergroup, as the discovery service is not tied to a specific parent service
		Object peergroup = super.getPropertySource().getDirective( PeerGroupDirectives.PEERGROUP );
		if( peergroup == null )
			return;
		NetPeerGroupFactory factory = (NetPeerGroupFactory) this.getBuilder().getFactory( JxtaComponents.NET_PEERGROUP_SERVICE.toString() );
		if( factory != null )
			return;
		Jp2pContainerPropertySource root = (Jp2pContainerPropertySource) DiscoveryPropertySource.findRootPropertySource(this.getPropertySource() );
		IJp2pPropertySource<IJp2pProperties> source = (IJp2pPropertySource<IJp2pProperties>) PeerGroupPropertySource.findPropertySource( root, JxtaComponents.NET_PEERGROUP_SERVICE.toString() );
		if( source == null ){
			factory =  new NetPeerGroupFactory();
			factory.prepare(JxtaComponents.NET_PEERGROUP_SERVICE.toString() , (IJp2pPropertySource<IJp2pProperties>) source, super.getBuilder(), null);
			super.getBuilder().addFactory( factory );
			factory.createPropertySource();
		}
		super.extendContainer();
	}

	@Override
	protected IJp2pComponent<DiscoveryService> onCreateComponent(
			IJp2pPropertySource<IJp2pProperties> properties) {
		return new Jp2pComponent<DiscoveryService>( super.getPropertySource(), super.getDependency().getModule().getDiscoveryService());
	}
}