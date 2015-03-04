/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.chaupal.jxta.platform.factory;

import java.util.Collection;

import net.jp2p.chaupal.jxta.platform.Activator;
import net.jp2p.container.properties.IJp2pProperties;
import net.jp2p.container.properties.IJp2pPropertySource;
import net.jxta.platform.NetworkConfigurator;

public class NetworkModuleFactory extends AbstractModuleFactory<Module> {

	private  NetworkConfigurator configurator;
	
	public NetworkModuleFactory( IJp2pPropertySource<IJp2pProperties> source,  NetworkConfigurator configurator ) {
		super(source, Activator.getModuleFactoryRegistrator() );
		this.configurator = configurator;
    }

	@SuppressWarnings("unchecked")
	@Override
	protected void addModules( Collection<IJxtaModuleService<Module>> modules ) {
		IJxtaModuleService<? extends Module> service = new ShadowPeerGroupModule( super.getSource(), configurator );
		//modules.add( (IJxtaModuleService<Module>) service );
		service = new PlatformModule( super.getSource(), configurator);
		modules.add( (IJxtaModuleService<Module>) service );
		service = new ContentServiceModule( super.getSource());
		modules.add( (IJxtaModuleService<Module>) service );
	}
}