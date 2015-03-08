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

import org.osgi.framework.BundleContext;

import net.jp2p.chaupal.builder.Jp2pContainerBuilder;
import net.jp2p.chaupal.context.Jp2pContextService;
import net.jp2p.chaupal.activator.AbstractJp2pBundleActivator;
import net.jp2p.container.builder.ContainerBuilderEvent;
import net.jp2p.container.builder.IContainerBuilderListener;
import net.jp2p.container.builder.IJp2pContainerBuilder;
import net.jp2p.container.component.ComponentEventDispatcher;
import net.jp2p.container.component.IComponentChangedListener;
import net.jp2p.container.context.ContextLoader;

public class Jp2pBundleActivator extends AbstractJp2pBundleActivator<Object> {

	protected Jp2pBundleActivator(String bundle_id) {
		super( bundle_id );
	}

	private static Jp2pContextService contextService; 
	private ContextLoader contextLoader;
	
	private IContainerBuilderListener<Object> listener;
	
	private IComponentChangedListener<?> componentListener;

	
	@Override
	public void start(BundleContext bundleContext) throws Exception {
		contextLoader = ContextLoader.getInstance();
		contextService = new Jp2pContextService( contextLoader, bundleContext );
		contextService.open();		
		super.start(bundleContext);
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		IJp2pContainerBuilder<Object> builder = super.getBuilder();
		if(( builder != null ) && ( listener != null )){
			builder.removeContainerBuilderListener(listener);
			listener = null;
		}

		if( contextService != null ){
			contextService.close();
			contextService = null;
		}
		
		if( contextLoader != null ){
			contextLoader.clear();
			contextLoader = null;
		}

		ComponentEventDispatcher dispatcher = ComponentEventDispatcher.getInstance();
		
		if( this.componentListener != null )
			dispatcher.removeServiceChangeListener( this.componentListener);

		super.stop(bundleContext);
	}

	@Override
	protected void createContainer() {
		//Add contexts, both default as the ones provided through DS
		Jp2pContainerBuilder<Object> builder = new Jp2pContainerBuilder<Object>( this, contextLoader );
		listener = new IContainerBuilderListener<Object>(){

			@Override
			public void notifyContainerBuilt(ContainerBuilderEvent<Object> event) {
				notifyListeners(event);
			}	
		};
		builder.addContainerBuilderListener(listener);
		
		builder.build();
		super.setContainer( builder.getContainer());
		ComponentEventDispatcher dispatcher = ComponentEventDispatcher.getInstance();
		this.componentListener = new ComponentChangedListener();
		dispatcher.addServiceChangeListener( this.componentListener);		
	}
}