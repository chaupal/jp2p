/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.jxta.socket;

import net.jp2p.container.component.IJp2pComponent;
import net.jp2p.container.component.Jp2pComponent;
import net.jp2p.container.factory.AbstractComponentFactory;
import net.jp2p.container.properties.IJp2pDirectives;
import net.jp2p.container.properties.IJp2pProperties;
import net.jp2p.container.properties.IJp2pPropertySource;
import net.jp2p.container.properties.IJp2pWritePropertySource;
import net.jp2p.jxta.socket.ISocketFactory;
import net.jp2p.jxta.socket.SocketPipeAdvertisementFactory;
import net.jxta.protocol.PipeAdvertisement;
import net.jxta.refplatform.platform.NetworkManager;
import net.jxta.socket.JxtaSocket;

public class SocketFactory extends AbstractComponentFactory<JxtaSocket> implements ISocketFactory{

	public static final String S_JXSE_SOCKET_SERVICE = "JxtaSocketService";
	
	private NetworkManager manager;
	private PipeAdvertisementFactory pipeFactory;

	public SocketFactory() {
		super();
		//this.manager = manager;
		//this.fillDefaultValues();
	}
	
	@Override
	public String getComponentName() {
		return S_JXSE_SOCKET_SERVICE;
	}


	public NetworkManager getManager() {
		return manager;
	}


	public IPipeAdvertisementFactory getPipeFactory() {
		return pipeFactory;
	}


	protected void fillDefaultValues() {
		IJp2pWritePropertySource<IJp2pProperties> source = (IJp2pWritePropertySource<IJp2pProperties>) super.getPropertySource();
		source.setProperty( Properties.TIME_OUT, 5000 );
		source.setProperty( Properties.SO_TIME_OUT, 0 );
		source.setProperty( Properties.WAIT_FOR_RENDEZ_VOUS, false );
	}

	
	@Override
	protected void onParseDirectivePriorToCreation(IJp2pDirectives directive, Object value) {
		// TODO Auto-generated method stub
		
	}

	@SuppressWarnings("unchecked")
	@Override
	protected IJp2pComponent<JxtaSocket> onCreateComponent( IJp2pPropertySource<IJp2pProperties> properties) {
		this.pipeFactory = new SocketPipeAdvertisementFactory();
		JxtaSocket socket = this.createSocket();
		//super.setCompleted(true);
		return new Jp2pComponent(  super.getPropertySource(), socket );
	}
		
	/**
	 * Create the socket
	 * @param manager
	 * @return
	 */
	private JxtaSocket createSocket() {
		PipeAdvertisement pipeAd = this.pipeFactory.getComponent().getModule();
		return null;//TODO Change to include parent factory: new JxtaSocketComponent( manager, pipeAd, super.getProperties() );
	}


	@Override
	protected IJp2pPropertySource<IJp2pProperties> onCreatePropertySource() {
		// TODO Auto-generated method stub
		return null;
	}
}