/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.chaupal.context;

import net.jp2p.container.Jp2pContainerPropertySource;
import net.jp2p.container.context.AbstractJp2pServiceBuilder;
import net.jp2p.container.context.IJp2pServiceBuilder;
import net.jp2p.container.context.Jp2pContainerPreferences;
import net.jp2p.container.log.LoggerFactory;
import net.jp2p.container.persistence.SimplePersistenceFactory;
import net.jp2p.container.properties.IJp2pProperties;
import net.jp2p.container.properties.IJp2pPropertySource;
import net.jp2p.container.properties.IJp2pWritePropertySource;
import net.jp2p.container.properties.IPropertyConvertor;
import net.jp2p.container.properties.IJp2pDirectives.Contexts;
import net.jp2p.container.properties.SimplePropertyConvertor;
import net.jp2p.container.startup.StartupServiceFactory;
import net.jp2p.container.utils.StringStyler;
import net.jp2p.container.xml.IJp2pHandler;

public class Jp2pServiceBuilder extends AbstractJp2pServiceBuilder {

	public Jp2pServiceBuilder() {
		super(Contexts.JP2P.toString());
	}

	
	@Override
	protected void prepare() {
		super.addFactory( new StartupServiceFactory() );
		super.addFactory( new SimplePersistenceFactory() );
		super.addFactory( new LoggerFactory() );
	}

	/**
	 * Get the handler for this context
	 * @return
	 */
	@Override
	public IJp2pHandler getHandler(){
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
		if( !IJp2pServiceBuilder.Components.isComponent( comp ))
			return getConvertor(source);
		IJp2pServiceBuilder.Components component = IJp2pServiceBuilder.Components.valueOf(comp);
		IPropertyConvertor<String, Object> convertor = null;
		switch( component ){
		case JP2P_CONTAINER:
			convertor = new Jp2pContainerPreferences( (Jp2pContainerPropertySource) source );
			break;
		default:
			convertor = new SimplePropertyConvertor( (IJp2pWritePropertySource<IJp2pProperties>) source );
			break;
		}
		return convertor;
	}

	/**
	 * Create a value for the given component name and id
	 * @param parent
	 * @param componentName
	 * @return
	 */
	@Override
	public Object createValue( String componentName, IJp2pProperties id ){
		return id.toString();
	}
}
