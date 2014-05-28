/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.jxta.socket;

import net.jp2p.container.component.IJp2pComponent;
import net.jp2p.container.properties.IJp2pProperties;
import net.jp2p.container.properties.IJp2pPropertySource;
import net.jp2p.jxta.factory.AbstractPeerGroupDependencyFactory;
import net.jxta.pipe.PipeMsgListener;

public class SocketFactory extends AbstractPeerGroupDependencyFactory<PipeMsgListener>{

	@Override
	protected SocketPropertySource onCreatePropertySource() {
		return new SocketPropertySource( super.getParentSource() );
	}

	@Override
	protected IJp2pComponent<PipeMsgListener> onCreateComponent( IJp2pPropertySource<IJp2pProperties> properties) {
		return new SocketService( (SocketPropertySource) super.getPropertySource(), super.getPeerGroup() );
	}
}