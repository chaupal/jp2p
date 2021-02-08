/**
 * The JXTA protocols often need to refer to peers, peer groups, pipes and other JXTA resources. These references are presented in
 * the protocols as JXTA IDs. JXTA IDs are a means for uniquely identifying specific peer groups, peers, pipes, codat and service
 * instances. JXTA IDs provide unambiguous references to the various JXTA entities. There are six types of JXTA entities which
 * have JXTA ID types defined: peergroups, peers, pipes, codats, module classes and module specifications. Additional JXTA ID
 * types may be defined in the future.
 * JXTA IDs are normally presented as URNs. URNs are a form of URI that ‘... are intended to serve as persistent, location independent,
 * resource identifiers. Like other forms of URI, JXTA IDs are presented as text. See IETF RFC 2141 RFC2141 for
 * more information on URNs.
 * 
 * @See: JXTA v2.0 Protocols Specification, Chapter 1
 * @author keesp
 * 
 * Copyright 2020: Apache 2.0 License
 *
*/
package net.jp2p.chaupal.id;

import java.net.URI;

/**
 * The NullID is often used as a placeholder in fields which are uninitialized.
 */
public final class NullID implements IJp2pID {
	private static final long serialVersionUID = 0L;

	final static String JXTAFormat = "jxta";

    final static String UNIQUEVALUE = "Null";

    /**
     *  NullID is not intended to be constructed. You should use the
     *  {@link #nullID} constant instead.
     */
    private NullID() {}
	
    /**
     *  {@inheritDoc}
     */
    @Override
    public String getIDFormat() {
        return JXTAFormat;
    }

    /**
     *  {@inheritDoc}
     */
    @Override
    public Object getUniqueValue() {
        return getIDFormat() + "-" + UNIQUEVALUE;
    }

	@Override
	public URI toURI() {
        return URI.create(URIEncodingName + ":" + URNNamespace + ":" + getUniqueValue());
	}
}

