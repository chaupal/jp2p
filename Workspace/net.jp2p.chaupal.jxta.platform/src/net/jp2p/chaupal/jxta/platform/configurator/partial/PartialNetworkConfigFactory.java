/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.chaupal.jxta.platform.configurator.partial;

import net.jp2p.container.builder.IContainerBuilder;
import net.jp2p.container.factory.AbstractPropertySourceFactory;
import net.jp2p.container.properties.IJp2pProperties;
import net.jp2p.container.properties.IJp2pPropertySource;

public class PartialNetworkConfigFactory<T extends Object> extends AbstractPropertySourceFactory {

	private String componentName;
	
	@Override
	public void prepare(String componentName,
			IJp2pPropertySource<IJp2pProperties> parentSource,
			IContainerBuilder builder, String[] attributes) {
		super.prepare(componentName, parentSource, builder, attributes);
		this.componentName = componentName;
	}


	@Override
	public String getComponentName() {
		return componentName;
	}

	@Override
	protected PartialNetworkConfigPropertySource onCreatePropertySource() {
		return new PartialNetworkConfigPropertySource( this.componentName, super.getParentSource() );
	}
}
