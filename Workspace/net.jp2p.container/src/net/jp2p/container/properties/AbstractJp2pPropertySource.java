/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.container.properties;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;

import net.jp2p.container.Jp2pContainerPropertySource;
import net.jp2p.container.properties.IJp2pDirectives.Contexts;
import net.jp2p.container.properties.IJp2pDirectives.DeveloperModes;
import net.jp2p.container.properties.IJp2pDirectives.Directives;
import net.jp2p.container.properties.IJp2pProperties.Jp2pProperties;
import net.jp2p.container.properties.IManagedPropertyListener.PropertyEvents;
import net.jp2p.container.utils.SimpleComparator;
import net.jp2p.container.utils.StringDirective;
import net.jp2p.container.utils.StringProperty;
import net.jp2p.container.utils.StringStyler;
import net.jp2p.container.utils.Utils;

public abstract class AbstractJp2pPropertySource implements IJp2pPropertySource<IJp2pProperties>, IPropertyEventDispatcher {
	
	public static final String S_RUNTIME = "Runtime"; //used for runtime properties
	public static final String S_DIRECTIVES = "Directives"; 
			
	private Map<IJp2pProperties,ManagedProperty<IJp2pProperties,Object>> properties;
	private Map<IJp2pDirectives,String> directives;
	
	private IJp2pPropertySource<IJp2pProperties> parent;

	private Collection<IJp2pPropertySource<?>> children;
	
	private Collection<IManagedPropertyListener<IJp2pProperties, Object>> listeners;

	private int depth = 0;
	private String componentName;
	
	protected AbstractJp2pPropertySource( String bundleId, String componentName) {
		this( bundleId, componentName, 0);
	}

	protected AbstractJp2pPropertySource( String bundleId, String componentName, int depth ) {
		this.properties = new TreeMap<IJp2pProperties,ManagedProperty<IJp2pProperties,Object>>( new SimpleComparator<IJp2pProperties>());
		this.properties.put( Jp2pProperties.BUNDLE_ID, new ManagedProperty<IJp2pProperties,Object>( this, Jp2pProperties.BUNDLE_ID, bundleId, S_JP2P ));
		this.directives = new TreeMap<IJp2pDirectives,String>(new SimpleComparator<IJp2pDirectives>());
		this.directives.put( IJp2pDirectives.Directives.ID, bundleId + "." + componentName.toLowerCase() );
		this.directives.put( IJp2pDirectives.Directives.CREATE, Boolean.TRUE.toString() );
		this.directives.put( IJp2pDirectives.Directives.ENABLED, Boolean.TRUE.toString() );
		this.componentName = componentName;
		this.children = new ArrayList<IJp2pPropertySource<?>>();
		this.listeners = new ArrayList<IManagedPropertyListener<IJp2pProperties, Object>>();
		this.depth = depth;
		this.parent = null;
	}

	protected AbstractJp2pPropertySource( String componentName, IJp2pPropertySource<IJp2pProperties> parent ) {
		this( getBundleId( parent ), parent.getComponentName(), parent.getDepth() + 1 );
		this.componentName = componentName;
		this.parent = parent;
		this.properties.put( Jp2pProperties.BUNDLE_ID, new ManagedProperty<IJp2pProperties,Object>( this, Jp2pProperties.BUNDLE_ID, parent.getProperty( Jp2pProperties.BUNDLE_ID), S_JP2P ));
		this.directives.put( IJp2pDirectives.Directives.AUTO_START, parent.getDirective(IJp2pDirectives.Directives.AUTO_START ));
		this.directives.put( IJp2pDirectives.Directives.CONTEXT, parent.getDirective(IJp2pDirectives.Directives.CONTEXT ));
		this.directives.put( IJp2pDirectives.Directives.ID, parent.getId() + "." + componentName.toLowerCase() );
	}

	/* (non-Javadoc)
	 * @see net.osgi.jp2p.properties.IPropertyEventDispatcher#addPropertyListener(net.osgi.jp2p.properties.IManagedPropertyListener)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public void addPropertyListener( IManagedPropertyListener<IJp2pProperties, ?> listener ){
		this.listeners.add((IManagedPropertyListener<IJp2pProperties, Object>) listener);
	}

	/* (non-Javadoc)
	 * @see net.osgi.jp2p.properties.IPropertyEventDispatcher#removePropertyListener(net.osgi.jp2p.properties.IManagedPropertyListener)
	 */
	@Override
	public void removePropertyListener( IManagedPropertyListener<IJp2pProperties, ?> listener ){
		this.listeners.remove(listener);
	}

