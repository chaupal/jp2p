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
 * @See: JXTA v2.0 Protocols Specification, Chapter 1
 * @author keesp
 * @Organisation: chaupal.org 
 * 
 * Copyright 2020: Apache 2.0 License
 *
*/
package net.jp2p.chaupal.jxse.core.peergroup;

import net.jp2p.chaupal.jxse.core.id.AbstractJp2pPeerGroupID;
import net.jp2p.chaupal.peergroup.IJp2pPeerGroupID;
import net.jp2p.container.utils.StringStyler;

public class PeergroupIDFactory {

	public enum PeergroupIDs{
		WORLD_PEERGROUP,
		DEFAULT_NET_PEERGROUP,
		PEERGROUP;

		@Override
		public String toString() {
			return StringStyler.prettyString( name());
		}
	}
	
	
	public static IJp2pPeerGroupID createPeerGroupID( PeergroupIDs id ) {
		IJp2pPeerGroupID pid = null;
		switch( id ) {
		case WORLD_PEERGROUP:
			pid = AbstractJp2pPeerGroupID.worldPeerGroupID;
			break;
		case DEFAULT_NET_PEERGROUP:
			pid = AbstractJp2pPeerGroupID.defaultNetPeerGroupID;
			break;
		default:
			break;
		}
		return pid;
	}
}
