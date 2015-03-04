/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.chaupal.jxta.platform.factory;

import java.net.URL;

import net.jp2p.container.properties.IJp2pProperties;
import net.jp2p.container.properties.IJp2pPropertySource;
import net.jxta.content.ContentService;
import net.jxta.document.Advertisement;
import net.jxta.impl.content.ContentServiceImpl;
import net.jxta.impl.protocol.PlatformConfig;
import net.jxta.peergroup.core.ModuleClassID;
import net.jxta.protocol.ModuleImplAdvertisement;

public class ContentServiceModule extends AbstractModuleComponent<ContentService> {

    public ContentServiceModule( IJp2pPropertySource<IJp2pProperties> source ) {
		super(source, new ContentServiceImpl());
	}
 
	@Override
	public ModuleClassID getModuleClassID() {
		return ContentServiceImpl.MODULE_SPEC_ID.getBaseClass();
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
}