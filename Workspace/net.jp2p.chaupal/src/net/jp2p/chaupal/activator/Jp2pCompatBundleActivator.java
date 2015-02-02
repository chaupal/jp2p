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

import org.eclipse.core.runtime.Platform;
import org.osgi.framework.BundleContext;

import net.jp2p.container.AbstractJp2pContainer;
import net.jp2p.container.IJp2pContainer;
import net.jp2p.container.builder.ContainerBuilderEvent;
import net.jp2p.container.component.ComponentEventDispatcher;
import net.jp2p.container.component.IComponentChangedListener;
import net.jp2p.container.component.IJp2pComponent;
import net.jp2p.container.component.Jp2pComponent;
import net.jp2p.container.properties.AbstractJp2pWritePropertySource;
import net.jp2p.container.properties.IJp2pDirectives;
import net.jxse.osgi.IJP2PCompatibility;

public class Jp2pCompatBundleActivator extends AbstractJp2pBundleActivator<Object> {

	private IJP2PCompatibility<Object> compat;
	private String identfier;
	
	//private 
	protected Jp2pCompatBundleActivator(String bundle_id, String identifier, IJP2PCompatibility<Object> compat ) {
		super( bundle_id );
		this.compat = compat;
		this.identfier = identifier;
	}

	private IComponentChangedListener<?> componentListener;
	
	@Override
	public void stop(BundleContext bundleContext) throws Exception {

		ComponentEventDispatcher dispatcher = ComponentEventDispatcher.getInstance();
		
		if( this.componentListener != null )
			dispatcher.removeServiceChangeListener( this.componentListener);

		super.stop(bundleContext);
	}

	@Override
	protected IJp2pContainer<Object> onCreateContainer() {
		JxtaContainer container = new JxtaContainer( super.getBundleId(), this.identfier, this.compat );
		super.setContainer(  container );

		ComponentEventDispatcher dispatcher = ComponentEventDispatcher.getInstance();
		this.componentListener = new ComponentChangedListener();
		dispatcher.addServiceChangeListener( this.componentListener);
		
		String[] args = Platform.getCommandLineArgs();
		//this.compat.main(args);
		//container.setModule();
		super.notifyListeners( new ContainerBuilderEvent<Object>( this, container ));
		return super.getContainer();
	}

	private static class JxtaContainer extends AbstractJp2pContainer<Object> {

		private IJP2PCompatibility<Object> compat;
		
		JxtaContainer( String bundleName, String identifier, IJP2PCompatibility<Object> compat ) {
			super( new PropertySource( bundleName, identifier ) );
			this.compat = compat;
		}
		
		/**
		 * Set the module
		 */
		void setModule(){
			IJp2pComponent<Object> module = new Jp2pComponent<Object>( super.getPropertySource(), this.compat.getRoot().getModule() ); 
			super.addChild( module );
		}
		
		/**
		 * Create a default property source
		 * @author Kees
		 *
		 */
		private static class PropertySource extends AbstractJp2pWritePropertySource{

			PropertySource( String bundleName, String identifier) {
				super( bundleName, identifier );
				setDirective( IJp2pDirectives.Directives.NAME, identifier );
			}
			
		}
	}

}