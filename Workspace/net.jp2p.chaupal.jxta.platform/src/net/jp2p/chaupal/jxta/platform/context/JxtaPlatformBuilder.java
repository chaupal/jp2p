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

import net.jp2p.chaupal.jxta.platform.NetworkManagerFactory;
import net.jp2p.chaupal.jxta.platform.configurator.NetworkConfigurationFactory;
import net.jp2p.chaupal.jxta.platform.configurator.OverviewPreferences;
import net.jp2p.chaupal.jxta.platform.http.Http2Factory;
import net.jp2p.chaupal.jxta.platform.http.Http2Preferences;
import net.jp2p.chaupal.jxta.platform.http.HttpFactory;
import net.jp2p.chaupal.jxta.platform.http.HttpPreferences;
import net.jp2p.chaupal.jxta.platform.multicast.MulticastFactory;
import net.jp2p.chaupal.jxta.platform.multicast.MulticastPreferences;
import net.jp2p.chaupal.jxta.platform.security.SecurityFactory;
import net.jp2p.chaupal.jxta.platform.security.SecurityPreferences;
import net.jp2p.chaupal.jxta.platform.seeds.SeedInfo;
import net.jp2p.chaupal.jxta.platform.seeds.SeedListFactory;
import net.jp2p.chaupal.jxta.platform.seeds.SeedListPropertySource;
import net.jp2p.chaupal.jxta.platform.tcp.TcpFactory;
import net.jp2p.chaupal.jxta.platform.tcp.TcpPreferences;
import net.jp2p.container.context.AbstractJp2pServiceBuilder;
import net.jp2p.container.context.IJp2pServiceBuilder;
import net.jp2p.container.partial.PartialPropertySource;
import net.jp2p.container.properties.IJp2pProperties;
import net.jp2p.container.properties.IJp2pPropertySource;
import net.jp2p.container.properties.IJp2pWritePropertySource;
import net.jp2p.container.properties.IPropertyConvertor;
import net.jp2p.container.properties.IJp2pDirectives.Contexts;
import net.jp2p.container.utils.StringStyler;
import net.jp2p.container.xml.IJp2pHandler;
import net.jp2p.jxta.context.IJxtaBuilder;
import net.jp2p.jxta.factory.IJxtaComponents.JxtaPlatformComponents;
import net.jp2p.jxta.network.NetworkManagerPreferences;
import net.jxta.peergroup.IModuleDefinitions.DefaultModules;
import net.jxta.peergroup.core.ModuleClassID;

public class JxtaPlatformBuilder extends AbstractJp2pServiceBuilder implements IJxtaBuilder {

	public JxtaPlatformBuilder() {
		super( Contexts.PLATFORM.toString());
	}

	@Override
	protected void prepare() {
		super.addFactory( new NetworkManagerFactory( ));
		super.addFactory( new NetworkConfigurationFactory( ));
		super.addFactory( new SecurityFactory());
		super.addFactory( new TcpFactory() );
		super.addFactory( new HttpFactory() );
		super.addFactory( new Http2Factory() );
		super.addFactory( new MulticastFactory() );
		super.addFactory( new SeedListFactory());
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
		if( !JxtaPlatformComponents.isComponent( comp ))
			return null;
		JxtaPlatformComponents component = JxtaPlatformComponents.valueOf(comp);
		IPropertyConvertor<String, Object> convertor = null;
		switch( component ){
		case NETWORK_MANAGER:
			convertor = new NetworkManagerPreferences( (IJp2pWritePropertySource<IJp2pProperties>) source );
			break;			
		case NETWORK_CONFIGURATOR:
			convertor = new OverviewPreferences( (IJp2pWritePropertySource<IJp2pProperties>) source );
			break;
		case SEED_LIST:
			//SeedListPropertySource slps = (SeedListPropertySource) source;
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

	@Override
	public int compareTo(IJp2pServiceBuilder o) {
		return this.getName().compareTo( o.getName() );
	}
}
