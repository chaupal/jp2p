/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.jxta.socket;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.jp2p.container.component.IJp2pComponent;
import net.jp2p.container.component.Jp2pComponent;
import net.jp2p.container.factory.AbstractComponentFactory;
import net.jp2p.container.properties.IJp2pProperties;
import net.jp2p.container.properties.IJp2pPropertySource;
import net.jp2p.container.utils.IOUtils;
import net.jp2p.jxta.socket.IServerSocketFactory;
import net.jp2p.jxta.socket.SocketPipeAdvertisementFactory;
import net.jxta.protocol.PipeAdvertisement;
import net.jxta.refplatform.platform.NetworkManager;
import net.jxta.socket.JxtaServerSocket;

public class ServerSocketFactory extends AbstractComponentFactory<JxtaServerSocket> implements IServerSocketFactory {

	public static final String S_JXSE_SERVER_SOCKET_SERVICE = "JxtaServerSocketService";	
	
	private NetworkManager manager;
	private PipeAdvertisementFactory pipeFactory;

	public ServerSocketFactory() {
		//super( container );
		//this.manager = manager;
		//this.fillDefaultValues();
	}

	protected void fillDefaultValues() {
		//super.getPropertySource().setProperty( Properties.TIME_OUT, 10 );
		//super.getPropertySource().setProperty( Properties.SO_TIME_OUT, 0 );
	}

	@Override
	public String getComponentName() {
		return S_JXSE_SERVER_SOCKET_SERVICE;
	}	

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	protected IJp2pComponent<JxtaServerSocket> onCreateComponent(IJp2pPropertySource<IJp2pProperties> properties) {
		this.pipeFactory = new SocketPipeAdvertisementFactory();
		JxtaServerSocket socket = this.createSocket();
		super.setCompleted(true);
		return new Jp2pComponent(  super.getPropertySource(),socket );
	}
		
	/**
	 * Create the socket
	 * @param manager
	 * @return
	 */
	private JxtaServerSocket createSocket() {
		PipeAdvertisement pipeAd = this.pipeFactory.getComponent().getModule();
		JxtaServerSocket serverSocket = null;
		try {
			IJp2pPropertySource<IJp2pProperties> source = super.getPropertySource();
			serverSocket = new JxtaServerSocket( manager.getNetPeerGroup(), pipeAd, ( boolean )source.getProperty( Properties.TIME_OUT ));
			serverSocket.setSoTimeout(( int )source.getProperty( Properties.SO_TIME_OUT ));
			return serverSocket;
		} catch (IOException e) {
			Logger log = Logger.getLogger( this.getClass().getName() );
			log.log( Level.SEVERE, e.getMessage() );
			e.printStackTrace();
			IOUtils.closeSocket( serverSocket );
		}
		return null;
	}

	@Override
	protected IJp2pPropertySource<IJp2pProperties> onCreatePropertySource() {
		// TODO Auto-generated method stub
		return null;
	}
}