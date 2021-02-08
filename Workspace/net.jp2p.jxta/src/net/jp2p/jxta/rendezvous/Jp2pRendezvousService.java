/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.jxta.rendezvous;

import net.jp2p.container.component.Jp2pComponent;
import net.jxta.peergroup.PeerGroup;
import net.jxta.rendezvous.RendezVousService;

public class Jp2pRendezvousService extends Jp2pComponent<RendezVousService>{

	public Jp2pRendezvousService( RendezVousPropertySource source, PeerGroup peergroup ) {
		super(source, peergroup.getRendezVousService() );
	}	
}