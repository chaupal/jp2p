/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.container;

import java.io.File;

import net.jp2p.container.IJp2pContainer.ContainerProperties;
import net.jp2p.container.context.Jp2pContext;
import net.jp2p.container.properties.AbstractJp2pWritePropertySource;
import net.jp2p.container.properties.IJp2pProperties;
import net.jp2p.container.properties.IJp2pProperties.Jp2pProperties;
import net.jp2p.container.utils.ProjectFolderUtils;

public class Jp2pContainerPropertySource extends AbstractJp2pWritePropertySource{

	public static final String DEF_HOME_FOLDER = "${user.home}/.jxse/${bundle-id}";
	
	public Jp2pContainerPropertySource( String bundleId) {
		super( bundleId, Jp2pContext.Components.JP2P_CONTAINER.toString() );
		this.setProperty( ContainerProperties.HOME_FOLDER, ProjectFolderUtils.getParsedUserDir(DEF_HOME_FOLDER, bundleId), false);
	}

	/**
	 * Get the bundle id
	 * @return
	 */
	public String getBundleId(){
		return getBundleId( this );
	}
	
	@Override
	public IJp2pProperties getIdFromString(String key) {
		IJp2pProperties id = super.getIdFromString(key);
		if( id == null )
			return ContainerProperties.valueOf( key );
		else
			return id;
	}

	@Override
	public Object getDefault( IJp2pProperties id) {
		if(!( id instanceof ContainerProperties ))
			return null;
		ContainerProperties cp = (ContainerProperties )id;
		String str = null;
		switch( cp ){
		case HOME_FOLDER:
			String bundle_id = (String) super.getProperty( Jp2pProperties.BUNDLE_ID );
			str = ProjectFolderUtils.getParsedUserDir( DEF_HOME_FOLDER, bundle_id ).getPath();
			File file = new File( str );
			return file.toURI();
		default:
			break;
		}
		return null;
	}

	@Override
	public boolean validate( IJp2pProperties id, Object value) {
		// TODO Auto-generated method stub
		return false;
	}
}
