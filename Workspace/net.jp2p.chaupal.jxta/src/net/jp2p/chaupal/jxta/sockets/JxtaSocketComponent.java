/*******************************************************************************
 * Copyright (c) 2013 Condast and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Kees Pieters - initial API and implementation
 *******************************************************************************/
package net.jp2p.chaupal.jxta.sockets;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.jp2p.container.component.IJp2pComponent;
import net.jp2p.container.component.Jp2pComponent;
import net.jp2p.container.utils.IOUtils;
import net.jxta.protocol.PipeAdvertisement;
import net.jxta.refplatform.platform.NetworkManager;
import net.jxta.socket.JxtaSocket;
import net.jp2p.jxta.socket.SocketFactory;

public class JxtaSocketComponent extends Jp2pComponent<JxtaSocket> {

	public static final String S_SERVER_SOCKET = "JXTA ServerSocket";

	public enum JxseSocketProperties{
		
	}
	
	private IJp2pComponent<PipeAdvertisement> pipeAd;

	
	public JxtaSocketComponent( IJp2pComponent<NetworkManager> manager, IJp2pComponent<PipeAdvertisement> pipeAd, 
			Properties properties ) {
		super( null, null );
		this.pipeAd = pipeAd;
	}

	@SuppressWarnings("unchecked")
	@Override
	public JxtaSocket getModule() {
		JxtaSocket socket = null;
		 IJp2pComponent<NetworkManager> manager = null;//(IJp2pComponent<NetworkManager> )super.getParent();
		try {
			return new JxtaSocket( manager.getModule().getNetPeerGroup(), null, pipeAd.getModule(), ( int )super.getPropertySource().getProperty( SocketFactory.Properties.TIME_OUT ));
		} catch (Exception e) {
			Logger log = Logger.getLogger( this.getClass().getName() );
			log.log( Level.SEVERE, e.getMessage() );
			e.printStackTrace();
			IOUtils.closeSocket( socket );
		}
		return null;
	}
}
