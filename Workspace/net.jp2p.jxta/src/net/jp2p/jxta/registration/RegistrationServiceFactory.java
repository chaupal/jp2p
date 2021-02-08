/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.jxta.registration;

import net.jp2p.container.component.IJp2pComponent;
import net.jp2p.container.component.Jp2pComponent;
import net.jp2p.container.factory.AbstractComponentFactory;
import net.jp2p.container.factory.ComponentBuilderEvent;
import net.jp2p.container.properties.IJp2pProperties;
import net.jp2p.container.properties.IJp2pPropertySource;
import net.jp2p.container.properties.IJp2pWritePropertySource;
import net.jp2p.jxta.factory.IJxtaComponents.JxtaComponents;

public class RegistrationServiceFactory extends
		AbstractComponentFactory<RegistrationService> {

	public  RegistrationServiceFactory() {
		super( JxtaComponents.REGISTRATION_SERVICE.toString());
	}

	@Override
	protected RegistrationPropertySource onCreatePropertySource() {
		return new RegistrationPropertySource( super.getParentSource() );
	}

	@Override
	public void onNotifyChange(ComponentBuilderEvent event) {
		/* DO NOTHING */
	}

	@Override
	protected IJp2pComponent<RegistrationService> onCreateComponent( IJp2pPropertySource<IJp2pProperties> properties) {
		RegistrationService service = new RegistrationService( (IJp2pWritePropertySource<IJp2pProperties>) properties );
		return new Jp2pComponent<RegistrationService>( service );
	}


}