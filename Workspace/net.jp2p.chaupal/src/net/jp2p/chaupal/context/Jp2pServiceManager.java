package net.jp2p.chaupal.context;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.jp2p.chaupal.activator.Jp2pBundleActivator;
import net.jp2p.chaupal.builder.IFactoryBuilder;
import net.jp2p.container.context.IJp2pFactoryCollection;
import net.jp2p.container.context.Jp2pLoaderEvent;
import net.jp2p.container.context.IContextLoaderListener;
import net.jp2p.container.context.Jp2pServiceDescriptor;
import net.jp2p.container.context.Jp2pServiceLoader;
import net.jp2p.container.factory.IPropertySourceFactory;

public class Jp2pServiceManager implements IJp2pFactoryCollection{

	public static final String S_CONTEXT_FOUND = "The following context was found and registered: ";
	public static final String S_INFO_BUILDING = "\n\nAll the required services have been found. Start to build the container: ";
	
	private Jp2pServiceLoader loader;

	private Collection<FactoryContainer> containers;
	private Collection<IServiceManagerListener> listeners;
	private Jp2pBundleActivator activator;
	private Collection<ContextServiceParser> parsers;

	private boolean completed;

	private IContextLoaderListener listener = new IContextLoaderListener() {
		
		@Override
		public void notifyRegisterContext(Jp2pLoaderEvent event) {
			logger.info( "Builder registered: " + event.getBuilder().getName() );
			switch( event.getType() ){

			case REGISTERED:
				if( completed )
					break;
				updateServiceDescriptors( event.getBuilder() );
				completeRegistration();
				break;
			case UNREGISTERED:
				updateServiceDescriptors( event.getBuilder() );
				break;
			}
		}
		
		@Override
		public void notifyUnregisterContext(Jp2pLoaderEvent event) {
			Collection<FactoryContainer> temp = new ArrayList<FactoryContainer>( containers );
			for( FactoryContainer container: temp ){
				if( !container.containsFactory() )
					continue;
				Jp2pServiceDescriptor info = container.getDescriptor();
				if( event.getBuilder().hasFactory( info ))
					container.removeFactory( event.getBuilder().getFactory(info ));
			}
		}
	};
		
	private Logger logger = Logger.getLogger( this.getClass().getName() );

	public Jp2pServiceManager( Jp2pBundleActivator activator, Jp2pServiceLoader contextLoader ) {
		this.loader = contextLoader;
		this.activator = activator;
		containers = new ArrayList<FactoryContainer>();
		listeners = new ArrayList<IServiceManagerListener>();
		parsers = new ArrayList<ContextServiceParser>();
		this.completed = false;		
	}
	
	@Override
	public String getName() {
		return null;
	}

	/**
	 * Update the service descriptor objects that are needed to build the JP2P container,
	 * by checking the available services
	 * @param builder
	 */
	protected void updateServiceDescriptors( IJp2pFactoryCollection builder ) {
		for( FactoryContainer container: containers ){
			Jp2pServiceDescriptor info = container.getDescriptor();
			if( builder.hasFactory( info ) ){
				container.addFactory( builder.getName(), builder.getFactory(info));
				this.isCompleted();
			}
		}
	}

	/**
	 * Sets and returns true if the registered builders are able to build all the factories from the
	 * list of descriptors
	 */
	private boolean isCompleted() {
		Logger log = Logger.getLogger( this.getClass().getName() );
		for( FactoryContainer container: containers ){
			Jp2pServiceDescriptor info = container.getDescriptor();
			if( info.isOptional())
				continue;
			if( !container.containsFactory() ){
				log.log( Level.WARNING, "waiting for: " + info.getName());
				this.completed = false;
				return completed;
			}else{
				log.log( Level.FINE, "Service registered: " + info.getName());				
			}
		}
		log.log( Level.INFO, "Building completed: " + activator.getBundleId() );
		this.completed = true;
		return this.completed;
	}

