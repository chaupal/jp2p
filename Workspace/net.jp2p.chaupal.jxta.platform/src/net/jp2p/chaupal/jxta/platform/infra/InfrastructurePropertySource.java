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
package net.jp2p.chaupal.jxta.platform.infra;

import java.net.URI;
import java.util.Iterator;

import org.w3c.dom.Element;

import net.jp2p.chaupal.id.IJp2pID;
import net.jp2p.chaupal.platform.INetworkConfigurator;
import net.jp2p.container.properties.AbstractJp2pWritePropertySource;
import net.jp2p.container.properties.IJp2pDirectives;
import net.jp2p.container.properties.IJp2pProperties;
import net.jp2p.container.properties.IJp2pPropertySource;
import net.jp2p.container.properties.IPropertyConvertor;
import net.jp2p.container.utils.StringStyler;
import net.jp2p.jxse.core.id.Jp2pIDFactory;

public class InfrastructurePropertySource extends AbstractJp2pWritePropertySource {
	
	/**
	 * Supported default properties for Multicast
	 * 
	 */
	public enum InfrastructureProperties implements IJp2pProperties{
		NAME,
		DESC,
		DESCRIPTION,
		ID_AS_STRING,
		ID;

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
			for( InfrastructureProperties prop: values() ){
				if( prop.equals( property ))
					return true;
				if( prop.name().equals(property.name() ))
					return true;				
			}
			return false;
		}

		public static InfrastructureProperties convertTo( String str ){
			String enumStr = StringStyler.styleToEnum( str );
			return valueOf( enumStr );
		}
	}

	public InfrastructurePropertySource( String componentName, IJp2pPropertySource<IJp2pProperties> parent) {
		super( componentName, parent);
		super.setDirective( IJp2pDirectives.Directives.ENABLED, Boolean.TRUE.toString());
	}

	@Override
	public IPropertyConvertor<IJp2pProperties, String, Object> getConvertor() {
		return new Convertor( this );
	}

	public static final void fillInfrastructureConfigurator( InfrastructurePropertySource source, INetworkConfigurator configurator ){
		Iterator<IJp2pProperties> iterator = source.propertyIterator();
		while( iterator.hasNext() ){
			InfrastructureProperties property = (InfrastructureProperties) iterator.next();
			switch( property ){
			case NAME:
				configurator.setInfrastructureName(( String ) source.getProperty( property ));
				break;
			case DESC:
				configurator.setInfrastructureDesc(( Element ) source.getProperty( property ));
				break;
			case DESCRIPTION:
				configurator.setInfrastructureDescriptionStr(( String ) source.getProperty( property ));
				break;
			case ID:
				configurator.setInfrastructureID(( IJp2pID ) source.getProperty( property ));
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
		public InfrastructureProperties getIdFromString(String key) {
			return InfrastructureProperties.valueOf( key );
		}

		@Override
		public Object convertTo( IJp2pProperties id, String value ) {
			InfrastructureProperties property = ( InfrastructureProperties )id;
			switch( property ){
			case ID:
				return Jp2pIDFactory.create( URI.create( value ));
			default:
				break;
			}
			return super.convertTo(id, value);
		}		
	}
}
