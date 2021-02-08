/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.container.properties;

import net.jp2p.container.utils.Utils;

public class CategoryPropertySource extends AbstractJp2pWritePropertySource implements IJp2pWritePropertySource<IJp2pProperties> {

	public static final String S_DOT_REGEX = "[.]";
	
	String category, id;

	protected CategoryPropertySource( String bundleId, String identifier, String cat ) {
		super( bundleId, cat ); 
		String[] split = cat.split("[.]");
		this.category = split[0];
		this.id = identifier + "." + category;
	}

	public CategoryPropertySource( String cat, IJp2pPropertySource<IJp2pProperties> parent ) {
		super( cat, parent );
		String[] split = cat.split("[.]");
		this.category = split[0];
		this.id = parent.getId() + "." + category;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public String getComponentName() {
		return category;
	}
	
	@Override
	public boolean addChild(IJp2pPropertySource<?> child) {
		return super.addChild(child);
	}

	@Override
	public boolean setDirective(IJp2pDirectives id, String value) {
		return super.setDirective(id, value);
	}

	/**
	 * Returns the category, by looking for '_8' in the id. Returns null if
	 * no category was found 
	 * @param id
	 * @return
	 */
	public String getCategory( String id ){
		String cat = id.toLowerCase().replace("_8", ".");
		String[] split = cat.split("[.]");
		if( split.length <= 1 )
			return null;
		else
			cat.replace(split[ split.length - 1], "");
			return cat.trim();
	}	
	
	public static IJp2pPropertySource<?> createCategoryPropertySource( String category,  IJp2pWritePropertySource<IJp2pProperties> root ){
		String[] split = breakCategory(category);
		if( split == null )
			return null;
		CategoryPropertySource child = new CategoryPropertySource( split[0], root );
		root.addChild(child);
		createCategoryPropertySource(split[1], child);
		return child;
	}
	
	public static CategoryPropertySource findCategoryPropertySource( String category, IJp2pPropertySource<?> source ){
		String[] split = breakCategory( category);
		if( split == null )
			return null;
		for( IJp2pPropertySource<?> ps: source.getChildren() ){
			String name = ps.getComponentName();
			if( name.equals(split[0])){
				CategoryPropertySource child = findCategoryPropertySource( split[2],ps);
				if( child == null )
					return (CategoryPropertySource) ps;
			}
		}
		return null;
	}

	/**
	 * Returns the category, by looking for '_8' in the id. Returns null if
	 * no category was found 
	 * @param id
	 * @return
	 */
	public static String[] breakCategory( String category ){
		if( Utils.isNull( category ))
			return null;
		String[] split = category.split("[.]");
		if( split.length <= 1 )
			return null;
		String[] retval = new String[4];
		retval[0] = split[0];
		String rest = category.replace( split[0] + ".", "");
		retval[1] = rest;
		rest = rest.replace( "." + split[split.length - 1], "").trim();
		retval[2] = rest;
		retval[3] = split[ split.length - 1];
		return retval;
	}
}