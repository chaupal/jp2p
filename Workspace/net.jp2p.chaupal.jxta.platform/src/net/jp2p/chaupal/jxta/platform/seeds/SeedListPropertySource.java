/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.chaupal.jxta.platform.seeds;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Properties;

import net.jp2p.container.properties.AbstractJp2pWritePropertySource;
import net.jp2p.container.properties.IJp2pDirectives.Directives;
import net.jp2p.container.properties.IJp2pProperties;
import net.jp2p.container.properties.IJp2pPropertySource;
import net.jp2p.container.properties.IPropertyConvertor;
import net.jp2p.container.properties.ManagedProperty;
import net.jp2p.container.utils.IOUtils;
import net.jp2p.container.utils.StringProperty;
import net.jp2p.container.utils.StringStyler;
import net.jp2p.jxta.factory.IJxtaComponents.JxtaPlatformComponents;
import net.jxta.platform.NetworkConfigurator;

public class SeedListPropertySource extends AbstractJp2pWritePropertySource {

	protected static final String S_SEEDS = "/JP2P-INF/seeds.txt";

	/**
	 * Supported default properties for Multicast
	 * 
	 */
	public enum SeedListProperties implements IJp2pProperties{
		MAX_CLIENTS,
		SEEDING_URIS,
		SEED_URIS,
		USE_ONLY_THESE_SEEDS;
	
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
			for( SeedListProperties prop: values() ){
				if( prop.equals( property ))
					return true;
			}
			return false;
		}

		public static SeedListProperties convertTo( String str ){
			String enumStr = StringStyler.styleToEnum( str );
			return valueOf( enumStr );
		}
	}

	private boolean hasSeeds;
	private Class<?> clss;
	
	public SeedListPropertySource( String componentName, IJp2pPropertySource<IJp2pProperties> parent ) {
		super( componentName, parent );
	}

	public SeedListPropertySource( IJp2pPropertySource<IJp2pProperties> parent, Class<?> clss ) {
		this( JxtaPlatformComponents.SEED_LIST.toString(), parent );
		this.hasSeeds = false;
		this.clss = clss;
		this.fillProperties( clss );
		super.setDirective( Directives.CREATE, Boolean.TRUE.toString());
	}
	
	protected void fillProperties( Class<?> clss ){
		InputStream in = clss.getResourceAsStream( S_SEEDS );
		if( in == null )
			return;
		Properties props = new Properties();
		SeedInfo seedInfo = new SeedInfo();
		try {
			props.load( in );
			Enumeration<?> enm = props.keys();
			String key, value;
			while( enm.hasMoreElements() ){
				key = ( String )enm.nextElement();
				value = (String) props.get(key);
				seedInfo.parse(key, value );
				if( seedInfo.isCommentedOut() )
					continue;
				this.setProperty( new StringProperty( key ), seedInfo );
				
			}
		} catch (IOException e) {
			throw new RuntimeException( e );
		}
		finally{
			IOUtils.closeInputStream( in );
		}
	}
	
	/**
	 * Get the class file of the parent
	 * @return
	 */
	public Class<?> getParentClass(){
		return clss;
	}
	
	public boolean setProperty(IJp2pProperties id, SeedInfo value ) {
		ManagedProperty<IJp2pProperties, Object> prop = this.getOrCreateManagedProperty(id, value, false);
		prop.setCategory(S_SEEDS);
		this.hasSeeds = true;
		return true;
	}

	/**
	 * Returns true if this seedlist
	 * @return
	 */
	public boolean hasSeeds() {
		return hasSeeds;
	}

	
	@Override
	public boolean validate(IJp2pProperties id, Object value) {
		return false;
	}

	@Override
	public IPropertyConvertor<IJp2pProperties, String, Object> getConvertor() {
		return new Convertor( this );
	}

	/**
	 * Set the values for realys
	 * @param source
	 * @param configurator
	 */
	public static final void fillRelayNetworkConfigurator( SeedListPropertySource source, NetworkConfigurator configurator ){
		Iterator<IJp2pProperties> iterator = source.propertyIterator();
		while( iterator.hasNext() ){
			SeedListProperties property = (SeedListProperties) iterator.next();
			switch( property ){
			case MAX_CLIENTS:
				configurator.setRelayMaxClients((int) source.getProperty( property ));
				break;
			case SEED_URIS:
				break;
			case SEEDING_URIS:
				break;
			case USE_ONLY_THESE_SEEDS:
				configurator.setUseOnlyRelaySeeds( (boolean) source.getProperty( property ) );
				break;
			default:
				break;
			}
		}
	}

	/**
	 * Set the values for realys
	 * @param source
	 * @param configurator
	 */
	public static final void fillRendezvousNetworkConfigurator( SeedListPropertySource source, NetworkConfigurator configurator ){
		Iterator<IJp2pProperties> iterator = source.propertyIterator();
		while( iterator.hasNext() ){
			SeedListProperties property = (SeedListProperties) iterator.next();
			switch( property ){
			case MAX_CLIENTS:
				configurator.setRendezvousMaxClients((int) source.getProperty( property ));
				break;
			case SEED_URIS:
				break;
			case SEEDING_URIS:
				break;
			case USE_ONLY_THESE_SEEDS:
				configurator.setUseOnlyRendezvousSeeds( (boolean) source.getProperty( property ) );
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
		public SeedListProperties getIdFromString(String key) {
			return SeedListProperties.valueOf( key );
		}

		@Override
		public String convertFrom(IJp2pProperties id) {
			SeedListProperties property = ( SeedListProperties )id;
			Object retval = getProperty( property );
			switch( property ){
			case USE_ONLY_THESE_SEEDS:
				return String.valueOf(( Boolean )retval );
			case MAX_CLIENTS:
				return String.valueOf(( Integer )retval );
			case SEED_URIS:
			case SEEDING_URIS:
			default:
				break;
			}
			return super.convertFrom(id);
		}

		@Override
		public Object convertTo(IJp2pProperties id, String value) {
			SeedListProperties property = ( SeedListProperties )id;
			switch( property ){
			case USE_ONLY_THESE_SEEDS:
				return Boolean.valueOf( value );
			case MAX_CLIENTS:
				return Integer.valueOf( value );
			case SEED_URIS:
			case SEEDING_URIS:
				return Integer.valueOf( value );
			default:
				break;
			}
			return super.convertTo(id, value);
		}		
	}

}