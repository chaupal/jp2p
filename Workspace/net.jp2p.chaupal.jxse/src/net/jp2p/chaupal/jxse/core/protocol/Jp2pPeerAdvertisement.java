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
package net.jp2p.chaupal.jxse.core.protocol;

import java.util.Hashtable;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import net.jp2p.chaupal.id.IJp2pID;
import net.jp2p.chaupal.jxse.advertisement.Jp2pAdvertisement;
import net.jp2p.chaupal.jxse.core.id.Jp2pID;
import net.jp2p.chaupal.jxse.core.id.Jp2pPeerGroupID;
import net.jp2p.chaupal.jxse.core.peer.Jp2pPeerID;
import net.jp2p.chaupal.jxse.utils.DocumentUtils;
import net.jp2p.chaupal.peer.IJp2pPeerID;
import net.jp2p.chaupal.peergroup.IJp2pPeerGroupID;
import net.jp2p.chaupal.protocol.IJp2pPeerAdvertisement;
import net.jxta.id.ID;
import net.jxta.protocol.PeerAdvertisement;

public class Jp2pPeerAdvertisement extends Jp2pAdvertisement<PeerAdvertisement> implements IJp2pPeerAdvertisement {

	public Jp2pPeerAdvertisement(PeerAdvertisement advertisement) {
		super(advertisement);
	}

	@Override
	public String getBaseAdvType() {
		return getAdvertisement().getBaseAdvType();
	}

	@Override
	public IJp2pPeerAdvertisement clone() {
		return new Jp2pPeerAdvertisement( getAdvertisement().clone());
	}

	@Override
	public String getName() {
		return getAdvertisement().getName();
	}

	@Override
	public void setName(String name) {
		this.getAdvertisement().setName(name);
	}

	@Override
	public IJp2pPeerGroupID getPeerGroupID() {
		return new Jp2pPeerGroupID( getAdvertisement().getPeerGroupID());
	}

	@Override
	public void setPeerGroupID(IJp2pPeerGroupID gid) {
		Jp2pPeerGroupID pid = (Jp2pPeerGroupID) gid;
		getAdvertisement().setPeerGroupID(pid.getID());
	}

	@Override
	public IJp2pID getID() {
		return new Jp2pID<ID>( getAdvertisement().getID());
	}

	@Override
	public String getDescription() {
		return getAdvertisement().getDescription();
	}

	@Override
	public void setDescription(String description) {
		getAdvertisement().setDescription(description);
	}

	@Override
	public Document getDesc() {
		try {
			return DocumentUtils.createDomDocument( getAdvertisement().getDesc());
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void setDesc(Element desc) {
		getAdvertisement().setDesc( DocumentUtils.convertToJxse(desc));
	}

	@Override
	public void setServiceParams(Hashtable<IJp2pID, Element> params) {
		getAdvertisement().setServiceParams( (Hashtable<ID, ? extends net.jxta.document.Element<?>>) DocumentUtils.convertToJxse(params));
	}

	@Override
	public Hashtable<IJp2pID, Document> getServiceParams() {
		return DocumentUtils.convertDocToDom(  getAdvertisement().getServiceParams());
	}

	@SuppressWarnings("unchecked")
	@Override
	public void putServiceParam(IJp2pID key, Element param) {
		Jp2pID<ID> id = (Jp2pID<ID>) key;
		getAdvertisement().putServiceParam(id.getID(), null);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Document getServiceParam(IJp2pID key) {
		Jp2pID<ID> id = (Jp2pID<ID>) key;
		try {
			return DocumentUtils.createDomDocument( getAdvertisement().getServiceParam(id.getID()));
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Document removeServiceParam(IJp2pID key) {
		Jp2pID<ID> id = (Jp2pID<ID>) key;
		try {
			return DocumentUtils.createDomDocument( getAdvertisement().removeServiceParam(id.getID()));
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public int getModCount() {
		return getAdvertisement().getModCount();
	}

	@Override
	public IJp2pPeerID getPeerID() {
		return new Jp2pPeerID( getAdvertisement().getPeerID());
	}

	@Override
	public void setPeerID(IJp2pPeerID pid) {
		Jp2pPeerID id = (Jp2pPeerID) pid;
		getAdvertisement().setPeerID( id.getID() );
	}

}
