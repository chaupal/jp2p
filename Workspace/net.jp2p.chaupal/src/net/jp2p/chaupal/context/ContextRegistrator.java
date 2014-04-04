/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.chaupal.context;


import java.util.Dictionary;

import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;

import net.jp2p.chaupal.activator.AbstractRegistrator;
import net.jp2p.container.context.IJp2pContext;

public class ContextRegistrator extends AbstractRegistrator<IJp2pContext> {
	
	private static final String S_CONTEXT_SERVICE = "ContextService";


	public ContextRegistrator( BundleContext context ) {
		super( IJp2pContext.class.getName(), context );
	}

	@Override
	protected void fillDictionary(Dictionary<String, Object> dictionary) {
		dictionary.put( S_CONTEXT_SERVICE, super.getRegistered() );				
	}

	@Override
	public void unregister() {
		try {
			super.unregister();
		} catch (InvalidSyntaxException e) {
			e.printStackTrace();
		}
	}
}
