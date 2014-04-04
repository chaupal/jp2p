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
package net.jp2p.chaupal.jxta.activator;

import java.util.ArrayList;
import java.util.Collection;

import org.osgi.framework.BundleContext;

import net.jp2p.chaupal.activator.AbstractJp2pBundleActivator;
import net.jp2p.chaupal.activator.ContainerBuilderEvent;
import net.jp2p.chaupal.activator.IContainerBuilderListener;
import net.jp2p.chaupal.context.Jp2pContextService;
import net.jp2p.chaupal.jxta.module.ModuleFactoryService;
import net.jp2p.container.IJp2pContainer;
import net.jp2p.container.component.ComponentEventDispatcher;
import net.jp2p.container.component.IComponentChangedListener;
import net.jp2p.container.context.ContextLoader;
import net.jp2p.container.startup.Jp2pStartupService;

public class Jp2pBundleActivator extends AbstractJp2pBundleActivator<Jp2pStartupService> {

	private String bundle_id;
	private IComponentChangedListener observer;
	
	private BundleContext bundleContext;
	
	private static Jp2pContextService contextService; 
	private ContextLoader contextLoader;
	private static ModuleFactoryService moduleService; 
	
	
	private Jp2pContainerBuilder builder;
	private IJp2pContainer container;
	private IContainerBuilderListener listener;
	
	private Collection<IContainerBuilderListener> listeners;

	public Jp2pBundleActivator(String bundle_id) {
		this.bundle_id = bundle_id;
		listeners = new ArrayList<IContainerBuilderListener>();
	}

	public String getBundleId() {
		return bundle_id;
	}

	
	public IJp2pContainer getContainer() {
		return container;
	}

	public IComponentChangedListener getObserver() {
		return observer;
	}

	public void setObserver(IComponentChangedListener observer) {
		this.observer = observer;
	}

	public void addContainerBuilderListener( IContainerBuilderListener listener ){
		listeners.add( listener );
	}

	public void removeContainerBuilderListener( IContainerBuilderListener listener ){
		listeners.remove( listener );
	}

	@Override
	public void start(BundleContext bundleContext) throws Exception {
		this.bundleContext = bundleContext;
		moduleService = new ModuleFactoryService( bundleContext );
		moduleService.open();			
		super.start(bundleContext);
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		if(( builder != null ) && ( listener != null )){
			builder.removeContainerBuilderListener(listener);
			listener = null;
		}

		moduleService.close();
		moduleService = null;

		contextService.close();
		contextService = null;
		contextLoader.clear();
		contextLoader = null;

		ComponentEventDispatcher dispatcher = ComponentEventDispatcher.getInstance();
		if( observer != null )
			dispatcher.removeServiceChangeListener(observer);

		super.stop(bundleContext);
	}

	private void notifyListeners( ContainerBuilderEvent event ){
		for( IContainerBuilderListener listener: listeners )
			listener.notifyContainerBuilt(event);		
	}
	
	@Override
	protected void createContainer() {
		
		//Add contexts, both default as the ones provided through DS
		contextLoader = ContextLoader.getInstance();

		contextService = new Jp2pContextService( contextLoader, bundleContext );
		builder = new Jp2pContainerBuilder( this, contextLoader );
		listener = new IContainerBuilderListener(){

			@Override
			public void notifyContainerBuilt(ContainerBuilderEvent event) {
				container = builder.getContainer();
				notifyListeners(event);
			}	
		};
		builder.addContainerBuilderListener(listener);
		
		builder.build();

		ComponentEventDispatcher dispatcher = ComponentEventDispatcher.getInstance();
		if( observer != null )
			dispatcher.addServiceChangeListener(observer);
		
		contextService.open();				
	}
}