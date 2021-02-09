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
package net.jp2p.jxse.core.protocol;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import net.jp2p.chaupal.document.IJp2pAdvertisement;
import net.jp2p.chaupal.id.IJp2pID;
import net.jp2p.chaupal.protocol.IJp2pConfigParams;
import net.jp2p.jxse.advertisement.Jp2pAdvertisement;
import net.jp2p.jxse.core.id.Jp2pID;
import net.jp2p.jxse.core.id.Jp2pPeerGroupID;
import net.jp2p.jxse.utils.DocumentUtils;
import net.jxta.document.Advertisement;
import net.jxta.id.ID;
import net.jxta.protocol.ConfigParams;

public class Jp2pConfigParams extends Jp2pExtendableAdvertisement<ConfigParams> implements IJp2pConfigParams {

	public Jp2pConfigParams(ConfigParams advertisement) {
		super(advertisement);
	}

	@Override
	public int getModCount() {
		return getAdvertisement().getModCount();
	}

	@Override
	public IJp2pID getID() {
		return new Jp2pID<ID>( getAdvertisement().getID());
	}
	@Override
	public String getPrivateKey() {
		return getAdvertisement().getPrivateKey();
	}

	@Override
	public void setPrivateKey(String privateKey) {
		getAdvertisement().setPrivateKey(privateKey);
	}

	@Override
	public String getAuthenticationType() {
		return getAdvertisement().getAuthenticationType();
	}

	@Override
	public void setAuthenticationType(String authenticationType) {
		this.getAdvertisement().setAuthenticationType(authenticationType);
	}

	@Override
	public boolean addDocumentElements(Document adv) {
		try {
			return getAdvertisement().addDocumentElements( DocumentUtils.createJxseDocument(adv));
		} catch (ParserConfigurationException | IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setSvcConfigAdvertisement(IJp2pID key, IJp2pAdvertisement adv) {
		Jp2pPeerGroupID id = (Jp2pPeerGroupID) key;
		Jp2pAdvertisement<Advertisement> jadv = (Jp2pAdvertisement<Advertisement>)adv;
		getAdvertisement().setSvcConfigAdvertisement( id.getID(), jadv.getAdvertisement());
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setSvcConfigAdvertisement(IJp2pID key, IJp2pAdvertisement adv, boolean enabled) {
		Jp2pPeerGroupID id = (Jp2pPeerGroupID) key;
		Jp2pAdvertisement<Advertisement> jadv = (Jp2pAdvertisement<Advertisement>)adv;
		getAdvertisement().setSvcConfigAdvertisement( id.getID(), jadv.getAdvertisement(), enabled );
	}

	@Override
	public boolean isSvcEnabled(IJp2pID key) {
		Jp2pPeerGroupID id = (Jp2pPeerGroupID) key;
		return getAdvertisement().isSvcEnabled(id.getID());
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public IJp2pAdvertisement getSvcConfigAdvertisement(IJp2pID key) {
		Jp2pPeerGroupID id = (Jp2pPeerGroupID) key;
		return new Jp2pAdvertisement( getAdvertisement().getSvcConfigAdvertisement(id.getID()));
	}

	@Override
	public void removeSvcConfigAdvertisement(IJp2pID key) {
		Jp2pPeerGroupID id = (Jp2pPeerGroupID) key;
		getAdvertisement().removeSvcConfigAdvertisement(id.getID());
	}

	@Override
	public Jp2pConfigParams clone() {
		try {
			return new Jp2pConfigParams( (ConfigParams) super.clone() );
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null;
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
