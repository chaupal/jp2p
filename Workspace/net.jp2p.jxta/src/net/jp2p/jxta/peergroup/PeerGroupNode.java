/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.jxta.peergroup;

import java.util.Stack;

import net.jp2p.container.utils.SimpleNode;
import net.jp2p.jxta.advertisement.IAdvertisementProvider;
import net.jxta.document.Advertisement;
import net.jxta.peergroup.PeerGroup;

public class PeerGroupNode extends SimpleNode<PeerGroup, PeerGroup> implements
		IAdvertisementProvider {

	public PeerGroupNode(PeerGroup data) {
		super(data);
	}

	@Override
	public Advertisement[] getAdvertisements() {
		PeerGroup peergroup = super.getData();
		Stack<Advertisement> stack = new Stack<Advertisement>();
		stack.push( peergroup.getImplAdvertisement() );
		stack.push( peergroup.getPeerAdvertisement() );
		stack.push( peergroup.getPeerGroupAdvertisement() );
		return stack.toArray( new Advertisement[ stack.size()]);
	}

}
