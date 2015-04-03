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
package net.jp2p.chaupal.jxta.platform.security;

import java.net.URI;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.Iterator;

import net.jp2p.container.properties.AbstractJp2pWritePropertySource;
import net.jp2p.container.properties.IJp2pDirectives;
import net.jp2p.container.properties.IJp2pProperties;
import net.jp2p.container.properties.IJp2pPropertySource;
import net.jp2p.container.properties.IPropertyConvertor;
import net.jp2p.container.utils.StringStyler;
import net.jxta.platform.NetworkConfigurator;

public class SecurityPropertySource extends AbstractJp2pWritePropertySource {
	
	/**
	 * Supported default properties for Security
	 * 
	 */
	public enum SecurityProperties implements IJp2pProperties{
		AUTHENTICATION_TYPE,
		CERTFICATE,
		CERTIFICATE_CHAIN,
		KEY_STORE_LOCATION,
		PASSWORD,
		PRINCIPAL,
		PRIVATE_KEY;

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
			for( SecurityProperties prop: values() ){
				if( prop.equals( property ))
					return true;
				if( prop.name().equals( property.name() ))
					return true;
			}
			return false;
		}

		public static SecurityProperties convertTo( String str ){
			String enumStr = StringStyler.styleToEnum( str );
			return valueOf( enumStr );
		}
	}


	public SecurityPropertySource( String componentName, IJp2pPropertySource<IJp2pProperties> parent) {
		super( componentName, parent);
		super.setDirective( IJp2pDirectives.Directives.ENABLED, Boolean.TRUE.toString());
		super.setProperty( SecurityProperties.PRINCIPAL, getBundleId(parent));
	}

	@Override
	public IPropertyConvertor<IJp2pProperties, String, Object> getConvertor() {
		return new Convertor( this );
	}

	public static final void fillNetworkConfigurator( SecurityPropertySource source, NetworkConfigurator configurator ){
		Iterator<IJp2pProperties> iterator = source.propertyIterator();
		while( iterator.hasNext() ){
			IJp2pProperties property =iterator.next();
			if( !SecurityProperties.isValidProperty(property))
				continue;
			SecurityProperties sp = (SecurityProperties) property;
			switch( sp ){
			case AUTHENTICATION_TYPE:
				configurator.setAuthenticationType((String) source.getProperty( property ));
				break;
			case CERTFICATE:
				configurator.setCertificate( (X509Certificate) source.getProperty( property ));
				break;
			case CERTIFICATE_CHAIN:
				configurator.setCertificateChain( (X509Certificate[]) source.getProperty( property ));
				break;
			case KEY_STORE_LOCATION:
				configurator.setKeyStoreLocation( (URI) source.getProperty( property ));
				break;
			case PASSWORD:
				configurator.setPassword( (String) source.getProperty( property ));
				break;
			case PRINCIPAL:
				configurator.setPrincipal( (String) source.getProperty( property ));
				break;
			case PRIVATE_KEY:
				configurator.setPrivateKey( (PrivateKey) source.getProperty( property ) );
				break;
			default:
				break;
			}
		}
	}

	/**
	 * Converts properties to and from Strings
	 * @author Kees
	 *
	 */
	private class Convertor extends SimplePropertyConvertor{

		public Convertor(IJp2pPropertySource<IJp2pProperties> source) {
			super(source);
		}

		@Override
		public SecurityProperties getIdFromString(String key) {
			return SecurityProperties.valueOf( key );
		}

		@Override
		public String convertFrom(IJp2pProperties id) {
			SecurityProperties property = ( SecurityProperties )id;
			Object retval = getProperty( property );
			switch( property ){
			case CERTFICATE:
				return null;
			case CERTIFICATE_CHAIN:
				return null;
			case KEY_STORE_LOCATION:
				return (String) retval;
			case PRIVATE_KEY:
				return null;
			default:
				break;
			}
			return super.convertFrom(id);
		}

		@Override
		public Object convertTo(IJp2pProperties id, String value) {
			SecurityProperties property = ( SecurityProperties )id;
			switch( property ){
			case CERTFICATE:
				return null;
			case CERTIFICATE_CHAIN:
				return null;
			case KEY_STORE_LOCATION:
				return URI.create( value );
			case PRIVATE_KEY:
				return null;
			default:
				break;
			}
			return super.convertTo(id, value);
		}		
	}
}