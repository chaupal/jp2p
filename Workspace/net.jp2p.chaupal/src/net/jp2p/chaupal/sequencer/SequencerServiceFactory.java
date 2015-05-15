/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.chaupal.sequencer;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import net.jp2p.container.Jp2pContainerPropertySource;
import net.jp2p.container.component.AbstractJp2pService;
import net.jp2p.container.component.IJp2pComponent;
import net.jp2p.container.context.IJp2pServiceBuilder.Components;
import net.jp2p.container.factory.AbstractComponentFactory;
import net.jp2p.container.factory.ComponentBuilderEvent;
import net.jp2p.container.properties.IJp2pProperties;
import net.jp2p.container.properties.IJp2pPropertySource;
import net.jp2p.container.properties.IJp2pWritePropertySource;
import net.jp2p.container.utils.Utils;

public class SequencerServiceFactory extends AbstractComponentFactory<Object>
{
	private Jp2pBundleSequencer<Object> sequencer;
	
	private ISequenceEventListener<Object> listener = new ISequenceEventListener<Object>(){

		@Override
		public void notifySequenceEvent(SequencerEvent<Object> event) {
			SequencerPropertySource source = (SequencerPropertySource) getPropertySource();
			String id = source.getWatchID();
			if( Utils.isNull( id ) || !id.equals( event.getTarget() ))
				return;
			if( !source.getWatchStatus().equals( event.getStatus() ))
				return;
			setCanCreate( true );
			executor.execute( runnable );
		}	
	};
	
	/**
	 * We use a separate thread to create the component in order to avoid concurrency problems
	 * when the service is used.
	 */
	private Runnable runnable = new Runnable(){

		@Override
		public void run() {
			SequencerPropertySource source = (SequencerPropertySource) getPropertySource();
			int time_out = source.getTimeOut();
			try{
				Thread.sleep( time_out );
			}
			catch( InterruptedException ex ){
				
			}
			createComponent();
		}		
	};
	private ExecutorService executor;
	
	public SequencerServiceFactory() {
		super( Components.SEQUENCER_SERVICE.toString() );
		executor = Executors.newCachedThreadPool();
	}

	public final void setSequencer(Jp2pBundleSequencer<Object> sequencer) {
		this.sequencer = sequencer;
	}

	@Override
	protected SequencerPropertySource onCreatePropertySource() {
		SequencerPropertySource source = new SequencerPropertySource( (Jp2pContainerPropertySource) super.getParentSource());
		return source;
	}

	/**
	 * We activate the listener after the ptroperty source has been created
	 */
	@Override
	public void extendContainer() {
		this.sequencer.addListener(listener);
		super.extendContainer();
	}

	
	@Override
	protected void onNotifyChange(ComponentBuilderEvent<Object> event) {
		/* DO NOTHING */	
	}

	@Override
	protected IJp2pComponent<Object> onCreateComponent(IJp2pPropertySource<IJp2pProperties> properties) {
		this.sequencer.removeListener(listener);
		return new SequencerService( (IJp2pWritePropertySource<IJp2pProperties>) properties );
	}

	
	@Override
	public void finalise() {
		Thread.currentThread().interrupt();
		super.finalise();
	}


	/**
	 * A dummy service, just to show an active state
	 * @author Kees
	 *
	 */
	private static class SequencerService extends AbstractJp2pService<Object>{

		protected SequencerService( IJp2pWritePropertySource<IJp2pProperties> properties) {
			super( properties, new Object() );
			super.setStatus( Status.ACTIVE);
		}

		@Override
		protected void deactivate() {
			super.setStatus( Status.DISABLED);
		}

		
	}
}