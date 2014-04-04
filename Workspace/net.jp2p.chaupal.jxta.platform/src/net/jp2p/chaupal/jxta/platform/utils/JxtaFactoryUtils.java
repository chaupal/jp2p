/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.chaupal.jxta.platform.utils;

import net.jp2p.chaupal.jxta.platform.NetworkManagerFactory;
import net.jp2p.chaupal.jxta.platform.configurator.NetworkConfigurationFactory;
import net.jp2p.chaupal.jxta.platform.configurator.partial.PartialNetworkConfigFactory;
import net.jp2p.container.factory.IPropertySourceFactory;
import net.jp2p.container.utils.StringStyler;
import net.jp2p.container.utils.Utils;
import net.jp2p.jxta.factory.IJxtaComponents.JxtaNetworkComponents;
import net.jp2p.jxta.seeds.SeedListFactory;

public class JxtaFactoryUtils {

	/**
	 * Get the default factory for this container
	 * @param parent
	 * @param componentName
	 * @return
	 */
	public static IPropertySourceFactory getDefaultFactory( String componentName ){
		if( Utils.isNull(componentName))
			return null;
		String comp = StringStyler.styleToEnum(componentName);
		if( !JxtaNetworkComponents.isComponent( comp ))
			return null;
		JxtaNetworkComponents component = JxtaNetworkComponents.valueOf(comp);
		IPropertySourceFactory factory = null;
		switch( component ){
		case NETWORK_MANAGER:
			factory = new NetworkManagerFactory( );
			break;
		case NETWORK_CONFIGURATOR:
			factory = new NetworkConfigurationFactory( );
			break;
		case SEED_LIST:
			factory = new SeedListFactory( );
			break;
		case TCP:
		case HTTP:
		case HTTP2:
		case MULTICAST:
		case SECURITY:
			factory = new PartialNetworkConfigFactory<Object>();
			break;
		default:
			break;
		}
		return factory;
	}
}
