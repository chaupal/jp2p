/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.container.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringStyler 
{

	/**
	 * Style the given string to a form that is used for enum types
	 * @param input
	 * @return
	 */
	public static String styleToEnum( String input ){
		if(( input == null ) || ( input.length() == 0))
			return input;
		String parsed = input.replace(".", "_8");
		parsed = parsed.replace(":", "_1");
		if( input.equals( parsed.toUpperCase() ))
			return input;
		String str = StringStyler.insertSpace( parsed );
		str = str.replace("-", "_");
		str = str.replace(" ", "_");
		return str.toUpperCase();
	}
	
	/**
	 * Utility inserts a space before every caps in a string  
	 * @param str
	 * @return
	 */
	public static String insertSpace( String str )
	{
  	String chr;
  	StringBuffer buffer = new StringBuffer();
  	for( int i = 0; i < str.length(); i++ ){
  		chr = String.valueOf( str.charAt( i ));
  		if(( i > 0 ) && ( chr.matches( "[A-Z]")))
  			buffer.append( " "); 
  		buffer.append( chr );
  	}
  	return buffer.toString();		
	}
	
	/**
	 * Places the insertString before the sequences matching the
	 * given regular expression.
	 * 
	 * @param value
	 * @param insertString
	 * @param regex
	 * @return
	*/
	public static String insertAll( String value, String insertString, String regex )
	{
		Pattern pattern = Pattern.compile( regex );
		StringBuffer buf = new StringBuffer( value );
		Matcher matcher = pattern.matcher( value );
		int start = 0;
		int count = 0;
		while (matcher.find( start )) {
			start = matcher.end() + 1;
			buf.insert( start - 2 + count, "\\" );
			if( start >= buf.length() )
				break;
			if(( matcher.end() - matcher.start() )%2 == 0)
				continue;
			start = matcher.end() + 1;
			count++;
		} 
		return buf.toString();
	}

	/**
	 * Set the first character of the string to uppercase
	 * @param strng
	 * @return
	 */
	public static String firstUpperCaseString( String strng ){
		char chr = strng.charAt(0);
		String str = strng.toString().toLowerCase().substring(1);
		return String.valueOf(chr) + str;		
	}
	
	/**
	 * Create a pretty string from the given one
	 * @param strng
	 * @return
	 */
	public static String prettyString( String strng ){
		strng = strng.replaceAll("_8", ".");
		strng = strng.replaceAll("_1", ":");
		strng = strng.replaceAll(" ", "_");
		String[] split = strng.split("[_]");
		StringBuffer buffer = new StringBuffer();
		for( String str: split ){
			buffer.append( firstUpperCaseString( str ));
		}
		return buffer.toString();
	}

	/**
	 * Create a pretty string from the given one
	 * @param strng
	 * @return
	 */
	public static String xmlStyleString( String strng ){
		String str = styleToEnum(strng);
		if( Utils.isNull( str ))
			return null;
		return str.replace("_", "-").toLowerCase();
	}

	/**
	 * Create a pretty string from the given one
	 * @param strng
	 * @return
	 */
	public static String prettyStringFromXml( String strng ){
		String str = styleToEnum(strng);
		return prettyString(str);
	}

}
