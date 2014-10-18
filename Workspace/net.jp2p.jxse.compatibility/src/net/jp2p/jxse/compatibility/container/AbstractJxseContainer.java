/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.jxse.compatibility.container;

import java.io.IOException;

import org.eclipse.core.runtime.Platform;

import net.jp2p.container.AbstractJp2pContainer;
import net.jp2p.container.IJp2pContainer;
import net.jp2p.container.Jp2pContainerPropertySource;
import net.jp2p.container.component.IJp2pComponent;
import net.jp2p.container.component.IJp2pComponentNode;
import net.jp2p.container.component.Jp2pComponentNode;
import net.jp2p.container.properties.AbstractJp2pPropertySource;
import net.jp2p.container.properties.DefaultPropertySource;
import net.jp2p.container.properties.IJp2pProperties;
import net.jp2p.container.properties.IJp2pPropertySource;
import net.jp2p.jxta.network.NetworkManagerPropertyFacade;
import net.jp2p.jxta.network.configurator.NetworkConfiguratorPropertyFacade;
import net.jp2p.jxta.peergroup.PeerGroupPropertyFacade;
import net.jxta.peergroup.PeerGroup;
import net.jxta.platform.NetworkConfigurator;
import net.jxta.platform.NetworkManager;

public abstract class AbstractJxseContainer extends AbstractJp2pContainer<NetworkManager>{

	private String identifier;
	
	protected AbstractJxseContainer( String bundle_id, String identifier ) {
		super(new Jp2pContainerPropertySource( bundle_id, identifier ));
		this.identifier = identifier;
	}

	@Override
	public String getIdentifier() {
		return identifier;
	}

	@Override
	public boolean addChild(IJp2pComponent<?> child) {
		if( child.getModule() instanceof NetworkManager ){
			super.setModule((NetworkManager) child.getModule());
		}
		return super.addChild(child);
	}

	@Override
	public void deactivate() {
		this.getModule().stopNetwork();
	}

	/**
	 * Implement the 'main' method
	 * @param args
	 */
	protected abstract void main(String[] args);

	public boolean start() {
		String[] args = Platform.getCommandLineArgs();
		main( args );
		return true;
	}

	/**
	 * Add a module
	 * @param container
	 * @param module
	 */
	protected static void addModule( IJp2pContainer<?> container, Object module ){
		IJp2pComponent<?> component = getComponent( container, module );
		if( module instanceof NetworkManager){
			NetworkManager manager = (NetworkManager) module;
			try {
				((Jp2pComponentNode<?>) component).addChild( getComponent( container, manager.getConfigurator()));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		container.addChild(component);
	}
	
	protected static IJp2pComponent<?> getComponent( IJp2pContainer<?> container, Object module ){
		IJp2pPropertySource<IJp2pProperties> properties = null;
		String bundleId = AbstractJp2pPropertySource.getBundleId( container.getPropertySource() );
		if( module instanceof NetworkManager ){
			properties = new NetworkManagerPropertyFacade( bundleId, (NetworkManager) module );
		}else if( module instanceof NetworkConfigurator ){
			properties =  new NetworkConfiguratorPropertyFacade( bundleId, (NetworkConfigurator)module );
		}else if( module instanceof PeerGroup ){
			properties =  new PeerGroupPropertyFacade( bundleId, (PeerGroup)module );
		}else{
			properties = new DefaultPropertySource( bundleId, module.getClass().getSimpleName());
		}
		return new Jp2pComponentNode<Object>( properties, module );
	}
	/**
	 * Find the component with the given module 
	 * @param componentName
	 * @param root
	 * @return
	 */
	public static IJp2pComponent<?> findComponent( Object module, IJp2pComponent<?> root ){
		if( module == null )
			return null;
		if( module.equals( root.getModule() ))
			return root;
		if(!( root instanceof IJp2pComponentNode ))
			return null;
		IJp2pComponentNode<?> node = ( IJp2pComponentNode<?>) root;
		IJp2pComponentNode<?> result;
		for( IJp2pComponent<?> child: node.getChildren() ){
			result = (IJp2pComponentNode<?>) findComponent( module, child);
			if( result != null )
				return result;
		}
		return null;
	}
	
}
