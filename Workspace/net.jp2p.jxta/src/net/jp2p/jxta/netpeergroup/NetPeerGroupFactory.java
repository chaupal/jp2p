/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.jxta.netpeergroup;

import net.jp2p.container.component.IJp2pComponent;
import net.jp2p.container.factory.AbstractComponentDependencyFactory;
import net.jp2p.container.factory.ComponentBuilderEvent;
import net.jp2p.container.factory.IComponentFactory;
import net.jp2p.container.factory.filter.ComponentCreateFilter;
import net.jp2p.container.factory.filter.IComponentFactoryFilter;
import net.jp2p.container.properties.IJp2pProperties;
import net.jp2p.container.properties.IJp2pPropertySource;
import net.jp2p.container.utils.StringStyler;
import net.jp2p.jxta.factory.IJxtaComponents.JxtaNetworkComponents;
import net.jp2p.jxta.factory.IJxtaComponents.JxtaComponents;
import net.jp2p.jxta.netpeergroup.NetPeerGroupService;
import net.jp2p.jxta.peergroup.PeerGroupPropertySource;
import net.jxta.peergroup.PeerGroup;
import net.jxta.platform.NetworkConfigurator;
import net.jxta.platform.NetworkManager;

public class NetPeerGroupFactory extends AbstractComponentDependencyFactory<PeerGroup, IJp2pComponent<NetworkManager>>{

	private boolean complete;
	
	@Override
	public String getComponentName() {
		return JxtaComponents.NET_PEERGROUP_SERVICE.toString();
	}	

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	protected IComponentFactoryFilter createFilter() {
		return new ComponentCreateFilter( BuilderEvents.FACTORY_COMPLETED, JxtaNetworkComponents.NETWORK_MANAGER.toString(), this );
	}

	@Override
	public PeerGroupPropertySource onCreatePropertySource() {
		PeerGroupPropertySource source = new PeerGroupPropertySource( JxtaComponents.NET_PEERGROUP_SERVICE.toString(), super.getParentSource());
		return source;
	}
	
	
	@Override
	public void notifyChange(ComponentBuilderEvent<Object> event) {
		super.notifyChange(event);
		if( !BuilderEvents.COMPONENT_CREATED.equals( event.getBuilderEvent()))
				return;
		IComponentFactory<?> factory = (IComponentFactory<?>) event.getFactory();
		String name = StringStyler.styleToEnum( factory.getComponentName() );
		if( JxtaNetworkComponents.NETWORK_CONFIGURATOR.name().equals(name) ){
			complete = true;
		}
	}

	@Override
	public boolean complete() {
		return super.complete() && ( this.complete );
	}

	@Override
	protected IJp2pComponent<PeerGroup> onCreateComponent( IJp2pPropertySource<IJp2pProperties> properties) {
		return new NetPeerGroupService( this, (NetworkManager) super.getDependency().getModule() );
	}
}