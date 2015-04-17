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
package net.jp2p.chaupal.jxta.pipe;

import java.io.IOException;

import net.jp2p.chaupal.jxta.advertisement.Jp2pAdvertisementService;
import net.jp2p.container.Jp2pContainer;
import net.jp2p.container.Jp2pContainer.ServiceChange;
import net.jp2p.container.activator.IJp2pService;
import net.jp2p.container.component.AbstractJp2pServiceNode;
import net.jp2p.container.component.ComponentChangedEvent;
import net.jp2p.container.component.ComponentEventDispatcher;
import net.jp2p.container.component.IComponentChangedListener;
import net.jp2p.container.component.IJp2pComponent;
import net.jp2p.container.properties.AbstractJp2pPropertySource;
import net.jp2p.container.properties.IJp2pProperties;
import net.jp2p.container.properties.IJp2pWritePropertySource;
import net.jxta.pipe.InputPipe;
import net.jxta.pipe.OutputPipe;
import net.jxta.pipe.PipeService;
import net.jxta.protocol.PipeAdvertisement;
import net.jp2p.jxta.pipe.PipePropertySource.PipeServiceProperties;

public class ChaupalPipeService extends AbstractJp2pServiceNode<PipeService>{

	private IComponentChangedListener<IJp2pComponent<PipeService>> listener;
	
	private PipeAdvertisement pipead;
	private IJp2pService<PipeAdvertisement> adService;
	
	private static IJp2pComponent<PipeService> service;

	
	public ChaupalPipeService( IJp2pWritePropertySource<IJp2pProperties> source, PipeService pipeService, IJp2pService<PipeAdvertisement> adService ) {
		super( source, pipeService );
		this.adService = adService;
		service = this;
	}
		
	/**
	 * Get an input pipe
	 * @return
	 * @throws IOException
	 */
	public InputPipe getInputPipe() throws IOException{
		return super.getModule().createInputPipe( pipead );
	}

	/**
	 * Get an input pipe
	 * @return
	 * @throws IOException
	 */
	public OutputPipe getOutputPipe() throws IOException{
		return super.getModule().createOutputPipe( pipead, (long) super.getPropertySource().getProperty( PipeServiceProperties.TIME_OUT ));
	}

	@Override
	public boolean start() {
		ComponentEventDispatcher dispatcher = ComponentEventDispatcher.getInstance();
		this.listener = new IComponentChangedListener<IJp2pComponent<PipeService>>(){
			
			@Override
			public void notifyServiceChanged(ComponentChangedEvent<IJp2pComponent<PipeService>> event) {
				if( event.getSource().equals( adService )){
					if( event.getChange().equals( Jp2pContainer.ServiceChange.COMPONENT_EVENT )){
						pipead = adService.getModule();
						if( pipead != null ){
							String identifier = AbstractJp2pPropertySource.getBundleId( getPropertySource());
							ComponentEventDispatcher dispatcher = ComponentEventDispatcher.getInstance();
							dispatcher.serviceChanged( new ComponentChangedEvent<IJp2pComponent<PipeService>>( service, identifier, ServiceChange.COMPONENT_EVENT));
						}
					}	
				}
			}

		};
		dispatcher.addServiceChangeListener(listener);;
		adService.start();
		return super.start();
	}

	@Override
	protected void deactivate() {
		ComponentEventDispatcher dispatcher = ComponentEventDispatcher.getInstance();
		if( this.listener != null )
			dispatcher.removeServiceChangeListener(listener);		
	}
	
	/**
	 * Get the discovery service
	 * @param adService
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Jp2pAdvertisementService<PipeAdvertisement> getAdvertisementService( ChaupalPipeService adService ){
		for( IJp2pComponent<?> component: adService.getChildren() ){
			if( component.getModule() instanceof Jp2pAdvertisementService )
				return (Jp2pAdvertisementService<PipeAdvertisement>) component.getModule();
		}
		return null;
	}
}