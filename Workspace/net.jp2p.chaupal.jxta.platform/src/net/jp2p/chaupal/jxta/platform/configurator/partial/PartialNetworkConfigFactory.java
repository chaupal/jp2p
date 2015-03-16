/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.chaupal.jxta.platform.configurator.partial;

import java.util.Map;

import net.jp2p.container.builder.IContainerBuilder;
import net.jp2p.container.factory.AbstractPropertySourceFactory;
import net.jp2p.container.properties.IJp2pProperties;
import net.jp2p.container.properties.IJp2pPropertySource;

public class PartialNetworkConfigFactory<T extends Object> extends AbstractPropertySourceFactory {

	
	public PartialNetworkConfigFactory() {
		super( null );
	}


	@Override
	public void prepare( IJp2pPropertySource<IJp2pProperties> parentSource,
			IContainerBuilder builder, Map<String, String> attributes) {
		super.prepare( parentSource, builder, attributes);
	}


	@Override
	protected PartialNetworkConfigPropertySource onCreatePropertySource() {
		return new PartialNetworkConfigPropertySource( super.getComponentName(), super.getParentSource() );
	}
}
