/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.chaupal.xml;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.jp2p.container.partial.PartialPropertySource;
import net.jp2p.container.properties.IJp2pDirectives;
import net.jp2p.container.properties.IJp2pProperties;
import net.jp2p.container.properties.IJp2pPropertySource;
import net.jp2p.container.properties.ManagedProperty;
import net.jp2p.container.properties.PropertySourceUtils;
import net.jp2p.container.utils.StringStyler;
import net.jp2p.container.utils.Utils;

public class XmlComponentBuilder<T extends Object> {

	public static final String DOC_HEAD = "<?xml version='1.0' encoding='UTF-8'?>\n";
	public static final String DOC_PROPERTY = "<properties>\n";
	public static final String DOC_PROPERTY_END = "</properties>\n";
	public static final String DOC_DIRECTIVE = "<directives>\n";
	public static final String DOC_DIRECTIVE_END = "</directives>\n";
	
	public XmlComponentBuilder() {
	}

	@SuppressWarnings("unchecked")
	public final String build( IJp2pPropertySource<T> source ){
		System.err.println( PropertySourceUtils.printPropertySource( source, true ));
		StringBuffer buffer = new StringBuffer();
		buffer.append( DOC_HEAD);
		buildSource( 0, buffer, ( IJp2pPropertySource<IJp2pProperties> )source );
		return buffer.toString();
	}
	
	/**
	 * Build the context
	 * @param source
	 */
	@SuppressWarnings("unchecked")
	protected static void buildSource( int offset, StringBuffer buffer, IJp2pPropertySource<IJp2pProperties> source ){
		IJp2pPropertySource<IJp2pProperties> expand = PartialPropertySource.expand(source );
		String component = StringStyler.xmlStyleString( expand.getComponentName());
		buffer.append( createComponent( offset, component, expand ));
		offset +=2;
		String str = createProperties( offset, expand );
		if( !Utils.isNull( str ))
			buffer.append(str );
		for( IJp2pPropertySource<?> child: expand.getChildren()){
			buildSource( offset, buffer, ( IJp2pPropertySource<IJp2pProperties>)child );
		}
		offset-=2;
		buffer.append( insertOffset( offset ));
		buffer.append( xmlEndTag( component ));
	}

	/**
	 * Create the component
	 * @param offset
	 * @param source
	 * @return
	 */
	private static final String createComponent( int offset, String component, IJp2pPropertySource<IJp2pProperties> source ){
		StringBuffer buffer = new StringBuffer();
		Map<String,String> directives = new HashMap<String, String>();
		if(!Utils.isNull( source.getId() ))
			directives.put( IJp2pDirectives.Directives.ID.toString().toLowerCase(), source.getId() );
		Iterator<?> iterator = source.directiveIterator();
		IJp2pDirectives key;
		Object value;
		while( iterator.hasNext() ){
			key = (IJp2pDirectives) iterator.next();
			value = source.getDirective(key);
			if(( value != null ) && ( !Utils.isNull( value.toString() ))){
				directives.put(key.toString(), value.toString());
			}
		}
		buffer.append( xmlBeginTag( offset, component, directives, true ));
		return buffer.toString();
	}
		
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static final String createProperties( int offset, IJp2pPropertySource source ){
		StringBuffer buffer = new StringBuffer();
		buffer.append( insertOffset( offset));
		buffer.append( DOC_PROPERTY );
		Iterator<Enum> iterator = source.propertyIterator();
		boolean properties = false;
		int newOffset = offset + 2;
		while( iterator.hasNext() ){
			ManagedProperty property = source.getManagedProperty( iterator.next() );
			String str = ( source instanceof PartialPropertySource )? createPartialProperty( newOffset, property): createProperty( newOffset, property);
			if( !Utils.isNull( str )){
				buffer.append( str );
				properties = true;
			}
		}
		for( IJp2pPropertySource<?> child: source.getChildren()){
			buildSource( offset, buffer,  ( IJp2pPropertySource<IJp2pProperties>)child );
		}
		if( !properties )
			return null;
		buffer.append( insertOffset( offset));
		buffer.append( DOC_PROPERTY_END );
		return buffer.toString();
	}
	