	@Override
	public IJp2pPropertySource<IJp2pProperties> getParent(){
		return this.parent;
	}

	@Override
	public String getId() {
		return this.directives.get( IJp2pDirectives.Directives.ID );
	}

	@Override
	public boolean isEnabled() {
		String directive = this.getDirective( Directives.ENABLED );
		if( directive == null )
			return true;
		return Boolean.valueOf(directive);
	}

	@Override
	public String getComponentName() {
		return this.componentName;
	}

	@Override
	public int getDepth() {
		return depth;
	}

	@Override
	public Object getProperty(IJp2pProperties id) {
		ManagedProperty<IJp2pProperties,Object> select = this.getManagedProperty(id);
		if( select == null )
			return null;
		return select.getValue();
	}

	/**
	 * Get the category for the given key
	 * @param key
	 * @return
	 */
	@Override
	public String getCategory( IJp2pProperties id ){
		ManagedProperty<IJp2pProperties,Object> select = this.getManagedProperty(id);
		return select.getCategory();
		
	}

	public boolean setManagedProperty( ManagedProperty<IJp2pProperties,Object> property ) {
		this.properties.put( property.getKey(), property );
		for( IManagedPropertyListener<IJp2pProperties, Object> listener: this.listeners ){
			property.addPropertyListener(listener);
			listener.notifyValueChanged( new ManagedPropertyEvent<IJp2pProperties, Object>( property, PropertyEvents.DEFAULT_VALUE_SET ));
		}
		return true;
	}

	protected boolean removeManagedProperty( ManagedProperty<IJp2pProperties,Object> property ) {
		this.properties.remove( property );
		for( IManagedPropertyListener<IJp2pProperties, Object> listener: this.listeners )
			property.removePropertyListener(listener);
		return true;
	}

	@Override
	public Object getDefault(IJp2pProperties id) {
		ManagedProperty<IJp2pProperties,Object> select = this.getManagedProperty(id);
		if( select == null )
			return null;
		return select.getDefaultValue();
	}

	@Override
	public ManagedProperty<IJp2pProperties,Object> getManagedProperty( IJp2pProperties id ){
		return properties.get( id );
	}
	
	@Override
	public Iterator<IJp2pProperties> propertyIterator() {
		return this.properties.keySet().iterator();
	}

	@Override
	public String getDirective( IJp2pDirectives id) {
		return directives.get( id );
	}

	/**
	 * Set the directive
	 * @param id
	 * @param value
	 * @return
	 */
	public boolean setDirective( IJp2pDirectives id, String value) {
		if( value == null )
			return false;
		directives.put( id, value );
		return true;
	}

