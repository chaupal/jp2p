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
 *******************************************************************************
 * Copyright (c) 2014-2021 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************
*/
package net.jp2p.chaupal.peergroup;

import java.io.IOException;
import java.net.URI;
import java.util.Iterator;

import org.w3c.dom.Element;

import net.jp2p.chaupal.document.IJp2pAdvertisement;
import net.jp2p.chaupal.exception.Jp2pPeerGroupException;
import net.jp2p.chaupal.exception.Jp2pProtocolNotSupportedException;
import net.jp2p.chaupal.exception.Jp2pServiceNotFoundException;
import net.jp2p.chaupal.id.IJp2pID;
import net.jp2p.chaupal.module.IJp2pModule;
import net.jp2p.chaupal.module.IJp2pService;
import net.jp2p.chaupal.peer.IJp2pPeerID;
import net.jp2p.chaupal.protocol.IJp2pConfigParams;
import net.jp2p.chaupal.protocol.IJp2pModuleImplAdvertisement;
import net.jp2p.chaupal.protocol.IJp2pPeerAdvertisement;
import net.jp2p.chaupal.protocol.IJp2pPeerGroupAdvertisement;
import net.jp2p.chaupal.utils.StringStyler;

public interface IJp2pPeerGroup {

	public enum PeerGroupServices{
		ACCESS_SERVICE,
		CONTENT_SERVICE,
		DISCOVERY_SERVICE,
		ENDPOINT_SERVICE,
		MEMBERSHIP_SERVICE,
		PEERINFO_SERVICE,
		PIPE_SERVICE,
		RENDEZVOUS_SERVICE,
		RESOLVER_SERVICE;

		@Override
		public String toString() {
			return StringStyler.prettyString( name());
		}	
	}
	
	/**
	 * Look for needed ModuleImplAdvertisement in this group.
	 */
	int Here = 0;
	/**
	 * Look for needed ModuleImplAdvertisement in the parent group of this group.
	 */
	int FromParent = 1;
	/**
	 * Look for needed ModuleImplAdvertisement in both this group and its parent.
	 */
	int Both = 2;
	/**
	 * Default life time for group advertisements in the publisher's cache.
	 * (a year)
	 */
	// without casting to long we lose precision

	long DEFAULT_LIFETIME = (long) 1000 * (long) 3600 * (long) 24 * 365L;
	/**
	 * Default expiration time for discovered group advertisements. (2 weeks)
	 */
	// without casting to long we lose precision

	long DEFAULT_EXPIRATION = (long) 1000 * (long) 3600 * (long) 24 * 14L;

	/**
	 * The global registry of Peer Group instances. Operations involving the
	 * instantiation or orderly shutdown of Peer Groups should synchronize upon
	 * this object.
	 * @return 
	 */

	IJp2pGlobalRegistry getGlobalRegistry();

	/**
	 * Returns the whether the group member is a Rendezvous peer for the group.
	 *
	 * @return boolean true if the peer is a rendezvous for the group.
	 */
	boolean isRendezvous();

	/**
	 * Return the PeerGroupAdvertisement for this group.
	 *
	 * @return PeerGroupAdvertisement this Group's advertisement.
	 */
	IJp2pPeerGroupAdvertisement getPeerGroupAdvertisement();

	/**
	 * Return the PeerAdvertisement of the local Peer within this Peer Group.
	 *
	 * @return the PeerAdvertisement of the local Peer within this Peer Group.
	 */
	IJp2pPeerAdvertisement getPeerAdvertisement();

	/**
	 * Lookup for a service by name.
	 *
	 * @param name the service identifier.
	 * @return Service, the Service registered by that name
	 * @throws Jp2pServiceNotFoundException could not find the service requested
	 */
	IJp2pService lookupService(IJp2pID name) throws Jp2pServiceNotFoundException;

