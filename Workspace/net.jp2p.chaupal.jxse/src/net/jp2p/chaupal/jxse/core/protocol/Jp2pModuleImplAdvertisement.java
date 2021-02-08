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

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import net.jp2p.chaupal.jxse.advertisement.Jp2pAdvertisement;
import net.jp2p.chaupal.jxse.utils.DocumentUtils;
import net.jp2p.chaupal.protocol.IJp2pModuleImplAdvertisement;
import net.jxta.protocol.ModuleImplAdvertisement;

public class Jp2pModuleImplAdvertisement extends Jp2pAdvertisement<ModuleImplAdvertisement> implements IJp2pModuleImplAdvertisement {

	public Jp2pModuleImplAdvertisement(ModuleImplAdvertisement advertisement) {
		super(advertisement);
	}

	@Override
	public Document getCompat() {
		try {
			return DocumentUtils.createDomDocument( getAdvertisement().getCompat());
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String getCode() {
		return getAdvertisement().getCode();
	}

	@Override
	public void setCode(String code) {
		getAdvertisement().setCode(code);
	}

	@Override
	public String getUri() {
		return getAdvertisement().getUri();
	}

	@Override
	public void setUri(String uri) {
		getAdvertisement().setUri(uri);
	}

	@Override
	public String getProvider() {
		return getAdvertisement().getProvider();
	}

	@Override
	public void setProvider(String provider) {
		getAdvertisement().setProvider(provider);
	}

	@Override
	public Document getParam() {
		try {
			return DocumentUtils.createDomDocument(getAdvertisement().getParam());
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void setParam(Element param) {
		getAdvertisement().setParam( DocumentUtils.convertToJxse(param ));
	}

}
