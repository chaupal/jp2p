/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.jxta.registration;

import java.util.Enumeration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

import net.jp2p.container.activator.AbstractActivator;
import net.jp2p.container.activator.IJp2pService;
import net.jp2p.container.log.Jp2pLevel;
import net.jp2p.container.properties.IJp2pProperties;
import net.jp2p.container.properties.IJp2pPropertySource;
import net.jp2p.container.properties.IJp2pWritePropertySource;
import net.jp2p.jxta.registration.RegistrationService;
import net.jp2p.jxta.registration.RegistrationPropertySource.RegistrationProperties;
import net.jxta.discovery.DiscoveryEvent;
import net.jxta.discovery.DiscoveryListener;
import net.jxta.document.Advertisement;
import net.jxta.protocol.DiscoveryResponseMsg;

public class RegistrationService extends AbstractActivator implements IJp2pService<RegistrationService>, Runnable, DiscoveryListener {
	
	private IJp2pWritePropertySource<IJp2pProperties> source;
	private ExecutorService executor;
	
	public RegistrationService( IJp2pWritePropertySource<IJp2pProperties> source ) {
		super();
		this.source = source;
		executor = Executors.newSingleThreadExecutor();
	}

	/**
	 * Get a String label for this component. This can be used for display options and 
	 * is not meant to identify the component;
	 * @return
	 */
	public String getComponentLabel(){
		return this.source.getComponentName();
	}
	
	@Override
	public IJp2pPropertySource<IJp2pProperties> getPropertySource() {
		// TODO Auto-generated method stub
		return null;
	}


	/**
	 * Implement pure discovery
	 */
	protected void discovery() {
		try {
			//String peerId = ( String )this.getProperty( RegistrationProperties.PEER_ID );
			//String attribute = ( String )this.getProperty( RegistrationProperties.ATTRIBUTE );
			//String wildcard = ( String )this.getProperty( RegistrationProperties.WILDCARD );
			//int threshold = ( Integer )this.getProperty( RegistrationProperties.THRESHOLD );

			//String adType = AdvertisementTypes.convert(( AdvertisementTypes) this.getProperty( null /*DiscoveryProperties.ADVERTISEMENT_TYPE*/ ));
			//discovery.getLocalAdvertisements( Integer.parseInt( adType ), attribute, wildcard );
			//discovery.getRemoteAdvertisements( peerId,  Integer.parseInt( adType ), attribute, wildcard, threshold, null);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * The activities performed in an active state. By defalt this is discovery
	 */
	protected void onActiveState(){
		//DiscoveryMode mode = ( DiscoveryMode )this.getProperty( RegistrationProperties.DISCOVERY_MODE );
		//if(!( mode.equals( AdvertisementMode.PUBLISH )))
		 // this.discovery();		
	}

	
	@Override
	public boolean start() {
		//service.addDiscoveryListener(this);
		this.executor.execute(this);
		return super.start();
	}

	@Override
	public void run() {
		int wait_time = ( Integer )this.getPropertySource().getProperty( RegistrationProperties.WAIT_TIME );
		while ( super.isActive()) {
			this.onActiveState();
			try {
				Thread.sleep(wait_time);
			} catch (Exception e) {
				Logger log = Logger.getLogger( this.getClass().getName() );
				log.log( Jp2pLevel.JP2PLEVEL, this.getClass().getSimpleName() + "Interrupted" );
			}
		}
	}
		
	@Override
	protected void deactivate() {
		Thread.currentThread().interrupt();
		//service.removeDiscoveryListener(this );
	}

	@Override
	public void discoveryEvent(DiscoveryEvent event) {
		DiscoveryResponseMsg res = event.getResponse();
		// let's get the responding peer's advertisement
		System.out.println(" [ Got a Discovery Response [" +
				res.getResponseCount() + " elements] from peer : " +
				event.getSource() + " ]");
		Advertisement adv;
		Enumeration<?> en = res.getAdvertisements();
		if (en != null) {
			while (en.hasMoreElements()) {
				adv = (Advertisement) en.nextElement();
				System.out.println(adv);
			}
		}
	}

	@Override
	public String getId() {
		return this.source.getId();
	}

	@Override
	public RegistrationService getModule() {
		return null;//this.service;
	}

	@Override
	protected boolean onInitialising() {
		return true;
	}

	@Override
	protected void onFinalising() {
	}
}