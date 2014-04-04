/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.jxta.seeds;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Properties;

import net.jp2p.container.properties.AbstractJp2pWritePropertySource;
import net.jp2p.container.properties.IJp2pDirectives.Directives;
import net.jp2p.container.properties.IJp2pProperties;
import net.jp2p.container.properties.IJp2pPropertySource;
import net.jp2p.container.seeds.SeedInfo;
import net.jp2p.container.utils.IOUtils;
import net.jp2p.container.utils.StringProperty;
import net.jp2p.jxta.factory.IJxtaComponents.JxtaNetworkComponents;

public class SeedListPropertySource extends AbstractJp2pWritePropertySource {

	protected static final String S_SEEDS = "/JP2P-INF/seeds.txt";

	private boolean hasSeeds;
	private Class<?> clss;
	
	public SeedListPropertySource( IJp2pPropertySource<IJp2pProperties> parent ) {
		super( JxtaNetworkComponents.SEED_LIST.toString(), parent );
	}

	public SeedListPropertySource( IJp2pPropertySource<IJp2pProperties> parent, Class<?> clss ) {
		this( parent );
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
		boolean retval = super.setProperty(id, value);
		this.hasSeeds |= retval;
		return retval;
	}

	/**
	 * Returns true if this seedlist
	 * @return
	 */
	public boolean hasSeeds() {
		return hasSeeds;
	}

	@Override
	public IJp2pProperties getIdFromString(String key) {
		return new StringProperty( key );
	}

	@Override
	public boolean validate(IJp2pProperties id, Object value) {
		return false;
	}
}