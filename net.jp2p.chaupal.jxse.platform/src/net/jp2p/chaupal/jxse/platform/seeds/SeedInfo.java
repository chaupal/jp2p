/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.chaupal.jxse.platform.seeds;

import java.net.InetAddress;
import java.net.URI;
import java.net.UnknownHostException;

import net.jp2p.container.utils.StringStyler;


public class SeedInfo implements ISeedInfo{

	protected static final String S_LOCALHOST = "localhost";
	 
	private SeedTypes seedType;
	
	private String name;
	private String url;
	
	private boolean commentedOut;
	
	public SeedInfo( String key ) {
		this.commentedOut = false;
		this.seedType = SeedTypes.valueOf( StringStyler.styleToEnum( key ));
	}

	public SeedInfo( String key, String value ) {
		this( key );
		parse( key, value );
	}

	protected void parse( String key, String value ){
		if( key.startsWith("//") || key.startsWith("/*)")){
			this.commentedOut = true;
			return;
		}
		String[] split = value.split("[,]");
		name = split[0].trim();
		url = split[1].trim();
	}

	/**
	 * Returns true id=f the line is commented out
	 * @return
	 */
	public boolean isCommentedOut(){
		return this.commentedOut;
	}
	
	protected String getName() {
		return name;
	}

	@Override
	public SeedTypes getSeedType() {
		return seedType;
	}

	/**
	 * Get the uri corresponding with the given string
	 * @return
	 */
	@Override
	public URI getUri() {
		try {
			return getURIFromString( url );
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Create the correct URI from the given string
	 * @param str
	 * @return
	 * @throws UnknownHostException
	 */
	public static URI getURIFromString( String str ) throws UnknownHostException{
		String uri = str;
		if( uri.toLowerCase().contains( S_LOCALHOST )){
			String localhost = InetAddress.getLocalHost().getHostName();
			uri = uri.replace( S_LOCALHOST, localhost );
		}
		return URI.create(uri);
	}
	
}