	/**
	 * Lookup for a service by class ID and index in a map.
	 * <p/>
	 * More than one service in a group may be of a given ModuleClass.
	 * However each of them has a unique assigned ID which serves as the
	 * index in the map of services. In most cases, there is only one
	 * service of each given Module Class, and the ID of that Module Class
	 * is the assigned ID. Otherwise, the group may have a list of existing
	 * assigned ID per base class. This routine may be used to retrieve
	 * services of the given Module Class and index in that list.
	 * In the absence of a mapping, index 0 is still valid and
	 * corresponds to the service which assigned ID is exactly the
	 * given ID.
	 * Group objects with a map are normally wrappers tailored
	 * specially by the loader of a module (often the group itself) in order
	 * to provide a map appropriate for that module. Modules that do not use
	 * more than one service of a given base class normally never need to call
	 * this method; lookupService(ID) is equivalent to lookupService(ID, 0)
	 * and will transparently remap index 0 to whatever the group's
	 * structure defines as the default for the invoking service.
	 * <p/>
	 * Note: traditionally, the given ID is expected to be a base Module
	 * Class ID, and the assigned ID of a Module is a Class ID of the
	 * same base class with a role suffix to make it unique. If the given
	 * ID already contains a role suffix, there may exist an entry for
	 * it in the map anyway, if not (which is the expected use pattern),
	 * then only index 0 exists and the given ID is used whole and
	 * untranslated.
	 *
	 * @param name      the service identifier
	 * @param roleIndex the index in the list of assigned IDs that match
	 *                  that identifier.
	 * @return Service, the corresponding Service
	 * @throws Jp2pServiceNotFoundException Could not find the service requested.
	 * @since JXTA 2.3.1
	 */
	IJp2pService lookupService(IJp2pID name, int roleIndex) throws Jp2pServiceNotFoundException;

	/**
	 * Returns the map of the assigned IDs currently associated with the given
	 * ModuleClassID by this PeerGroup object. The IDs are returned in the order
	 * of their index in the map. So the first ID returned will be identical to
	 * what would be returned by the lookup method for the given ID and index 0.
	 *
	 * @param name The ModuleClassID for which the role map is desired.
	 * @return The ModuleClassIDs for all of the services which match the 
	 * specified base ModuleClassID or {@code null} if there are no services
	 * which match the specified ID. If there is no explicit such map, this 
	 * method will return a list containing only the given ID as this is the 
	 * default mapping. There is no guarantee that any of the returned IDs 
	 * correspond to an actually registered service.
	 * @since JXTA 2.3.1
	 */
	Iterator<IJp2pID> getRoleMap(IJp2pID name);

	/**
	 * Return {@code true} if the provided compatibility statement is compatible 
	 * with this peer group.
	 *
	 * @param compat A compatibility statement.
	 * @return {@code true} if the compatibility statement is compatible.
	 */
	boolean compatible(Element compat);

	/**
	 * Load a Module from a ModuleImplAdv.
	 * <p/>
	 * Compatibility is checked and load is attempted. If compatible and loaded
	 * successfully, the resulting Module is initialized and returned.
	 * In most cases {@link #loadModule(net.jxta.id.ID, Jp2pModuleSpecID, int)} 
	 * should be preferred, since unlike this method, it will try all
	 * compatible implementation advertisements until one works. The home group 
	 * of the new module (its' parent group if the new Module is a group) will 
	 * be this group.
	 *
	 * @param assignedID Id to be assigned to that module (usually its ClassID).
	 * @param impl       An implementation advertisement for that module.
	 * @return Module the module loaded and initialized.
	 * @throws jp2pProtocolNotSupportedException The implementation described by the
	 *                                       advertisement is incompatible with this peer. The module cannot be loaded.
	 * @throws PeerGroupException            The module could not be loaded or initialized
	 */
	IJp2pModule loadModule(IJp2pID assignedID, IJp2pAdvertisement impl) throws Jp2pProtocolNotSupportedException, Jp2pPeerGroupException;

	/**
	 * Load a module from a ModuleSpecID
	 * <p/>
	 * Advertisement is sought, compatibility is checked on all candidates
	 * and load is attempted. The first one that is compatible and loads
	 * successfully is initialized and returned.
	 *
	 * @param assignedID Id to be assigned to that module (usually its ClassID).
	 * @param specID     The specID of this module.
	 * @param where      May be one of: {@code Here}, {@code FromParent}, or
	 *                   {@code Both}, meaning that the implementation advertisement will be
	 *                   searched in this group, its parent or both. As a general guideline, the
	 *                   implementation advertisements of a group should be searched in its
	 *                   prospective parent (that is {@code Here}), the implementation
	 *                   advertisements of a group standard service should be searched in the same
	 *                   group than where this group's advertisement was found (that is,
	 *                   {@code FromParent}), while applications may be sought more freely
	 *                   ({@code Both}).
	 * @return Module the new module, or null if no usable implementation was
	 *         found.
	 */
	IJp2pModule loadModule(IJp2pID assignedID, IJp2pModuleSpecID specID, int where);

