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
*/
package net.jp2p.chaupal.module;

import net.jp2p.chaupal.document.IJp2pAdvertisement;
import net.jp2p.chaupal.exception.Jp2pPeerGroupException;
import net.jp2p.chaupal.id.IJp2pID;
import net.jp2p.chaupal.peergroup.IJp2pPeerGroup;

/**
 * Defines the interface for modules loaded by PeerGroups. Message transports,
 * services and applications need to implement this interface if they are
 * to be loaded and started by a PeerGroup. Service and Application extend
 * Module, PeerGroup implements Service.
 *
 * <p/>Jxta Modules are given their initialization parameters via the init()
 * method rather than a non-default constructor.
 *
 * <p/>Modules are passed the peer group within which they are created.
 * From the peergroup object, Modules can access all the peer group
 * services. The PeerGroup within which a PeerGroup runs is known as its
 * parent.
 *
 * <p/>The initial root peer group is known as the World Peer Group and is
 * implemented by an object of class Platform, a subclass of PeerGroup.
 * The "parent" PeerGroup of the World Peer Group is null.
 *
 ** @see net.jxta.peergroup.coree.ModuleClassID
 * @see net.jxta.id.ID
 * @seenet.jxta.peergroup.corere.Application
 * @see net.jp2p.chaupal.service.Service
 **/
public interface IJp2pModule {

    /**
     * <code>startApp()</code> completed successfully. This module claims to now
     * be fully functional and no further invocation of startApp is required.
     **/
    public static final int START_OK = 0;

    /**
     * This is to be used mostly by co-dependent services when started as
     * a set (such as {@link PeerGroup} services) so that their
     * <code>startApp()</code> method may be invoked multiple times.
     *
     * <p/>This value indicates that startApp must be retried later in order for
     * this module to become fully functional. However, some progress in
     * functionality was accomplished.
     *
     * <p/>This is a strong indication that some other modules may be able
     * to advance or complete their initialization if their
     * <code>startApp()</code> method is invoked again.
     *
     * <p/>The distinction between START_AGAIN_STALLED and START_AGAIN_PROGRESS
     * is only a hint. Each module makes an arbitrary judgment in this
     * respect. It is up to the invoker of startApp to ensure that the
     * starting of a set of modules eventually succeeds or fails.
     **/
    public static final int START_AGAIN_PROGRESS = 1;

    /**
     * This is to be used mostly by co-dependent services when started as
     * a set (such as {@link PeerGroup} services) so that their startApp
     * method may be invoked multiple times.
     *
     * <p/>This value indicates that startApp must be retried later in order for
     * this module to become fully functional. However, some progress in
     * functionality was accomplished.
     *
     * <p/>If all modules in a set return this value, it is a strong indication
     * that the modules co-dependency is such that it prevents them
     * collectively from starting.
     *
     * <p/>The distinction between START_AGAIN_STALLED and START_AGAIN_PROGRESS
     * is only a hint. Each module makes an arbitrary judgment in this
     * respect. It is up to the invoker of startApp to ensure that the
     * starting of a set of modules eventually succeeds or fails.
     **/
    public static final int START_AGAIN_STALLED = 2;

    /**
     * This return result is used to indicate that the module refuses to start
     * because it has been configured to be disabled or otherwise cannot run
     * (missing hardware, missing system resources, etc.) The module will not be
     * functional and should be discarded but the failure to load may be ignored 
     * by the loader at it's discretion.
     */
    public static final int START_DISABLED = Integer.MIN_VALUE + 100;

    /**
     * Initialize the module, passing it its peer group and advertisement.
     *
     * <p/>Note: when subclassing one of the existing PeerGroup implementations
     * (which implement Module), it may not be recommended to overload the init
     * method. See the documentation of the PeerGroup class being subclassed.
     *
     *  @param group The PeerGroup from which this Module can obtain services.
     *  If this module is a Service, this is also the PeerGroup of which this
     *  module is a service.
     *
     *  @param assignedID Identity of Module within group.
     *  modules can use it as a the root of their namespace to create
     *  names that are unique within the group but predictable by the
     *  same module on another peer. This is normally the ModuleClassID
     *  which is also the name under which the module is known by other
     *  modules. For a group it is the PeerGroupID itself.
     *  The parameters of a service, in the Peer configuration, are indexed
     *  by the assignedID of that service, and a Service must publish its
     *  run-time parameters in the Peer Advertisement under its assigned ID.
     *
     *  @param implAdv The implementation advertisement for this
     *  Module. It is permissible to pass null if no implementation
     *  advertisement is available. This may happen if the
     *  implementation was selected by explicit class name rather than
     *  by following an implementation advertisement. Modules are not
     *  required to support that style of loading, but if they do, then
     *  their documentation should mention it.
     *
     *  @exception Jp2pPeerGroupException This module failed to initialize.
     * @throws Jp2pPeerGroupException 
     **/
    public void init(IJp2pPeerGroup group, IJp2pID assignedID, IJp2pAdvertisement implAdv) throws Jp2pPeerGroupException, Jp2pPeerGroupException;

    /**
     * Complete any remaining initialization of the module. The module should
     * be fully functional after <code>startApp()</code> is completed. That is
     * also the opportunity to supply arbitrary arguments (mostly to
     * applications).
     *
     * <p/>If this module is a {@link PeerGroup} service, it may be invoked
     * several times depending on its return value.
     *
     * @param args An array of Strings forming the parameters for this
     * Module.
     *
     * @return int A status indication which may be one of
     * {@link #START_OK}, {@link #START_AGAIN_PROGRESS},
     * {@link #START_AGAIN_STALLED}, which indicates partial or complete
     * success, or any other value (negative values are
     * recommended for future compatibility), which indicates failure.
     **/
    public int startApp(String[] args);

    /**
     *  Stop a module. This may be called any time after <code>init()</code>
     *  completes and should not assume that <code>startApp()</code> has been
     *  called or completed.
     *
     *  <p/>The Module cannot be forced to comply, but in the future
     *  we might be able to deny it access to anything after some timeout.
     **/
    public void stopApp();

}
