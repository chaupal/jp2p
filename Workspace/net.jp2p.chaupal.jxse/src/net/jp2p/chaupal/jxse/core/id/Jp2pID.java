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
 * 
 * Copyright 2020: Apache 2.0 License
 *
*/
package net.jp2p.chaupal.jxse.core.id;

import java.net.URI;

import net.jp2p.chaupal.id.IJp2pID;
import net.jxta.id.ID;

public class Jp2pID<I extends ID> implements IJp2pID {
	private static final long serialVersionUID = 5433521342152257221L;

	private I id;
	
	public Jp2pID( I id ) {
		this.id =id;
	}

	public I getID() {
		return id;
	}
	
	@Override
	public String getIDFormat() {
		return id.getIDFormat();
	}

	@Override
	public Object getUniqueValue() {
		return id.getUniqueValue();
	}

	@Override
	public URI toURI() {
		return id.toURI();
	}
}
