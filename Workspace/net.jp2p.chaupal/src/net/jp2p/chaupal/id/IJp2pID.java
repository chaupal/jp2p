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
package net.jp2p.chaupal.id;

import java.net.URI;

public interface IJp2pID extends java.io.Serializable {

	/**
	 * This defines the URI scheme that we will be using to present JXTA IDs.
	 * JXTA IDs are encoded for presentation into URIs (see
	 * {@link <a href="http://www.ietf.org/rfc/rfc2396.txt">IETF RFC 2396 Uniform Resource Identifiers (URI) : Generic Syntax</a>}
	 * ) as URNs (see
	 * {@link <a href="http://www.ietf.org/rfc/rfc2141.txt">IETF RFC 2141 Uniform Resource Names (URN) Syntax</a>}
	 * ).
	 */
	String URIEncodingName = "urn";
	/**
	 *  This defines the URN Namespace that we will be using to present JXTA IDs.
	 *  The namespace allows URN resolvers to determine which sub-resolver to use
	 *  to resolve URN references. All JXTA IDs are presented in this namespace.
	 */
	String URNNamespace = "jxta";

	/**
	 *  Returns a string identifier which indicates which ID format is
	 *  used by this ID instance.
	 *
	 *  @return	a string identifier which indicates which ID format is
	 *  used by this ID instance.
	 */
	String getIDFormat();

	/**
	 *  Returns an object containing the unique value of the ID. This object
	 *  must provide implementations of toString(), equals() and hashCode() that
	 *  are canonical and consistent from run-to-run given the same input values.
	 *  Beyond this nothing should be assumed about the nature of this object.
	 *  For some implementations the object returned may be <code>this</code>.
	 *
	 *  @return	Object which can provide canonical representations of the ID.
	 */
	Object getUniqueValue();

	/**
	 *  Returns a URI representation of the ID. {@link java.net.URI URIs} are
	 *  the preferred way of externalizing and presenting JXTA IDs. The
	 *  {@link net.jxta.id.IDFactory JXTA ID Factory} can be used to construct
	 *  ID Objects from URIs containing JXTA IDs.
	 *
	 *  @see net.jxta.id.IDFactory#fromURI( java.net.URI )
	 *
	 *  @return	URI Object containing the URI
	 */
	URI toURI();

}