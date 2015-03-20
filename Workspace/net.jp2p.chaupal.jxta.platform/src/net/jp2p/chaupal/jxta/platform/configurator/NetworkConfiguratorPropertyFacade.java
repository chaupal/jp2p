package net.jp2p.chaupal.jxta.platform.configurator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import net.jp2p.chaupal.jxta.platform.NetworkManagerPropertySource.NetworkManagerProperties;
import net.jp2p.chaupal.jxta.platform.configurator.NetworkConfigurationPropertySource.NetworkConfiguratorProperties;
import net.jp2p.container.properties.AbstractPropertyFacade;
import net.jp2p.container.properties.IJp2pProperties;
import net.jxta.platform.NetworkConfigurator;

public class NetworkConfiguratorPropertyFacade extends AbstractPropertyFacade<NetworkConfigurator> {

	public NetworkConfiguratorPropertyFacade( String bundleId, NetworkConfigurator module) {
		super( bundleId, module);
	}

	@Override
	public Object getProperty( IJp2pProperties id) {
		NetworkConfigurator configurator = super.getModule();
		if(!( id instanceof NetworkConfiguratorProperties ))
			return null;
		NetworkConfiguratorProperties property = ( NetworkConfiguratorProperties )id;
		switch( property ){
		case HOME:
			return configurator.getHome();
		case CONFIG_MODE:
			return configurator.getMode();

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
