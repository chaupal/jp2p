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
package net.jp2p.chaupal.jxta.platform.http;

import java.util.Iterator;

import net.jp2p.container.properties.AbstractJp2pWritePropertySource;
import net.jp2p.container.properties.IJp2pDirectives;
import net.jp2p.container.properties.IJp2pProperties;
import net.jp2p.container.properties.IJp2pPropertySource;
import net.jp2p.container.properties.IPropertyConvertor;
import net.jp2p.container.utils.StringStyler;
import net.jp2p.jxta.transport.TransportPropertySource.TransportProperties;
import net.jxta.platform.NetworkConfigurator;

public class HttpPropertySource extends AbstractJp2pWritePropertySource {
	
	/**
	 * The supported types
	 * @author Kees
	 *
	 */
	public enum HttpTypes{
		HTTP,
		HTTP2;

		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString() );
		}	

	}

	public HttpPropertySource( String componentName, IJp2pPropertySource<IJp2pProperties> parent) {
		super( componentName, parent);
		super.setDirective( IJp2pDirectives.Directives.ENABLED, Boolean.TRUE.toString());
	}

	/**
	 * Get the incoming status
	 * @return
	 */
	public boolean getIncomingStatus(){
		return (Boolean) super.getProperty( TransportProperties.INCOMING_STATUS );
	}

	/**
	 * Get the outgoing
	 * @return
	 */
	public boolean getOutgoingStatus(){
		return (Boolean) super.getProperty( TransportProperties.OUTGOING_STATUS );
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
		return (Boolean) super.getProperty( TransportProperties.PUBLIC_ADDRESS_EXCLUSIVE );
	}

	/**
	 * Get the port
	 * @return
	 */
	public int getPort(){
		return (Integer) super.getProperty( TransportProperties.PORT );
	}

	@Override
	public IPropertyConvertor<IJp2pProperties, String, Object> getConvertor() {
		return new Convertor( this );
	}

	public static final void fillHttpNetworkConfigurator( HttpPropertySource source, NetworkConfigurator configurator ){
		Iterator<IJp2pProperties> iterator = source.propertyIterator();
		configurator.setHttpEnabled( source.isEnabled());
		while( iterator.hasNext() ){
			IJp2pProperties property =iterator.next();
			if( !TransportProperties.isValidProperty(property))
				continue;
			TransportProperties tp = (TransportProperties) property;
			switch( tp ){
			case PORT:
				configurator.setHttpPort( source.getPort());
				break;
			case INCOMING_STATUS:
				configurator.setHttpIncoming( source.getIncomingStatus() );
				break;
			case OUTGOING_STATUS:
				configurator.setHttpOutgoing( source.getOutgoingStatus() );
				break;
			case INTERFACE_ADDRESS:
				configurator.setHttpInterfaceAddress( source.getInterfaceAddress() );
				break;
			case PUBLIC_ADDRESS:
				configurator.setHttpPublicAddress( source.getPublicAddress(), source.getPublicAddressExclusive() );
				break;
			default:
				break;
			}
		}
	}

	public static final void fillHttp2NetworkConfigurator( HttpPropertySource source, NetworkConfigurator configurator ){
		Iterator<IJp2pProperties> iterator = source.propertyIterator();
		configurator.setHttpEnabled( source.isEnabled());
		while( iterator.hasNext() ){
			IJp2pProperties property =iterator.next();
			if( !TransportProperties.isValidProperty(property))
				continue;
			TransportProperties tp = (TransportProperties) property;
			switch( tp ){
			case PORT:
				configurator.setHttp2Port( source.getPort());
				break;
			case START_PORT:
				configurator.setHttp2StartPort((Integer) source.getProperty( property ));
				break;
			case END_PORT:
				configurator.setHttp2EndPort( (Integer) source.getProperty( property ));
				break;
			case INCOMING_STATUS:
				configurator.setHttp2Incoming( source.getIncomingStatus() );
				break;
			case OUTGOING_STATUS:
				configurator.setHttp2Outgoing( source.getOutgoingStatus() );
				break;
			case INTERFACE_ADDRESS:
				configurator.setHttp2InterfaceAddress( source.getInterfaceAddress() );
				break;
			case PUBLIC_ADDRESS:
				configurator.setHttp2PublicAddress( source.getPublicAddress(), source.getPublicAddressExclusive() );
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
			case INCOMING_STATUS:
			case OUTGOING_STATUS:
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