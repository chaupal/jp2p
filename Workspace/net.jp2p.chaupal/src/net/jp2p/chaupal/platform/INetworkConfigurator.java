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
package net.jp2p.chaupal.platform;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.security.PrivateKey;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Set;

import org.w3c.dom.Element;

import net.jp2p.chaupal.exception.Jp2pConfiguratorException;
import net.jp2p.chaupal.id.IJp2pID;
import net.jp2p.chaupal.peer.IJp2pPeerID;
import net.jp2p.chaupal.protocol.IJp2pConfigParams;

public interface INetworkConfigurator {

	public enum ToggleSettings{
		HTTP,
		HTTP2,
		HTTP_INCOMING_STATUS,
		HTTP_OUTGOING_STATUS,
		HTTP2_INCOMING_STATUS,
		HTTP2_OUTGOING_STATUS,
		HTTP_PUBLIC_ADDRESS_EXCLUSIVE,
		HTTP2_PUBLIC_ADDRESS_EXCLUSIVE,
		MULTICAST_STATUS,
		TCP_ENABLED,
		TCP_INCOMING_STATUS,
		TCP_OUTGOING_STATUS,
		TCP_PUBLIC_ADDRESS_EXCLUSIVE,
		USE_ONLY_RELAY_SEEDS,
		USE_ONLY_RENDEZVOUS_SEEDS
	}

	public enum IntSettings{
		HTTP_PORT,
		HTTP_START_PORT,
		HTTP_END_PORT,
		HTTP2_PORT,
		HTTP2_START_PORT,
		HTTP2_END_PORT,
		MODE,
		MULTICAST_PORT,
		MULTICAST_POOL_SIZE,
		MULTICAST_SIZE,
		RELAY_MAX_CLIENTS,
		RENDEZVOUS_MAX_CLIENTS,
		TCP_PORT,
		TCP_START_PORT,
		TCP_END_PORT,
		USE_ONLY_RELAY_SEEDS,
		USE_ONLY_MULTICAST_SEEDS
	}

	/**
	 * Relay off Mode
	 */
	int RELAY_OFF = 1 << 2;
	/**
	 * Relay client Mode
	 */
	int RELAY_CLIENT = 1 << 3;
	/**
	 * Relay Server Mode
	 */
	int RELAY_SERVER = 1 << 4;
	/**
	 * Proxy Server Mode
	 */
	int PROXY_SERVER = 1 << 5;
	/**
	 * TCP transport client Mode
	 */
	int TCP_CLIENT = 1 << 6;
	/**
	 * TCP transport Server Mode
	 */
	int TCP_SERVER = 1 << 7;
	/**
	 * HTTP transport client Mode
	 */
	int HTTP_CLIENT = 1 << 8;
	/**
	 * HTTP transport server Mode
	 */
	int HTTP_SERVER = 1 << 9;
	/**
	 * IP multicast transport Mode
	 */
	int IP_MULTICAST = 1 << 10;
	/**
	 * RendezVousService Mode
	 */
	int RDV_SERVER = 1 << 11;
	/**
	 * RendezVousService Client
	 */
	int RDV_CLIENT = 1 << 12;
	/**
	 * RendezVousService Ad-Hoc mode
	 */
	int RDV_AD_HOC = 1 << 13;
	/**
	 * HTTP2 (netty http tunnel) client
	 */
	int HTTP2_CLIENT = 1 << 14;
	/**
	 * HTTP2 (netty http tunnel) server
	 */
	int HTTP2_SERVER = 1 << 15;
	/**
	 * Default AD-HOC configuration
	 */
	int ADHOC_NODE = TCP_CLIENT | TCP_SERVER | IP_MULTICAST | RDV_AD_HOC | RELAY_OFF;
	/**
	 * Default Edge configuration
	 */
	int EDGE_NODE = TCP_CLIENT | TCP_SERVER | HTTP_CLIENT | HTTP2_CLIENT | IP_MULTICAST | RDV_CLIENT | RELAY_CLIENT;
	/**
	 * Default Rendezvous configuration
	 */
	int RDV_NODE = RDV_SERVER | TCP_CLIENT | TCP_SERVER | HTTP_SERVER | HTTP2_SERVER;
	/**
	 * Default Relay configuration
	 */
	int RELAY_NODE = RELAY_SERVER | TCP_CLIENT | TCP_SERVER | HTTP_SERVER | HTTP2_SERVER;

