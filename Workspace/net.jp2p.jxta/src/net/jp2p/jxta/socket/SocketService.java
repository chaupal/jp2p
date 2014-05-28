/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.jxta.socket;

import java.io.IOException;
import java.io.InputStream;

import net.jp2p.container.component.AbstractJp2pService;
import net.jp2p.container.properties.IJp2pProperties;
import net.jp2p.container.properties.IJp2pWritePropertySource;
import net.jp2p.jxta.pipe.PipeAdvertisementPropertySource;
import net.jp2p.jxta.socket.SocketPropertySource.SocketProperties;
import net.jp2p.jxta.socket.SocketPropertySource.SocketTypes;
import net.jxta.peergroup.PeerGroup;
import net.jxta.pipe.PipeMsgListener;
import net.jxta.protocol.PipeAdvertisement;
import net.jxta.socket.JxtaMulticastSocket;
import net.jxta.socket.JxtaServerSocket;
import net.jxta.socket.JxtaSocket;

public class SocketService extends AbstractJp2pService<PipeMsgListener>{

	private PeerGroup peerGroup;

	public SocketService( SocketPropertySource source, PeerGroup peerGroup ) {
		super(( IJp2pWritePropertySource<IJp2pProperties> ) source, null );
		this.peerGroup = peerGroup;
	}

	@Override
	protected void activate() {
		SocketPropertySource source = (SocketPropertySource) super.getPropertySource();
		SocketTypes type = (SocketTypes) source.getProperty( SocketProperties.TYPE );
		int time_out = (int) source.getProperty( SocketProperties.TIME_OUT );
		PipeMsgListener socket = null;
		try {
			PipeAdvertisement pipeAdv = PipeAdvertisementPropertySource.createPipeAdvertisement(source, peerGroup);
			switch( type ){
			case CLIENT:
				socket = new JxtaSocket(peerGroup, pipeAdv, time_out );
				break;
			case SERVER:
				socket = new JxtaServerSocket( peerGroup, pipeAdv, time_out);
				break;
			case MULTICAST:
				socket = new JxtaMulticastSocket( peerGroup, pipeAdv );
				break;
			}
			super.setModule( socket );
			super.activate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void deactivate() {
		PipeMsgListener socket = super.getModule();
		try {
			if( socket instanceof JxtaSocket ){
				JxtaSocket js = (JxtaSocket) socket;
				InputStream in = js.getInputStream();
				in.close();
				js.close();
			}
			if( socket instanceof JxtaServerSocket ){
				JxtaServerSocket js = (JxtaServerSocket) socket;
				js.close();
			}
			if( socket instanceof JxtaMulticastSocket ){
				JxtaMulticastSocket js = (JxtaMulticastSocket) socket;
				js.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	
}