/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.chaupal.jxta.platform;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.jp2p.chaupal.container.ContainerFactory;
import net.jp2p.chaupal.jxta.platform.NetworkManagerPropertySource.NetworkManagerProperties;
import net.jp2p.container.Jp2pContainerPropertySource;
import net.jp2p.container.builder.IContainerBuilder;
import net.jp2p.container.component.IJp2pComponent;
import net.jp2p.container.component.Jp2pComponentNode;
import net.jp2p.container.context.IJp2pServiceBuilder;
import net.jp2p.container.factory.AbstractFilterFactory;
import net.jp2p.container.factory.filter.ComponentCreateFilter;
import net.jp2p.container.factory.filter.IComponentFactoryFilter;
import net.jp2p.container.properties.AbstractJp2pPropertySource;
import net.jp2p.container.properties.IJp2pProperties;
import net.jp2p.container.properties.IJp2pPropertySource;
import net.jp2p.container.properties.IJp2pDirectives.Directives;
import net.jp2p.container.properties.IManagedPropertyListener.PropertyEvents;
import net.jp2p.container.properties.ManagedProperty;
import net.jp2p.jxta.factory.IJxtaComponents.JxtaPlatformComponents;
import net.jp2p.jxta.factory.JxtaFactoryUtils;
import net.jp2p.jxta.factory.IJxtaComponents.JxtaComponents;
import net.jp2p.jxta.peergroup.PeerGroupPropertySource;
import net.jxta.id.IDFactory;
import net.jxta.peer.PeerID;
import net.jxta.peergroup.PeerGroupID;
import net.jxta.platform.JxtaApplication;
import net.jxta.platform.NetworkManager;

public class NetworkManagerFactory extends AbstractFilterFactory<NetworkManager>{
		
	public static final String S_WRN_NO_CONFIGURATOR = "Could not add network configurator";
	
	public NetworkManagerFactory() {
		super( JxtaPlatformComponents.NETWORK_MANAGER.toString());
	}
	
	@Override
	protected IComponentFactoryFilter createFilter() {
		return new ComponentCreateFilter<IJp2pComponent<NetworkManager>, ContainerFactory>( BuilderEvents.COMPONENT_CREATED, IJp2pServiceBuilder.Components.JP2P_CONTAINER.toString(), this );
	}

	@Override
	protected NetworkManagerPropertySource onCreatePropertySource() {
		NetworkManagerPropertySource source = new NetworkManagerPropertySource( (Jp2pContainerPropertySource) super.getParentSource());
		AbstractJp2pPropertySource.setParentDirective(Directives.AUTO_START, source);
		return source;
	}

	@Override
	public void extendContainer() {
		IContainerBuilder<?> builder = super.getBuilder();
		JxtaFactoryUtils.getOrCreateChildFactory( builder, new HashMap<String, String>(), super.getPropertySource(), JxtaPlatformComponents.NETWORK_CONFIGURATOR.toString(), true );
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
	protected IJp2pComponent<NetworkManager> onCreateComponent( IJp2pPropertySource<IJp2pProperties> properties) {
		// Removing any existing configuration?
		NetworkManagerPreferences preferences = new NetworkManagerPreferences( (NetworkManagerPropertySource) properties );
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
			
			//Load the config file
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