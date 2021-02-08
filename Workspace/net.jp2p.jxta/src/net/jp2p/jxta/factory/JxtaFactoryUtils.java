/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.jxta.factory;

import java.util.Map;

import net.jp2p.container.builder.IContainerBuilder;
import net.jp2p.container.factory.IPropertySourceFactory;
import net.jp2p.container.properties.IJp2pProperties;
import net.jp2p.container.properties.IJp2pPropertySource;
import net.jp2p.container.utils.StringStyler;
import net.jp2p.container.utils.Utils;
import net.jp2p.jxta.advertisement.service.JxtaAdvertisementFactory;
import net.jp2p.jxta.discovery.DiscoveryServiceFactory;
import net.jp2p.jxta.factory.IJxtaComponents.JxtaComponents;
import net.jp2p.jxta.netpeergroup.NetPeerGroupFactory;
import net.jp2p.jxta.peergroup.PeerGroupFactory;
import net.jp2p.jxta.pipe.PipeServiceFactory;
import net.jp2p.jxta.registration.RegistrationServiceFactory;
import net.jp2p.jxta.rendezvous.RendezVousFactory;
import net.jp2p.jxta.socket.SocketFactory;

public class JxtaFactoryUtils {

	/**
	 * Get the default factory for this container
	 * @param parent
	 * @param componentName
	 * @return
	 */
	public static IPropertySourceFactory getDefaultFactory( String componentName ){
		if( Utils.isNull(componentName))
			return null;
		String comp = StringStyler.styleToEnum(componentName);
		if( !JxtaComponents.isComponent( comp ))
			return null;
		JxtaComponents component = JxtaComponents.valueOf(comp);
		IPropertySourceFactory factory = null;
		switch( component ){
		case NET_PEERGROUP_SERVICE:
			factory = new NetPeerGroupFactory();
			break;			
		case PIPE_SERVICE:
			factory = new PipeServiceFactory();
			break;			
		case REGISTRATION_SERVICE:
			factory = new RegistrationServiceFactory();
			break;
		case DISCOVERY_SERVICE:
			factory = new DiscoveryServiceFactory();
			break;			
		case PEERGROUP_SERVICE:
			factory = new PeerGroupFactory();
			break;			
		case ADVERTISEMENT:
			factory = new JxtaAdvertisementFactory();
			break;
		case JXSE_SOCKET_SERVICE:
			factory = new SocketFactory();
			break;
		case RENDEZVOUS_SERVICE:
			factory = new RendezVousFactory();
			break;
		default:
			break;
		}
		return factory;
	}

	/**
	 * Get or create a corresponding factory for a child component of the given source, with the given component name.
	 * @param source: the source who should have a child source
	 * @param componentName: the required component name of the child
	 * @param createSource: create the property source immediately
	 * @return
	 */
	public static IPropertySourceFactory getOrCreateChildFactory( IContainerBuilder<Object> builder, Map<String,String> attributes, IJp2pPropertySource<IJp2pProperties> parentSource, String componentName, boolean createSource ){
		IJp2pPropertySource<?> child = parentSource.getChild( componentName ); 
		if( child != null )
			return builder.getFactory(child );
		IPropertySourceFactory factory = getDefaultFactory(componentName );
		factory.prepare( parentSource, builder, attributes);
		if( createSource )
			factory.createPropertySource();
		builder.addFactory( factory );
		factory.extendContainer();
		return factory;
	}

}
