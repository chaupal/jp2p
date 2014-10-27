/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.jxta.rendezvous;

import net.jp2p.container.component.IJp2pComponent;
import net.jp2p.container.component.Jp2pComponent;
import net.jp2p.container.properties.IJp2pProperties;
import net.jp2p.container.properties.IJp2pPropertySource;
import net.jp2p.jxta.factory.AbstractPeerGroupDependencyFactory;
import net.jp2p.jxta.factory.IJxtaComponents.JxtaComponents;
import net.jp2p.jxta.peergroup.PeerGroupPropertySource;
import net.jxta.peergroup.PeerGroup;
import net.jxta.rendezvous.RendezVousService;

public class RendezVousFactory extends AbstractPeerGroupDependencyFactory<RendezVousService>{

	@Override
	public String getComponentName() {
		return JxtaComponents.RENDEZVOUS_SERVICE.toString();
	}	

	@Override
	public RendezVousPropertySource onCreatePropertySource() {
		RendezVousPropertySource source = new RendezVousPropertySource( (PeerGroupPropertySource) super.getParentSource());
		return source;
	}
	
	@Override
	protected IJp2pComponent<RendezVousService> onCreateComponent( IJp2pPropertySource<IJp2pProperties> properties) {
		PeerGroup peergroup = super.getPeerGroup();
		return new Jp2pComponent<RendezVousService>( super.getPropertySource(), peergroup.getRendezVousService() );
	}
}