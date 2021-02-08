/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.container.utils;

import java.io.File;
import java.net.URI;

public class ProjectFolderUtils {

	public static final String S_APPLICATION = "Application";
	public static final String S_FOLDER = ".condastadmin";
	public static final String S_CONFIG = "config";
	public static final String S_ADMIN = "admin";
	public static final String S_USER_HOME_PROPERTY = "user.home";
	public static final String S_JXTA_CACHE = "cache";
	
	public static final String S_USER_HOME = "${user.home}";
	public static final String S_BUNDLE_ID = "${bundle-id}";

	/**
	 * Return the default user directory. This is '%system-user%\<organisation>\'
	 * @param aieon
	 * @return
	 */
	public static URI getPublicDatabaseDir( String folder )
	{
		File file = new File( S_FOLDER + File.separator + 
				folder + File.separator );
		return file.toURI();
	}

	/**
	 * Return the default user directory. This is '%system-user%\<folder>\'
	 * @param aieon
	 * @return 
	 */
	public static URI getDefaultPublicDatabase( String folder, String name )
	{
		File file = new File( S_FOLDER + File.separator + folder + 
				File.separator + name + ".sqlite" );
		return file.toURI();
	}

	/**
	 * Return the default user directory. This is '%system-user%\<folder>\'
	 * @param aieon
	 * @return
	 */
	public static URI getParsedUserDir( String str, String bundle_id )
	{
		String parsed = str.replace( S_USER_HOME, "");
		parsed = parsed.replace( S_BUNDLE_ID, "");
		if( parsed.equals(str))
			return URI.create( str );
		
		String[] split = str.split("[/]");
		StringBuffer buffer = new StringBuffer();
		for( String line: split ){
			if( line.equals( S_USER_HOME ))
				buffer.append( System.getProperty( S_USER_HOME_PROPERTY ));
			else if( line.equals( S_BUNDLE_ID ))
				buffer.append( bundle_id );
			else
				buffer.append( line );
			buffer.append( File.separator );
		}
		return new File( buffer.toString() ).toURI();
	}

	/**
	 * Return the default user directory. This is '%system-user%\<folder>\'
	 * @param aieon
	 * @return
	 */
	public static URI getDefaultUserDir( String folder )
	{
		File file = new File( System.getProperty( S_USER_HOME_PROPERTY ) + File.separator +
				folder + File.separator );
		return file.toURI();
	}

	/**
	 * Return the default user directory. This is '%system-user%\<folder>\<name>.sqlite'
	 * @param aieon
	 * @return 
	 */
	public static URI getDefaultUserDatabase( String folder, String name )
	{
		File file = new File( System.getProperty( S_USER_HOME_PROPERTY ) + File.separator + 
				folder + File.separator +  StringStyler.prettyString( name ) + ".sqlite" );
		return file.toURI();
	}

	/**
	 * Return the default user directory. This is '%system-user%\<folder>\<name>\config'
	 * @param aieon
	 * @return 
	 */
	public static URI getDefaultJxseDir( String folder, String name )
	{
		File file = new File( System.getProperty( S_USER_HOME_PROPERTY ) + File.separator + 
				folder + File.separator + StringStyler.prettyString( name ) + File.separator + S_JXTA_CACHE);
		return file.toURI();
	}

}
