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
package net.jp2p.chaupal.jxta.platform.multicast;

import java.util.Iterator;

import net.jp2p.container.properties.AbstractJp2pWritePropertySource;
import net.jp2p.container.properties.IJp2pDirectives;
import net.jp2p.container.properties.IJp2pProperties;
import net.jp2p.container.properties.IJp2pPropertySource;
import net.jp2p.container.properties.IPropertyConvertor;
import net.jp2p.container.utils.StringStyler;
import net.jxta.platform.NetworkConfigurator;

public class MulticastPropertySource extends AbstractJp2pWritePropertySource {
	
	/**
	 * Supported default properties for Multicast
	 * 
	 */
	public enum MulticastProperties implements IJp2pProperties{
		ADDRESS,
		INTERFACE,
		POOL_SIZE,
		PORT,
		SIZE,
		STATUS;
	
		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString() );
		}	

		/**
		 * Returns true if the given property is valid for this enumeration
		 * @param property
		 * @return
		 */
		public static boolean isValidProperty( IJp2pProperties property ){
			if( property == null )
				return false;
			for( MulticastProperties prop: values() ){
				if( prop.equals( property ))
					return true;
			}
			return false;
		}

		public static MulticastProperties convertTo( String str ){
			String enumStr = StringStyler.styleToEnum( str );
			return valueOf( enumStr );
		}
	}


	public MulticastPropertySource( String componentName, IJp2pPropertySource<IJp2pProperties> parent) {
		super( componentName, parent);
		super.setDirective( IJp2pDirectives.Directives.ENABLED, Boolean.TRUE.toString());
	}

	/**
	 * Get the port
	 * @return
	 */
	public int getPort(){
		return (int) super.getProperty( MulticastProperties.PORT );
	}

	@Override
	public IPropertyConvertor<IJp2pProperties, String, Object> getConvertor() {
		return new Convertor( this );
	}

	public static final void fillNetworkConfigurator( MulticastPropertySource source, NetworkConfigurator configurator ){
		Iterator<IJp2pProperties> iterator = source.propertyIterator();
		while( iterator.hasNext() ){
			MulticastProperties property = (MulticastProperties) iterator.next();
			switch( property ){
			case ADDRESS:
				configurator.setMulticastAddress((String) source.getProperty( property ));
				break;
			case INTERFACE:
				configurator.setMulticastInterface((String) source.getProperty( property ));
				break;
			case PORT:
				configurator.setMulticastPort( source.getPort());
				break;
			case POOL_SIZE:
				configurator.setMulticastPoolSize((int) source.getProperty( property ));
				break;
			case SIZE:
				configurator.setMulticastSize( (int) source.getProperty( property ));
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
		public MulticastProperties getIdFromString(String key) {
			return MulticastProperties.valueOf( key );
		}

		@Override
		public String convertFrom(IJp2pProperties id) {
			MulticastProperties property = ( MulticastProperties )id;
			Object retval = getProperty( property );
			switch( property ){
			case PORT:
			case SIZE:
			case POOL_SIZE:
			case STATUS:
				return String.valueOf(( Integer )retval );
			default:
				break;
			}
			return super.convertFrom(id);
		}

		@Override
		public Object convertTo(IJp2pProperties id, String value) {
			MulticastProperties property = ( MulticastProperties )id;
			switch( property ){
			case PORT:
			case POOL_SIZE:
			case STATUS:
				return Integer.valueOf( value );
			default:
				break;
			}
			return super.convertTo(id, value);
		}		
	}
}