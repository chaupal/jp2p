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
package net.jp2p.chaupal.peergroup;


public interface IJp2pGlobalRegistry {

	/**
	 * Registers a new instance.
	 *
	 * @param gid the ID of the group of which an instance is being registered.
	 * @param pg  the group instance being registered.
	 * @return false if the instance could not be registered because there
	 *         was already such an instance registered.
	 */
	boolean registerInstance(IJp2pPeerGroupID gid, IJp2pPeerGroup pg);

	/**
	 * Unregisters a group instance (normally because the group is being
	 * stopped).
	 *
	 * @param gid the ID of the group of which an instance is unregistered.
	 * @param pg  the group instance itself (serves as a credential).
	 * @return false if the group could not be unregistered because no such
	 *         registration (exact ID, exact object) was not found.
	 */
	boolean unRegisterInstance(IJp2pPeerGroupID gid, IJp2pPeerGroup pg);

	/**
	 * Returns a running instance of the peergroup with given ID if any
	 * exists. The instance should be {@link PeerGroup#unref()}ed when it is
	 * no longer needed.
	 *
	 * @param gid the id of the group of which an instance is wanted.
	 * @return the group, or {@code null} if no instance exists.
	 */
	IJp2pPeerGroup lookupInstance(IJp2pPeerGroupID gid);

	/**
	 * Returns {@code true} if there is a registered peergroup of the
	 * specified ID.
	 *
	 * @param gid the id of the group of which an instance is wanted.
	 * @return {@code} true if the peergroup is currently registered
	 *         otherwise false;
	 */
	boolean registeredInstance(IJp2pPeerGroupID gid);

}