	/**
	 * Publish this group's Peer Group Advertisement. The Advertisement will be
	 * published using the parent peer group's Discovery service.
	 * <p/>
	 * Calling this method is only useful if the group is being created
	 * from scratch and the PeerGroup advertisement has not been
	 * created beforehand. In such a case, the group has never been named or
	 * described. Therefore this information has to be supplied here.
	 *
	 * @param name        The name of this group.
	 * @param description The description of this group.
	 * @throws IOException The publication could not be accomplished
	 *                     because of a network or storage failure.
	 */
	void publishGroup(String name, String description) throws IOException;

	/**
	 * Instantiate a peer group from the provided advertisement. This peer
	 * group will be the parent of the newly instantiated peer group.
	 * <p/>
	 * The pgAdv itself may be all new and unpublished. Therefore, the two
	 * typical uses of this routine are:
	 * <p/>
	 * <ul>
	 * <li>Creating an all new group with a new ID while using an existing
	 * and published implementation. (Possibly a new one published for
	 * that purpose). The information should first be gathered in a new
	 * PeerGroupAdvertisement which is then passed to this method.</li>
	 * <p/>
	 * <li>Instantiating a group which advertisement has already been
	 * discovered (therefore there is no need to find it by groupID
	 * again).</li>
	 * </ul>
	 *
	 * @since 2.6 If the peergroup has not been instantiated yet (i.e., does
	 * not belong to {@code GlobalRegistry}), the {@code ConfigParams} of the newly
	 * instanced object are copied from this peer group.
	 *
	 * @param pgAdv The advertisement for the group to be instantiated.
	 * @return PeerGroup the initialized (but not started) peergroup.
	 * @throws PeerGroupException For problems instantiating the peer group.
	 */
	IJp2pPeerGroup newGroup(IJp2pAdvertisement pgAdv) throws Jp2pPeerGroupException;

	/**
	 * Instantiate a group from its Peer Group ID only. Use this when using a
	 * group that has already been published and discovered.
	 * <p/>
	 * The typical uses of this routine are therefore:
	 * <p/>
	 * <ul>
	 * <li>Instantiating a peer group which is assumed to exist and whose Peer
	 * Group ID is already known.</li>
	 * <p/>
	 * <li>Creating a new peer group instance using an already published
	 * Group advertisement, typically published for that purpose. All other
	 * referenced advertisements must also be available.</li>
	 * </ul>
	 * <p/>
	 * To create a group from a known implAdv, just use
	 * {@link #loadModule(ID,Advertisement)} or even:<p>
	 * <p/>
	 * <code>
	 * grp = new GroupSubClass();
	 * grp.init(parentGroup, gid, impladv);
	 * </code>
	 * <p/>
	 * then, <strong>REMEMBER TO PUBLISH THE GROUP IF IT IS ALL NEW.</strong>
	 *
	 * @since 2.6 If the peergroup has not been instantiated yet (i.e., does
	 * not belong to {@code GlobalRegistry}), the {@code ConfigParams} of the newly
	 * instanced object are copied from this peer group.
	 *
	 * @param gid the groupID.
	 * @return PeerGroup the initialized (but not started) peergroup.
	 * @throws PeerGroupException Thrown if the group could not be instantiated.
	 */
	IJp2pPeerGroup newGroup(IJp2pPeerGroupID gid) throws Jp2pPeerGroupException;

