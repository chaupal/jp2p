/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.container.context;

import java.net.URI;
import java.net.URISyntaxException;

import net.jp2p.container.Jp2pContainerPropertySource;
import net.jp2p.container.IJp2pContainer.ContainerProperties;
import net.jp2p.container.persistence.AbstractPreferences;
import net.jp2p.container.properties.IJp2pProperties;
import net.jp2p.container.properties.IJp2pPropertySource;
import net.jp2p.container.properties.IJp2pWritePropertySource;
import net.jp2p.container.utils.ProjectFolderUtils;
import net.jp2p.container.utils.StringProperty;
import net.jp2p.container.utils.Utils;

public class Jp2pContainerPreferences extends AbstractPreferences<IJp2pProperties, String, Object>
{
	public Jp2pContainerPreferences( Jp2pContainerPropertySource source )
	{
		super( source );
	}

	/* (non-Javadoc)
	 * @see net.osgi.jxta.preferences.IJxtaPreferences#getHomeFolder()
	 */
	public URI getHomeFolder( ) throws URISyntaxException{
		IJp2pPropertySource<IJp2pProperties> source = super.getSource();
		return (URI) source.getProperty( ContainerProperties.HOME_FOLDER );
	}

	public void setHomeFolder( URI homeFolder ){
		IJp2pWritePropertySource<IJp2pProperties> source = (IJp2pWritePropertySource<IJp2pProperties>) super.getSource();
		source.setProperty( ContainerProperties.HOME_FOLDER, homeFolder );
	}
	
	@Override
	public String convertFrom(IJp2pProperties id) {
		Object value = super.getSource().getProperty(id);
		if( value != null )
			return value.toString();
		return null;
	}

	@Override
	public Object convertTo( IJp2pProperties props, String value ){
		if(( props == null ) || ( Utils.isNull(value )))
			return false;
		if( !( props instanceof ContainerProperties )){
			return value;
		}
		ContainerProperties id = (ContainerProperties) props;
		switch( id ){
		case HOME_FOLDER:
			Jp2pContainerPropertySource source = (Jp2pContainerPropertySource) super.getSource();
			String bundleId = source.getBundleId();
			return ProjectFolderUtils.getParsedUserDir(value, bundleId );
		default:
			return value;
		}
	}

	@Override
	public IJp2pProperties getIdFromString(String key) {
		IJp2pProperties id = new StringProperty( key );
		if( ContainerProperties.isValidKey(id ))
			return ContainerProperties.valueOf( key );
		else
			return id;
	}
}
