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
package net.jp2p.jxta.builder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import net.jp2p.container.AbstractJp2pContainer;
import net.jp2p.container.IJp2pContainer;
import net.jp2p.container.builder.ContainerBuilderEvent;
import net.jp2p.container.builder.IContainerBuilderListener;
import net.jp2p.container.builder.IJp2pContainerBuilder;
import net.jp2p.container.component.IJp2pComponent;
import net.jp2p.container.component.IJp2pComponentNode;
import net.jp2p.container.component.Jp2pComponent;
import net.jp2p.container.component.Jp2pComponentNode;
import net.jp2p.container.properties.AbstractJp2pWritePropertySource;
import net.jp2p.container.properties.IJp2pDirectives;
import net.jp2p.container.properties.IJp2pProperties;
import net.jp2p.container.properties.IJp2pPropertySource;
import net.jp2p.jxta.network.NetworkManagerPropertyFacade;
import net.jp2p.jxta.network.configurator.NetworkConfiguratorPropertyFacade;
import net.jxse.osgi.compat.CompatibilityEvent;
import net.jxse.osgi.compat.ICompatibilityListener;
import net.jxse.osgi.compat.IJP2PCompatibility;
import net.jxse.osgi.compat.IJxtaNode;
import net.jxse.osgi.platform.activator.PlatformJxseBuilder;
import net.jxta.platform.NetworkManager;

public class Jp2pCompatBuilder<T extends Object> extends PlatformJxseBuilder<T> implements IJp2pContainerBuilder<T>{

	private String bundle_id;
	private IJP2PCompatibility<T> compat;
	private  IJp2pContainer<T> container;
	private Collection<IContainerBuilderListener<T>> listeners;
				
	public Jp2pCompatBuilder(String bundle_id, IJP2PCompatibility<T> compat ) {
		super( bundle_id, compat );
		this.bundle_id = bundle_id;
		this.compat = compat;
		listeners = new ArrayList<IContainerBuilderListener<T>>();
	}
	
	@Override
	public IJp2pContainer<T> getContainer() {
		return this.container;
	}

	private final void notifyListeners( ContainerBuilderEvent<T> event ){
		for( IContainerBuilderListener<T> listener: listeners ){
			listener.notifyContainerBuilt(event);
		}
	}

	@Override
	public void addContainerBuilderListener( IContainerBuilderListener<T> listener ){
		listeners.add( listener );
	}

	@Override
	public void removeContainerBuilderListener( IContainerBuilderListener<T> listener ){
		listeners.remove( listener );
	}

	/**
	 * Create the container;
	 */
	public void build(){
		container = new JxtaContainer<T>( this.bundle_id, compat );
		notifyListeners( new ContainerBuilderEvent<T>(this, container));
	}

	private static class JxtaContainer<T extends Object> extends AbstractJp2pContainer<T> {

		private String bundle_id;
		private IJP2PCompatibility<T> compat;
		private ICompatibilityListener listener = new ICompatibilityListener(){

			@Override
			public void notifyNodeChanged(CompatibilityEvent event) {
				clearModules();
				addChild( setStructure( compat ));	
			}
		};
		
		JxtaContainer( String bundle_id, IJP2PCompatibility<T> compat ) {
			super( new PropertySource( bundle_id, compat.getIdentifier() ));
			this.compat = compat;
			this.bundle_id = bundle_id;
			this.compat.addListener(listener);
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

		private IJp2pComponent<T> setStructure( IJP2PCompatibility<T> compat ){
			IJxtaNode<T> root = compat.getRoot();
			IJp2pComponentNode<T> main = (IJp2pComponentNode<T>) getComponent( root.getModule() ); 
			for( T child: root.getChildren() ){
				setStructure( main, child );
			}
			return main;
		}
		
		private void setStructure( IJp2pComponentNode<T> node, T module ){
			node.addChild( getComponent( module ));
		}

		private IJp2pComponent<T> getComponent( T module ){
			IJp2pComponentNode<T> comp = null;
			IJp2pPropertySource<IJp2pProperties> source = null;
			if( module instanceof NetworkManager){
				NetworkManager manager = (NetworkManager) module;
				source = new NetworkManagerPropertyFacade( bundle_id, manager);
				comp = new Jp2pComponentNode<T>( source, module );
				try {
					source = new NetworkConfiguratorPropertyFacade( bundle_id, manager.getConfigurator() );
					comp.addChild( new Jp2pComponentNode<Object>( source, manager.getConfigurator() ));
				} catch (IOException e) {
					e.printStackTrace();
				}
				return comp; 
			}else{
				return new Jp2pComponent<T>( super.getPropertySource(), module );
			}
			
		}
		
		@Override
		public void deactivate() {
			this.compat.removeListener(listener);
			super.deactivate();
		}
	}
}