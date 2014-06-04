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
	private PipeAdvertisement pipeAdv;

	public SocketService( SocketPropertySource ps, PeerGroup peerGroup, PipeAdvertisement pipeadv ) {
		super(( IJp2pWritePropertySource<IJp2pProperties> ) ps, null );
		this.peerGroup = peerGroup;
		this.pipeAdv = pipeadv;
	}

	/**
	 * Get the correct server socket by selecting the correct constructor
	 * @return
	 * @throws IOException
	 */
	protected JxtaSocket getSocket() throws IOException{
		SocketPropertySource source = (SocketPropertySource) super.getPropertySource();
		int time_out = (int) source.getProperty( SocketProperties.TIME_OUT );
		//boolean reliable = (boolean)source.getProperty( SocketProperties.RELIABLE );
		if( time_out <= 0 )
			return new JxtaSocket( peerGroup, pipeAdv );
		else
			return new JxtaSocket( peerGroup, pipeAdv, time_out );
	}

	/**
	 * Get the correct server socket by selecting the correct constructor
	 * @return
	 * @throws IOException
	 */
	protected JxtaServerSocket getServerSocket() throws IOException{
		SocketPropertySource source = (SocketPropertySource) super.getPropertySource();
		int back_log = (int) source.getProperty( SocketProperties.BACKLOG );
		int time_out = (int) source.getProperty( SocketProperties.TIME_OUT );
		boolean encrypt = (boolean)source.getProperty( SocketProperties.ENCRYPT );
		if(( back_log <= 0 ) && ( time_out <= 0 ))
			return new JxtaServerSocket( peerGroup, pipeAdv, encrypt );
		if(( time_out <= 0 ))
			return new JxtaServerSocket( peerGroup, pipeAdv, back_log, encrypt );
		else
			return new JxtaServerSocket( peerGroup, pipeAdv, back_log, time_out, encrypt );			
	}
	
	@Override
	protected void activate() {
		SocketPropertySource source = (SocketPropertySource) super.getPropertySource();
		SocketTypes type = SocketPropertySource.getSocketType(source);
		PipeMsgListener socket = null;
		try {
			switch( type ){
			case CLIENT:
				socket = this.getSocket();
				break;
			case SERVER:
				socket = this.getServerSocket();
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