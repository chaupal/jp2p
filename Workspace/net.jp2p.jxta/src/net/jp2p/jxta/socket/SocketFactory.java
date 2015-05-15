/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.jxta.socket;

import java.net.URISyntaxException;

import net.jp2p.container.component.IJp2pComponent;
import net.jp2p.container.factory.ComponentBuilderEvent;
import net.jp2p.container.properties.IJp2pProperties;
import net.jp2p.container.properties.IJp2pPropertySource;
import net.jp2p.container.properties.IJp2pWritePropertySource;
import net.jp2p.jxta.advertisement.AdvertisementPropertySource;
import net.jp2p.jxta.factory.AbstractPeerGroupDependencyFactory;
import net.jp2p.jxta.factory.IJxtaComponents.JxtaComponents;
import net.jp2p.jxta.pipe.PipeAdvertisementPropertySource;
import net.jxta.pipe.PipeMsgListener;
import net.jxta.protocol.PipeAdvertisement;

public class SocketFactory extends AbstractPeerGroupDependencyFactory<PipeMsgListener>{
	
	private AdvertisementPropertySource pipeSource;
	private boolean canCreate;

	public SocketFactory() {
		super( JxtaComponents.JXSE_SOCKET_SERVICE.toString() );
	}

	
	@Override
	protected SocketPropertySource onCreatePropertySource() {
		return new SocketPropertySource( super.getParentSource() );
	}

	@Override
	protected IJp2pComponent<PipeMsgListener> onCreateComponent( IJp2pPropertySource<IJp2pProperties> properties) {
		PipeAdvertisement pipead;
		try {
			SocketPropertySource source = (SocketPropertySource) super.getPropertySource();
			pipead = PipeAdvertisementPropertySource.createPipeAdvertisement( this.pipeSource, super.getPeerGroup() );
			return new SocketService<PipeMsgListener>( source, super.getPeerGroup(), pipead );
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void onNotifyChange(ComponentBuilderEvent<Object> event) {
		if( super.isChildEvent( event )){
			IJp2pWritePropertySource<IJp2pProperties> source = (IJp2pWritePropertySource<IJp2pProperties>) event.getFactory().getPropertySource();
			this.pipeSource = (AdvertisementPropertySource) source;
		}
		super.notifyChange(event);
		if( this.pipeSource == null )
			return;
		if( super.canCreate() )
			this.canCreate = true;
		super.setCanCreate(canCreate && (this.pipeSource != null ));
	}
}