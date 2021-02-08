/*******************************************************************************
 * Copyright (c) 2013 Condast and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Kees Pieters - initial API and implementation
 *******************************************************************************/
package net.jp2p.chaupal.jxse.discovery;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

import net.jp2p.container.Jp2pContainer;
import net.jp2p.container.activator.AbstractJp2pServiceNode;
import net.jp2p.container.component.ComponentChangedEvent;
import net.jp2p.container.component.ComponentEventDispatcher;
import net.jp2p.container.log.Jp2pLevel;
import net.jp2p.container.properties.AbstractJp2pPropertySource;
import net.jp2p.container.properties.IJp2pProperties;
import net.jp2p.container.properties.IJp2pPropertySource;
import net.jp2p.container.properties.IJp2pWritePropertySource;
import net.jp2p.container.properties.IJp2pDirectives.Directives;
import net.jxta.discovery.DiscoveryEvent;
import net.jxta.discovery.DiscoveryListener;
import net.jxta.discovery.DiscoveryService;
import net.jxta.document.Advertisement;
import net.jxta.protocol.DiscoveryResponseMsg;
import net.jp2p.jxta.advertisement.AdvertisementPropertySource;
import net.jp2p.jxta.advertisement.IAdvertisementProvider;
import net.jp2p.jxta.discovery.DiscoveryPropertySource.DiscoveryMode;
import net.jp2p.jxta.discovery.DiscoveryPropertySource.DiscoveryProperties;

public class ChaupalDiscoveryService extends AbstractJp2pServiceNode<DiscoveryService> implements IAdvertisementProvider{
	
	private ExecutorService executor;
	private Runnable runnable;
	
	private DiscoveryListener listener;
	private int size;
	private ChaupalDiscoveryService service;

	public ChaupalDiscoveryService( IJp2pWritePropertySource<IJp2pProperties> source, DiscoveryService discoveryService ) {
		super( source, discoveryService );
		this.size = 0;
		this.service = this;
		executor = Executors.newSingleThreadExecutor();
	}
	
	@Override
	public String getComponentLabel() {
		return super.getComponentLabel() + "( " + this.size + " )";
	}


	@Override
	public synchronized Advertisement[] getAdvertisements() {
		Collection<Advertisement> advertisements = discovery( false );
		return advertisements.toArray( new Advertisement[advertisements.size()]);
	}