	/**
	 * Sets PlaformConfig Peer Description element
	 *
	 * @param description the peer description
	 */
	void setDescription(String description);

	/**
	 * Set the current directory for configuration and cache persistent store
	 * <p/>(default is $CWD/.jxta)
	 * <p/>
	 * <dt>Simple example :</dt>
	 * <pre>
	 *  <code>
	 *   //Create an application home
	 *   File appHome = new File(System.getProperty("JXTA_HOME", ".cache"));
	 *   //Create an instance home under the application home
	 *   File instanceHome = new File(appHome, instanceName);
	 *   jxtaConfig.setHome(instanceHome);
	 *   </code>
	 * </pre>
	 *
	 * @param home the new home value
	 * @see #getHome
	 */
	void setHome(File home);

	/**
	 * Returns the current directory for configuration and cache persistent
	 * store. This is the same location as returned by {@link #getStoreHome()}
	 * which is more general than this method.
	 *
	 * @return Returns the current home directory
	 * @see #setHome
	 */
	File getHome();

	/**
	 * Returns the location which will serve as the parent for all stored items
	 * used by JXTA.
	 *
	 * @return The location which will serve as the parent for all stored
	 *         items used by JXTA.
	 * 
	 */
	URI getStoreHome();

	/**
	 * Sets the location which will serve as the parent for all stored items
	 * used by JXTA.
	 *
	 * @param newHome new home directory URI
	 * 
	 */
	void setStoreHome(URI newHome);

	/**
	 * returns true if the given setting is enabled
	 * @param toggle
	 * @return
	 */
	boolean isEnabled( ToggleSettings toggle );
	
	/**
	 * Enable or disable the given settings
	 * @param toggle
	 * @param choice
	 */
	public void setEnabled( ToggleSettings toggle, boolean choice );

	/**
	 * returns true if the given setting is enabled
	 * @param toggle
	 * @return
	 */
	int getValue( IntSettings toggle );
	
	/**
	 * Enable or disable the given settings
	 * @param toggle
	 * @param choice
	 */
	public void setValue( IntSettings toggle, int value );

	/**
	 * Sets the HTTP listening port (default 9901)
	 *
	 * @param port the new HTTP port value
	 */
	void setHttpPort(int port);

	/**
	 * Sets the HTTP interface Address to bind the HTTP transport to
	 * <p/>e.g. "192.168.1.1"
	 *
	 * @param address the new address value
	 */
	void setHttpInterfaceAddress(String address);

	/**
	 * Returns the HTTP interface Address
	 *
	 * @param address the HTTP interface address
	 */
	String getHttpInterfaceAddress();

	/**
	 * Sets the HTTP JXTA Public Address
	 * e.g. "192.168.1.1:9700"
	 *
	 * @param address   the HTTP transport public address
	 * @param exclusive determines whether an address is advertised exclusively
	 */
	void setHttpPublicAddress(String address, boolean exclusive);

	String getHttp2InterfaceAddress();

	void setHttp2InterfaceAddress(String address);

	String getHttp2PublicAddress();

	void setHttp2PublicAddress(String address, boolean exclusive);

	/**
	 * Returns the HTTP JXTA Public Address
	 *
	 * @return exclusive determines whether an address is advertised exclusively
	 */
	String getHttpPublicAddress();

	/**
	 * Sets the ID which will be used for new net peer group instances.
	 * <p/>
	 * <p/>By Setting an alternate infrastructure PeerGroup ID (aka NetPeerGroup),
	 * it prevents heterogeneous infrastructure PeerGroups from intersecting.
	 * <p/>This is highly recommended practice for application deployment
	 *
	 * @param id the new infrastructure PeerGroupID as a string
	 * @see net.jxta.peergroup.PeerGroupFactory#setNetPGID
	 */
	void setInfrastructureID( IJp2pID id);

	/**
	 * Sets the ID which will be used for new net peer group instances.
	 * <p/>
	 * <p/>By Setting an alternate infrastructure PeerGroup ID (aka NetPeerGroup),
	 * it prevents heterogeneous infrastructure PeerGroups from intersecting.
	 * <p/>This is highly recommended practice for application deployment
	 *
	 * @param idStr the new infrastructure PeerGroupID as a string
	 * @see net.jxta.peergroup.PeerGroupFactory#setNetPGID
	 */
	void setInfrastructureID(String idStr);

