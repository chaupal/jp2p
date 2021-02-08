/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.jxta.factory;

import net.jp2p.container.component.IJp2pComponent;
import net.jp2p.container.factory.AbstractComponentDependencyFactory;
import net.jp2p.container.factory.filter.IComponentFactoryFilter;
import net.jp2p.jxta.filter.PeerGroupFilter;
import net.jxta.peergroup.PeerGroup;

public abstract class AbstractPeerGroupDependencyFactory<T extends Object> extends
		AbstractComponentDependencyFactory<T, IJp2pComponent<PeerGroup>> {

	
	protected AbstractPeerGroupDependencyFactory(String componentName) {
		super(componentName);
	}

	@Override
	protected IComponentFactoryFilter createFilter() {
		return new PeerGroupFilter<T>( this );
	}
	
	protected PeerGroup getPeerGroup(){
		return (PeerGroup) super.getDependency().getModule();
	}
}