	/**
	 * Implement pure discovery
	 */
	protected synchronized Collection<Advertisement> discovery( boolean remote ) {
		DiscoveryService discovery = super.getModule();
		Collection<Advertisement> advertisements = new ArrayList<Advertisement>();
		try {
			IJp2pPropertySource<IJp2pProperties> source = super.getPropertySource();
			String peerId = ( String )source.getProperty( DiscoveryProperties.PEER_ID );
			String attribute = ( String )source.getProperty( DiscoveryProperties.ATTRIBUTE );
			String wildcard = ( String )source.getProperty( DiscoveryProperties.WILDCARD );
			int threshold = ( Integer )source.getProperty( DiscoveryProperties.THRESHOLD );
			int adType = AdvertisementPropertySource.AdvertisementTypes.convertForDiscovery(( AdvertisementPropertySource.AdvertisementTypes) source.getProperty( DiscoveryProperties.ADVERTISEMENT_TYPE ));
			Enumeration<Advertisement> enm= discovery.getLocalAdvertisements( adType, attribute, wildcard );
			while( enm.hasMoreElements()){
				advertisements.add(enm.nextElement());
			}
			if( remote )
				discovery.getRemoteAdvertisements( peerId,  adType, attribute, wildcard, threshold, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return advertisements;
	}

	/**
	 * Get the size of the advertisements that were found
	 * @return
	 */
	public int getSize() {
		return size;
	}
		
	/**
	 * The activities performed in an active state. By default this is discovery
	 */
	protected void onActiveState(){
		this.discovery( true);		
	}

	/**
	 * even though a counter is used, the discovery mode determines which count value is used:
	 * - continuous: no stop
	 * - one-shot: only once
	 * - count (default): countdown
	 * 
	 * @return
	 */
	protected int getCount(){
		IJp2pPropertySource<IJp2pProperties> source = super.getPropertySource();
		int count = ( Integer )source.getProperty( DiscoveryProperties.COUNT );
		DiscoveryMode mode = (DiscoveryMode) source.getProperty( DiscoveryProperties.MODE );
		switch( mode ){
		case CONTINUOUS:
			return -1;
		case ONE_SHOT:
			return 1;
		default:
			return count;
		}
	}
	
	@Override
	public synchronized boolean start() {
		boolean clear = AbstractJp2pPropertySource.getBoolean( super.getPropertySource(), Directives.CLEAR );
		final IJp2pWritePropertySource<IJp2pProperties> source = (IJp2pWritePropertySource<IJp2pProperties>) super.getPropertySource();
		if( !clear ){
			Advertisement[] advertisements = this.getAdvertisements();
			if(( advertisements != null ) && ( advertisements.length > 0 )){
				size = advertisements.length;
				source.setProperty( DiscoveryProperties.FOUND, size );
				if( size > 0 )
					return false;
			}
		}
		boolean retval = super.start();
		DiscoveryService discovery = super.getModule();
		listener = new DiscoveryListener(){

			@Override
			public void discoveryEvent(DiscoveryEvent event) {
				DiscoveryResponseMsg res = event.getResponse();
				// let's get the responding peer's advertisement
				Logger log = Logger.getLogger( ChaupalDiscoveryService.class.getName() );
				log.log( Jp2pLevel.JP2PLEVEL, " [ Got a Discovery Response [" +
						res.getResponseCount() + " elements] from peer : " +
						event.getSource() + " ]");
				Advertisement adv;
				Enumeration<?> en = res.getAdvertisements();
				size = 0;
				if (en != null) {
					while (en.hasMoreElements()) {
						adv = (Advertisement) en.nextElement();
						size++;
						log.log( Jp2pLevel.JP2PLEVEL,adv.toString() );
					}
				}
				source.setProperty( DiscoveryProperties.FOUND, size );
			}	
		};
		discovery.addDiscoveryListener(listener);
		this.runnable = new Runnable(){
			@Override
			public void run() {
				int wait_time = ( Integer )source.getProperty( DiscoveryProperties.WAIT_TIME );
				int count = getCount();
				IJp2pWritePropertySource<IJp2pProperties> source = (IJp2pWritePropertySource<IJp2pProperties>)getPropertySource();
				source.getOrCreateManagedProperty( DiscoveryProperties.COUNTER, AbstractJp2pPropertySource.S_RUNTIME, false );
				while (( isActive() ) && ( count > 0 )) {
					onActiveState();
					try {
						Thread.sleep(wait_time);
					} catch (Exception e) {
						Logger log = Logger.getLogger( this.getClass().getName() );
						log.log( Jp2pLevel.JP2PLEVEL, this.getClass().getSimpleName() + "Interrupted" );
					}
					if( count > 0 )
						count--;
					source.setProperty( DiscoveryProperties.COUNTER, count);
					ComponentEventDispatcher dispatcher = ComponentEventDispatcher.getInstance();
					String identifier = AbstractJp2pPropertySource.getBundleId( getPropertySource());
					dispatcher.serviceChanged( new ComponentChangedEvent<ChaupalDiscoveryService>( service, identifier, Jp2pContainer.ServiceChange.COMPONENT_EVENT ));
				}
				stop();
				if( size > 0)
					setStatus( Status.COMPLETED );
				else
					setStatus( Status.FAILED );
			}		
		};
		this.executor.execute(runnable);
		return retval;
	}

	@Override
	protected void deactivate() {
		Thread.currentThread().interrupt();
		DiscoveryService discovery = super.getModule();
		if( listener != null)
			discovery.removeDiscoveryListener( listener );
		this.listener = null;
	}
}