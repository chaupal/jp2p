/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.container.startup;

import net.jp2p.container.Jp2pContainerPropertySource;
import net.jp2p.container.IJp2pContainer.ContainerProperties;
import net.jp2p.container.context.IJp2pServiceBuilder;
import net.jp2p.container.properties.AbstractJp2pWritePropertySource;
import net.jp2p.container.properties.IJp2pProperties;
import net.jp2p.container.properties.IJp2pDirectives.Directives;
import net.jp2p.container.utils.StringStyler;

public class Jp2pStartupPropertySource extends AbstractJp2pWritePropertySource{
	
	public enum StartupProperties implements IJp2pProperties{
		RETRIES;

		@Override
		public String toString() {
			return StringStyler.prettyString(super.toString());
		}
	}
	
	public Jp2pStartupPropertySource( Jp2pContainerPropertySource parent ) {
		super( IJp2pServiceBuilder.Components.STARTUP_SERVICE.toString(), parent );
		super.setDirective( Directives.AUTO_START, parent.getDirective( Directives.AUTO_START ));
		super.setProperty( StartupProperties.RETRIES, 10 );
	}

	@Override
	public Object getDefault( IJp2pProperties id) {
		if(!( id instanceof ContainerProperties ))
			return null;
		ContainerProperties cp = (ContainerProperties )id;
		switch( cp ){
		default:
			break;
		}
		return null;
	}

	@Override
	public boolean validate( IJp2pProperties id, Object value) {
		// TODO Auto-generated method stub
		return false;
	}
}
