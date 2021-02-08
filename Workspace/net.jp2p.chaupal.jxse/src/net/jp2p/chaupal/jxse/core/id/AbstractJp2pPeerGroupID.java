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
package net.jp2p.chaupal.jxse.core.id;

import java.net.URI;

import net.jp2p.chaupal.peergroup.IJp2pPeerGroupID;
import net.jxta.id.ID;
import net.jxta.peergroup.PeerGroupID;

/**
 *  This class implements a PeerGroup ID. Each peer group is assigned a
 *  unique id.
 *
 *  @see         net.jxta.id.ID
 *  @see         net.jxta.id.IDFactory
 *  @see         net.jxta.peer.PeerID
 *
 * @since JXTA 1.0
 */
public abstract class AbstractJp2pPeerGroupID extends ID implements IJp2pPeerGroupID {
	private static final long serialVersionUID = -6922250953299272963L;

	/**
	 * The well known Unique Identifier of the world peergroup.
	 * This is a singleton within the scope of a VM.
	 */
	public static  IJp2pPeerGroupID worldPeerGroupID = (new WorldPeerGroupID( PeerGroupID.worldPeerGroupID )).intern();
	/**
	 * The well known Unique Identifier of the net peergroup.
	 * This is a singleton within the scope of this VM.
	 */
	public static IJp2pPeerGroupID defaultNetPeerGroupID = (new NetPeerGroupID( PeerGroupID.defaultNetPeerGroupID )).intern();

	private PeerGroupID peergroupID;

	public AbstractJp2pPeerGroupID( PeerGroupID peergroupID ) {
		this.peergroupID = peergroupID;
	}

	public PeerGroupID getID() {
		return peergroupID;
	}

	/**
	 * Creates an ID by parsing the given URI.
	 *
	 * <p>This convenience factory method works as if by invoking the
	 * {@link net.jxta.id.IDFactory#fromURI(URI)} method; any 
	 * {@link java.net.URISyntaxException} thrown is caught and wrapped in a 
	 * new {@link IllegalArgumentException} object, which is then thrown.
	 *
	 * <p> This method is provided for use in situations where it is known that
	 * the given string is a legal ID, for example for ID constants declared
	 * within in a program, and so it would be considered a programming error
	 * for the URI not to parse as such.  The {@link net.jxta.id.IDFactory}, 
	 * which throws {@link java.net.URISyntaxException} directly, should be used 
	 * situations where a ID is being constructed from user input or from some 
	 * other source that may be prone to errors. 
	 *
	 * @param  fromURI   The URI to be parsed into an ID
	 * @return The new ID
	 *
	 * @throws  NullPointerException If {@code fromURI} is {@code null}.
	 * @throws  IllegalArgumentException If the given URI is not a valid ID.
	 */
	public static AbstractJp2pPeerGroupID create(URI fromURI) {
		return (AbstractJp2pPeerGroupID) ID.create(fromURI);
	}

	/**
	 *  {@inheritDoc}
	 */
	@Override
	public AbstractJp2pPeerGroupID intern() {
		return (AbstractJp2pPeerGroupID) super.intern();
	}

	/**
	 *  Returns the parent peer group id of this peer group id, if any.
	 *
	 *  @return the id of the parent peergroup or null if this group has no
	 *  parent group.
	 */
	@Override
	public abstract IJp2pPeerGroupID getParentPeerGroupID();
}

final class WorldPeerGroupID extends AbstractJp2pPeerGroupID {
	private static final long serialVersionUID = -1737823370585794371L;

	/**
	 * The name associated with this ID Format.
	 */
	final static String JXTAFormat = "jxta";

	private static final String UNIQUEVALUE = "WorldGroup";

	/**
	 *  WorldPeerGroupID is not intended to be constructed. You should use the 
	 *  {@link AbstractJp2pPeerGroupID#worldPeerGroupID} constant instead.
	 */
	WorldPeerGroupID( PeerGroupID peergroupID ) {
		super( peergroupID );
	}

	/**
	 *  {@inheritDoc}
	 */
	@Override
	public boolean equals(Object target) {
		return (this == target); // worldPeerGroupID is only itself.
	}

	/**
	 * deserialization has to point back to the singleton in this VM
	 */
	private Object readResolve() {
		return worldPeerGroupID;
	}

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

	/**
	 *  {@inheritDoc}
	 */
	@Override
	public IJp2pPeerGroupID getParentPeerGroupID() {
		return null;
	}
}

final class NetPeerGroupID extends AbstractJp2pPeerGroupID {
	private static final long serialVersionUID = 5888888378879757553L;

	/**
	 * The name associated with this ID Format.
	 */
	final static String JXTAFormat = "jxta";

	private static final String UNIQUEVALUE = "NetGroup";

	/**
	 *  NetPeerGroupID is not intended to be constructed. You should use the 
	 *  {@link AbstractJp2pPeerGroupID#defaultNetPeerGroupID} constant instead.
	 */
	NetPeerGroupID( PeerGroupID peergroupID ) {
		super( peergroupID );
	}

	/**
	 *  {@inheritDoc}
	 */
	@Override
	public boolean equals(Object target) {
		return (this == target); // netPeerGroupID is only itself.
	}

	/**
	 * deserialization has to point back to the singleton in this VM
	 */
	private Object readResolve() {
		return defaultNetPeerGroupID;
	}

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

	/**
	 *  {@inheritDoc}
	 */
	@Override
	public IJp2pPeerGroupID getParentPeerGroupID() {
		return worldPeerGroupID;
	}
}
