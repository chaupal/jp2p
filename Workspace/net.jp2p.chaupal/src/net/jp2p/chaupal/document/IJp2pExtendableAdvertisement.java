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
package net.jp2p.chaupal.document;

import javax.ws.rs.core.MediaType;

import org.w3c.dom.Document;

public interface IJp2pExtendableAdvertisement extends IJp2pAdvertisement{

	/**
	 * Returns the base type of this advertisement hierarchy. Typically, only
	 * the most basic advertisement of a type will implement this method and
	 * declare it as <code>final</code>.
	 *
	 * @return String the base type of advertisements in this hierarchy.
	 */
	String getBaseAdvType();

	/**
	 * {@inheritDoc}
	 * <p/>
	 * We don't have any content to add, just build the document instance and 
	 * return it to implementations that actually do something with it.
	 */
	Document getDocument(MediaType encodeAs);
}