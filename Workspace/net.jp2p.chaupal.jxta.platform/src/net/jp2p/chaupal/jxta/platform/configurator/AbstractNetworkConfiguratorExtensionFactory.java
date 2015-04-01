/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.chaupal.jxta.platform.configurator;

import net.jp2p.chaupal.jxta.platform.configurator.NetworkConfigurationFactory;
import net.jp2p.container.factory.AbstractPropertySourceFactory;
import net.jp2p.container.factory.ComponentBuilderEvent;
import net.jp2p.container.utils.StringStyler;
import net.jp2p.jxta.factory.IJxtaComponents.JxtaPlatformComponents;
import net.jxta.platform.NetworkConfigurator;

public abstract class AbstractNetworkConfiguratorExtensionFactory extends AbstractPropertySourceFactory{
	
	private NetworkConfigurator configurator;
	
	protected AbstractNetworkConfiguratorExtensionFactory( String componentName ) {
		super( componentName );
	}

	protected final NetworkConfigurator getConfigurator() {
		return configurator;
	}

	protected abstract void onNetworkConfiguratorCreated( NetworkConfigurationFactory factory );
	
	@Override
	public void notifyChange(ComponentBuilderEvent<Object> event) {
		if( !BuilderEvents.COMPONENT_CREATED.equals( event.getBuilderEvent()))
			return;
		String name = StringStyler.styleToEnum( event.getFactory().getComponentName() );
		if( !JxtaPlatformComponents.isComponent( name ))
			return;
		if( !JxtaPlatformComponents.NETWORK_CONFIGURATOR.equals( JxtaPlatformComponents.valueOf( name )))
			return;
		NetworkConfigurationFactory factory = (NetworkConfigurationFactory) event.getFactory();
		this.configurator = factory.getComponent().getModule();
		this.onNetworkConfiguratorCreated(factory);
		super.notifyChange(event);
	}
}