	/**
	 * Gets the ID which will be used for new net peer group instances.
	 * <p/>
	 *
	 * @return the infrastructure PeerGroupID as a string
	 */
	String getInfrastructureIDStr();

	/**
	 * Sets the infrastructure PeerGroup name meta-data
	 *
	 * @param name the Infrastructure PeerGroup name
	 * @see net.jxta.peergroup.PeerGroupFactory#setNetPGName
	 */
	void setInfrastructureName(String name);

	/**
	 * Gets the infrastructure PeerGroup name meta-data
	 *
	 * @return the Infrastructure PeerGroup name
	 */
	String getInfrastructureName();

	/**
	 * Sets the infrastructure PeerGroup description meta-data
	 *
	 * @param description the infrastructure PeerGroup description
	 * @see net.jxta.peergroup.PeerGroupFactory#setNetPGDesc
	 */
	void setInfrastructureDescriptionStr(String description);

	/**
	 * Returns the infrastructure PeerGroup description meta-data
	 *
	 * @return the infrastructure PeerGroup description meta-data
	 */
	String getInfrastructureDescriptionStr();

	/**
	 * Sets the infrastructure PeerGroup description meta-data
	 *
	 * @param description the infrastructure PeerGroup description
	 * @see net.jxta.peergroup.PeerGroupFactory#setNetPGDesc
	 */
	void setInfrastructureDesc(Element description);

	/**
	 * Sets the IP group multicast address (default 224.0.1.85)
	 *
	 * @param mcastAddress the new multicast group address
	 * @see #setMulticastPort
	 */
	void setMulticastAddress(String mcastAddress);

	/**
	 * Gets the multicast network interface
	 *
	 * @return the multicast network interface, null if none specified
	 */
	String getMulticastInterface();

	/**
	 * Sets the multicast network interface
	 *
	 * @param interfaceAddress multicast network interface
	 */
	void setMulticastInterface(String interfaceAddress);

	/**
	 * Sets the node name
	 *
	 * @param name node name
	 */
	void setName(String name);

	/**
	 * Gets the node name
	 *
	 * @return node name
	 */
	String getName();

	/**
	 * Sets the Principal for the peer root certificate
	 *
	 * @param principal the new principal value
	 * @see #setPassword
	 * @see #getPrincipal
	 * @see #setPrincipal
	 */
	void setPrincipal(String principal);

	/**
	 * Gets the Principal for the peer root certificate
	 *
	 * @return principal  if a principal is set, null otherwise
	 * @see #setPassword
	 * @see #getPrincipal
	 * @see #setPrincipal
	 */
	String getPrincipal();

	/**
	 * Sets the public Certificate for this configuration.
	 *
	 * @param cert the new cert value
	 */
	void setCertificate(X509Certificate cert);

	/**
	 * Returns the public Certificate for this configuration.
	 *
	 * @return X509Certificate
	 */
	X509Certificate getCertificate();

	/**
	 * Sets the public Certificate chain for this configuration.
	 *
	 * @param certificateChain the new Certificate chain value
	 */
	void setCertificateChain(X509Certificate[] certificateChain);

	/**
	 * Gets the public Certificate chain for this configuration.
	 *
	 * @return X509Certificate chain
	 */
	X509Certificate[] getCertificateChain();

	/**
	 * Sets the Subject private key
	 *
	 * @param subjectPkey the subject private key
	 */
	void setPrivateKey(PrivateKey subjectPkey);

	/**
	 * Gets the Subject private key
	 *
	 * @return the subject private key
	 */
	PrivateKey getPrivateKey();

	/**
	 * Sets freestanding keystore location
	 *
	 * @param keyStoreLocation the absolute location of the freestanding keystore
	 */
	void setKeyStoreLocation(URI keyStoreLocation);

	/**
	 * Gets the freestanding keystore location
	 *
	 * @return the location of the freestanding keystore
	 */
	URI getKeyStoreLocation();

	/**
	 * Gets the authenticationType
	 *
	 * @return authenticationType the authenticationType value
	 */
	String getAuthenticationType();

	/**
	 * Sets the authenticationType
	 *
	 * @param authenticationType the new authenticationType value
	 */
	void setAuthenticationType(String authenticationType);

	/**
	 * Sets the password used to sign the private key of the root certificate
	 *
	 * @param password the new password value
	 * @see #setPassword
	 * @see #getPrincipal
	 * @see #setPrincipal
	 */
	void setPassword(String password);

