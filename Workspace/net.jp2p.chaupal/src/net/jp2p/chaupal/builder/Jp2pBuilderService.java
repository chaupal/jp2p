/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.chaupal.builder;

import net.jp2p.chaupal.utils.AbstractDeclarativeService;
import net.jp2p.container.context.IJp2pServiceBuilder;
import net.jp2p.container.context.Jp2pServiceLoader;

/**
 * <p>
 * Wrapper class which listens for all framework services of
 * class ImoduleFactory. Each such service is picked
 * up and installed into the module container
 * </p>
 * <p>
 * <p>
 * The alias used for the servlet/resource is taken from the 
 * PROP_ALIAS service property.
 * </p>
 * <p>
 * The resource dir used for contexts is taken from the 
 * PROP_DIR service property.
 * </p>
 */
public class Jp2pBuilderService extends AbstractDeclarativeService<IJp2pServiceBuilder>{

	//in the component.xml file you will use target="(jp2p.context=contextName)"
   private static String filter = "(jp2p.builder=*)"; 

	private Jp2pServiceLoader loader;
	
	public Jp2pBuilderService( Jp2pServiceLoader loader) {
		super( filter, IJp2pServiceBuilder.class );
		this.loader = loader;
	}
	
	@Override
	protected void onDataRegistered(IJp2pServiceBuilder context) {
		loader.addBuilder(context);
	}

	@Override
	protected void onDataUnRegistered(IJp2pServiceBuilder context) {
		loader.removeBuilder(context);
	}
}