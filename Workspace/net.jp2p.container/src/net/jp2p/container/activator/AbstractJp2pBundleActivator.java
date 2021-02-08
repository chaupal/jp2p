/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.container.activator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.jp2p.container.IJp2pContainer;
import net.jp2p.container.builder.ContainerBuilderEvent;
import net.jp2p.container.builder.IContainerBuilderListener;
import net.jp2p.container.builder.IJp2pContainerBuilder;
import net.jp2p.container.component.ComponentChangedEvent;
import net.jp2p.container.component.IComponentChangedListener;
import net.jp2p.container.component.IJp2pComponent;
import net.jp2p.container.log.Jp2pLevel;
import net.jp2p.container.properties.IJp2pDirectives.DeveloperModes;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.service.log.LogService;
import org.osgi.util.tracker.ServiceTracker;

public abstract class AbstractJp2pBundleActivator<T extends Object> implements BundleActivator, IJp2pContainerBuilder<T> {

	private static final String S_MSG_NOT_A_JP2P_BUNDLE = "\n\nThis bundle is not a valid JP2P Bundle. A JP2P-INF directory is required!\n\n";
	private static final String S_JP2P_INF = "/JP2P-INF";
	private static final String S_MSG_LOG = "Logging at JXSE LEVEL!!!!";
	
	private BundleContext bundleContext;

	private String bundle_id;
	private Collection<IComponentChangedListener<IJp2pComponent<T>>> observers;
	
	private ServiceTracker<BundleContext,LogService> logServiceTracker;
	private LogService logService;
	
	private IJp2pContainer<T> container;
	private Collection<IContainerBuilderListener<T>> containerListeners;
	
	private DeveloperModes mode;
	
	protected AbstractJp2pBundleActivator( String bundle_id, DeveloperModes mode ) {
		observers = new ArrayList<IComponentChangedListener<IJp2pComponent<T>>>();
		this.bundle_id = bundle_id;
		this.mode = mode;
		containerListeners = new ArrayList<IContainerBuilderListener<T>>();
	}

	public final String getBundleId() {
		return bundle_id;
	}

	protected DeveloperModes getMode() {
		return mode;
	}

	public IJp2pContainer<T> getContainer() {
		return container;
	}

	protected synchronized final void setContainer(IJp2pContainer<T> container) {
		this.container = container;
		if( DeveloperModes.DEBUG.equals( this.mode)){
			bundleContext.registerService( IJp2pContainer.class.getName(), this.container, null );
		}
		for( IContainerBuilderListener<T> listener: containerListeners )
			listener.notifyContainerBuilt( new ContainerBuilderEvent<T>( this, container ));
	}

	@Override
	public void addContainerBuilderListener(
			IContainerBuilderListener<T> listener) {
		this.containerListeners.add( listener );
	}


	@Override
	public void removeContainerBuilderListener(
			IContainerBuilderListener<T> listener) {
		this.containerListeners.remove( listener );
	}

	/**
	 * Build the container
	 */
	protected abstract void build();
	
	public final void addObserver(IComponentChangedListener<IJp2pComponent<T>> observer) {
		this.observers.add( observer );
	}

	public final void removeObserver(IComponentChangedListener<IJp2pComponent<T>> observer) {
		this.observers.remove( observer );
	}

	protected final void notifyObservers( ComponentChangedEvent<IJp2pComponent<T>> event ){
		if(!event.isMatched( this.bundle_id ))
			return;
		for(IComponentChangedListener<IJp2pComponent<T>> observer: this.observers )
			observer.notifyServiceChanged(event);
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
		
		build();
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
		if(logService != null)
			logService.log(LogService.LOG_INFO, "Logging service Stopped");
		
		// close the service tracker
		logServiceTracker.close();
		logServiceTracker = null;
		this.bundleContext=  null;
	}
	
	/**
	 * a default listener for component change events
	 * @author Kees
	 *
	 */
	protected class ComponentChangedListener implements IComponentChangedListener<IJp2pComponent<T>>{

		public ComponentChangedListener() {
		}

		@Override
		public void notifyServiceChanged(
				ComponentChangedEvent<IJp2pComponent<T>> event) {
					notifyObservers(event);	
		}
	};
 
}