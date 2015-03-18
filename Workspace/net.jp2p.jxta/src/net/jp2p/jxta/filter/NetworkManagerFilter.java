/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.jxta.filter;

import net.jp2p.container.builder.ICompositeBuilderListener.BuilderEvents;
import net.jp2p.container.component.IJp2pComponent;
import net.jp2p.container.factory.ComponentBuilderEvent;
import net.jp2p.container.factory.IComponentFactory;
import net.jp2p.container.factory.filter.AbstractComponentFactoryFilter;
import net.jp2p.container.utils.StringStyler;
import net.jp2p.jxta.factory.IJxtaComponents.JxtaPlatformComponents;
import net.jxta.platform.NetworkManager;

public class NetworkManagerFilter<T extends Object> extends AbstractComponentFactoryFilter<T> {

	private boolean acceptManager;
	private boolean acceptConfigurator;
	private NetworkManager manager;
	
	public NetworkManagerFilter( IComponentFactory<T> factory ) {
		super( factory );
	}

	public NetworkManager getManager() {
		return manager;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean onAccept(ComponentBuilderEvent<?> event) {
		if( !BuilderEvents.FACTORY_COMPLETED.equals( event.getBuilderEvent()))
			return false;
		IComponentFactory<?> factory = (IComponentFactory<?>) event.getFactory();
		String name = StringStyler.styleToEnum( factory.getComponentName() );
		if( JxtaPlatformComponents.NETWORK_MANAGER.name().equals(name) ){
			acceptManager = true;
			IJp2pComponent<NetworkManager> comp = (IJp2pComponent<NetworkManager>) factory.getComponent();
			this.manager = comp.getModule();
		}
		if( JxtaPlatformComponents.NETWORK_CONFIGURATOR.name().equals(name) ){
			acceptConfigurator = true;
		}
		return ( this.acceptManager && this.acceptConfigurator);
	}
}
