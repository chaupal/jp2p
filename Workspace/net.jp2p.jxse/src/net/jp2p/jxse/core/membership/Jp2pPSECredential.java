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
package net.jp2p.jxse.core.membership;

import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.cert.X509Certificate;

import javax.ws.rs.core.MediaType;

import org.w3c.dom.Document;

import net.jp2p.chaupal.exception.Jp2pPeerGroupException;
import net.jp2p.chaupal.id.IJp2pID;
import net.jp2p.chaupal.membership.pse.IPSECredential;
import net.jp2p.chaupal.module.IJp2pService;
import net.jp2p.jxse.core.id.Jp2pID;
import net.jp2p.jxse.core.module.Jp2pService;
import net.jp2p.jxse.utils.DocumentUtils;
import net.jxta.exception.PeerGroupException;
import net.jxta.id.ID;
import net.jxta.impl.membership.pse.PSECredential;

public class Jp2pPSECredential implements IPSECredential {

	private PSECredential pse;
	
	public Jp2pPSECredential( PSECredential pse) {
		this.pse = pse;
	}

	public PSECredential getPSECredential() {
		return pse;
	}
	
	@Override
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		pse.addPropertyChangeListener(listener);
	}

	@Override
	public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
		pse.addPropertyChangeListener(propertyName, listener);
	}

	@Override
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		pse.removePropertyChangeListener(listener);
	}

	@Override
	public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
		pse.removePropertyChangeListener(propertyName, listener);
	}

	@Override
	public IJp2pID getPeerGroupID() {
		return new Jp2pID<ID>( pse.getPeerGroupID() );
	}

	@Override
	public IJp2pID getPeerID() {
		return new Jp2pID<ID>( pse.getPeerID() );
	}

	@Override
	public boolean isExpired() {
		return pse.isExpired();
	}

	@Override
	public boolean isValid() {
		return pse.isValid();
	}

	@Override
	public Object getSubject() {
		return pse.getSubject();
	}

	@Override
	public IJp2pService getSourceService() {
		return new Jp2pService( pse.getSourceService() );
	}

	@Override
	public Document getDocument(MediaType encodeAs) throws Exception {
		return DocumentUtils.createDomDocument( pse.getDocument( DocumentUtils.convert(encodeAs)));
	}

	@Override
	public X509Certificate getCertificate() {
		return pse.getCertificate();
	}

	@Override
	public X509Certificate[] getCertificateChain() {
		return pse.getCertificateChain();
	}

	@Override
	public PrivateKey getPrivateKey() {
		return pse.getPrivateKey();
	}

	@Override
	public IJp2pID getKeyID() {
		return new Jp2pID<ID>( this.pse.getKeyID() );
	}

	@Override
	public Signature getSigner(String algorithm) throws NoSuchAlgorithmException {
		return pse.getSigner(algorithm);
	}

	@Override
	public Signature getSignatureVerifier(String algorithm) throws NoSuchAlgorithmException {
		return this.pse.getSignatureVerifier(algorithm);
	}

	@SuppressWarnings("unchecked")
	@Override
	public X509Certificate[] generateServiceCertificate(IJp2pID assignedID)
			throws IOException, KeyStoreException, InvalidKeyException, SignatureException {
		Jp2pID<ID> id = (Jp2pID<ID>) assignedID;
		return this.pse.generateServiceCertificate(id.getID() );
	}

	@SuppressWarnings("unchecked")
	@Override
	public IPSECredential getServiceCredential(IJp2pID assignedID)
			throws IOException, Jp2pPeerGroupException, InvalidKeyException, SignatureException {
		Jp2pID<ID> id = (Jp2pID<ID>) assignedID;
		try {
			return new Jp2pPSECredential( this.pse.getServiceCredential(id.getID() ));
		} catch (PeerGroupException e) {
			throw new Jp2pPeerGroupException(e);
		}
	}
}