	/**
	 * Gets the password used to sign the private key of the root certificate
	 *
	 * @return password  if a password is set, null otherwise
	 * @see #setPassword
	 * @see #getPrincipal
	 * @see #setPrincipal
	 */
	String getPassword();

	/**
	 * Sets the PeerID (by default, a new PeerID is generated).
	 * <p/>Note: Persist the PeerID generated, or use load()
	 * to avoid overridding a node's PeerID between restarts.
	 *
	 * @param peerid the new <code>net.jxta.peer.PeerID</code>
	 */
	void setPeerID(IJp2pPeerID peerid);

	/**
	 * Gets the PeerID
	 *
	 * @return peerid  the <code>net.jxta.peer.PeerID</code> value
	 */
	IJp2pPeerID getPeerID();

	/**
	 * Sets Rendezvous Seeding URI
	 *
	 * @param seedURI Rendezvous service seeding URI
	 */
	void addRdvSeedingURI(URI seedURI);

	/**
	 * Sets the RelayService maximum number of simultaneous relay clients
	 *
	 * @param relayMaxClients the new relayMaxClients value
	 */
	void setRelayMaxClients(int relayMaxClients);

	/**
	 * Sets the RelayService Seeding URI
	 * <p/>A seeding URI (when read) is expected to provide a list of
	 * physical endpoint addresse(s) to relay peers
	 *
	 * @param seedURI RelayService seeding URI
	 */
	void addRelaySeedingURI(URI seedURI);

	/**
	 * Sets the TCP transport interface address
	 * <p/>e.g. "192.168.1.1"
	 *
	 * @param address the TCP transport interface address
	 */
	void setTcpInterfaceAddress(String address);

	/**
	 * Sets the node public address
	 * <p/>e.g. "192.168.1.1:9701"
	 * <p/>This address is the physical address defined in a node's
	 * AccessPointAdvertisement.  This often required for NAT'd/FW nodes
	 *
	 * @param address   the TCP transport public address
	 * @param exclusive public address advertised exclusively
	 */
	void setTcpPublicAddress(String address, boolean exclusive);

	/**
	 * Adds RelayService peer seed address
	 * <p/>A RelayService seed is defined as a physical endpoint address
	 * <p/>e.g. http://192.168.1.1:9700, or tcp://192.168.1.1:9701
	 *
	 * @param seedURI the relay seed URI
	 */
	void addSeedRelay(URI seedURI);

	/**
	 * Adds Rendezvous peer seed, physical endpoint address
	 * <p/>A RendezVousService seed is defined as a physical endpoint address
	 * <p/>e.g. http://192.168.1.1:9700, or tcp://192.168.1.1:9701
	 *
	 * @param seedURI the rendezvous seed URI
	 */
	void addSeedRendezvous(URI seedURI);

	/**
	 * Returns true if a PlatformConfig file exist under store home
	 *
	 * @return true if a PlatformConfig file exist under store home
	 */
	boolean exists();

	/**
	 * Sets the PeerID for this Configuration
	 *
	 * @param peerIdStr the new PeerID as a string
	 */
	void setPeerId(String peerIdStr);

	/**
	 * Sets the new RendezvousService seeding URI as a string.
	 * <p/>A seeding URI (when read) is expected to provide a list of
	 * physical endpoint address to rendezvous peers
	 *
	 * @param seedURIStr the new rendezvous seed URI as a string
	 */
	void addRdvSeedingURI(String seedURIStr);

	/**
	 * Sets the new RelayService seeding URI as a string.
	 * <p/>A seeding URI (when read) is expected to provide a list of
	 * physical endpoint address to relay peers
	 *
	 * @param seedURIStr the new RelayService seed URI as a string
	 */
	void addRelaySeedingURI(String seedURIStr);

	/**
	 * Sets the List relaySeeds represented as Strings
	 * <p/>A RelayService seed is defined as a physical endpoint address
	 * <p/>e.g. http://192.168.1.1:9700, or tcp://192.168.1.1:9701
	 *
	 * @param seeds the Set RelayService seed URIs as a string
	 */
	void setRelaySeedURIs(List<String> seeds);

	/**
	 * Sets the relaySeeds represented as Strings
	 * <p/>A seeding URI (when read) is expected to provide a list of
	 * physical endpoint address to relay peers
	 *
	 * @param seedURIs the List relaySeeds represented as Strings
	 */
	void setRelaySeedingURIs(Set<String> seedURIs);

	/**
	 * Clears the List of RelayService seeds
	 */
	void clearRelaySeeds();

