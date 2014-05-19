/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.jxta.advertisement.service;

import net.jp2p.container.component.IJp2pComponent;
import net.jp2p.container.properties.IJp2pProperties;
import net.jp2p.container.properties.IJp2pPropertySource;
import net.jp2p.jxta.advertisement.AdvertisementPropertySource;
import net.jxta.document.Advertisement;

public class JxtaAdvertisementFactory extends
		AbstractJxtaAdvertisementFactory<Advertisement, Advertisement> {

	
	@Override
	protected AdvertisementPropertySource onCreatePropertySource() {
		return super.onCreatePropertySource();
	}

	@Override
	protected Advertisement createAdvertisement(IJp2pPropertySource<IJp2pProperties> source) {
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected IJp2pComponent<Advertisement> createComponent(Advertisement advertisement) {
		return (IJp2pComponent<Advertisement>) this.createAdvertisement( super.getPropertySource());
	}

}
