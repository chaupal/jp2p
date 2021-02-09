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
 *  THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
 *  INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND 
 *  FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL CHAUPAL 
 *  MICROSYSTEMS OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, 
 *  INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT 
 *  LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, 
 *  OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF 
 *  LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING 
 *  NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, 
 *  EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
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
package net.jp2p.jxse.core.platform;

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
import net.jp2p.chaupal.platform.INetworkConfigurator;
import net.jp2p.chaupal.protocol.IJp2pConfigParams;
import net.jxta.platform.NetworkConfigurator;

public class Jp2pNetworkConfigurator implements INetworkConfigurator {

	private NetworkConfigurator config;
	
	public Jp2pNetworkConfigurator( NetworkConfigurator config) {
		this.config = config;
	}

	@Override
	public void setDescription(String description) {
		this.config.setDescription(description);
	}

	@Override
	public void setHome(File home) {
		this.config.setHome(home);
	}

	@Override
	public File getHome() {
		return this.config.getHome();
	}

	@Override
	public URI getStoreHome() {
		return this.config.getStoreHome();
	}

	@Override
	public void setStoreHome(URI newHome) {
		this.config.setStoreHome(newHome);
	}

	@Override
	public boolean isEnabled(ToggleSettings toggle) {
		boolean result = false;
		switch( toggle ) {
		case HTTP:
			result = this.config.isHttpEnabled();
			break;
		case HTTP_INCOMING_STATUS:
			result = this.config.getHttpIncomingStatus();
			break;
		case HTTP_OUTGOING_STATUS:
			result = this.config.getHttpOutgoingStatus();
			break;
		case HTTP_PUBLIC_ADDRESS_EXCLUSIVE:
			result = this.config.isHttpPublicAddressExclusive();
			break;
		case HTTP2:
			result = this.config.isHttp2Enabled();
			break;
		case HTTP2_INCOMING_STATUS:
			result = this.config.getHttp2IncomingStatus();
			break;
		case HTTP2_OUTGOING_STATUS:
			result = this.config.getHttp2OutgoingStatus();
			break;
		case HTTP2_PUBLIC_ADDRESS_EXCLUSIVE:
			result = this.config.isHttp2PublicAddressExclusive();
			break;
		case MULTICAST_STATUS:
			result = this.config.getMulticastStatus();
			break;
		case TCP_ENABLED:
			result = this.config.isTcpEnabled();
			break;
		case TCP_INCOMING_STATUS:
			result = this.config.getTcpIncomingStatus();
			break;
		case TCP_OUTGOING_STATUS:
			result = this.config.getTcpOutgoingStatus();
			break;
		case TCP_PUBLIC_ADDRESS_EXCLUSIVE:
			result = this.config.isTcpPublicAddressExclusive();
			break;
		case USE_ONLY_RENDEZVOUS_SEEDS:
			result = this.config.getUseOnlyRendezvousSeedsStatus();
			break;
		case USE_ONLY_RELAY_SEEDS:
			result = this.config.getUseOnlyRelaySeedsStatus();
			break;
		}
		return result;
	}

