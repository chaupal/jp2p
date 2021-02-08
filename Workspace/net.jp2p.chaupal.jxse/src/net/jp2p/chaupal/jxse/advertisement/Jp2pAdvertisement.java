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
package net.jp2p.chaupal.jxse.advertisement;

import javax.ws.rs.core.MediaType;
import javax.xml.crypto.dsig.XMLSignature;

import org.w3c.dom.Document;

import net.jp2p.chaupal.document.IJp2pAdvertisement;
import net.jp2p.chaupal.id.IJp2pID;
import net.jp2p.chaupal.jxse.core.id.Jp2pID;
import net.jp2p.chaupal.jxse.core.membership.Jp2pPSECredential;
import net.jp2p.chaupal.membership.pse.IPSECredential;
import net.jxta.document.Advertisement;
import net.jxta.id.ID;

public class Jp2pAdvertisement<A extends Advertisement> implements IJp2pAdvertisement {

	private A advertisement ;
	
	public Jp2pAdvertisement( A advertisement) {
		this.advertisement = advertisement;
	}

	public A getAdvertisement() {
		return advertisement;
	}
	
	@Override
	public String getAdvType() {
		return advertisement.getAdvType();
	}

	@Override
	public IJp2pID getID() {
		return new Jp2pID<ID>( advertisement.getID());
	}

	@Override
	public String[] getIndexFields() {
		return this.advertisement.getIndexFields();
	}

	@Override
	public Document getSignedDocument() {
		return null;//this.advertisement.getSignedDocument();
	}

	@Override
	public XMLSignature getSignature() {
		return null;//this.advertisement.getSignature
	}

	@Override
	public boolean isAuthenticated() {
		return this.advertisement.isAuthenticated();
	}

	@Override
	public boolean isMember() {
		return this.advertisement.isMember();
	}

	@Override
	public boolean isCorrectMembershipKey() {
		return this.isCorrectMembershipKey();
	}

	@Override
	public boolean sign(IPSECredential pseCredential, boolean includePublicKey, boolean includePeerID) {
		Jp2pPSECredential credential = (Jp2pPSECredential) pseCredential;
		return this.advertisement.sign(credential.getPSECredential(), includePublicKey, includePeerID);
	}

	@Override
	public boolean verify(IPSECredential pseCredential, boolean verifyKeyWithKeystore) {
		Jp2pPSECredential credential = (Jp2pPSECredential) pseCredential;
		return this.advertisement.verify(credential.getPSECredential(), verifyKeyWithKeystore);
	}

	@Override
	public Document getDocument(MediaType asMimeType) {
		return null;//this.advertisement.getDocument();
	}

}
