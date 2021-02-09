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
package net.jp2p.jxse.core.module;

import net.jp2p.chaupal.document.IJp2pAdvertisement;
import net.jp2p.chaupal.exception.Jp2pPeerGroupException;
import net.jp2p.chaupal.id.IJp2pID;
import net.jp2p.chaupal.module.IJp2pModule;
import net.jp2p.chaupal.peergroup.IJp2pPeerGroup;
import net.jp2p.jxse.advertisement.Jp2pAdvertisement;
import net.jp2p.jxse.core.id.Jp2pID;
import net.jp2p.jxse.core.platform.Jp2pPeerGroup;
import net.jxta.document.Advertisement;
import net.jxta.exception.PeerGroupException;
import net.jxta.id.ID;
import net.jxta.peergroup.core.Module;

public class Jp2pModule<M extends Module> implements IJp2pModule {

	private M module;
	
	public Jp2pModule( M module ) {
		this.module = module;
	}

	public M getModule() {
		return module;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void init(IJp2pPeerGroup group, IJp2pID assignedID, IJp2pAdvertisement implAdv)
			throws Jp2pPeerGroupException {
		Jp2pPeerGroup pg = (Jp2pPeerGroup) group;
		Jp2pID<ID> id = (Jp2pID<ID>) assignedID;
		Jp2pAdvertisement<Advertisement> adv = (Jp2pAdvertisement<Advertisement>) implAdv;
		try {
			module.init(pg.getPeergroup(), id.getID(), adv.getAdvertisement());
		} catch (PeerGroupException e) {
			throw new Jp2pPeerGroupException( e );
		}
	}

	@Override
	public int startApp(String[] args) {
		return module.startApp(args);
	}

	@Override
	public void stopApp() {
		module.stopApp();
	}

}
