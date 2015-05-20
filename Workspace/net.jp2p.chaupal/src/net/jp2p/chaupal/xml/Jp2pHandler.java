/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.chaupal.xml;

import java.lang.reflect.Constructor;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.jp2p.chaupal.container.ContainerFactory;
import net.jp2p.chaupal.context.Jp2pServiceManager;
import net.jp2p.container.context.Jp2pServiceDescriptor;
import net.jp2p.chaupal.persistence.IContextFactory;
import net.jp2p.chaupal.sequencer.Jp2pBundleSequencer;
import net.jp2p.chaupal.sequencer.SequencerServiceFactory;
import net.jp2p.container.builder.ComponentNode;
import net.jp2p.container.builder.IContainerBuilder;
import net.jp2p.container.context.IJp2pServiceBuilder;
import net.jp2p.container.factory.IComponentFactory;
import net.jp2p.container.factory.IJp2pComponents;
import net.jp2p.container.factory.IPropertySourceFactory;
import net.jp2p.container.properties.AbstractJp2pPropertySource;
import net.jp2p.container.properties.IJp2pDirectives.DeveloperModes;
import net.jp2p.container.properties.IJp2pProperties;
import net.jp2p.container.properties.IJp2pPropertySource;
import net.jp2p.container.properties.IJp2pWritePropertySource;
import net.jp2p.container.properties.ManagedProperty;
import net.jp2p.container.properties.IJp2pDirectives.Directives;
import net.jp2p.container.utils.StringStyler;
import net.jp2p.container.utils.Utils;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

class Jp2pHandler extends DefaultHandler implements IContextEntities{

	private static final String S_ERR_INVALID_BUNDLE = "The parsed bundle-id is wrong";
	
	public static final int MAX_COUNT = 200;	

	private ManagedProperty<IJp2pProperties,Object> property;

	private Jp2pServiceManager manager;
	private Jp2pBundleSequencer<Object> sequencer;
	private FactoryNode node;
	private Class<?> clss;
	private Stack<String> stack;
	private IContainerBuilder<Object> builder;
	private String context = null;
	private String bundle_id;
	private DeveloperModes mode;
	
	private static Logger logger = Logger.getLogger( XMLFactoryBuilder.class.getName() );

	public Jp2pHandler( String bundle_id, DeveloperModes mode, IContainerBuilder<Object> builder, Jp2pServiceManager manager, Jp2pBundleSequencer<Object> sequencer, Class<?> clss ) {
		this.manager = manager;
		this.sequencer = sequencer;
		this.bundle_id = bundle_id;
		this.builder = builder;
		this.clss = clss;
		this.mode = mode;
		this.stack = new Stack<String>();
	}

	@Override
	public void startElement(String uri, String localName, String qName, 
			Attributes attributes) throws SAXException {
		IPropertySourceFactory factory = null;
		
		//First check for groups
		if( Groups.isGroup( qName )){
			stack.push( qName );
			return;
		}
		
		String componentName = StringStyler.prettyStringFromXml( qName );
		Jp2pServiceDescriptor descriptor = new Jp2pServiceDescriptor( componentName, context );
		if( manager.hasFactory( descriptor )){
			factory = manager.getFactory( descriptor );
		}
		
		//The name is not a group. try the default JP2P components
		if( IJp2pServiceBuilder.Components.isComponent( qName )){
			IJp2pComponents current = IJp2pServiceBuilder.Components.valueOf( StringStyler.styleToEnum( qName ));
			switch(( IJp2pServiceBuilder.Components )current ){
			case JP2P_CONTAINER:
				factory = new ContainerFactory( this.bundle_id );
				break;
			case CONTEXT:
				stack.push( qName );
				return;//skip, the contexts were parsed in the first round
			case PERSISTENCE_SERVICE:
				if( factory instanceof IContextFactory ){
					IContextFactory cf = (IContextFactory) factory;
					cf.setManager(manager);
				}		
				break;			
			case SEQUENCER_SERVICE:
				SequencerServiceFactory ssf = (SequencerServiceFactory) factory;
				ssf.setSequencer(sequencer);
			default:
				break;
			}
		}

		//Apparently the factory is available elsewhere
		if( factory == null ){
			factory = this.getFactoryFromClass(qName, attributes, node.getData().getPropertySource());
		}
		
		//The factory is either a service or a property
		if( factory != null ){
			logger.info( "Factory parsed: " + factory.getComponentName());
			IJp2pPropertySource<IJp2pProperties> source = ( node == null )? null: node.getData().getPropertySource(); 
			if( source != null ){
				String bundle = AbstractJp2pPropertySource.getBundleId( source );
				if(  !this.bundle_id.equals( bundle )){
					logger.severe( S_ERR_INVALID_BUNDLE + bundle + " should be " + this.bundle_id );
					return;
				}
			}
			factory.prepare( source, builder, convertAttributes(attributes));
			FactoryNode nd = this.processFactory(attributes, node, factory);
			if( nd != null )
				node = nd;
			this.stack.push( qName );
			return;
		}else{
			try{
				this.property = this.createProperty(qName, attributes );
			}
			catch( Exception ex ){
				logger.log( Level.SEVERE, "\n\n !!!Value " + qName + " incorrectly parsed as property.\n\n\n");
				ex.printStackTrace();
			}
		}
	}

