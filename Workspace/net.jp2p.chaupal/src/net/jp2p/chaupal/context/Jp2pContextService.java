/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.chaupal.context;

import org.osgi.framework.BundleContext;

import net.jp2p.chaupal.module.AbstractService;
import net.jp2p.container.context.ContextLoader;
import net.jp2p.container.context.IJp2pContext;

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
public class Jp2pContextService extends AbstractService<IJp2pContext>{

	//in the component.xml file you will use target="(jp2p.context=contextName)"
   private static String filter = "(jp2p.context=*)"; 

	private ContextLoader loader;
	
	public Jp2pContextService(ContextLoader loader, BundleContext bc) {
		super( bc, IJp2pContext.class, filter );
		this.loader = loader;
	}
	
	@Override
	public void open() {
		super.open();
	}

	@Override
	protected void onDataRegistered(IJp2pContext context) {
		loader.addContext(context);
	}

	@Override
	protected void onDataUnRegistered(IJp2pContext context) {
		loader.removeContext(context);
	}

	@Override
	public void close() {
		super.close();
	}
}