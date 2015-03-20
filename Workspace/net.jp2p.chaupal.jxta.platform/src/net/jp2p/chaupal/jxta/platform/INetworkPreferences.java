/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.chaupal.jxta.platform;

import net.jp2p.container.properties.IJp2pProperties;
import net.jp2p.container.properties.IPropertyConvertor;
import net.jxta.platform.NetworkConfigurator;

public interface INetworkPreferences extends IPropertyConvertor<IJp2pProperties, String, Object>{

	/**
	 * Fill the given configurator with the source
	 * @param configurator
	 * @return
	 */
	boolean fillConfigurator(NetworkConfigurator configurator);
}