	@Override
	public void setEnabled(ToggleSettings toggle, boolean choice) {
		switch( toggle ) {
		case HTTP:
			this.config.setHttpEnabled( choice );
			break;
		case HTTP_INCOMING_STATUS:
			this.config.setHttp2Incoming(choice);
			break;
		case HTTP_OUTGOING_STATUS:
			this.config.setHttpOutgoing(choice);
			break;
		case HTTP_PUBLIC_ADDRESS_EXCLUSIVE:
			this.config.setHttpPublicAddress(this.config.getHttpPublicAddress(), choice );
			break;
		case HTTP2:
			this.config.setHttp2Enabled(choice);
			break;
		case HTTP2_INCOMING_STATUS:
			this.config.setHttp2Incoming(choice);
			break;
		case HTTP2_OUTGOING_STATUS:
			this.config.setHttp2Outgoing(choice);
			break;
		case HTTP2_PUBLIC_ADDRESS_EXCLUSIVE:
			this.config.setHttp2PublicAddress( this.config.getHttp2PublicAddress(), choice );
			break;
		case MULTICAST_STATUS:
			this.config.setUseMulticast(choice);
			break;
		case TCP_ENABLED:
			this.config.setTcpEnabled(choice);
			break;
		case TCP_INCOMING_STATUS:
			this.config.setTcpIncoming(choice);
			break;
		case TCP_OUTGOING_STATUS:
			 this.config.setTcpOutgoing(choice);
			break;
		case TCP_PUBLIC_ADDRESS_EXCLUSIVE:
			this.config.setTcpPublicAddress(this.config.getTcpPublicAddress(), choice);
			break;
		case USE_ONLY_RENDEZVOUS_SEEDS:
			this.config.setUseOnlyRendezvousSeeds(choice);
			break;
		case USE_ONLY_RELAY_SEEDS:
			this.config.setUseOnlyRelaySeeds(choice);
			break;
		}
	}

	@Override
	public int getValue(IntSettings toggle) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setValue(IntSettings toggle, int value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setHttpPort(int port) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setHttpInterfaceAddress(String address) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getHttpInterfaceAddress() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setHttpPublicAddress(String address, boolean exclusive) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getHttp2InterfaceAddress() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setHttp2InterfaceAddress(String address) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getHttp2PublicAddress() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setHttp2PublicAddress(String address, boolean exclusive) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getHttpPublicAddress() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setInfrastructureID(IJp2pID id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setInfrastructureID(String idStr) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getInfrastructureIDStr() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setInfrastructureName(String name) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getInfrastructureName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setInfrastructureDescriptionStr(String description) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getInfrastructureDescriptionStr() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setInfrastructureDesc(Element description) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setMulticastAddress(String mcastAddress) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getMulticastInterface() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setMulticastInterface(String interfaceAddress) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setName(String name) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setPrincipal(String principal) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getPrincipal() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setCertificate(X509Certificate cert) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public X509Certificate getCertificate() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setCertificateChain(X509Certificate[] certificateChain) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public X509Certificate[] getCertificateChain() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setPrivateKey(PrivateKey subjectPkey) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public PrivateKey getPrivateKey() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setKeyStoreLocation(URI keyStoreLocation) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public URI getKeyStoreLocation() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getAuthenticationType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setAuthenticationType(String authenticationType) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPassword(String password) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setPeerID(IJp2pPeerID peerid) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public IJp2pPeerID getPeerID() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addRdvSeedingURI(URI seedURI) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setRelayMaxClients(int relayMaxClients) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addRelaySeedingURI(URI seedURI) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setTcpInterfaceAddress(String address) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setTcpPublicAddress(String address, boolean exclusive) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addSeedRelay(URI seedURI) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addSeedRendezvous(URI seedURI) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean exists() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setPeerId(String peerIdStr) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addRdvSeedingURI(String seedURIStr) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addRelaySeedingURI(String seedURIStr) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setRelaySeedURIs(List<String> seeds) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setRelaySeedingURIs(Set<String> seedURIs) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void clearRelaySeeds() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void clearRelaySeedingURIs() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setRendezvousSeeds(Set<String> seeds) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setRendezvousSeedingURIs(List<String> seedingURIs) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void clearRendezvousSeeds() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void clearRendezvousSeedingURIs() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public IJp2pConfigParams load() throws IOException, CertificateException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IJp2pConfigParams load(URI uri) throws IOException, CertificateException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void save() throws IOException, Jp2pConfiguratorException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public IJp2pConfigParams getPlatformConfig() throws Jp2pConfiguratorException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IJp2pID getInfrastructureID() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getMulticastAddress() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTcpInterfaceAddress() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTcpPublicAddress() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public URI[] getRdvSeedingURIs() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public URI[] getRdvSeedURIs() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public URI[] getRelaySeedURIs() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public URI[] getRelaySeedingURIs() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getMode() {
		// TODO Auto-generated method stub
		return null;
	}

}
