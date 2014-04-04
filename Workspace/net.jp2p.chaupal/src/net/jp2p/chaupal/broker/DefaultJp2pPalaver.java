/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.chaupal.broker;

import org.eclipselabs.osgi.ds.broker.service.AbstractPalaver;

/**
 * The palaver contains the conditions for attendees to create an assembly. In this case, the attendees must
 * pass a string identifier (the package id) and provide a token that is equal
 * @author Kees
 *
 */
public class DefaultJp2pPalaver extends AbstractPalaver<String>{

	private static final String S_IJXTACONTAINER_PACKAGE_ID = "org.osgi.jxta.service.ijxtaservicecomponent";
	private static final String S_IJXTA_TOKEN = "org.osgi.jxse.server.token";
	
	protected DefaultJp2pPalaver() {
		super(S_IJXTACONTAINER_PACKAGE_ID);
	}

	@Override
	public String giveToken() {
		return S_IJXTA_TOKEN;
	}

	@Override
	public boolean confirm(Object token) {
		return ( token.equals(S_IJXTA_TOKEN ));
	}	
}
