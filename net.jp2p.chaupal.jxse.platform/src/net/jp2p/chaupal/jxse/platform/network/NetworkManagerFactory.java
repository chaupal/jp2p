/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.chaupal.jxse.platform.network;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.jp2p.container.ContainerFactory;
import net.jp2p.container.Jp2pContainerPropertySource;
import net.jp2p.container.builder.IContainerBuilder;
import net.jp2p.container.component.IJp2pComponent;
import net.jp2p.container.component.Jp2pComponentNode;
import net.jp2p.container.context.Jp2pContext;
import net.jp2p.container.factory.AbstractFilterFactory;
import net.jp2p.container.factory.filter.ComponentCreateFilter;
import net.jp2p.container.factory.filter.IComponentFactoryFilter;
import net.jp2p.container.properties.AbstractJp2pPropertySource;
import net.jp2p.container.properties.IJp2pDirectives;
import net.jp2p.container.properties.IJp2pProperties;
import net.jp2p.container.properties.IJp2pPropertySource;
import net.jp2p.container.properties.IJp2pWritePropertySource;
import net.jp2p.container.properties.IJp2pDirectives.Directives;
import net.jp2p.container.properties.IManagedPropertyListener.PropertyEvents;
import net.jp2p.container.properties.ManagedProperty;
import net.jp2p.jxta.factory.IJxtaComponents.JxtaNetworkComponents;
import net.jp2p.jxta.factory.JxtaFactoryUtils;
import net.jp2p.jxta.factory.IJxtaComponents.JxtaComponents;
import net.jp2p.jxta.network.NetworkManagerPreferences;
import net.jp2p.jxta.network.NetworkManagerPropertySource;
import net.jp2p.jxta.network.NetworkManagerPropertySource.NetworkManagerDirectives;
import net.jp2p.jxta.network.NetworkManagerPropertySource.NetworkManagerProperties;
import net.jp2p.jxta.peergroup.PeerGroupPropertySource;
import net.jxta.id.IDFactory;
import net.jxta.peer.PeerID;
import net.jxta.peergroup.PeerGroupID;
import net.jxta.platform.JxtaApplication;
import net.jxta.platform.NetworkManager;

public class NetworkManagerFactory extends AbstractFilterFactory<NetworkManager>{
		
	public static final String S_WRN_NO_CONFIGURATOR = "Could not add network configurator";
	
	@Override
	public String getComponentName() {
		return JxtaNetworkComponents.NETWORK_MANAGER.toString();
	}
	
	@Override
	protected IComponentFactoryFilter createFilter() {
		return new ComponentCreateFilter<IJp2pComponent<NetworkManager>, ContainerFactory>( BuilderEvents.COMPONENT_CREATED, Jp2pContext.Components.JP2P_CONTAINER.toString(), this );
	}

	@Override
	protected NetworkManagerPropertySource onCreatePropertySource() {
		NetworkManagerPropertySource source = new NetworkManagerPropertySource( (Jp2pContainerPropertySource) super.getParentSource());
		AbstractJp2pPropertySource.setParentDirective(Directives.AUTO_START, source);
		return source;
	}

	@Override
	public void extendContainer() {
		IContainerBuilder builder = super.getBuilder();
		JxtaFactoryUtils.getOrCreateChildFactory( builder, new HashMap<String, String>(), super.getPropertySource(), JxtaNetworkComponents.NETWORK_CONFIGURATOR.toString(), true );
		PeerGroupPropertySource npps = (PeerGroupPropertySource) JxtaFactoryUtils.getOrCreateChildFactory( builder, new HashMap<String, String>(), super.getParentSource(), JxtaComponents.NET_PEERGROUP_SERVICE.toString(), true ).getPropertySource();
		npps.setDirective( Directives.AUTO_START, this.getPropertySource().getDirective( Directives.AUTO_START ));
		super.extendContainer();
	}
	
	@Override
	protected void onParseProperty( ManagedProperty<IJp2pProperties, Object> property) {
		if(( !ManagedProperty.isCreated(property)) || ( !NetworkManagerProperties.isValidProperty(property.getKey())))
			return;
		NetworkManagerProperties id = (NetworkManagerProperties) property.getKey();
		switch( id ){
		case PEER_ID:
			String name = AbstractJp2pPropertySource.getIdentifier( super.getPropertySource() );
			PeerID peerid = IDFactory.newPeerID( PeerGroupID.defaultNetPeerGroupID, name.getBytes() );
			property.setValue( peerid, PropertyEvents.DEFAULT_VALUE_SET );
			property.reset();
			break;
		default:
			break;
		}
		super.onParseProperty(property);
	}

	@Override
	protected void onParseDirectivePriorToCreation( IJp2pDirectives directive, Object value) {
		if(( directive != null ) && !directive.equals( Directives.CLEAR  ) && !directive.equals( NetworkManagerDirectives.CLEAR_CONFIG))
			return;
		Path path = Paths.get(( URI )super.getPropertySource().getProperty( NetworkManagerProperties.INSTANCE_HOME ));
		if(Files.exists(path, LinkOption.NOFOLLOW_LINKS )){
			File file = path.toFile();
			NetworkManager.RecursiveDelete( file );
		}
	}

	@Override
	protected IJp2pComponent<NetworkManager> onCreateComponent( IJp2pPropertySource<IJp2pProperties> properties) {
		// Removing any existing configuration?
		NetworkManagerPreferences preferences = new NetworkManagerPreferences( (IJp2pWritePropertySource<IJp2pProperties>) properties );
		String name = preferences.getInstanceName();
		try {
			Path path = Paths.get( preferences.getHomeFolder() );
			if(!Files.exists(path, LinkOption.NOFOLLOW_LINKS )){
				try {
					Files.createDirectories( path );
				} catch (IOException e1) {
					e1.printStackTrace();
					return null;
				}
			}
			
			//Load the http module
			//IJp2pContext context = Activator.getLoader().
			File file = path.toFile();
			NetworkManager manager = JxtaApplication.getNetworkManager( preferences.getConfigMode(), name, file.toURI());
			return new Jp2pComponentNode<NetworkManager>( super.getPropertySource(), manager );
		} catch (Exception e) {
			Logger log = Logger.getLogger( this.getClass().getName() );
			log.log( Level.SEVERE, e.getMessage() );
			e.printStackTrace();
			return null;
		}
	}
}