	/**
	 * Complete the registration
	 */
	protected void completeRegistration(){
		if( !completed )
			return;
		logger.info(S_INFO_BUILDING + activator.getBundleId() + "\n");
		for( IServiceManagerListener listener: listeners )
			listener.notifyContainerBuilt( new ServiceManagerEvent( this ));		
	}
	
	@Override
	public boolean hasFactory(Jp2pServiceDescriptor descriptor) {
		for( FactoryContainer container: containers ){
			if( !container.containsFactory() )
				continue;
			if( container.getDescriptor().equals( descriptor ))
				return true;
		}
		return this.loader.hasFactory(descriptor);
	}

	/**
	 * Returns true if the manager supports a factory with the given context and name
	 * @param componentName
	 * @return
	 */
	@Override
	public IPropertySourceFactory getFactory( Jp2pServiceDescriptor descriptor  ){
		for( FactoryContainer container: containers ){
			if( !container.containsFactory() )
				continue;
			if( container.getDescriptor().equals( descriptor ))
				return container.getFirst();
		}
		return this.loader.getFactory( descriptor);
	}

	public void addListener( IServiceManagerListener listener ){
		this.listeners.add( listener );
	}

	public void removeListener( IServiceManagerListener listener ){
		this.listeners.remove( listener );
	}

	/**
	 * First we load the service descriptors
	 * by checking the available services
	 * @param builder
	 */
	protected void loadServiceDescriptors() {
		//We parse the jp2p xml file to see which services we need, and then include the contexts we find
		try {
			extendParsers( activator.getClass() );
			extendParsers( Jp2pServiceManager.class );
		} catch (IOException e) {
			e.printStackTrace();
		}

		//first we parse the xml files to determine which services we need 
		for(ContextServiceParser parser: parsers ){
			Collection<Jp2pServiceDescriptor> descriptors = parser.parse();
			for( Jp2pServiceDescriptor descriptor: descriptors )
				containers.add( new FactoryContainer( descriptor ));
		}
	}


	/**
	 * Open the manager
	 * @return 
	 */
	public boolean open(){
		this.loadServiceDescriptors();
		this.updateServiceDescriptors( this.loader );
		completeRegistration();
		loader.addContextLoaderListener(listener);
		return true;
	}

	public void close(){
		loader.removeContextLoaderListener(listener);
		listener = null;
	}

	/**
	 * Allow additional builders to extend the primary builder, by looking at resources with the
	 * similar name and location, for instance provided by fragments
	 * @param clss
	 * @param containerBuilder
	 * @throws IOException
	 */
	private void extendParsers( Class<?> clss ) throws IOException{
		Enumeration<URL> enm = clss.getClassLoader().getResources( IFactoryBuilder.S_DEFAULT_LOCATION );
		while( enm.hasMoreElements()){
			URL url = enm.nextElement();
			parsers.add( new ContextServiceParser( url, clss ));
		}
	}
	
	/**
	 * Lists the correct factories for the given descriptor
	 * @author Kees
	 *
	 */
	private class FactoryContainer{

		private Jp2pServiceDescriptor descriptor;
		private Collection<IPropertySourceFactory> factories;
		
		public FactoryContainer( Jp2pServiceDescriptor descriptor ) {
			super();
			this.descriptor = descriptor;
			factories = new ArrayList<IPropertySourceFactory>();
		}
		
		final Jp2pServiceDescriptor getDescriptor() {
			return descriptor;
		}

		void addFactory( String context, IPropertySourceFactory factory ){
			descriptor.setContext(context);
			this.factories.add( factory );
		}

		void removeFactory( IPropertySourceFactory factory ){
			this.factories.remove( factory );
		}
		
		public boolean containsFactory(){
			return !this.factories.isEmpty();
		}
		
		public IPropertySourceFactory getFirst(){
			return this.factories.iterator().next();
		}
	}
}