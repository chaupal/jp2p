/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.chaupal.jxta.platform.factory;

import java.net.URL;

import net.jp2p.chaupal.jxta.root.network.configurator.NetworkConfigurationPropertySource;
import net.jp2p.jxta.module.AbstractModuleComponent;
import net.jxta.document.Advertisement;
import net.jxta.impl.protocol.PlatformConfig;
import net.jxta.peergroup.PeerGroup;
import net.jxta.platform.ModuleClassID;
import net.jxta.protocol.ModuleImplAdvertisement;
import net.jxta.refplatform.peergroup.WorldPeerGroupFactory;
import net.jxta.refplatform.platform.NetworkConfigurator;

public class WorldPeerGroupModule extends AbstractModuleComponent<PeerGroup> {

    public WorldPeerGroupModule( NetworkConfigurationPropertySource source, NetworkConfigurator configurator ) {
		super(source, getWorldPeerGroup() );
	}
 
	@Override
	public ModuleClassID getModuleClassID() {
		return null;//TODO
	}


	@Override
	protected ModuleImplAdvertisement onCreateAdvertisement() {
		URL url = WorldPeerGroupModule.class.getResource( S_RESOURCE_LOCATION );
		return getAdvertisementFromResource(url, this.getModuleClassID());
	}

	@Override
	public Advertisement getAdvertisement(PlatformConfig config) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private static PeerGroup getWorldPeerGroup(){
		try{
			return new WorldPeerGroupFactory().getWorldPeerGroup();
		}
		catch( Exception ex ){
			ex.printStackTrace();
		}
		return null;
	}
}