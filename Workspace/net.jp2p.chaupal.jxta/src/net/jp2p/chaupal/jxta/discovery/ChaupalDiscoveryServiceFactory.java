/*******************************************************************************
 * Copyright (c) 2013 Condast and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Kees Pieters - initial API and implementation
 *******************************************************************************/
package net.jp2p.chaupal.jxta.discovery;

import net.jp2p.container.component.IJp2pComponent;
import net.jp2p.container.factory.ComponentBuilderEvent;
import net.jp2p.container.properties.IJp2pProperties;
import net.jp2p.container.properties.IJp2pPropertySource;
import net.jp2p.container.properties.IJp2pWritePropertySource;
import net.jxta.discovery.DiscoveryService;
import net.jp2p.jxta.discovery.DiscoveryServiceFactory;

public class ChaupalDiscoveryServiceFactory extends DiscoveryServiceFactory {

	@Override
	public void notifyChange(ComponentBuilderEvent<Object> event) {
		System.out.println( event.toString());
		super.notifyChange(event);
	}

	@Override
	protected ChaupalDiscoveryService onCreateComponent( IJp2pPropertySource<IJp2pProperties> source) {
		IJp2pComponent<DiscoveryService> ds = super.onCreateComponent( source );
		ChaupalDiscoveryService service = new ChaupalDiscoveryService( (IJp2pWritePropertySource<IJp2pProperties>) source, ds.getModule() );
		return service;
	}

}