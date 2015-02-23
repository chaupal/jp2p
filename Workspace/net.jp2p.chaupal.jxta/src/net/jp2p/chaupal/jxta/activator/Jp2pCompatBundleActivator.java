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

import org.osgi.framework.BundleContext;

import net.jp2p.chaupal.activator.AbstractJp2pBundleActivator;
import net.jp2p.container.builder.ContainerBuilderEvent;
import net.jp2p.container.builder.IContainerBuilderListener;
import net.jp2p.container.builder.IJp2pContainerBuilder;
import net.jp2p.container.component.ComponentEventDispatcher;
import net.jp2p.container.component.IComponentChangedListener;
import net.jp2p.container.component.IJp2pComponent;
import net.jp2p.jxta.builder.Jp2pCompatBuilder;
import net.jxse.osgi.compat.IJP2PCompatibility;

public class Jp2pCompatBundleActivator<T extends Object> extends AbstractJp2pBundleActivator<T> {

	private IContainerBuilderListener<T> listener = new IContainerBuilderListener<T>(){

		@Override
		public void notifyContainerBuilt(ContainerBuilderEvent<T> event) {
			notifyListeners(event);
		}	
	};
	
	private IComponentChangedListener<IJp2pComponent<T>> componentListener;
	
	private Jp2pCompatBuilder<T> builder;
	
	protected Jp2pCompatBundleActivator(String bundle_id, IJP2PCompatibility<T> compat ) {
		super( bundle_id );
		this.builder = new Jp2pCompatBuilder<T>( super.getBundleId(), compat );;
	}

	
	@Override
	public void start(BundleContext bundleContext) throws Exception {
		super.start(bundleContext);		
		this.builder.addContainerBuilderListener(listener);
		try{
		this.builder.start(bundleContext);
		}
		catch( Exception ex ){
			ex.printStackTrace();
		}
	}


	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		this.builder.removeContainerBuilderListener(listener);
		this.builder.stop(bundleContext);
		IJp2pContainerBuilder<T> builder = super.getBuilder();
		if(( builder != null ) && ( listener != null )){
			builder.removeContainerBuilderListener(listener);
			listener = null;
		}

		ComponentEventDispatcher dispatcher = ComponentEventDispatcher.getInstance();
		
		if( this.componentListener != null )
			dispatcher.removeServiceChangeListener( this.componentListener);

		super.stop(bundleContext);
	}

	
	@Override
	protected void createContainer() {
		super.setContainer( builder.getContainer());

		ComponentEventDispatcher dispatcher = ComponentEventDispatcher.getInstance();
		this.componentListener = new ComponentChangedListener();
		dispatcher.addServiceChangeListener( this.componentListener);
	}
}