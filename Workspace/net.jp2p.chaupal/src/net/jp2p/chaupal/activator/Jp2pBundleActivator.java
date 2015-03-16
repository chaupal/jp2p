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

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import net.jp2p.chaupal.builder.Jp2pBuilderService;
import net.jp2p.chaupal.builder.Jp2pServiceManager;
import net.jp2p.chaupal.xml.XMLContainerBuilder;
import net.jp2p.chaupal.activator.AbstractJp2pBundleActivator;
import net.jp2p.container.IJp2pContainer;
import net.jp2p.container.builder.ContainerBuilderEvent;
import net.jp2p.container.builder.IContainerBuilderListener;
import net.jp2p.container.builder.IJp2pContainerBuilder;
import net.jp2p.container.component.ComponentEventDispatcher;
import net.jp2p.container.component.IComponentChangedListener;
import net.jp2p.container.context.IContextLoaderListener;
import net.jp2p.container.context.Jp2pLoaderEvent;
import net.jp2p.container.context.Jp2pServiceLoader;

public class Jp2pBundleActivator extends AbstractJp2pBundleActivator<Object> {

	private static Jp2pBuilderService jp2pBuilderService; 
	private Jp2pServiceLoader loader;
	private Jp2pServiceManager<Object> manager;
	
	private IContainerBuilderListener<Object> listener;
	
	private IComponentChangedListener<?> componentListener;

	protected Jp2pBundleActivator(String bundle_id) {
		super( bundle_id );
	}

	
	@Override
	public void start(BundleContext bundleContext) throws Exception {
		loader = Jp2pServiceLoader.getInstance();
		manager = new Jp2pServiceManager<Object>(this, loader);
		jp2pBuilderService = new Jp2pBuilderService( bundleContext, loader );
		jp2pBuilderService.open();		
		super.start(bundleContext);
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		IJp2pContainerBuilder<Object> builder = super.getBuilder();
		if(( builder != null ) && ( listener != null )){
			builder.removeContainerBuilderListener(listener);
			listener = null;
		}

		if( jp2pBuilderService != null ){
			jp2pBuilderService.close();
			jp2pBuilderService = null;
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

	@Override
	protected void createContainer() {
		//Add contexts, both default as the ones provided through DS
		Jp2pServiceManager<Object> builder = new Jp2pServiceManager<Object>( this, loader );
		listener = new IContainerBuilderListener<Object>(){

			@Override
			public void notifyContainerBuilt(ContainerBuilderEvent<Object> event) {
				notifyListeners(event);
			}	
		};
		//builder.addContainerBuilderListener(listener);
		
		builder.build();
		super.setContainer( builder.getContainer());
		ComponentEventDispatcher dispatcher = ComponentEventDispatcher.getInstance();
		this.componentListener = new ComponentChangedListener();
		dispatcher.addServiceChangeListener( this.componentListener);		
	}
}