	private static String createProperty( int offset, ManagedProperty<?, IJp2pDirectives> property ){
		if(( property == null ) || property.isDerived() || ( property.getValue() == null ))
			return null;
		StringBuffer buffer = new StringBuffer();
		String key = toXmlStyle( property.getKey() );
		buffer.append( xmlBeginTag( offset, key, property.getAttributes(), false));
		buffer.append( property.getValue() );
		buffer.append( xmlParseEndTag( offset, key ));	
		return buffer.toString();
	}

	private static String createPartialProperty( int offset, ManagedProperty<?, IJp2pDirectives> property ){
		if(( property == null ) || property.isDerived() || ( property.getValue() == null ))
			return null;
		StringBuffer buffer = new StringBuffer();
		String key = toXmlStyle( property.getKey() );
		int index = key.indexOf("-8");
		if( index >= 0 )
			key = key.substring(index + 2, key.length());
		buffer.append( xmlBeginTag( offset, key , property.getAttributes(), false));
		buffer.append( property.getValue() );
		buffer.append( xmlParseEndTag( offset, key ));	
		return buffer.toString();
	}

	/**
	 * Create the attributes from the managed property
	 * @param property
	 * @return
	 */
	private static String createAttributes( Map<String, String> attributes ){
		if(( attributes == null ) || ( attributes.size() == 0 ))
			return null;
		StringBuffer buffer = new StringBuffer();
		String value;
		for( String attr: attributes.keySet()){
			value = attributes.get( attr );
			if( Utils.isNull( attr ) || Utils.isNull( value ))
				continue;
			buffer.append( StringStyler.xmlStyleString( attr ) + "=\"" + value + "\" ");
		}
		return buffer.toString();
	}

	/**
	 * Replace the given enum the str
	 * @param enm
	 * @return
	 */
	private static String toXmlStyle( Object enm ){
		String str = enm.toString();
		if( enm instanceof Enum<?> ){
			Enum<?> nm = ( Enum<?> )enm;
			str = nm.name().toLowerCase();
		}
		str = str.replace("_", "-").trim();
		return str;
	}
	
	/**
	 * Get the leading spaces for this source
	 * @param source
	 * @return
	 */
	private static String insertOffset( int offset ){
		StringBuffer buffer = new StringBuffer();
		for( int i=0; i<offset; i++ )
			buffer.append(" ");
		return buffer.toString();
	}
	
	/**
	 * Create an XML begin tag for the given string 
	 * @param str
	 * @return
	 */
	public static String xmlBeginTag( int offset, String str, Map<String, String> attributes, boolean eol ){
		String attrStr = createAttributes(attributes);
		if( Utils.isNull( attrStr ))
			return xmlParseBeginTag( offset, str, eol );
		else{
			return xmlParseBeginTag( offset, str + " " + attrStr, eol );
		}
	}

	private static String xmlParseBeginTag( int offset, String str, boolean eol ){
		StringBuffer buffer = new StringBuffer();
		String[] split = str.split("-8");
		if( split.length == 1 ){
			buffer.append( insertOffset( offset ));
			buffer.append( xmlBeginTag( str, eol ));
			return buffer.toString();
		}
		for( int i=0; i<split.length; i++ ){
			String line = split[i];
			if( Utils.isNull(line))
				continue;
			buffer.append( insertOffset( offset + 2*i  ));
			buffer.append( xmlBeginTag( line, i < split.length - 1 ));
		}
		return buffer.toString();
	}

	/**
	 * Create an XML begin tag for the given string 
	 * @param str
	 * @return
	 */
	public static String xmlBeginTag( String str, boolean eol ){
		String newstr = "<" + str + ">";
		if( eol )
			newstr += "\n";
		return newstr;
	}

	/**
	 * Create an XML end tag for the given string 
	 * @param str
	 * @return
	 */
	private static String xmlEndTag( String str ){
		return "</" + str + ">\n";
	}

	private static String xmlParseEndTag( int offset, String str ){
		StringBuffer buffer = new StringBuffer();
		String[] split = str.split("-8");
		if( split.length == 1 ){
			buffer.append(xmlEndTag( str ));
			return buffer.toString();
		}
		for( int i = split.length-1; i>=0; i-- ){
			String line = split[i];
			if( Utils.isNull( line ))
				continue;
			buffer.append( xmlEndTag( line ));
			if( i > 0 )
				buffer.append( insertOffset( offset + 2*(i-1)));
		}
		return buffer.toString();
	}

}
