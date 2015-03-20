/*******************************************************************************
 * Copyright 2014 Chaupal
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributors:
 *     Kees Pieters - initial API and implementation
 *******************************************************************************/
package net.jp2p.chaupal.jxta.platform.tcp;

import java.util.Iterator;

import net.jp2p.container.properties.AbstractJp2pWritePropertySource;
import net.jp2p.container.properties.IJp2pDirectives;
import net.jp2p.container.properties.IJp2pProperties;
import net.jp2p.container.properties.IJp2pPropertySource;
import net.jp2p.container.properties.IPropertyConvertor;
import net.jp2p.jxta.transport.TransportPropertySource.TransportProperties;
import net.jxta.platform.NetworkConfigurator;

public class TcpPropertySource extends AbstractJp2pWritePropertySource {
	
	public TcpPropertySource( String componentName, IJp2pPropertySource<IJp2pProperties> parent) {
		super( componentName, parent);
		super.setDirective( IJp2pDirectives.Directives.ENABLED, Boolean.TRUE.toString());
	}

	/**
	 * Get the incoming status
	 * @return
	 */
	public boolean getIncomingStatus(){
		return (boolean) super.getProperty( TransportProperties.INCOMING_STATUS );
	}

	/**
	 * Get the outgoing
	 * @return
	 */
	public boolean getOutgoingStatus(){
		return (boolean) super.getProperty( TransportProperties.OUTGOING_STATUS );
	}

	/**
	 * Get the interface address
	 * @return
	 */
	public String getInterfaceAddress(){
		return (String) super.getProperty( TransportProperties.INTERFACE_ADDRESS );
	}

	/**
	 * Get the interface address
	 * @return
	 */
	public String getPublicAddress(){
		return (String) super.getProperty( TransportProperties.PUBLIC_ADDRESS );
	}

	/**
	 * Get the flag whether the public address is exlusive
	 * @return
	 */
	public boolean getPublicAddressExclusive(){
		return (boolean) super.getProperty( TransportProperties.PUBLIC_ADDRESS_EXCLUSIVE );
	}

	/**
	 * Get the port
	 * @return
	 */
	public int getPort(){
		return (int) super.getProperty( TransportProperties.PORT );
	}

	@Override
	public IPropertyConvertor<IJp2pProperties, String, Object> getConvertor() {
		return new Convertor( this );
	}

	public static final void fillTcpNetworkConfigurator( TcpPropertySource source, NetworkConfigurator configurator ){
		Iterator<IJp2pProperties> iterator = source.propertyIterator();
		configurator.setHttpEnabled( source.isEnabled());
		while( iterator.hasNext() ){
			TransportProperties property = (TransportProperties) iterator.next();
			switch( property ){
			case PORT:
				configurator.setTcpPort( source.getPort());
				break;
			case START_PORT:
				configurator.setTcpStartPort((int) source.getProperty( property ));
				break;
			case END_PORT:
				configurator.setTcpEndPort( (int) source.getProperty( property ));
				break;
			case INCOMING_STATUS:
				configurator.setTcpIncoming( source.getIncomingStatus() );
				break;
			case OUTGOING_STATUS:
				configurator.setTcpOutgoing( source.getOutgoingStatus() );
				break;
			case INTERFACE_ADDRESS:
				configurator.setTcpInterfaceAddress( source.getInterfaceAddress() );
				break;
			case PUBLIC_ADDRESS:
				configurator.setTcpPublicAddress( source.getPublicAddress(), source.getPublicAddressExclusive() );
				break;
			default:
				break;
			}
		}
	}

	private class Convertor extends SimplePropertyConvertor{

		public Convertor(IJp2pPropertySource<IJp2pProperties> source) {
			super(source);
		}

		@Override
		public TransportProperties getIdFromString(String key) {
			return TransportProperties.valueOf( key );
		}

		@Override
		public String convertFrom(IJp2pProperties id) {
			TransportProperties property = ( TransportProperties )id;
			Object retval = getProperty( property );
			switch( property ){
			case PUBLIC_ADDRESS_EXCLUSIVE:
			case INCOMING_STATUS:
			case OUTGOING_STATUS:
				return String.valueOf(( Boolean )retval );
			case PORT:
				return String.valueOf(( Integer )retval );
			default:
				break;
			}
			return super.convertFrom(id);
		}

		@Override
		public Object convertTo(IJp2pProperties id, String value) {
			TransportProperties property = ( TransportProperties )id;
			switch( property ){
			case PUBLIC_ADDRESS_EXCLUSIVE:
				return Boolean.valueOf( value );
			case PORT:
			case START_PORT:
			case END_PORT:
				return Integer.valueOf( value );
			default:
				break;
			}
			return super.convertTo(id, value);
		}		
	}
}