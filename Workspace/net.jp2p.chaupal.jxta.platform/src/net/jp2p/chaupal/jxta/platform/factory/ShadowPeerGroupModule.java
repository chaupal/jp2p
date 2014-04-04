/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.chaupal.jxta.platform.factory;

import java.net.URI;

import net.jp2p.chaupal.jxta.root.network.configurator.NetworkConfigurationPropertySource.NetworkConfiguratorProperties;
import net.jp2p.container.properties.IJp2pProperties;
import net.jp2p.container.properties.IJp2pPropertySource;
import net.jp2p.jxta.module.AbstractModuleComponent;
import net.jxta.document.Advertisement;
import net.jxta.impl.peergroup.Platform;
import net.jxta.impl.peergroup.ShadowPeerGroup;
import net.jxta.impl.peergroup.StdPeerGroup;
import net.jxta.impl.protocol.PlatformConfig;
import net.jxta.platform.ModuleClassID;
import net.jxta.protocol.ModuleImplAdvertisement;
import net.jxta.refplatform.platform.NetworkConfigurator;

public class ShadowPeerGroupModule extends AbstractModuleComponent<StdPeerGroup> {

    public ShadowPeerGroupModule( IJp2pPropertySource<IJp2pProperties> source, NetworkConfigurator configurator ) {
		super(source, new Platform( configurator.getPlatformConfig(), ( URI)source.getProperty( NetworkConfiguratorProperties.STORE_HOME )));
	}
 
	@Override
	public ModuleClassID getModuleClassID() {
		return null;//TODO
	}


	@Override
	protected ModuleImplAdvertisement onCreateAdvertisement() {
		return ShadowPeerGroup.getDefaultModuleImplAdvertisement();
	}

	@Override
	public Advertisement getAdvertisement(PlatformConfig config) {
		// TODO Auto-generated method stub
		return null;
	}
}