	/**
	 * Instantiates a peer group from its elementary pieces
	 * and eventually publishes the corresponding PeerGroupAdvertisement.
	 * The pieces are: the groups implementation adv, the group id,
	 * the name and description.
	 * <p/>
	 * The typical use of this routine is creating a whole new group based
	 * on a newly created and possibly unpublished implementation adv.
	 * <p/>
	 * This is a convenience method equivalent to either:
	 * <p/>
	 * <pre>
	 * newGrp = thisGroup.loadModule(gid, impl);
	 * newGrp.publishGroup(name, description); // if publication is requested
	 * </pre>
	 * or, but only if the implementation advertisement has been published:
	 * <p/>
	 * <pre>
	 * newPGAdv = AdvertisementFactory.newAdvertisement(
	 *                 PeerGroupAdvertisement.getAdvertisementType());
	 * newPGAdv.setPeerGroupID(gid);
	 * newPGAdv.setModuleSpecID(impl.getModuleSpecID());
	 * newPGAdv.setName(name);
	 * newPGAdv.setDescription(description);
	 * newGrp = thisGroup.newGroup(newPGAdv);
	 * </pre>
	 *
	 * @since 2.6 If the peergroup has not been instantiated yet (i.e., does
	 * not belong to {@code GlobalRegistry}), the {@code ConfigParams} of the newly
	 * instanced object are copied from this peer group.
	 *
	 * @param gid         The ID of that group. If <code>null</code> then a new group ID
	 *                    will be chosen.
	 * @param impl        The advertisement of the implementation to be used.
	 * @param name        The name of the group.
	 * @param description A description of this group.
	 * @param publish publishes new group if {@code true}
	 * @return PeerGroup the initialized (but not started) peergroup.
	 * @throws PeerGroupException Thrown if the group could not be instantiated.
	 */
	IJp2pPeerGroup newGroup(IJp2pPeerGroupID gid, IJp2pAdvertisement impl, String name, String description, boolean publish)
			throws Jp2pPeerGroupException;

	/**
	 * Return the Rendezvous Service for this Peer Group. This service is
	 * optional and may not be present in all groups.
	 *
	 * @return The Rendezvous Service for this Peer Group or <code>null</code>
	 *         if there is no Rendezvous Service in this Peer Group.
	 */
	IJp2pService getService( PeerGroupServices service );

	/**
	 * Return the Peer Group ID of this Peer Group.
	 *
	 * @return PeerGroupId The Peer Group ID of this Peer Group.
	 */
	IJp2pPeerGroupID getPeerGroupID();

	/**
	 * Return the Peer ID by which this Peer is known within this Peer Group.
	 *
	 * @return the Peer ID by which this Peer is known within this Peer Group.
	 */
	IJp2pPeerID getPeerID();

	/**
	 * Return the Name of this group. This name is not canonical, meaning that
	 * there may be other groups with the same name.
	 *
	 * @return This groups's name or <code>null</code> if no name was specified.
	 */
	String getPeerGroupName();

	/**
	 * Return the name of the local peer within this group. This name is not
	 * canonical, meaning that there may be other peers with the same name.
	 *
	 * @return This peer's name or <code>null</code> if no name was specified.
	 */
	String getPeerName();

	/**
	 * Returns the config advertisement for this peer in this group (if any).
	 *
	 * @return The advertisement or <code>null</code> if none is available.
	 */
	IJp2pConfigParams getConfigAdvertisement();

	/**
	 * Get an all purpose peerGroup ModuleImplAdvertisement that is compatible
	 * with this group. This impl adv can be used to create any group that
	 * relies only on the standard services. Or to derive other impl advs, using
	 * this impl advertisement as a basis.
	 * <p/>
	 * This defines a peergroup implementation that can be used for
	 * many purposes, and from which one may derive slightly different
	 * peergroup implementations.
	 * <p/>
	 * This definition is always the same and has a well known ModuleSpecID.
	 * It includes the basic service and no protocols.
	 * <p/>
	 * The user must remember to change the specID if the set of services
	 * protocols or applications is altered before use.
	 *
	 * @return ModuleImplAdvertisement The new peergroup impl adv.
	 * @throws Exception if an error occurs while creating the implementation advertisement
	 */
	IJp2pModuleImplAdvertisement getAllPurposePeerGroupImplAdvertisement() throws Exception;

	/**
	 * Returns the parent group of this peer group. Not all peer groups have a
	 * parent and some implementations may not reveal their parents.
	 *
	 * @return The parent peer group or {@code null} if no parent group is
	 *         available.
	 * @since JXTA 2.3
	 */
	IJp2pPeerGroup getParentGroup();

	/**
	 * Returns the location of the parent of all items that this peer group is
	 * using for persistently storing its preferences, cache, persistent store,
	 * properties, etc. May be {@code null} if the peergroup has no defined
	 * location for storing persistent data.
	 *
	 * @return The location of the parent of all persistent items stored by
	 *         this peer group.
	 * @since JXTA 2.3.7
	 */
	URI getStoreHome();
}