	@Override
	public Iterator<IJp2pDirectives> directiveIterator() {
		return directives.keySet().iterator();
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
	public IJp2pPropertySource<?> getChild( String componentName ){
		for( IJp2pPropertySource<?> child: this.children ){
			if( child.getComponentName().equals(componentName ))
				return child;
		}
		return null;
	}

	@Override
	public boolean isRoot() {
		return ( this.parent == null );
	}

	@Override
	public boolean hasChildren() {
		return !this.children.isEmpty();
	}

	@Override
	public IJp2pPropertySource<?>[] getChildren() {
		return this.children.toArray(new IJp2pPropertySource[children.size()]);
	}
	
	@Override
	public boolean isEmpty(){
		return this.properties.isEmpty();
	}

	@Override
	public IPropertyConvertor<IJp2pProperties, String, Object> getConvertor() {
		return new SimplePropertyConvertor( this);
	}

	@Override
	public boolean validate(IJp2pProperties id, Object value) {
		return false;
	}

	/**
	 * Copy the directives and properties into the given property source.
	 * @param source
	 * @throws CloneNotSupportedException
	 */
	@SuppressWarnings("unchecked")
	public void copy( IJp2pPropertySource<IJp2pProperties> source ) throws CloneNotSupportedException{
		Iterator<IJp2pDirectives> directives = directiveIterator();
		while( directives.hasNext() ){
			IJp2pDirectives directive = directives.next();
			source.setDirective( directive, source.getDirective( directive ));
		}
		Iterator<IJp2pProperties> properties = propertyIterator();
		while( properties.hasNext() ){
			IJp2pProperties property = properties.next();
			source.setManagedProperty( (ManagedProperty<IJp2pProperties, Object>) source.getManagedProperty( property ).clone() );
		}
		
	}
	
	@Override
	public String toString() {
		return super.toString() + "[" + this.getComponentName() + "]";
	}
	
	/**
	 * If true, the service is auto-started
	 * @return
	 */
	public static boolean isAutoStart( IJp2pPropertySource<?> source ){
		return Boolean.parseBoolean( source.getDirective( IJp2pDirectives.Directives.AUTO_START ));		
	}

	/**
	 * Set the parent directive to match that of the child, if the child's direcvtive is set and the parent's is null
	 * @param id
	 * @param source
	 * @return
	 */
	protected static boolean setDirectiveFromParent( IJp2pDirectives id, IJp2pWritePropertySource<?> source ) {
		String directive = source.getParent().getDirective( id );
		if( Utils.isNull( directive ))
			return false;
		return source.setDirective(id, directive);
	}

	/**
	 * Set the parent directive to match that of the child, if the child's direcvtive is set and the parent's is null
	 * @param id
	 * @param source
	 */
	public static void setParentDirective( IJp2pDirectives id, IJp2pPropertySource<?> source ) {
		Object directive = source.getDirective( id );
		IJp2pWritePropertySource<?> parent = (IJp2pWritePropertySource<?>) source.getParent();
		Object parent_autostart = parent.getDirective( id );
		if(( directive != null ) && ( parent_autostart == null ))
			parent.setDirective( id, Boolean.TRUE.toString());
	}
	
	/**
	 * Find the property source with the given component name, starting from the given source
	 * @param source
	 * @param componentName
	 * @return
	 */
	public static  IJp2pPropertySource<?> findPropertySource( IJp2pPropertySource<?> source, String componentName ){
		if( Utils.isNull( componentName ))
			return null;
		if( componentName.equals( source.getComponentName()))
			return source;
		IJp2pPropertySource<?> result;
		for( IJp2pPropertySource<?> child: source.getChildren() ){
			if( child.equals(source))
				continue;
			result = findPropertySource(child, componentName);
			if( result != null )
				return result;
		}
		return null;			
	}

	/**
	 * Find the first ancestor the given directive, starting from the given source
	 * @param source
	 * @param componentName
	 * @return
	 */
	public static IJp2pPropertySource<?> findPropertySource( IJp2pPropertySource<?> source, IJp2pDirectives id, String value ){
		if(( source == null ) || ( id == null ) || ( Utils.isNull( value )))
			return null;
		String directive = source.getDirective(id);
		if( value.equals(directive))
			return source;
		return findPropertySource(source.getParent(), id, value);
	}

	/**
	 * Find the first ancestor the given directive, starting from the given source
	 * @param source
	 * @param componentName
	 * @return
	 */
	public static IJp2pPropertySource<?> findPropertySource( IJp2pPropertySource<?> source, IJp2pDirectives id ){
		if(( source == null ) || ( id == null ))
			return null;
		String directive = source.getDirective(id);
		if( !Utils.isNull( directive))
			return source;
		return findPropertySource( source.getParent(), id );
	}

	/**
	 * Get a boolean value for the given directive
	 * @param source
	 * @param id
	 * @return
	 */
	public static boolean getBoolean( IJp2pPropertySource<?> source, IJp2pDirectives id ){
		String directive = source.getDirective(id);
		if( Utils.isNull( directive ))
			return false;
		return Boolean.parseBoolean( directive);
	}
	
	/**
	 * Get the context of the given property source
	 * @param source
	 * @return
	 */
	public static Contexts getContext( IJp2pPropertySource<?> source ){
		String value = source.getDirective( Directives.CONTEXT );
		if( Utils.isNull( value ))
			return Contexts.JXTA;
		return Contexts.valueOf( StringStyler.styleToEnum( value ));
	}

	/**
	 * Returns true if the property source is a child of the parent
	 * @param source
	 * @return
	 */
	public static boolean isChild( IJp2pPropertySource<?> parent, IJp2pPropertySource<?> source ){
		if( source.getParent() == null )
			return false;
		return source.getParent().equals( parent );
	}

	/**
	 * Get the bundle id for the given source
	 * @param source
	 * @return
	 */
	public static String getBundleId( IJp2pPropertySource<IJp2pProperties> source ){
		return (String) source.getProperty( Jp2pProperties.BUNDLE_ID );
	}

	/**
	 * Get the bundle id for the given source
	 * @param source
	 * @return
	 */
	public static String getIdentifier( IJp2pPropertySource<IJp2pProperties> source ){
		return source.getDirective( IJp2pDirectives.Directives.NAME );
	}

	/**
	 * Get the bundle id for the given source
	 * @param source
	 * @return
	 */
	public static DeveloperModes getDeveloperMode( IJp2pPropertySource<IJp2pProperties> source ){
		String mode = source.getDirective( Directives.DEVELOP_MODE );
		return (mode == null ) ? DeveloperModes.ANY: DeveloperModes.valueOf( mode );
	}

	/**
	 * Get the bundle id for the given source
	 * @param source
	 * @return
	 */
	public static String getType( IJp2pPropertySource<IJp2pProperties> source ){
		return source.getDirective( IJp2pDirectives.Directives.TYPE );
	}

	/**
	 * Get the extended property iterator. This iterator adds the category before the key:
	 *   category.key
	 * @return
	 */
	public static Iterator<IJp2pProperties> getExtendedIterator( IJp2pPropertySource<IJp2pProperties> source) {
		Collection<IJp2pProperties> keys = new ArrayList<IJp2pProperties>();
		Iterator<IJp2pProperties> iterator = source.propertyIterator();
		while( iterator.hasNext()){
			IJp2pProperties prop = iterator.next();
			ManagedProperty<?,?> mp = source.getManagedProperty(prop);
			keys.add( new StringProperty( mp.getCategory() + "." + mp.getKey() ));
		}
		Iterator<IJp2pDirectives> diriterator = source.directiveIterator();
		while( iterator.hasNext()){
			IJp2pDirectives prop = diriterator.next();
			keys.add( new StringProperty( S_DIRECTIVES + "." + prop.toString() ));
		}
		return keys.iterator();
	}

	/**
	 * Get the extended property iterator. This iterator adds the category before the key:
	 *   category.key
	 * @return
	 */
	public static Object getExtendedProperty( IJp2pPropertySource<IJp2pProperties> source, IJp2pProperties key) {
		String[] split = key.toString().split("[.]");
		String id;
		switch( split.length ){
		case 0:
		case 1:
			return source.getProperty(key);
		case 2:
			id = StringStyler.styleToEnum( split[1]);
			if( split[0].equals(S_DIRECTIVES ))
				return source.getDirective( new StringDirective( id ));
			else
				return source.getProperty( new StringProperty( id ));
		default:
			id = StringStyler.styleToEnum( split[ split.length - 1]);
			return source.getProperty( new StringProperty( id ));
		}
	}
	
	/**
	 * Find the first value that was specifically entered for the given directive along the 
	 * ancestors, or null if none was found
	 * @param directive
	 * @return
	 */
	public static String findFirstAncestorDirective( IJp2pPropertySource<?> current, IJp2pDirectives directive ){
		String value = current.getDirective(directive);
		if( !Utils.isNull( value ))
			return value;
		if( current.getParent() == null )
			return null;
		return findFirstAncestorDirective(current.getParent(), directive );
	}

	/**
	 * Find the root property source 
	 * @param current
	 * @return
	 */
	public static Jp2pContainerPropertySource findRootPropertySource( IJp2pPropertySource<?> current ){
		if( current instanceof Jp2pContainerPropertySource )
			return (Jp2pContainerPropertySource) current;
		return findRootPropertySource( current.getParent() );
	}

	/**
	 * Convert the given object to a Jp2p property
	 * @param key
	 * @return
	 */
	public static IJp2pProperties convert(Object key) {
		IJp2pProperties id = null;
		if (!( key instanceof IJp2pProperties ))
			id = new StringProperty( key.toString() );
		else
			id = (IJp2pProperties) key;
		return id;
	}

	protected static class SimplePropertyConvertor implements IPropertyConvertor<IJp2pProperties, String, Object>{

		private IJp2pPropertySource<IJp2pProperties> source;
		
		private static final String S_WRN_NULL_VALUE = "A null value was found for property: ";

		private Logger logger = Logger.getLogger( this.getClass().getName() );
		
		public SimplePropertyConvertor( IJp2pPropertySource<IJp2pProperties> source ) {
			this.source = source;
		}

		@Override
		public IJp2pProperties getIdFromString(String key) {
			return new StringProperty( key );
		}

		@Override
		public String convertFrom( IJp2pProperties id) {
			Object value = source.getProperty(id);
			if( value == null ){
				logger.warning( S_WRN_NULL_VALUE + id );
				return null;
			}
			return value.toString();
		}

		@Override
		public Object convertTo(IJp2pProperties id, String value) {
			return value;
		}

		@Override
		public boolean setPropertyFromConverion(IJp2pProperties id,
				String value) {
			if(!( source instanceof IJp2pWritePropertySource))
				return false;
			IJp2pWritePropertySource<IJp2pProperties> ws = (IJp2pWritePropertySource<IJp2pProperties>) source;
			return ws.setProperty(id, convertTo( id, value));
		}
		
	}

}