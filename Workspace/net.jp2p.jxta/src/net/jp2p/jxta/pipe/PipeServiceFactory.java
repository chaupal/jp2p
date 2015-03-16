/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.jxta.pipe;

import net.jp2p.container.component.IJp2pComponent;
import net.jp2p.container.component.Jp2pComponentNode;
import net.jp2p.container.properties.IJp2pProperties;
import net.jp2p.container.properties.IJp2pPropertySource;
import net.jp2p.jxta.factory.AbstractPeerGroupDependencyFactory;
import net.jp2p.jxta.factory.IJxtaComponents.JxtaComponents;
import net.jp2p.jxta.pipe.PipePropertySource;
import net.jxta.pipe.PipeService;

public class PipeServiceFactory extends
		AbstractPeerGroupDependencyFactory<PipeService> {

	
	public PipeServiceFactory() {
		super(JxtaComponents.PIPE_SERVICE.toString() );
	}

	@Override
	protected PipePropertySource onCreatePropertySource() {
		return new PipePropertySource( super.getParentSource() );
	}

	@Override
	protected IJp2pComponent<PipeService> onCreateComponent( IJp2pPropertySource<IJp2pProperties> properties) {
		return new Jp2pComponentNode<PipeService>( super.getPropertySource(), super.getDependency().getModule().getPipeService());
	}
}