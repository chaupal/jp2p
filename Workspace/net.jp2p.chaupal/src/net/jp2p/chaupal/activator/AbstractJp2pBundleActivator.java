/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.chaupal.activator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.jp2p.container.IJp2pContainer;
import net.jp2p.container.activator.IJp2pBundleActivator;
import net.jp2p.container.builder.ContainerBuilderEvent;
import net.jp2p.container.builder.IContainerBuilderListener;
import net.jp2p.container.component.ComponentChangedEvent;
import net.jp2p.container.component.IComponentChangedListener;
import net.jp2p.container.component.IJp2pComponent;
import net.jp2p.container.log.Jp2pLevel;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.service.log.LogService;
import org.osgi.util.tracker.ServiceTracker;

public abstract class AbstractJp2pBundleActivator<T extends Object> implements IJp2pBundleActivator<T>, BundleActivator {

	private static final String S_MSG_NOT_A_JP2P_BUNDLE = "\n\nThis bundle is not a valid JP2P Bundle. A JP2P-INF directory is required!\n\n";
	private static final String S_JP2P_INF = "/JP2P-INF";
	private static final String S_MSG_LOG = "Logging at JXSE LEVEL!!!!";
	
	private Jp2pActivator jp2pActivator;

	private BundleContext bundleContext;
	private IJp2pContainer<T> container;

	private String bundle_id;
	private Collection<IComponentChangedListener<IJp2pComponent<?>>> observers;
	
	private ServiceTracker<BundleContext,LogService> logServiceTracker;
	private LogService logService;
	private Collection<IContainerBuilderListener<T>> listeners;
	
	protected AbstractJp2pBundleActivator( String bundle_id ) {
		listeners = new ArrayList<IContainerBuilderListener<T>>();
		observers = new ArrayList<IComponentChangedListener<IJp2pComponent<?>>>();
		this.bundle_id = bundle_id;
	}

	@Override
	public final String getBundleId() {
		return bundle_id;
	}
	
	public final void addObserver(IComponentChangedListener<IJp2pComponent<?>> observer) {
		this.observers.add( observer );
	}

	public final void removeObserver(IComponentChangedListener<IJp2pComponent<?>> observer) {
		this.observers.remove( observer );
	}

	protected final void notifyObservers( ComponentChangedEvent<IJp2pComponent<?>> event ){
		if(!event.isMatched( this.bundle_id ))
			return;
		for(IComponentChangedListener<IJp2pComponent<?>> observer: this.observers )
			observer.notifyServiceChanged(event);
	}
	
	@Override
	public final void addContainerBuilderListener( IContainerBuilderListener<T> listener ){
		listeners.add( listener );
	}

	@Override
	public final void removeContainerBuilderListener( IContainerBuilderListener<T> listener ){
		listeners.remove( listener );
	}

	protected final void notifyListeners( ContainerBuilderEvent<T> event ){
		for( IContainerBuilderListener<T> listener: listeners )
			listener.notifyContainerBuilt(event);		
	}

	/**
	 * Create the container;
	 */
	protected abstract IJp2pContainer<T> onCreateContainer();

	/**
	 * Create the container;
	 */
	protected void createContainer(){
		container = this.onCreateContainer();
	}

	@Override
	public IJp2pContainer<T> getContainer() {
		return this.container;
	}

	
	protected void setContainer(IJp2pContainer<T> container) {
		this.container = container;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	@Override
	public void start(BundleContext bundleContext) throws Exception {
		if(this.getClass().getResource( S_JP2P_INF ) == null )
			Logger.getLogger( this.getClass().getName() ).warning( S_MSG_NOT_A_JP2P_BUNDLE);
		
		this.bundleContext = bundleContext;
		Level level = Jp2pLevel.getJxtaLevel();
		Logger log = Logger.getLogger( this.getClass().getName() );
		log.log( level, S_MSG_LOG );
		// create a tracker and track the log service
		logServiceTracker = 
				new ServiceTracker<BundleContext,LogService>(bundleContext, LogService.class.getName(), null);
		logServiceTracker.open();

		// grab the service
		logService = logServiceTracker.getService();

		if(logService != null)
			logService.log(LogService.LOG_INFO, "Logging service started");

		jp2pActivator = new Jp2pActivator( this );
		jp2pActivator.start();
	}

	/**
	 * Get the log service
	 * @return
	 */
	public LogService getLog(){
		return logService;
	}
	
	protected BundleContext getBundleContext() {
		return bundleContext;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(BundleContext bundleContext) throws Exception {		
		if( jp2pActivator != null )
			jp2pActivator.stop();
		if(logService != null)
			logService.log(LogService.LOG_INFO, "Logging service Stopped");
		
		// close the service tracker
		logServiceTracker.close();
		logServiceTracker = null;
		this.bundleContext=  null;
	}

	private class Jp2pActivator implements Runnable {

		private AbstractJp2pBundleActivator<T> activator;
		
		private ExecutorService executor;
			
		public Jp2pActivator( AbstractJp2pBundleActivator<T> activator ) {
			this.activator = activator;
			executor = Executors.newSingleThreadExecutor();
		}

		/*
		 * (non-Javadoc)
		 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
		 */
		public void start(){
			executor.execute(this);
		}

		/*
		 * (non-Javadoc)
		 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
		 */
		public void stop(){
			executor.shutdown();
		}

		@Override
		public void run() {
			try{
				activator.createContainer();
			}
			catch( Exception ex ){
				ex.printStackTrace();
			}
		}
	}
	
	/**
	 * a default listener for component change events
	 * @author Kees
	 *
	 */
	protected class ComponentChangedListener implements IComponentChangedListener<IJp2pComponent<?>>{

		@Override
		public void notifyServiceChanged(
				ComponentChangedEvent<IJp2pComponent<?>> event) {
					notifyObservers(event);	
		}
	};
 
}