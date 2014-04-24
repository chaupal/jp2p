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

import net.jp2p.chaupal.context.Jp2pContextService;
import net.jp2p.chaupal.activator.AbstractJp2pBundleActivator;
import net.jp2p.container.IJp2pContainer;
import net.jp2p.container.builder.ContainerBuilderEvent;
import net.jp2p.container.builder.IContainerBuilderListener;
import net.jp2p.container.component.ComponentEventDispatcher;
import net.jp2p.container.component.IComponentChangedListener;
import net.jp2p.container.context.ContextLoader;

public class Jp2pBundleActivator extends AbstractJp2pBundleActivator<Object> {

	protected Jp2pBundleActivator(String bundle_id) {
		super(bundle_id);
	}

	private static Jp2pContextService contextService; 
	private ContextLoader contextLoader;
	
	
	private Jp2pContainerBuilder builder;
	private IContainerBuilderListener listener;
	
	
	@Override
	public void start(BundleContext bundleContext) throws Exception {
		super.start(bundleContext);
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
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
		
		IComponentChangedListener observer= super.getObserver();
		if( observer != null )
			dispatcher.removeServiceChangeListener(observer);

		super.stop(bundleContext);
	}

	@Override
	protected IJp2pContainer onCreateContainer() {
		//Add contexts, both default as the ones provided through DS
		contextLoader = ContextLoader.getInstance();

		contextService = new Jp2pContextService( contextLoader, super.getBundleContext() );
		builder = new Jp2pContainerBuilder( this, contextLoader );
		listener = new IContainerBuilderListener(){

			@Override
			public void notifyContainerBuilt(ContainerBuilderEvent event) {
				setContainer( builder.getContainer() );
				notifyListeners(event);
			}	
		};
		builder.addContainerBuilderListener(listener);
		
		builder.build();

		ComponentEventDispatcher dispatcher = ComponentEventDispatcher.getInstance();
		IComponentChangedListener observer= super.getObserver();
		if( observer != null )
			dispatcher.addServiceChangeListener(observer);
		
		contextService.open();		
		return super.getContainer();
	}
}