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
package net.jp2p.chaupal.jxse.core.platform;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.w3c.dom.Element;

import net.jp2p.chaupal.document.IJp2pAdvertisement;
import net.jp2p.chaupal.exception.Jp2pPeerGroupException;
import net.jp2p.chaupal.exception.Jp2pProtocolNotSupportedException;
import net.jp2p.chaupal.exception.Jp2pServiceNotFoundException;
import net.jp2p.chaupal.id.IJp2pID;
import net.jp2p.chaupal.jxse.advertisement.Jp2pAdvertisement;
import net.jp2p.chaupal.jxse.core.id.Jp2pID;
import net.jp2p.chaupal.jxse.core.id.Jp2pPeerGroupID;
import net.jp2p.chaupal.jxse.core.module.Jp2pModule;
import net.jp2p.chaupal.jxse.core.module.Jp2pService;
import net.jp2p.chaupal.jxse.core.peer.Jp2pPeerID;
import net.jp2p.chaupal.jxse.core.peergroup.Jp2pGlobalRegistry;
import net.jp2p.chaupal.jxse.core.peergroup.Jp2pModuleSpecID;
import net.jp2p.chaupal.jxse.core.protocol.Jp2pConfigParams;
import net.jp2p.chaupal.jxse.core.protocol.Jp2pModuleImplAdvertisement;
import net.jp2p.chaupal.jxse.core.protocol.Jp2pPeerAdvertisement;
import net.jp2p.chaupal.jxse.core.protocol.Jp2pPeerGroupAdvertisement;
import net.jp2p.chaupal.module.IJp2pModule;
import net.jp2p.chaupal.module.IJp2pService;
import net.jp2p.chaupal.peer.IJp2pPeerID;
import net.jp2p.chaupal.peergroup.IJp2pGlobalRegistry;
import net.jp2p.chaupal.peergroup.IJp2pModuleSpecID;
import net.jp2p.chaupal.peergroup.IJp2pPeerGroup;
import net.jp2p.chaupal.peergroup.IJp2pPeerGroupID;
import net.jp2p.chaupal.protocol.IJp2pConfigParams;
import net.jp2p.chaupal.protocol.IJp2pModuleImplAdvertisement;
import net.jp2p.chaupal.protocol.IJp2pPeerAdvertisement;
import net.jp2p.chaupal.protocol.IJp2pPeerGroupAdvertisement;
import net.jxta.exception.PeerGroupException;
import net.jxta.exception.ProtocolNotSupportedException;
import net.jxta.exception.ServiceNotFoundException;
import net.jxta.id.ID;
import net.jxta.peergroup.core.Module;
import net.jxta.peergroup.PeerGroup;
import net.jxta.protocol.PeerGroupAdvertisement;
import net.jxta.service.Service;

public class Jp2pPeerGroup implements IJp2pPeerGroup {

	private PeerGroup peergroup;
	
	public Jp2pPeerGroup(PeerGroup peergroup) {
		this.peergroup = peergroup;
	}

	public PeerGroup getPeergroup() {
		return peergroup;
	}

	@Override
	public IJp2pGlobalRegistry getGlobalRegistry() {
		return new Jp2pGlobalRegistry( this.peergroup.getGlobalRegistry());
	}

	@Override
	public boolean isRendezvous() {
		return this.peergroup.isRendezvous();
	}

	@Override
	public IJp2pPeerGroupAdvertisement getPeerGroupAdvertisement() {
		return new Jp2pPeerGroupAdvertisement( this.peergroup.getPeerGroupAdvertisement());
	}

	@Override
	public IJp2pPeerAdvertisement getPeerAdvertisement() {
		return new Jp2pPeerAdvertisement( this.peergroup.getPeerAdvertisement() );
	}

