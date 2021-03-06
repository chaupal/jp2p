/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.container.startup;

import java.util.Map;

import net.jp2p.container.IContainerFactory;
import net.jp2p.container.Jp2pContainerPropertySource;
import net.jp2p.container.builder.IContainerBuilder;
import net.jp2p.container.context.IJp2pServiceBuilder;
import net.jp2p.container.factory.AbstractPropertySourceFactory;
import net.jp2p.container.factory.IPropertySourceFactory;
import net.jp2p.container.properties.AbstractJp2pPropertySource;
import net.jp2p.container.properties.IJp2pProperties;
import net.jp2p.container.properties.IJp2pPropertySource;
import net.jp2p.container.properties.IJp2pWritePropertySource;
import net.jp2p.container.properties.IJp2pDirectives.Directives;

public class StartupServiceFactory extends AbstractPropertySourceFactory
{
	
	public StartupServiceFactory() {
		super(  IJp2pServiceBuilder.Components.STARTUP_SERVICE.toString() );
	}

	@Override
	public void prepare( IJp2pPropertySource<IJp2pProperties> parentSource,
			IContainerBuilder<Object> builder, Map<String, String> attributes) {
		super.prepare( parentSource, builder, attributes);
		super.setCanCreate( builder != null );
	}

	@Override
	protected Jp2pStartupPropertySource onCreatePropertySource() {
		Jp2pStartupPropertySource source = new Jp2pStartupPropertySource( (Jp2pContainerPropertySource) super.getParentSource());
		return source;
	}

	@Override
	public void extendContainer() {
		IContainerBuilder<?> builder = super.getBuilder();
		IPropertySourceFactory factory = builder.getFactory( IJp2pServiceBuilder.Components.JP2P_CONTAINER.toString() );
		IContainerFactory<?> cf = (IContainerFactory<?>) factory;
		if( !cf.isAutoStart() )
			return;
		IJp2pWritePropertySource<IJp2pProperties> props = (IJp2pWritePropertySource<IJp2pProperties>) factory.getPropertySource();
		props.setDirective( Directives.AUTO_START, Boolean.TRUE.toString());
		AbstractJp2pPropertySource.setParentDirective(Directives.AUTO_START, super.getPropertySource());
		factory = builder.getFactory( IJp2pServiceBuilder.Components.PERSISTENCE_SERVICE.toString() );
		if( factory == null ){
			builder.addFactoryToContainer( IJp2pServiceBuilder.Components.PERSISTENCE_SERVICE.toString(), super.getParentSource(), true, true);
		}
			
	}
}
