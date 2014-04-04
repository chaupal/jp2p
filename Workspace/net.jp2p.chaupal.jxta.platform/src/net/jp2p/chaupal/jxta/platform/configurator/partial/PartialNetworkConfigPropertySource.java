/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.chaupal.jxta.platform.configurator.partial;

import net.jp2p.chaupal.jxta.root.network.configurator.NetworkConfigurationPropertySource.NetworkConfiguratorProperties;
import net.jp2p.container.partial.PartialPropertySource;
import net.jp2p.container.properties.IJp2pProperties;
import net.jp2p.container.properties.IJp2pPropertySource;
import net.jp2p.container.utils.StringStyler;

/**
 * A partial property source breaks a parent up in distinct parts, based on the existence of dots (_8) in the given properties
 * @author Kees
 *
 * @param <IJp2pComponents>
 * @param <U>
 */
public class PartialNetworkConfigPropertySource extends PartialPropertySource{

	public PartialNetworkConfigPropertySource( IJp2pPropertySource<IJp2pProperties> parent) {
		super( parent );
	}

	public PartialNetworkConfigPropertySource( String componentName,  IJp2pPropertySource<IJp2pProperties> parent) {
		super( componentName, parent );
	}

	@Override
	public IJp2pProperties getIdFromString(String key) {
		String cat = this.getCategory().toLowerCase();
		String id = StringStyler.styleToEnum( cat + "." + key.toLowerCase() );
		return NetworkConfiguratorProperties.valueOf( id );
	}
}