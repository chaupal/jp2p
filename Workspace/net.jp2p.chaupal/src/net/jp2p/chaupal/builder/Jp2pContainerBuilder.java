package net.jp2p.chaupal.builder;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.osgi.service.log.LogService;

import net.jp2p.chaupal.activator.Jp2pBundleActivator;
import net.jp2p.chaupal.context.ContextServiceParser;
import net.jp2p.chaupal.context.ServiceInfo;
import net.jp2p.chaupal.xml.XMLContainerBuilder;
import net.jp2p.container.IJp2pContainer;
import net.jp2p.container.builder.ContainerBuilderEvent;
import net.jp2p.container.builder.IContainerBuilderListener;
import net.jp2p.container.builder.IFactoryBuilder;
import net.jp2p.container.builder.IJp2pContainerBuilder;
import net.jp2p.container.context.Jp2pLoaderEvent;
import net.jp2p.container.context.IContextLoaderListener;
import net.jp2p.container.context.Jp2pServiceLoader;

public class Jp2pContainerBuilder<T extends Object> implements IJp2pContainerBuilder<T>{

	public static final String S_CONTEXT_FOUND = "The following context was found and registered: ";
	public static final String S_INFO_BUILDING = "All the required services have been found. Start to build the container ";
	
	private Jp2pServiceLoader contextLoader;

	private Collection<ServiceInfo> services;
	private Collection<IContainerBuilderListener<T>> listeners;
	private Jp2pBundleActivator activator;
	private Collection<ContextServiceParser> parsers;
	private IJp2pContainer<T> container;
	private IContextLoaderListener listener;
	
	private Logger logger = Logger.getLogger( this.getClass().getName() );

	public Jp2pContainerBuilder( Jp2pBundleActivator activator, Jp2pServiceLoader contextLoader ) {
		this.contextLoader = contextLoader;
		this.activator = activator;
		services = new ArrayList<ServiceInfo>();
		listeners = new ArrayList<IContainerBuilderListener<T>>();
		parsers = new ArrayList<ContextServiceParser>();
	}

	
	public IJp2pContainer<T> getContainer() {
		return container;
	}

	@Override
	public void addContainerBuilderListener( IContainerBuilderListener<T> listener ){
		listeners.add( listener );
	}

	@Override
	public void removeContainerBuilderListener( IContainerBuilderListener<T> listener ){
		listeners.remove( listener );
	}

	private final void notifyListeners( ContainerBuilderEvent<T> event ){
		for( IContainerBuilderListener<T> listener: listeners )
			listener.notifyContainerBuilt(event);
	}
	
	/**
	 * Build the services
	 * @return 
	 */
	public boolean build(){

		//We first parse the jp2p xml file to see which services we need, and then include the contexts we find
		try {
			this.extendParsers( activator.getClass() );
			this.extendParsers( Jp2pContainerBuilder.class );
		} catch (IOException e) {
			e.printStackTrace();
		}

		//first we parse the xml files to determine which services we need 
		for(ContextServiceParser parser: parsers ){
			services.addAll( parser.parse() );
		}

		//We listen until all the services are available
		listener = new IContextLoaderListener() {
			
			@Override
			public void notifyRegisterContext(Jp2pLoaderEvent event) {
				activator.getLog().log( LogService.LOG_INFO, S_CONTEXT_FOUND + event.getBuilder().getName() );
				for( ServiceInfo info: services ){
					for( String name: event.getBuilder().getSupportedServices() ){
						if( !info.getName().equals(name))
							continue;
						String context = event.getBuilder().getName().toLowerCase();
						if(( info.getContext() == null ) || ( context.equals( info.getContext() ))){
							info.setContext( event.getBuilder().getName());
							info.setFound( true );
							if( !isCompleted())
								continue;
							logger.info(S_INFO_BUILDING);
							
							if( buildContainer() )
								return;
						}
					}
				}		
			}
			
			@Override
			public void notifyUnregisterContext(Jp2pLoaderEvent event) {
				for( String name: event.getBuilder().getSupportedServices() ){
					for( ServiceInfo info: services ){
						if( !info.getName().equals(name))
							continue;
						if(( info.getContext() != null ) || ( event.getBuilder().getName().equals( info.getContext() )))
							info.setFound( false );
					}
				}
			}
		};
		
		contextLoader.addContextLoaderListener(listener);
		return true;
	}

	public void close(){
		contextLoader.removeContextLoaderListener(listener);
		listener = null;
	}
	
	/* (non-Javadoc)
	 * @see net.osgi.jp2p.chaupal.xml.IFactoryBuilder#isCompleted()
	 */
	private boolean isCompleted() {
		Logger log = Logger.getLogger( this.getClass().getName() );
		for( ServiceInfo info: services ){
			if( !info.isFound()){
				log.log( Level.WARNING, "waiting for: " + info.getName());
				return false;
			}
		}
		log.log( Level.INFO, "Building completed: " + activator.getBundleId() );
		return true;
	}

	/**
	 * Try to build the container. Returns false if the container cannot be built
	 */
	@SuppressWarnings("unchecked")
	protected boolean buildContainer(){

		XMLContainerBuilder builder = new XMLContainerBuilder( activator.getBundleId(), activator.getClass(), contextLoader );
		this.container = (IJp2pContainer<T>) builder.build();
		this.notifyListeners( new ContainerBuilderEvent<T>(this, container));
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
			parsers.add( new ContextServiceParser( contextLoader, url, clss ));
		}
	}
}