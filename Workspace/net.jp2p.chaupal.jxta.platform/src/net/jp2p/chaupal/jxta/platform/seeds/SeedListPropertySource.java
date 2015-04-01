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
import java.util.Properties;

import net.jp2p.chaupal.jxta.platform.seeds.ISeedInfo.SeedTypes;
import net.jp2p.container.properties.AbstractJp2pPropertySource;
import net.jp2p.container.properties.AbstractJp2pWritePropertySource;
import net.jp2p.container.properties.IJp2pDirectives;
import net.jp2p.container.properties.IJp2pDirectives.Directives;
import net.jp2p.container.properties.IJp2pProperties;
import net.jp2p.container.properties.IJp2pPropertySource;
import net.jp2p.container.properties.IPropertyConvertor;
import net.jp2p.container.properties.ManagedProperty;
import net.jp2p.container.utils.IOUtils;
import net.jp2p.container.utils.StringProperty;
import net.jp2p.container.utils.StringStyler;
import net.jp2p.container.utils.Utils;
import net.jp2p.jxta.factory.IJxtaComponents.JxtaPlatformComponents;
import net.jp2p.jxta.transport.TransportPropertySource;
import net.jp2p.jxta.transport.TransportPropertySource.NetworkTypes;
import net.jxta.platform.NetworkConfigurator;

public class SeedListPropertySource extends AbstractJp2pWritePropertySource {

	protected static final String S_SEEDS = "/JP2P-INF/seeds.txt";

	/**
	 * Supported default directives for Seed lists
	 * 
	 */
	public enum SeedListDirectives implements IJp2pDirectives{
		MAX_CLIENTS,
		USE_ONLY;
	
		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString() );
		}	

		/**
		 * Returns true if the given property is valid for this enumeration
		 * @param directive
		 * @return
		 */
		public static boolean isValidDirective( IJp2pDirectives directive ){
			if( directive == null )
				return false;
			for( IJp2pDirectives prop: values() ){
				if( prop.equals( directive ))
					return true;
			}
			return false;
		}

		public static IJp2pDirectives convertTo( String str ){
			String enumStr = StringStyler.styleToEnum( str );
			return valueOf( enumStr );
		}
	}

	/**
	 * Supported default directives for Seed lists
	 * 
	 */
	public enum SeedListAttributes{
		NET;
	
		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString() );
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
		super.setDirective( SeedListDirectives.USE_ONLY, Boolean.FALSE.toString());
		super.setDirective( Directives.ENABLED, Boolean.TRUE.toString());
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
	 * Set the values for relays
	 * @param source
	 * @param configurator
	 */
	public static final void fillRelayNetworkConfigurator( SeedListPropertySource source, NetworkConfigurator configurator ){
		configurator.clearRelaySeedingURIs();
		configurator.clearRelaySeeds();
		boolean useOnly = AbstractJp2pPropertySource.getBoolean( source, SeedListDirectives.USE_ONLY );
		configurator.setUseOnlyRelaySeeds(useOnly);
		
		String str = source.getDirective( SeedListDirectives.MAX_CLIENTS );
		int maxClients = Utils.isNull( str )? 0: Integer.parseInt( str );
		configurator.setRelayMaxClients( maxClients );
	}

	/**
	 * Set the values for relays
	 * @param source
	 * @param configurator
	 */
	public static final void fillRendezvousNetworkConfigurator( SeedListPropertySource source, NetworkConfigurator configurator ){
		configurator.clearRendezvousSeedingURIs();
		configurator.clearRendezvousSeeds();
		boolean useOnly = AbstractJp2pPropertySource.getBoolean( source, SeedListDirectives.USE_ONLY );
		configurator.setUseOnlyRendezvousSeeds(useOnly);

		String str = source.getDirective( SeedListDirectives.MAX_CLIENTS );
		int maxClients = Utils.isNull( str )? 0: Integer.parseInt( str );
		configurator.setRendezvousMaxClients( maxClients );
	}

	/**
	 * The convertor converts the properties to and from their String represntation
	 * @author Kees
	 *
	 */
	private class Convertor extends SimplePropertyConvertor{

		public Convertor(IJp2pPropertySource<IJp2pProperties> source) {
			super(source);
		}

		@Override
		public Object convertTo(IJp2pProperties id, String value) {
			String str = getDirective( Directives.TYPE );
			SeedTypes type = Utils.isNull(str)? SeedTypes.RDV: SeedTypes.valueOf( str.toUpperCase( ));
			ManagedProperty<IJp2pProperties, Object> prop = getOrCreateManagedProperty(id, value, false);	
			str = prop.getAttribute( SeedListAttributes.NET.toString().toLowerCase() );
			TransportPropertySource.NetworkTypes net = Utils.isNull( str )? NetworkTypes.TCP: NetworkTypes.valueOf( str.toUpperCase() );
			return new SeedInfo( type, net, value );
		}		
	}

	/**
	 * Get the seed list type
	 * @param source
	 * @return
	 */
	public static SeedTypes getSeedListType( SeedListPropertySource source ){
		String str = source.getDirective( Directives.TYPE );
		SeedTypes type = Utils.isNull( str )? SeedTypes.RDV: SeedTypes.valueOf( StringStyler.styleToEnum( str ));
		return type;
	}
}