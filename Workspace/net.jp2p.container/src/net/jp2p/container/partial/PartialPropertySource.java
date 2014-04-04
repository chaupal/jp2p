/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.container.partial;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import net.jp2p.container.properties.IJp2pDirectives;
import net.jp2p.container.properties.IJp2pProperties;
import net.jp2p.container.properties.IJp2pPropertySource;
import net.jp2p.container.properties.IJp2pWritePropertySource;
import net.jp2p.container.properties.ManagedProperty;
import net.jp2p.container.properties.PropertySourceWrapper;
import net.jp2p.container.utils.StringStyler;
import net.jp2p.container.utils.Utils;

/**
 * A partial property source breaks a parent up in distinct parts, based on the existence of dots (_8) in the given properties
 * TODO: This approach is not very nice yet. Reconsider this idea
 * @author Kees
 *
 * @param <IJp2pComponents>
 * @param <U>
 */
public class PartialPropertySource extends PropertySourceWrapper<IJp2pProperties>
implements  IJp2pWritePropertySource<IJp2pProperties>{

	private int offset;
	private String componentName;

	private Collection<IJp2pPropertySource<?>> children;

	@SuppressWarnings("unchecked")
	protected PartialPropertySource( IJp2pPropertySource<IJp2pProperties> parent, int offset ) {
		super( (IJp2pPropertySource<IJp2pProperties>) selectParent( parent ));
		this.offset = offset;
		children = new ArrayList<IJp2pPropertySource<?>>();
	}

	public PartialPropertySource( IJp2pPropertySource<IJp2pProperties> parent) {
		this( parent, -1);
	}

	@SuppressWarnings("unchecked")
	PartialPropertySource( String componentName,  IJp2pPropertySource<IJp2pProperties> parent, int offset ) {
		super( (IJp2pPropertySource<IJp2pProperties>) selectParent( parent ));
		this.offset = offset;
		this.componentName = componentName;
		children = new ArrayList<IJp2pPropertySource<?>>();
	}

	public PartialPropertySource( String componentName,  IJp2pPropertySource<IJp2pProperties> parent) {
		this( componentName, parent, -1 );
	}

	/**
	 * Select the parent of this property source. Always take over the parent of a partial property source
	 * @param parent
	 * @return
	 */
	private static final IJp2pPropertySource<?> selectParent( IJp2pPropertySource<?> parent ){
		if( parent instanceof PartialPropertySource)
			return parent.getParent();
		return parent;
	}

	@Override
	public String getId() {
		return  Utils.isNull( this.componentName )? super.getId(): null;
	}

	@Override
	public IJp2pPropertySource<?> getParent() {
		return super.getSource();
	}

	public String getCategory() {
		if( offset < 0 )
			return this.componentName;
		String[] split = this.componentName.split("[.]");
		return split[offset];
	}

	@Override
	public IJp2pProperties getIdFromString(String key) {
		String cat = this.getCategory().toLowerCase();
		String id = StringStyler.styleToEnum( cat + "." + key.toLowerCase() );
		return super.getSource().getIdFromString( id );
	}

	@Override
	public String getComponentName() {
		String cat = this.getCategory();
		return Utils.isNull(cat)?super.getComponentName(): cat;
	}

	protected int getOffset() {
		return offset;
	}

	
	@Override
	public int getDepth() {
		return super.getDepth() + 1;
	}

	/**
	 * returns true if the given id is valid for this partial property source
	 * @param id
	 * @return
	 */
	protected boolean isValidId( IJp2pProperties id ){
		if( id == null )
			return false;
		String check =  id.toString().toLowerCase();
		String cat = this.getCategory();
		if( Utils.isNull( cat )){
			return ( check.indexOf(".") < 0 );
		}else{
			String str = ( cat.toLowerCase() + "." ); 
			return check.startsWith(str);
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public ManagedProperty<IJp2pProperties,Object> getOrCreateManagedProperty(IJp2pProperties id, Object value, boolean derived) {
		if(!isValidId(id))
			return null;
		IJp2pWritePropertySource<IJp2pProperties> source = (IJp2pWritePropertySource<IJp2pProperties>) this.getParent();
		ManagedProperty<IJp2pProperties, Object> mp = source.getOrCreateManagedProperty(id, value, derived);
		mp.setCategory( this.getCategory());
		return mp;
	}

	@Override
	public ManagedProperty<IJp2pProperties, Object> getManagedProperty(IJp2pProperties id) {
		if(!isValidId(id))
			return null;
		return super.getManagedProperty(id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Iterator<IJp2pProperties> propertyIterator() {
		Iterator<IJp2pProperties> iterator  = (Iterator<IJp2pProperties>) this.getParent().propertyIterator();
		Collection<IJp2pProperties> ids = new ArrayList<IJp2pProperties>();
		while( iterator.hasNext() ){
			IJp2pProperties id = iterator.next();
			if( isValidId(id) )
				ids.add(id);
		}
		return ids.iterator();
	}

	@Override
	public boolean validate(IJp2pProperties id, Object value) {
		if(!isValidId(id))
			return false;
		return false;
	}

	@Override
	public Iterator<IJp2pDirectives> directiveIterator() {
		if( Utils.isNull( this.componentName ))
			return super.directiveIterator();
		Collection<IJp2pDirectives> col = new ArrayList<IJp2pDirectives>();
		return col.iterator();
	}

	protected static boolean canExpand( IJp2pPropertySource<IJp2pProperties> source ){
		Iterator<IJp2pProperties> iterator  = source.propertyIterator();
		while( iterator.hasNext() ){
			IJp2pProperties id = iterator.next();
			String check =  StringStyler.prettyString( id.name() );
			int index = check.indexOf(".");
			if( index >= 0 )
				return true;
			}
		return false;
	}

	public static IJp2pPropertySource<IJp2pProperties> expand( IJp2pPropertySource<IJp2pProperties> parent ){
		if((!canExpand( parent ) || ( parent instanceof PartialPropertySource )))
			return parent;
		PartialPropertySource root = new PartialPropertySource( parent );
		expand( parent, root );
		return root;
	}

	protected static void expand( IJp2pPropertySource<IJp2pProperties> parent, PartialPropertySource current ){
		Iterator<IJp2pProperties> iterator  = parent.propertyIterator();
		while( iterator.hasNext() ){
			IJp2pProperties id = iterator.next();
			int offset = calculateOffset( id );
			if( offset <= current.getOffset() ) 
				continue;
			String ct = getCategory( id, offset );
			if( Utils.isNull( ct ))
				continue;
			if( !hasChildWithName( current, ct )){
				PartialPropertySource child = new PartialPropertySource( id.toString(), parent, current.getOffset() + 1 ); 
				expand( parent, child );
				current.addChild( child);
			}
		}
	}

	/**
	 * Returns true if the given souce has a child with the given name
	 * @param source
	 * @param name
	 * @return
	 */
	protected static boolean hasChildWithName( IJp2pPropertySource<IJp2pProperties> source, String name ){
		for( IJp2pPropertySource<?> child: source.getChildren() ){
			if( child.getComponentName().equals( name ))
				return true;
		}
		return false;
		
	}
	
	/**
	 * Calculate the offset for the given id. returns a negative value if no offset exists
	 * @param id
	 * @return
	 */
	protected static int calculateOffset( IJp2pProperties id ){
		String check =  StringStyler.prettyString( id.name() );
		int index = check.indexOf(".");
		if( index < 0 )
			return index;
		String[] split = check.split("[.]");
		return split.length - 2;
	}

	/**
	 * Calculate the offset for the given id
	 * @param id
	 * @return
	 */
	protected static String getCategory( IJp2pProperties id, int offset ){
		if( offset < 0 )
			return null;
		String check =  StringStyler.prettyString( id.name() );
		int index = check.indexOf(".");
		if( index < 0 )
			return check;
		String[] split = check.split("[.]");	
		if( offset >= split.length - 1)
			return null;
		return split[ offset ];
	}

	@Override
	public boolean setProperty(IJp2pProperties id, Object value) {
		IJp2pWritePropertySource<IJp2pProperties> source = (IJp2pWritePropertySource<IJp2pProperties>) super.getSource();
		return source.setProperty(id, value);
	}

	@Override
	public boolean setDirective(IJp2pDirectives id, String value) {
		IJp2pWritePropertySource<IJp2pProperties> source = (IJp2pWritePropertySource<IJp2pProperties>) super.getSource();
		return source.setDirective(id, value);
	}

	@Override
	public boolean addChild( IJp2pPropertySource<?> child ){
		return this.children.add( child );
	}

	@Override
	public void removeChild( IJp2pPropertySource<?> child ){
		this.children.remove( child );
	}

	@Override
	public IJp2pPropertySource<?>[] getChildren() {
		return this.children.toArray(new IJp2pPropertySource[children.size()]);
	}
}
