package net.jp2p.chaupal.builder;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.jp2p.chaupal.activator.Jp2pBundleActivator;
import net.jp2p.chaupal.context.ContextServiceParser;
import net.jp2p.container.builder.IFactoryBuilder;
import net.jp2p.container.context.IJp2pServiceBuilder;
import net.jp2p.container.context.Jp2pLoaderEvent;
import net.jp2p.container.context.IContextLoaderListener;
import net.jp2p.container.context.Jp2pServiceDescriptor;
import net.jp2p.container.context.Jp2pServiceLoader;

public class Jp2pServiceManager{

	public static final String S_CONTEXT_FOUND = "The following context was found and registered: ";
	public static final String S_INFO_BUILDING = "All the required services have been found. Start to build the container ";
	
	private Jp2pServiceLoader loader;

	private Collection<Jp2pServiceDescriptor> descriptors;
	private Collection<IServiceManagerListener> listeners;
	private Jp2pBundleActivator activator;
	private Collection<ContextServiceParser> parsers;

	private IContextLoaderListener listener = new IContextLoaderListener() {
		
		@Override
		public void notifyRegisterContext(Jp2pLoaderEvent event) {
			logger.info( "Builder registered: " + event.getBuilder().getName() );
			for( Jp2pServiceDescriptor info: descriptors ){
				if( event.getBuilder().hasFactory( info )){
					String context = event.getBuilder().getName();
					if(( context == null ) || ( context.equals( info.getContext() ))){
						info.setContext( event.getBuilder().getName());
						info.setFound( true );
						if( !isCompleted())
							continue;
						logger.info(S_INFO_BUILDING);
						
						for( IServiceManagerListener listener: listeners )
							listener.notifyContainerBuilt( new ServiceManagerEvent( this ));
					}
				}
			}		
		}
		
		@Override
		public void notifyUnregisterContext(Jp2pLoaderEvent event) {
			for( Jp2pServiceDescriptor info: descriptors ){
				if( event.getBuilder().hasFactory(info))
					if(( info.getContext() != null ) || ( event.getBuilder().getName().equals( info.getContext() )))
						info.setFound( false );
			}
		}
	};
	private Logger logger = Logger.getLogger( this.getClass().getName() );

	public Jp2pServiceManager( Jp2pBundleActivator activator, Jp2pServiceLoader contextLoader ) {
		this.loader = contextLoader;
		this.activator = activator;
		this.loader.addContextLoaderListener(listener);
		descriptors = new TreeSet<Jp2pServiceDescriptor>();
		listeners = new ArrayList<IServiceManagerListener>();
		parsers = new ArrayList<ContextServiceParser>();
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
			descriptors.addAll( parser.parse() );
		}
	}

	/**
	 * Update the service descriptor objects that are needed to build the JP2P container,
	 * by checking the available services
	 * @param builder
	 */
	protected void createServiceDescriptors( IJp2pServiceBuilder builder ) {
		
		this.loadServiceDescriptors();
		for( Jp2pServiceDescriptor info: descriptors ){
			if( builder.hasFactory( info ) ){
				String context = builder.getName().toLowerCase();
				if(( info.getContext() == null ) || ( context.equals( info.getContext() ))){
					info.setContext( builder.getName());
					info.setFound( true );
				}
			}
		}
	}

	/**
	 * Build the services
	 * @return 
	 */
	public boolean build(){

		//We first parse the jp2p xml file to see which services we need, and then include the contexts we find
		try {
			this.extendParsers( activator.getClass() );
			this.extendParsers( Jp2pServiceManager.class );
		} catch (IOException e) {
			e.printStackTrace();
		}

		//first we parse the xml files to determine which services we need 
		for(ContextServiceParser parser: parsers ){
			descriptors.addAll( parser.parse() );
		}

		
		
		loader.addContextLoaderListener(listener);
		return true;
	}

	public void close(){
		loader.removeContextLoaderListener(listener);
		listener = null;
	}
	
	/* (non-Javadoc)
	 * @see net.osgi.jp2p.chaupal.xml.IFactoryBuilder#isCompleted()
	 */
	private boolean isCompleted() {
		Logger log = Logger.getLogger( this.getClass().getName() );
		for( Jp2pServiceDescriptor info: descriptors ){
			if( info.isOptional())
				continue;
			if( !info.isFound()){
				log.log( Level.WARNING, "waiting for: " + info.getName());
				return false;
			}
		}
		log.log( Level.INFO, "Building completed: " + activator.getBundleId() );
		return true;
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
			parsers.add( new ContextServiceParser( loader, url, clss ));
		}
	}
}