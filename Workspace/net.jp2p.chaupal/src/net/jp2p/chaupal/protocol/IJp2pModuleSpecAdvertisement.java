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
package net.jp2p.chaupal.protocol;

import org.w3c.dom.Element;

import net.jp2p.chaupal.peergroup.IJp2pModuleSpecID;

public interface IJp2pModuleSpecAdvertisement extends IJp2pModuleAdvertisement {

	/**
	 * sets the name of the module spec
	 *
	 * @param name name of the module spec to be set
	 **/
	void setName(String name);

	/**
	 * Returns the creator of the module spec, in case someone cares.
	 *
	 * @return String the creator.
	 **/
	String getCreator();

	/**
	 * Sets the creator of this module spec.
	 * Note: the usefulness of this is unclear.
	 *
	 * @param creator name of the creator of the module
	 **/
	void setCreator(String creator);

	/**
	 * returns the uri. This uri normally points at the actual specification
	 * that this advertises.
	 *
	 * @return String uri
	 **/
	String getSpecURI();

	/**
	 * sets the uri
	 *
	 * @param uri string uri
	 **/
	void setSpecURI(String uri);

	/**
	 * returns the specification version number
	 *
	 * @return String version number
	 **/
	String getVersion();

	/**
	 * sets the version of the module
	 *
	 * @param version  version number
	 **/
	void setVersion(String version);

	/**
	 * sets the param element.
	 *
	 * @param param Element of an unspecified content.
	 **/
	void setParam(Element param);

	/**
	 * returns the specID of a proxy module.
	 *
	 * @return ModuleSpecID the spec id
	 **/
	IJp2pModuleSpecID getProxySpecID();

	/**
	 * sets a proxy module specID
	 *
	 * @param proxySpecID The spec id
	 **/
	void setProxySpecID(IJp2pModuleSpecID proxySpecID);

	/**
	 * returns the specID of an authenticator module.
	 *
	 * @return ModuleSpecID the spec id
	 **/
	IJp2pModuleSpecID getAuthSpecID();

	/**
	 * sets an authenticator module specID
	 *
	 * @param authSpecID The spec id
	 **/
	void setAuthSpecID(IJp2pModuleSpecID authSpecID);

}