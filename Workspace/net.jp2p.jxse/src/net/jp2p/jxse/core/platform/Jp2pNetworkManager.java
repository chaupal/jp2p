/**
 * The JXTA protocols often need to refer to peers, peer groups, pipes and other JXTA resources. These references are presented in
 * the protocols as JXTA IDs. JXTA IDs are a means for uniquely identifying specific peer groups, peers, pipes, codat and service
 * instances. JXTA IDs provide unambiguous references to the various JXTA entities. There are six types of JXTA entities which
 * have JXTA ID types defined: peergroups, peers, pipes, codats, module classes and module specifications. Additional JXTA ID
 * types may be defined in the future.
 * JXTA IDs are normally presented as URNs. URNs are a form of URI that ‘... are intended to serve as persistent, locationindependent,
 * resource identifiers’. Like other forms of URI, JXTA IDs are presented as text. See IETF RFC 2141 RFC2141 for
 * more information on URNs.
 *
 *  THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
 *  INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND 
 *  FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL CHAUPAL 
 *  MICROSYSTEMS OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, 
 *  INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT 
 *  LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, 
 *  OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF 
 *  LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING 
 *  NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, 
 *  EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * 
 * @See: JXTA v2.0 Protocols Specification, Chapter 1
 * @author keesp
 * @Organisation: chaupal.org 
 * 
 *******************************************************************************
 * Copyright (c) 2014-2021 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************
*/
package net.jp2p.jxse.core.platform;

import java.io.IOException;
import java.net.URI;

import net.jp2p.chaupal.peer.IJp2pPeerID;
import net.jp2p.chaupal.peergroup.IJp2pPeerGroup;
import net.jp2p.chaupal.peergroup.IJp2pPeerGroupID;
import net.jp2p.chaupal.platform.INetworkConfigurator;
import net.jp2p.chaupal.platform.INetworkManager;
import net.jp2p.chaupal.rendezvous.JP2PRendezvousEvent;
import net.jp2p.jxse.core.id.Jp2pPeerGroupID;
import net.jp2p.jxse.core.peer.Jp2pPeerID;
import net.jxta.exception.ConfiguratorException;
import net.jxta.exception.PeerGroupException;
import net.jxta.platform.NetworkManager;
import net.jxta.platform.NetworkManager.ConfigMode;
import net.jxta.rendezvous.RendezvousEvent;

public class Jp2pNetworkManager implements INetworkManager<RendezvousEvent> {

	private NetworkManager manager;
	
	public Jp2pNetworkManager( NetworkManager manager ) {
		this.manager = manager;
	}

	@Override
	public INetworkConfigurator getConfigurator() throws IOException {
		return new Jp2pNetworkConfigurator(manager.getConfigurator());
	}

	@Override
	public IJp2pPeerGroupID getInfrastructureID() {
		return new Jp2pPeerGroupID( manager.getInfrastructureID() );
	}

	@Override
	public void setInfrastructureID(IJp2pPeerGroupID infrastructureID) {
		Jp2pPeerGroupID jpgid = (Jp2pPeerGroupID) infrastructureID;
		manager.setInfrastructureID( jpgid.getID());	
	}

	@Override
	public String getInstanceName() {
		return manager.getInstanceName();
	}

	@Override
	public URI getInstanceHome() {
		return manager.getInstanceHome();
	}

	@Override
	public ConfigModes getMode() {
		return ConfigModes.valueOf(manager.getMode().name());
	}

	@Override
	public void setMode(ConfigModes mode) throws IOException {
		this.manager.setMode(ConfigMode.valueOf(mode.name()));
	}

	@Override
	public IJp2pPeerID getPeerID() {
		return new Jp2pPeerID( manager.getPeerID());
	}

	@Override
	public void setPeerID(IJp2pPeerID peerID) {
		Jp2pPeerID jpid= (Jp2pPeerID) peerID;
		this.manager.setPeerID(jpid.getID());
	}

	@Override
	public boolean isConfigPersistent() {
		return manager.isConfigPersistent();
	}

	@Override
	public void setConfigPersistent(boolean persisted) {
		manager.setConfigPersistent(persisted);
	}

	@Override
	public IJp2pPeerGroup startNetwork() {
		IJp2pPeerGroup jpg = null;
		try {
			jpg=  new Jp2pPeerGroup( manager.startNetwork() );
		} catch (PeerGroupException e) {
			e.printStackTrace();;
		} catch (ConfiguratorException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return jpg;
	}

	@Override
	public void stopNetwork() {
		manager.stopNetwork();
	}

	@Override
	public IJp2pPeerGroup getNetPeerGroup() {
		return new Jp2pPeerGroup( manager.getNetPeerGroup() );
	}

	@Override
	public boolean waitForRendezvousConnection(long timeout) {
		return this.manager.waitForRendezvousConnection(timeout);
	}

	@Override
	public void rendezvousEvent(JP2PRendezvousEvent<RendezvousEvent> event) {
		this.manager.rendezvousEvent( event.getEvent());
	}

	@Override
	public void registerShutdownHook() {
		this.manager.registerShutdownHook();
	}

	@Override
	public void unregisterShutdownHook() {
		this.manager.unregisterShutdownHook();
	}

	@Override
	public boolean isStarted() {
		return this.manager.isStarted();
	}

}
