/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.chaupal.jxta.platform.context;

import java.util.ArrayList;
import java.util.Collection;

import net.jp2p.chaupal.jxta.platform.configurator.OverviewPreferences;
import net.jp2p.chaupal.jxta.platform.http.Http2Preferences;
import net.jp2p.chaupal.jxta.platform.http.HttpPreferences;
import net.jp2p.chaupal.jxta.platform.multicast.MulticastPreferences;
import net.jp2p.chaupal.jxta.platform.security.SecurityPreferences;
import net.jp2p.chaupal.jxta.platform.tcp.TcpPreferences;
import net.jp2p.chaupal.jxta.platform.utils.JxtaFactoryUtils;
import net.jp2p.chaupal.jxta.root.network.NetworkManagerPreferences;
import net.jp2p.container.context.Jp2pContext;
import net.jp2p.container.factory.IPropertySourceFactory;
import net.jp2p.container.partial.PartialPropertySource;
import net.jp2p.container.properties.IJp2pProperties;
import net.jp2p.container.properties.IJp2pPropertySource;
import net.jp2p.container.properties.IJp2pWritePropertySource;
import net.jp2p.container.properties.IPropertyConvertor;
import net.jp2p.container.properties.IJp2pDirectives.Contexts;
import net.jp2p.container.utils.StringStyler;
import net.jp2p.container.utils.Utils;
import net.jp2p.container.xml.IJp2pHandler;
import net.jp2p.jxta.context.IJxtaContext;
import net.jp2p.jxta.factory.IJxtaComponents.JxtaNetworkComponents;
import net.jp2p.jxta.seeds.SeedListPropertySource;
import net.jxta.peergroup.IModuleDefinitions.DefaultModules;
import net.jxta.platform.ModuleClassID;

public class JxtaNetworkContext implements IJxtaContext {

	public JxtaNetworkContext() {
	}

	@Override
	public String getName() {
		return Contexts.JXTA.toString();
	}

	/**
	 * Get the supported services
	 */
	@Override
	public String[] getSupportedServices() {
		JxtaNetworkComponents[] components = JxtaNetworkComponents.values();
		String[] names = new String[ components.length ];
		for( int i=0; i<components.length; i++ )
			names[i] = components[i].toString();
		return names;
	}

	
	@Override
	public ModuleClassID[] getSupportedModuleClassIDs() {
		Collection<ModuleClassID> ids = new ArrayList<ModuleClassID>();
		for( DefaultModules dm: DefaultModules.values()){
			switch( dm ){
			case HTTP:
			case TCP:
				break;
			default:
				ids.add( DefaultModules.getModuleClassID(dm));
				break;
			}
		}		
		return ids.toArray( new ModuleClassID[ ids.size() ]);
	}

	/**
	 * Returns true if the given component name is valid for this context
	 * @param componentName
	 * @return
	 */
	@Override
	public boolean isValidComponentName( String contextName, String componentName ){
		if( !Utils.isNull( contextName ) && !Jp2pContext.isContextNameEqual(Contexts.JXTA.toString(), contextName ))
			return false;
		return JxtaNetworkComponents.isComponent( componentName );
	}

	/**
	 * Change the factory to a Chaupal factory if required and available
	 * @param factory
	 * @return
	 */
	@Override
	public IPropertySourceFactory getFactory( String componentName ){
		JxtaNetworkComponents component = JxtaNetworkComponents.valueOf( StringStyler.styleToEnum(componentName));
		String[] attrs;
		switch( component ){
		default:
			attrs = new String[0];
		}
		IPropertySourceFactory factory = JxtaFactoryUtils.getDefaultFactory(componentName);
		return factory;
	}

	@Override
	public Object createValue( String componentName, IJp2pProperties id ){
		return null;
	}
	
	/**
	 * Get the default factory for this container
	 * @param parent
	 * @param componentName
	 * @return
	 */
	@Override
	public IPropertyConvertor<String, Object> getConvertor( IJp2pPropertySource<IJp2pProperties> source ){
		String comp = StringStyler.styleToEnum( source.getComponentName());
		if( !JxtaNetworkComponents.isComponent( comp ))
			return null;
		JxtaNetworkComponents component = JxtaNetworkComponents.valueOf(comp);
		IPropertyConvertor<String, Object> convertor = null;
		switch( component ){
		case NETWORK_MANAGER:
			convertor = new NetworkManagerPreferences( (IJp2pWritePropertySource<IJp2pProperties>) source );
			break;			
		case NETWORK_CONFIGURATOR:
			convertor = new OverviewPreferences( (IJp2pWritePropertySource<IJp2pProperties>) source );
			break;
		case SEED_LIST:
			SeedListPropertySource slps = (SeedListPropertySource) source;
			//SeedInfo seedInfo = new SeedInfo((( IJp2pProperties )source.getKey()).name(), ( String )value );
			//slps.setProperty( (IJp2pProperties) property.getKey(), seedInfo );
			break;
		case TCP:
			convertor = new TcpPreferences( (PartialPropertySource) source );
			break;
		case HTTP:
			convertor = new HttpPreferences( (PartialPropertySource) source );
			break;
		case HTTP2:
			convertor = new Http2Preferences( (PartialPropertySource) source );
			break;
		case MULTICAST:
			convertor = new MulticastPreferences( (PartialPropertySource) source );
			break;
		case SECURITY:
			convertor = new SecurityPreferences( (PartialPropertySource) source );
			break;
		default:
			break;
		}
		return convertor;
	}

	@Override
	public IJp2pHandler getHandler() {
		// TODO Auto-generated method stub
		return null;
	}
}
