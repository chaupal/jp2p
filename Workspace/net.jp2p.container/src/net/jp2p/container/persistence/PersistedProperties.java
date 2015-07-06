/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.container.persistence;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.Properties;

import net.jp2p.container.Jp2pContainerPropertySource;
import net.jp2p.container.properties.AbstractJp2pPropertySource;
import net.jp2p.container.properties.IJp2pProperties;
import net.jp2p.container.properties.IJp2pPropertySource;
import net.jp2p.container.properties.IJp2pWritePropertySource;
import net.jp2p.container.properties.IPropertyConvertor;
import net.jp2p.container.utils.io.IOUtils;
import net.jp2p.container.utils.ProjectFolderUtils;

public class PersistedProperties extends
		AbstractPersistedProperty<IJp2pProperties, String,Object> {

	public static final String S_PROPERTIES = "properties.txt";
	
	public PersistedProperties(IJp2pWritePropertySource<IJp2pProperties> source) {
		super(source);
	}
	
	@Override
	public void setConvertor( IPropertyConvertor<IJp2pProperties, String,Object> convertor ) {
		// TODO Auto-generated method stub		
	}

	@Override
	public void clear(IJp2pPropertySource<IJp2pProperties> source) {
		File file = getPropertyFile(source);
		file.delete();
	}

	@Override
	public String getProperty(IJp2pPropertySource<IJp2pProperties> source, IJp2pProperties id) {
		Properties properties = loadProperties( super.getSource() );
		return properties.getProperty( id.toString() );
	}

	@Override
	public boolean setProperty( IJp2pPropertySource<IJp2pProperties> source, IJp2pProperties id, String value) {
		Properties properties = loadProperties( super.getSource() );
		properties.setProperty(id.toString(), value);
		return saveProperties(source, properties);
	}

	/**
	 * Load the properties from a file location
	 * @param source
	 * @param properties
	 * @throws FileNotFoundException
	 */
	protected static Properties loadProperties( IJp2pPropertySource<IJp2pProperties> source ){
		File file = getPropertyFile(source);
		Properties properties = new Properties();
		FileReader reader = null;
		try {
			if( !file.exists() ){
				file.mkdirs();
				file.createNewFile();
			}

			reader = new FileReader( file );
		} catch (Exception e1) {
			e1.printStackTrace();
			return properties;
		}
		try {
			properties.load(reader);
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally{
			IOUtils.closeReader( reader );
		}
		return properties;
	}

	/**
	 * Load the properties from a file location
	 * @param source
	 * @param properties
	 * @throws FileNotFoundException
	 */
	protected static boolean saveProperties( IJp2pPropertySource<IJp2pProperties> source, Properties properties ){
		File file = getPropertyFile(source);
		FileWriter writer = null;
		try {
			writer = new FileWriter( file );
			properties.store(writer, Calendar.getInstance().getTime().toString());
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally{
			IOUtils.closeWriter( writer );
		}
		return false;
	}

	/**
	 * Get the property file
	 * @param source
	 * @return
	 */
	protected static final File getPropertyFile( IJp2pPropertySource<IJp2pProperties> source ){
		String bundle_id = AbstractJp2pPropertySource.getBundleId( source );
		String str = ProjectFolderUtils.getParsedUserDir( Jp2pContainerPropertySource.DEF_HOME_FOLDER + File.separator + S_PROPERTIES, bundle_id ).getPath();
		return new File( str );
	}
}