	@SuppressWarnings("unchecked")
	@Override
	public IJp2pService lookupService(IJp2pID name) throws Jp2pServiceNotFoundException {
		Jp2pID<ID> id = (Jp2pID<ID>) name;
		try {
			return new Jp2pService( this.peergroup.lookupService( id.getID() ));
		} catch (ServiceNotFoundException e) {
			throw new Jp2pServiceNotFoundException();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public IJp2pService lookupService(IJp2pID name, int roleIndex) throws Jp2pServiceNotFoundException {
		Jp2pID<ID> id = (Jp2pID<ID>) name;
		try {
			return new Jp2pService( this.peergroup.lookupService( id.getID(), roleIndex ));
		} catch (ServiceNotFoundException e) {
			throw new Jp2pServiceNotFoundException();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Iterator<IJp2pID> getRoleMap(IJp2pID name) {
		Jp2pID<ID> id = (Jp2pID<ID>) name;
		Iterator<ID> iterator = this.peergroup.getRoleMap(id.getID()); 
		Collection<IJp2pID> results = new ArrayList<>();
		while( iterator.hasNext()) {
			results.add( new Jp2pID<ID>( iterator.next()));
		}
		return results.iterator();
	}

	@Override
	public boolean compatible(Element compat) {
		return this.peergroup.compatible(null);
	}

	@SuppressWarnings("unchecked")
	@Override
	public IJp2pModule loadModule(IJp2pID assignedID, IJp2pAdvertisement impl)
			throws Jp2pProtocolNotSupportedException, Jp2pPeerGroupException {
		Jp2pID<ID> id = (Jp2pID<ID>) assignedID;
		Jp2pAdvertisement<PeerGroupAdvertisement> adv = (Jp2pAdvertisement<PeerGroupAdvertisement>) impl;
		try {
			return new Jp2pModule<Module>( this.peergroup.loadModule(id.getID(), adv.getAdvertisement()));
		} catch (ProtocolNotSupportedException e) {
			throw new Jp2pProtocolNotSupportedException(e);
		} catch (PeerGroupException e) {
			throw new Jp2pPeerGroupException(e);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public IJp2pModule loadModule(IJp2pID assignedID, IJp2pModuleSpecID specID, int where) {
		Jp2pID<ID> id = (Jp2pID<ID>) assignedID;
		Jp2pModuleSpecID sid = (Jp2pModuleSpecID) specID;
		return new Jp2pModule<Module>( this.peergroup.loadModule(id.getID(), sid.getID(), where ));
	}

	@Override
	public void publishGroup(String name, String description) throws IOException {
		this.peergroup.publishGroup(name, description);
	}

	@SuppressWarnings("unchecked")
	@Override
	public IJp2pPeerGroup newGroup(IJp2pAdvertisement pgAdv)
			throws Jp2pPeerGroupException {
		Jp2pAdvertisement<PeerGroupAdvertisement> adv = (Jp2pAdvertisement<PeerGroupAdvertisement>) pgAdv;
		try {
			return new Jp2pPeerGroup( this.peergroup.newGroup(adv.getAdvertisement()));
		} catch (PeerGroupException e) {
			throw new Jp2pPeerGroupException(e);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public IJp2pPeerGroup newGroup(IJp2pPeerGroupID gid,
			IJp2pAdvertisement impl, String name, String description, boolean publish ) throws Jp2pPeerGroupException {
		Jp2pPeerGroupID id = (Jp2pPeerGroupID) gid;
		Jp2pAdvertisement<PeerGroupAdvertisement> adv = (Jp2pAdvertisement<PeerGroupAdvertisement>) impl;
		try {
			return new Jp2pPeerGroup( this.peergroup.newGroup(id.getID(), adv.getAdvertisement(), name, description, publish));
		} catch (PeerGroupException e) {
			throw new Jp2pPeerGroupException(e);
		}
	}

	@Override
	public IJp2pPeerGroup newGroup(IJp2pPeerGroupID gid)
			throws Jp2pPeerGroupException {
		Jp2pPeerGroupID id = (Jp2pPeerGroupID) gid;
		try {
			return new Jp2pPeerGroup( this.peergroup.newGroup(id.getID()));
		} catch (PeerGroupException e) {
			throw new Jp2pPeerGroupException(e);
		}
	}

	@Override
	public IJp2pService getService(PeerGroupServices service) {
		Service module = null;
		switch( service ) {
		case ACCESS_SERVICE:
			module = this.peergroup.getAccessService();
			break;
		case CONTENT_SERVICE:
			module = this.peergroup.getContentService();
			break;
		case DISCOVERY_SERVICE:
			module = this.peergroup.getDiscoveryService();
			break;
		case ENDPOINT_SERVICE:
			module = this.peergroup.getEndpointService();
			break;
		case MEMBERSHIP_SERVICE:
			module = this.peergroup.getMembershipService();
			break;
		case PEERINFO_SERVICE:
			module = this.peergroup.getPeerInfoService();
			break;
		case PIPE_SERVICE:
			module = this.peergroup.getPipeService();
			break;
		case RENDEZVOUS_SERVICE:
			module = this.peergroup.getRendezVousService();
			break;
		case RESOLVER_SERVICE:
			module = this.peergroup.getResolverService();
			break;
		}
		return new Jp2pService( module );
	}

	@Override
	public IJp2pPeerGroupID getPeerGroupID() {
		return new Jp2pPeerGroupID( this.peergroup.getPeerGroupID() );
	}

	@Override
	public IJp2pPeerID getPeerID() {
		return new Jp2pPeerID( this.peergroup.getPeerID() );
	}

	@Override
	public String getPeerGroupName() {
		return this.peergroup.getPeerGroupName();
	}

	@Override
	public String getPeerName() {
		return this.peergroup.getPeerName();
	}

	@Override
	public IJp2pConfigParams getConfigAdvertisement() {
		return new Jp2pConfigParams( this.peergroup.getConfigAdvertisement() );
	}

	@Override
	public IJp2pModuleImplAdvertisement getAllPurposePeerGroupImplAdvertisement() throws Exception {
		return new Jp2pModuleImplAdvertisement( this.peergroup.getAllPurposePeerGroupImplAdvertisement() );
	}

	@Override
	public IJp2pPeerGroup getParentGroup() {
		return new Jp2pPeerGroup( this.peergroup.getParentGroup() );
	}

	@Override
	public URI getStoreHome() {
		return this.peergroup.getStoreHome();
	}
	
	
}