package net.jp2p.chaupal.jxta.root.network.configurator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import net.jp2p.chaupal.jxta.root.network.NetworkManagerPropertySource.NetworkManagerProperties;
import net.jp2p.chaupal.jxta.root.network.configurator.NetworkConfigurationPropertySource.NetworkConfiguratorProperties;
import net.jp2p.container.properties.AbstractPropertyFacade;
import net.jp2p.container.properties.IJp2pProperties;
import net.jxta.refplatform.platform.NetworkConfigurator;

public class NetworkConfiguratorPropertyFacade extends AbstractPropertyFacade<NetworkConfigurator> {

	public NetworkConfiguratorPropertyFacade( String bundleId, NetworkConfigurator module) {
		super( bundleId, module);
	}

	@Override
	public Object getProperty( IJp2pProperties id) {
		NetworkConfigurator configurator = super.getModule();
		if(!( id instanceof NetworkManagerProperties ))
			return null;
		NetworkConfiguratorProperties property = ( NetworkConfiguratorProperties )id;
		switch( property ){
		case HOME:
			return configurator.getHome();
		case CONFIG_MODE:
			return configurator.getMode();
		case HTTP_8PUBLIC_ADDRESS:
			return configurator.getHttpPublicAddress();
		case HTTP_8ENABLED:
			return configurator.isHttpEnabled();
		case HTTP_8PUBLIC_ADDRESS_EXCLUSIVE:
			return configurator.isHttpPublicAddressExclusive();
		case HTTP_8INCOMING_STATUS:
			return configurator.getHttpIncomingStatus();
		case HTTP_8INTERFACE_ADDRESS:
			return configurator.getHttpInterfaceAddress();
		case HTTP_8OUTGOING_STATUS:
			return configurator.getHttpOutgoingStatus();
		case HTTP_8PORT:
			return configurator.getHttp2Port();
		case HTTP_8TO_PUBLIC_ADDRESS_EXCLUSIVE:
			return configurator.isHttp2PublicAddressExclusive();
		case HTTP2_8PUBLIC_ADDRESS:
			return configurator.getHttp2PublicAddress();
		case HTTP2_8ENABLED:
			return configurator.isHttp2Enabled();
		case HTTP2_8PUBLIC_ADDRESS_EXCLUSIVE:
			return configurator.isHttp2PublicAddressExclusive();
		case HTTP2_8INCOMING_STATUS:
			return configurator.getHttp2IncomingStatus();
		case HTTP2_8INTERFACE_ADDRESS:
			return configurator.getHttp2InterfaceAddress();
		case HTTP2_8OUTGOING_STATUS:
			return configurator.getHttp2OutgoingStatus();
		case HTTP2_8PORT:
			return configurator.getHttp2Port();
		case HTTP2_8END_PORT:
			return configurator.getHttp2EndPort();
		case HTTP2_8START_PORT:
			return configurator.getHttp2StartPort();
		case HTTP2_8TO_PUBLIC_ADDRESS_EXCLUSIVE:
			return configurator.isHttp2PublicAddressExclusive();

		case INFRASTRUCTURE_8DESCRIPTION:
			return configurator.getInfrastructureDescriptionStr();
		case INFRASTRUCTURE_8ID:
			return configurator.getInfrastructureID();
		case INFRASTRUCTURE_8NAME:
			return configurator.getInfrastructureName();
		case SECURITY_8AUTHENTICATION_TYPE:
			return configurator.getAuthenticationType();
		case SECURITY_8CERTFICATE:
			return configurator.getCertificate();
		case SECURITY_8CERTIFICATE_CHAIN:
			return configurator.getCertificateChain();
		case SECURITY_8PASSWORD:
			return configurator.getPassword();
		case SECURITY_8KEY_STORE_LOCATION:
			return configurator.getKeyStoreLocation();
		case SECURITY_8PRINCIPAL:
			return configurator.getPrincipal();
		case SECURITY_8PRIVATE_KEY:
			return configurator.getPrivateKey();
		case MULTICAST_8ADDRESS:
			return configurator.getMulticastAddress();
		case MULTICAST_8INTERFACE:
			return configurator.getMulticastInterface();
		case MULTICAST_8POOL_SIZE:
			return configurator.getMulticastPoolSize();
		case MULTICAST_8PORT:
			return configurator.getMulticastPort();
		case MULTICAST_8SIZE:
			return configurator.getMulticastSize();
		case MULTICAST_8STATUS:
			return configurator.getMulticastStatus();
		case NAME:
			return configurator.getName();
		case PEER_ID:
			return configurator.getPeerID();
		case RELAY_8MAX_CLIENTS:
			return configurator.getRelayMaxClients();
		case RELAY_8SEED_URIS:
			return  configurator.getRelaySeedURIs();
		case RELAY_8SEEDING_URIS:
			return configurator.getRelaySeedingURIs();
		case RENDEZVOUS_8MAX_CLIENTS:
			return configurator.getRendezvousMaxClients();
		case RENDEZVOUS_8SEED_URIS:
			return configurator.getRdvSeedURIs();
		case RENDEZVOUS_8SEEDING_URIS:
			return configurator.getRdvSeedingURIs();
		case STORE_HOME:
			return configurator.getStoreHome();
		case TCP_8PUBLIC_ADDRESS:
			return configurator.getTcpPublicAddress();
		case TCP_8ENABLED:
			return configurator.isTcpEnabled();
		case TCP_8END_PORT:
			return configurator.getTcpEndport();
		case TCP_8PUBLIC_ADDRESS_EXCLUSIVE:
			return configurator.isTcpPublicAddressExclusive();
		case TCP_8INCOMING_STATUS:
			return configurator.getTcpIncomingStatus();
		case TCP_8INTERFACE_ADDRESS:
			return configurator.getTcpInterfaceAddress();
		case TCP_8OUTGOING_STATUS:
			return configurator.getTcpOutgoingStatus();
		case TCP_8PORT:
			return configurator.getTcpPort();
		case TCP_8START_PORT:
			return configurator.getTcpStartPort();
		case MULTICAST_8ENABLED:
			return configurator.getMulticastStatus();
		case USE_ONLY_RELAY_SEEDS:
			return configurator.getUseOnlyRelaySeedsStatus();
		case USE_ONLY_RENDEZVOUS_SEEDS:
			return configurator.getUseOnlyRendezvousSeedsStatus();
		default:
			break;
		}
		return null;
	}

	@Override
	public Iterator<IJp2pProperties> propertyIterator() {
		Collection<IJp2pProperties> results = new ArrayList<IJp2pProperties>();
		for( NetworkManagerProperties nmp: NetworkManagerProperties.values())
			results.add( nmp );
		return results.iterator();
	}
}
