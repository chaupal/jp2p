/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.jxta.socket;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.jp2p.container.builder.IContainerBuilder;
import net.jp2p.container.component.IJp2pComponent;
import net.jp2p.container.component.Jp2pComponent;
import net.jp2p.container.factory.AbstractComponentFactory;
import net.jp2p.container.factory.ComponentBuilderEvent;
import net.jp2p.container.properties.IJp2pProperties;
import net.jp2p.container.properties.IJp2pPropertySource;
import net.jp2p.container.properties.IJp2pWritePropertySource;
import net.jxta.document.AdvertisementFactory;
import net.jxta.id.IDFactory;
import net.jxta.pipe.PipeID;
import net.jxta.pipe.PipeService;
import net.jxta.protocol.PipeAdvertisement;

public class PipeAdvertisementFactory extends AbstractComponentFactory<PipeAdvertisement> implements IPipeAdvertisementFactory {

	public static final String S_PIPE_ADVERTISEMENT_SERVICE = "PipeAdvertisementService";

	public final static String SOCKETIDSTR = "urn:jxta:uuid-59616261646162614E5047205032503393B5C2F6CA7A41FBB0F890173088E79404";
	public final static String DEFAULT_SOCKET_NAME = "Default Socket Server";

	@Override
	public void prepare(String componentName,
			IJp2pPropertySource<IJp2pProperties> parentSource,
			IContainerBuilder builder, String[] attributes) {
		super.prepare(componentName, parentSource, builder, attributes);
		this.fillDefaultValues();
	}


	protected void fillDefaultValues() {
		IJp2pWritePropertySource<IJp2pProperties> source = (IJp2pWritePropertySource<IJp2pProperties>) super.getPropertySource();
		try {
			source.setProperty( Properties.SOCKET_ID, new URI( SOCKETIDSTR ));
		} catch (URISyntaxException e) {
			Logger log = Logger.getLogger( this.getClass().getName() );
			log.log( Level.SEVERE, e.getMessage() );
			e.printStackTrace();
		}
		source.setProperty( Properties.NAME, DEFAULT_SOCKET_NAME );
		source.setProperty( Properties.TYPE, PipeService.UnicastType );		
	}
	
	@Override
	public String getComponentName() {
		return S_PIPE_ADVERTISEMENT_SERVICE;
	}

	@Override
	protected IJp2pPropertySource<IJp2pProperties> onCreatePropertySource() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected IJp2pComponent<PipeAdvertisement> onCreateComponent( IJp2pPropertySource<IJp2pProperties> properties) {
		PipeID socketID = null;
		IJp2pPropertySource<IJp2pProperties> source = super.getPropertySource();
		try {
			socketID = (PipeID) IDFactory.fromURI( (URI) source.getProperty( Properties.SOCKET_ID ));
		} catch (URISyntaxException use) {
			use.printStackTrace();
		}
		PipeAdvertisement advertisement = (PipeAdvertisement)
				AdvertisementFactory.newAdvertisement(PipeAdvertisement.getAdvertisementType());
		advertisement.setPipeID(socketID);
		advertisement.setType( (String) source.getProperty( Properties.TYPE ));
		advertisement.setName( (String) source.getProperty( Properties.NAME ));
		return new Jp2pComponent<PipeAdvertisement>( super.getPropertySource(), advertisement );
	}

	@Override
	public void notifyChange(ComponentBuilderEvent<Object> event) {
		// TODO Auto-generated method stub
		
	}
}
