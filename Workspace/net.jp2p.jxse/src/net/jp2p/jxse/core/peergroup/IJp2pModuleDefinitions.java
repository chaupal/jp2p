/**
 * The jp2p protocols often need to refer to peers, peer groups, pipes and other jp2p resources. These references are presented in
 * the protocols as jp2p IDs. jp2p IDs are a means for uniquely identifying specific peer groups, peers, pipes, codat and service
 * instances. jp2p IDs provide unambiguous references to the various jp2p entities. There are six types of jp2p entities which
 * have jp2p ID types defined: peergroups, peers, pipes, codats, module classes and module specifications. Additional jp2p ID
 * types may be defined in the future.
 * jp2p IDs are normally presented as URNs. URNs are a form of URI that ‘... are intended to serve as persistent, locationindependent,
 * resource identifiers’. Like other forms of URI, jp2p IDs are presented as text. See IETF RFC 2141 RFC2141 for
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
 * @See: jp2p v2.0 Protocols Specification, Chapter 1
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
package net.jp2p.jxse.core.peergroup;

import net.jxta.peergroup.IModuleDefinitions;

/**
 * TODO: keesp: jp2pLoader removed
 * 
 * Peer groups are formed as a collection of peers that have agreed upon a
 * common set of services. Each peer group is assigned a unique peer group ID
 * and a peer group advertisement. The peer group advertisement contains a
 * ModuleSpecID which refers to a module specification for this peer group.
 * <p/>
 * The peer group specification mandates each of the group services (membership,
 * discovery, resolver, etc). Implementations of that specification are
 * described by ModuleImplAdvertisements which are identified by the group's
 * ModuleSpecID. Implementations are responsible for providing the services mandated
 * by the specification.
 * <p/>
 * The java reference implementation achieves this by loading additional Modules
 * which ModuleSpecIDs are listed by the group implementation advertisement.
 * <p/>
 * In order to fully participate in a group, a peer may need to authenticate
 * with the group using the peer group membership service.
 *
 * @see net.jp2p.jxse.core.id.AbstractJp2pPeerGroupID.core.peergroup.peergroup.PeerGroupID
 * @see net.jp2p.service.Service
 * @see net.jp2p.peergroup.PeerGroupFactory
 * @see net.jp2p.protocol.PeerGroupAdvertisement
 * @see net.jp2p.protocol.ModuleImplAdvertisement
 * @see net.jp2p.peergroup.core.ModuleSpecID
 * @see net.jp2p.peergroup.core.ModuleClassID
 */

public interface IJp2pModuleDefinitions extends IModuleDefinitions{

    public enum DefaultJp2pModules{
    	PEERGROUP,
    	RESOLVER,
    	DISCOVERY,
    	PIPE,
    	MEMBERSHIP,
    	RENDEZVOUS,
    	ENDPOINT,
    	TCP,
    	MULTICAST,
    	HTTP,
    	ROUTER,
    	APPLICATION,
    	TLS,
    	RELAY,
    	ACCESS,
    	CONTENT;
    	
    	/**
    	 * Get the corresponding module class ids
    	 * @param module
    	 * @return
    	 */
    	public static Jp2pModuleClassID getModuleClassID( DefaultJp2pModules module ) {
    		return new Jp2pModuleClassID( DefaultModules.getModuleClassID(DefaultModules.valueOf( module.name())));
    	}

    	/**
    	 * Get the corresponding module class ids
    	 * @param module
    	 * @return
    	 */
    	public static Jp2pModuleClassID getJp2pModuleClassID( DefaultModules module ) {
    		return new Jp2pModuleClassID( DefaultModules.getModuleClassID( module ));
    	}

    	/**
    	 * Get the corresponding module class ids
    	 * @param module
    	 * @return
    	 */
    	public static Jp2pModuleSpecID getJp2pModuleSpecID( DefaultModules module ) {
    		return getJp2pModuleSpecID( module );
    	}
    }
}
