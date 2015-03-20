/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.chaupal.context;

import net.jp2p.chaupal.persistence.SimplePersistenceFactory;
import net.jp2p.container.context.AbstractJp2pServiceBuilder;
import net.jp2p.container.log.LoggerFactory;
import net.jp2p.container.properties.IJp2pProperties;
import net.jp2p.container.properties.IJp2pDirectives.Contexts;
import net.jp2p.container.startup.StartupServiceFactory;

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