	/**
	 * Clears the List of RelayService seeding URIs
	 */
	void clearRelaySeedingURIs();

	/**
	 * Sets the List of RendezVousService seeds represented as Strings
	 * <p/>A RendezvousService seed is defined as a physical endpoint address
	 * <p/>e.g. http://192.168.1.1:9700, or tcp://192.168.1.1:9701
	 *
	 * @param seeds the Set of rendezvousSeeds represented as Strings
	 */
	void setRendezvousSeeds(Set<String> seeds);

	/**
	 * Sets the List of RendezVousService seeding URIs represented as Strings.
	 * A seeding URI (when read) is expected to provide a list of
	 * physical endpoint address to rendezvous peers.
	 *
	 * @param seedingURIs the List rendezvousSeeds represented as Strings.
	 */
	void setRendezvousSeedingURIs(List<String> seedingURIs);

	/**
	 * Clears the list of RendezVousService seeds
	 */
	void clearRendezvousSeeds();

	/**
	 * Clears the list of RendezVousService seeding URIs
	 */
	void clearRendezvousSeedingURIs();

	/**
	 * Load a configuration from the specified store home uri
	 * <p/>
	 * e.g. file:/export/dist/EdgeConfig.xml, e.g. http://configserver.net/configservice?Edge
	 *
	 * @return The loaded configuration.
	 * @throws IOException          if an i/o error occurs
	 * @throws CertificateException if the MembershipService is invalid
	 */
	IJp2pConfigParams load() throws IOException, CertificateException;

	/**
	 * Loads a configuration from a specified uri
	 * <p/>
	 * e.g. file:/export/dist/EdgeConfig.xml, e.g. http://configserver.net/configservice?Edge
	 *
	 * @param uri the URI to PlatformConfig
	 * @return The loaded configuration.
	 * @throws IOException          if an i/o error occurs
	 * @throws CertificateException if the MemebershipService is invalid
	 */
	IJp2pConfigParams load(URI uri) throws IOException, CertificateException;

	/**
	 * Persists a PlatformConfig advertisement under getStoreHome()+"/PlaformConfig"
	 * <p/>
	 * Home may be overridden by a call to setHome()
	 *
	 * @throws IOException If there is a failure saving the PlatformConfig.
	 * @see #load
	 */
	void save() throws IOException, Jp2pConfiguratorException;

	/**
	 * Returns a PlatformConfig which represents a platform configuration.
	 * <p/>Fine tuning is achieved through accessing each configured advertisement
	 * and achieved through accessing each configured advertisement and modifying
	 * each object directly.
	 *
	 * @return the PeerPlatformConfig Advertisement
	 * @throws net.jp2p.Jp2pConfiguratorException.JP2PConfiguratorException
	 */
	IJp2pConfigParams getPlatformConfig() throws Jp2pConfiguratorException;

	/**
	 * Retrieves the current infrastructure ID
	 *
	 * @return the current infrastructure ID
	 * @see #setInfrastructureID
	 */
	IJp2pID getInfrastructureID();

	/**
	 * Retrieves the current multicast address
	 *
	 * @return the current multicast address
	 * @see #setMulticastAddress
	 */
	String getMulticastAddress();

	/**
	 * Retrieves the Tcp interface address
	 *
	 * @return the current tcp interface address
	 * @see #setTcpInterfaceAddress
	 */
	String getTcpInterfaceAddress();

	/**
	 * Retrieves the current Tcp public address
	 *
	 * @return the current tcp public address
	 * @see #setTcpPublicAddress
	 */
	String getTcpPublicAddress();

	/**
	 * Retrieves the rendezvous seedings
	 *
	 * @return the array of rendezvous seeding URL
	 * @see #addRdvSeedingURI
	 */
	URI[] getRdvSeedingURIs();

	/**
	 * Retrieves the rendezvous seeds
	 *
	 * @return the array of rendezvous seeds URL
	 * @see #addRdvSeedURI
	 */
	URI[] getRdvSeedURIs();

	/**
	 * Retrieves the relay seeds
	 *
	 * @return the array of relay seeds URL
	 * @see #addRelaySeedURI
	 */
	URI[] getRelaySeedURIs();

	/**
	 * Retrieves the relay seeds
	 *
	 * @return the array of rendezvous seed URL
	 * @see #addRelaySeedingURI
	 */
	URI[] getRelaySeedingURIs();

	Object getMode();
}