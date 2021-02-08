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

import java.util.Hashtable;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import net.jp2p.chaupal.document.IJp2pAdvertisement;
import net.jp2p.chaupal.id.IJp2pID;
import net.jp2p.chaupal.peer.IJp2pPeerID;

public interface IJp2pModuleAdvertisement extends IJp2pAdvertisement{

	/**
	 * Returns the number of times this object has been modified since
	 * it was created. This permits to detection of local changes that require
	 * refreshing of other data which depends upon the peer advertisement.
	 *
	 * @return int the current modification count.
	 */

	int getModCount();

	/**
	 * {@inheritDoc}
	 */
	String getBaseAdvType();

	/**
	 * returns the name of the peer.
	 *
	 * @return String name of the peer.
	 */

	String getName();

	/**
	 * sets the name of the peer.
	 *
	 * @param name name of the peer.
	 */

	void setName(String name);

	/**
	 * Returns the id of the peer.
	 *
	 * @return PeerID the peer id
	 */

	IJp2pPeerID getPeerID();

	/**
	 * Sets the id of the peer.
	 *
	 * @param pid the id of this peer.
	 */

	void setPeerID( IJp2pPeerID pid);

	/**
	 * Returns the id of the peergroup this peer advertisement is for.
	 *
	 * @return PeerGroupID the peergroup id
	 */

	IJp2pID getModuleID();

	/**
	 * Returns the id of the peergroup this peer advertisement is for.
	 *
	 * @param gid The id of the peer.
	 */

	void setModuleID(IJp2pID gid);

	/**
	 * returns the description
	 *
	 * @return String the description
	 */
	String getDescription();

	/**
	 * sets the description
	 *
	 * @param description the description
	 * @since JXTA 1.0
	 */
	void setDescription(String description);

	/**
	 * returns the description
	 *
	 * @return the description
	 */
	Document getDesc();

	/**
	 * sets the description
	 *
	 * @param desc the description
	 */
	void setDesc(Element desc);

	/**
	 * sets the sets of parameters for all services. This method first makes a
	 * deep copy, in order to protect the active information from uncontrolled
	 * sharing. This quite an expensive operation. If only a few of the
	 * parameters need to be added, it is wise to use putServiceParam()
	 * instead.
	 *
	 * @param params The whole set of parameters.
	 */
	void setServiceParams(Hashtable<IJp2pID, ? extends Element> params);

	/**
	 * Returns the sets of parameters for all services.
	 *
	 * <p/>This method returns a deep copy, in order to protect the real
	 * information from uncontrolled sharing while keeping it shared as long as
	 * it is safe. This quite an expensive operation. If only a few parameters
	 * need to be accessed, it is wise to use getServiceParam() instead.
	 *
	 * @return all of the parameters.
	 */
	Hashtable<IJp2pID, Document> getServiceParams();

	/**
	 * Puts a service parameter in the service parameters table under the given
	 * key. The key is of a subclass of ID; usually a ModuleClassID. This
	 * method makes a deep copy of the given element into an independent
	 * document.
	 *
	 * @param key   The key.
	 * @param param The parameter, as an element. What is stored is a copy as a
	 *              standalone StructuredDocument which type is the element's name.
	 */
	void putServiceParam(IJp2pID key, Element param);

	/**
	 * Returns the parameter element that matches the given key from the
	 * service parameters table. The key is of a subclass of ID; usually a
	 * ModuleClassID.
	 *
	 * @param key The key.
	 * @return StructuredDocument The matching parameter document or null if
	 *         none matched. The document type id "Param".
	 */
	Document getServiceParam(IJp2pID key);

	/**
	 * Removes and returns the parameter element that matches the given key
	 * from the service parameters table. The key is of a subclass of ID;
	 * usually a ModuleClassID.
	 *
	 * @param key The key.
	 * @return Element the removed parameter element or null if not found.
	 *         This is actually a StructureDocument of type "Param".
	 */
	Document removeServiceParam(IJp2pID key);

}