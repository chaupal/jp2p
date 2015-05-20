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
package net.jp2p.chaupal.activator;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.osgi.framework.BundleContext;

import net.jp2p.chaupal.builder.Jp2pBuilderService;
import net.jp2p.chaupal.context.IServiceManagerListener;
import net.jp2p.chaupal.context.Jp2pServiceManager;
import net.jp2p.chaupal.context.ServiceManagerEvent;
import net.jp2p.chaupal.sequencer.SequenceDeclarativeService;
import net.jp2p.chaupal.xml.XMLContainerBuilder;
import net.jp2p.chaupal.activator.AbstractJp2pBundleActivator;
import net.jp2p.container.component.ComponentEventDispatcher;
import net.jp2p.container.component.IComponentChangedListener;
import net.jp2p.container.context.Jp2pServiceLoader;
import net.jp2p.container.properties.IJp2pDirectives.DeveloperModes;

public class Jp2pBundleActivator extends AbstractJp2pBundleActivator<Object> {

	private static Jp2pBuilderService jp2pBuilderService; 
	private Jp2pServiceLoader loader;
	private Jp2pServiceManager manager;
	private SequenceDeclarativeService<Object> sequencerService;
	
	private IServiceManagerListener listener;	
	private IComponentChangedListener<?> componentListener;
	
	private ExecutorService service;
	
	private Class<?> clss;

	protected Jp2pBundleActivator(String bundle_id ) {
		this( bundle_id, DeveloperModes.PRODUCTION );
	}

	/**
	 * Create the bundle activator in production mode. 
	 * The container is not added as a declarative service
	 * @param bundle_id
	 */
	protected Jp2pBundleActivator(String bundle_id,  DeveloperModes mode ) {
		super( bundle_id, mode );
		sequencerService = new SequenceDeclarativeService<Object>( bundle_id );
		this.clss = this.getClass();
	}

	/**
	 * Start the activator for the given bundle id. The JP2P container will be built
	 * if this is possible.
	 * If the Developer mode is set to debug, then the container will be visible for the IDE
	 * @param bundle_id
	 * @param mode
	 * @param enableSequencer
	 */
	protected Jp2pBundleActivator(String bundle_id, Class<?> clss, DeveloperModes mode  ) {
		super( bundle_id, mode );
		this.clss = clss;
		sequencerService = new SequenceDeclarativeService<Object>( bundle_id );
	}

	@Override
	public void start(BundleContext bundleContext) throws Exception {
		loader = Jp2pServiceLoader.getInstance();

		sequencerService.start( bundleContext );
		super.addObserver( sequencerService );

		//Passes the builders through to the loader
		jp2pBuilderService = new Jp2pBuilderService( loader );
		jp2pBuilderService.start(bundleContext);

		super.start(bundleContext);
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		if(( manager != null ) && ( listener != null )){
			manager.removeListener(listener);
			manager.close();
			listener = null;
		}
		
		if( jp2pBuilderService != null ){
			jp2pBuilderService.stop(bundleContext);
			jp2pBuilderService = null;
		}
		
		if( sequencerService != null ){
			super.removeObserver( sequencerService);
			sequencerService.stop( bundleContext );
		}
		if( loader != null ){
			loader.clear();
			loader = null;
		}

		ComponentEventDispatcher dispatcher = ComponentEventDispatcher.getInstance();
		
		if( this.componentListener != null )
			dispatcher.removeServiceChangeListener( this.componentListener);

		super.stop(bundleContext);
	}

	/**
	 * Build the container
	 */
	protected void build() {
		//Add contexts, both default as the ones provided through DS
		manager = new Jp2pServiceManager(this, loader);
		listener = new IServiceManagerListener(){

			@Override
			public void notifyContainerBuilt(ServiceManagerEvent event) {
				Runnable runnable = new Runnable(){

					@Override
					public void run() {
						XMLContainerBuilder builder = new XMLContainerBuilder( getBundleId(), clss, getMode(), manager, sequencerService.getSequencer() );
						ComponentEventDispatcher dispatcher = ComponentEventDispatcher.getInstance();
						componentListener = new ComponentChangedListener();
						dispatcher.addServiceChangeListener( componentListener);		
						builder.build();
						setContainer( builder.getContainer() );
					}
					
				};
				service = Executors.newSingleThreadExecutor();
				service.execute( runnable );		
			}	
		};
		manager.addListener(listener);
		manager.open();
	}
}