	/**
	 * Create a factory from a class definition
	 * @param componentName
	 * @param attributes
	 * @param parentSource
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected IPropertySourceFactory getFactoryFromClass( String componentName, Attributes attributes, IJp2pPropertySource<?> parentSource ){
		if( attributes.getValue(Directives.CLASS.toString().toLowerCase()) == null )
			return null;
		String className = attributes.getValue(Directives.CLASS.toString().toLowerCase());
		Class<?> factoryClass = null;
		try{
			factoryClass = clss.getClassLoader().loadClass( className );
		}
		catch( ClassNotFoundException ex1 ){
			try{
				factoryClass = XMLFactoryBuilder.class.getClassLoader().loadClass( className );
			}
			catch( ClassNotFoundException ex2 ){ /* do nothing */ }
		}
		if( factoryClass != null  ){
			try {
				Constructor<IComponentFactory<?>> constructor = (Constructor<IComponentFactory<?>>) factoryClass.getDeclaredConstructor(IJp2pPropertySource.class );
				return constructor.newInstance( node.getData().getPropertySource() );
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	/**
	 * Process the factory by adding the directives and adding it to the container
	 * Returns null if the factory is to be skipped (depends on developer mode)
	 * @param attributes
	 * @param parent
	 * @param factory
	 * @return
	 */
	protected synchronized FactoryNode processFactory( Attributes attributes, FactoryNode parent, IPropertySourceFactory factory ){
		logger.log( Level.FINE, "Factory found for: " + factory.getComponentName() );
		IJp2pWritePropertySource<IJp2pProperties> source = (IJp2pWritePropertySource<IJp2pProperties>) factory.createPropertySource();

		DeveloperModes readMode = AbstractJp2pPropertySource.getDeveloperMode(source);
		boolean accept = readMode.equals( this.mode ) || DeveloperModes.ANY.equals( readMode );
		if( !accept )
			return null;
		if( parent != null )
			parent.getData().getPropertySource().addChild( source);
		
		builder.addFactory( factory );
		if( node == null )
			return new FactoryNode( factory );
		else
			return (FactoryNode) node.addChild(factory);	
	}
	
	/**
	 * Create the property
	 * @param qName
	 * @param attributes
	 * @return
	 */
	protected ManagedProperty<IJp2pProperties,Object> createProperty( String qName, Attributes attributes ){
		String id = StringStyler.styleToEnum( qName );
		Object value = null;
		IJp2pWritePropertySource<IJp2pProperties> source = (IJp2pWritePropertySource<IJp2pProperties>) node.getData().getPropertySource();
		ManagedProperty<IJp2pProperties,Object> prop = source.getOrCreateManagedProperty( source.getConvertor().getIdFromString( id ), value, false);
		for( int i=0; i<attributes.getLength(); i++  ){
			if( !Utils.isNull( attributes.getLocalName(i)))
				prop.addAttribute(attributes.getLocalName(i), attributes.getValue(i));
		}
		return prop;
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		if( !stack.peek().equals( qName ))
			return;
		if(!Groups.isGroup(qName ))
			node = (FactoryNode) node.getParent();
		this.property = null;
		this.stack.pop();
	}

	@Override
	public void characters(char ch[], int start, int length) throws SAXException {
		String value = new String(ch, start, length);
		if( Utils.isNull( value  ))
			return;

		if(( property == null ) || ( property.getKey() == null ))
			return;
		IJp2pPropertySource<IJp2pProperties> source = node.getData().getPropertySource();
		source.getConvertor().setPropertyFromConverion( property.getKey(), value);
	}


	@Override
	public void error(SAXParseException e) throws SAXException {
		print(e);
		super.error(e);
	}

	@Override
	public void fatalError(SAXParseException arg0) throws SAXException {
		print(arg0);
		super.fatalError(arg0);
	}

	@Override
	public void warning(SAXParseException arg0) throws SAXException {
		print(arg0);
		super.warning(arg0);
	}


	private MessageFormat message =
		      new MessageFormat("({0}: {1}, {2}): {3}");
	
	private void print(SAXParseException x)
	{
		String msg = message.format(new Object[]
				{
				x.getSystemId(),
				new Integer(x.getLineNumber()),
				new Integer(x.getColumnNumber()),
				x.getMessage()
				});
		Logger.getLogger( this.getClass().getName()).info(msg);
	}
	
	/**
	 * Returns true if one of the attributes allows creation of values
	 * @param attributes
	 * @return
	 */
	protected static boolean isCreated( Attributes attributes ){
		String attr = attributes.getValue( ManagedProperty.Attributes.PERSIST.name().toLowerCase() );
		if( Utils.isNull(attr))
			return false;
		return Boolean.parseBoolean( attr );
	}

	/**
	 * The factory node is used to create a treemap of the property sources 
	 * @author Kees
	 *
	 */
	private class FactoryNode extends ComponentNode<IPropertySourceFactory>{

		protected FactoryNode(IPropertySourceFactory data, FactoryNode parent) {
			super(data, parent);
		}

		public FactoryNode(IPropertySourceFactory data) {
			super(data);
		}

		@Override
		public ComponentNode<?> addChild(Object data) {
			FactoryNode child = new FactoryNode( (IPropertySourceFactory) data, this );
			super.getChildrenAsCollection().add(child);
			return child;
		}	
	}
	
	/**
	 * Convert the attributes to a string map
	 * @param attributes
	 * @return
	 */
	private static Map<String, String> convertAttributes( Attributes attributes ){
		Map<String,String> attrs = new HashMap<String,String>();
		for( int i=0; i<attributes.getLength(); i++  ){
			if( !Utils.isNull( attributes.getLocalName(i))){
				attrs.put( attributes.getLocalName( i ), attributes.getValue(i));
			}
		}
		return attrs;
	}
}