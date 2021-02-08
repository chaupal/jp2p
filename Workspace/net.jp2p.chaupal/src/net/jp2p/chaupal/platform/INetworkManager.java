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
package net.jp2p.chaupal.platform;

import java.io.IOException;
import java.net.URI;
import java.util.EventObject;

import net.jp2p.chaupal.exception.Jp2pConfiguratorException;
import net.jp2p.chaupal.exception.Jp2pPeerGroupException;
import net.jp2p.chaupal.peer.IJp2pPeerID;
import net.jp2p.chaupal.peergroup.IJp2pPeerGroup;
import net.jp2p.chaupal.peergroup.IJp2pPeerGroupID;
import net.jp2p.chaupal.rendezvous.JP2PRendezvousEvent;
import net.jp2p.chaupal.utils.StringStyler;

public interface INetworkManager<E extends EventObject> {

	public enum ConfigModes{
		ADHOC,
		EDGE,
		RENDEZVOUS,
		RELAY,
		RENDEZVOUS_RELAY,
		SUPER;

		@Override
		public String toString() {
			return StringStyler.prettyString( name() );
		}
		
		/**
		 * Get the config modes as string
		 * @return
		 */
		public static final String[] getConfigModes(){
			ConfigModes[] modes = values();
			String[] results = new String[ modes.length];
			for( int i=0; i<modes.length; i++ ){
				INetworkManager.ConfigModes mode = modes[i];
				results[i] = mode.toString();
			}
			return results;
		}
	}

	/**
	 * Returns the {@link NetworkConfigurator} for additional tuning
	 *
	 * @return the {@link NetworkConfigurator} for additional tuning
	 * @throws java.io.IOException if an io error occurs
	 */
	INetworkConfigurator getConfigurator() throws IOException;

	/**
	 * Getter for property 'infrastructureID'.
	 *
	 * @return Value for property 'infrastructureID'.
	 */
	IJp2pPeerGroupID getInfrastructureID();

	/**
	 * Setter for property 'infrastructureID'.
	 *
	 * @param infrastructureID Value to set for property 'infrastructureID'.
	 */
	void setInfrastructureID(IJp2pPeerGroupID infrastructureID);

	/**
	 * Getter for property 'instanceName'.
	 *
	 * @return Value for property 'instanceName'.
	 */
	String getInstanceName();

	/**
	 * Getter for property 'instanceHome'.
	 *
	 * @return Value for property 'instanceHome'.
	 */
	URI getInstanceHome();

	/**
	 * Getter for property node operating 'mode'.
	 *
	 * @return Value for property 'mode'.
	 */
	ConfigModes getMode();

	/**
	 * Setter for property 'mode'.
	 *
	 * @param mode Value to set for property 'mode'.
	 * @throws IOException if an io error occurs
	 */
	void setMode(ConfigModes mode) throws IOException;

	/**
	 * Getter for property 'peerID'.
	 *
	 * @return Value for property 'peerID'.
	 */
	IJp2pPeerID getPeerID();

	/**
	 * Setter for property 'peerID'.
	 *
	 * @param peerID Value to set for property 'peerID'.
	 */
	void setPeerID(IJp2pPeerID peerID);

	/**
	 * Getter for property 'configPersistent'.
	 *
	 * @return Value for property 'configPersistent'.
	 */
	boolean isConfigPersistent();

	/**
	 * Setter for property 'configPersistent'. If enabled, the PlatformConfig is persisted
	 * when {@code startNetwork()} is called.
	 *
	 * @param persisted Value to set for property 'configPersistent'.
	 */
	void setConfigPersistent(boolean persisted);

	/**
	 * Creates and starts the JXTA infrastructure peer group (aka NetPeerGroup) based on the specified mode
	 * template. This class also registers a listener for rendezvous events.
	 *
	 * @return The Net Peer Group
	 * @throws net.jp2p.Jp2pPeerGroupException.JP2PPeerGroupException
	 *                             if the group fails to initialize
	 * @throws java.io.IOException if an io error occurs
	 * @throws net.jp2p.Jp2pConfiguratorException.JP2PConfiguratorException if platform is not configured properly
	 */
	IJp2pPeerGroup startNetwork() throws Jp2pPeerGroupException, IOException, Jp2pConfiguratorException;

	/**
	 * Stops NetPeerGroup
	 */
	void stopNetwork();

	/**
	 * Gets the netPeerGroup object
	 *
	 * @return The netPeerGroup value
	 */
	IJp2pPeerGroup getNetPeerGroup();

	/**
	 * Blocks only, if not connected to a rendezvous, or until a connection to rendezvous node occurs.
	 *
	 * @param timeout timeout in milliseconds, a zero timeout of waits forever
	 * @return true if connected to a rendezvous, false otherwise
	 */
	boolean waitForRendezvousConnection(long timeout);

	/**
	 * rendezvousEvent the rendezvous event
	 *
	 * @param event rendezvousEvent
	 */
	void rendezvousEvent(JP2PRendezvousEvent<E> event);

	/**
	 * Registers a Runtime shutdown hook to cleanly shutdown the JXTA platform
	 */
	void registerShutdownHook();

	/**
	 * Unregisters a Runtime shutdown hook to cleanly shutdown the JXTA platform
	 */
	void unregisterShutdownHook();

	/**
	 * Returns true if started
	 *
	 * @return true if started
	 */
	boolean isStarted();
}