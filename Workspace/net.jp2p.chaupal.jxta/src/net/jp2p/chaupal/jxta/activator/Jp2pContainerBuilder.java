package net.jp2p.chaupal.jxta.activator;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.logging.Logger;

import org.osgi.service.log.LogService;

import net.jp2p.chaupal.activator.ContainerBuilderEvent;
import net.jp2p.chaupal.activator.IContainerBuilderListener;
import net.jp2p.chaupal.context.ContextServiceParser;
import net.jp2p.chaupal.context.ServiceInfo;
import net.jp2p.chaupal.xml.XMLContainerBuilder;
import net.jp2p.container.IJp2pContainer;
import net.jp2p.container.builder.IFactoryBuilder;
import net.jp2p.container.context.ContextLoader;
import net.jp2p.container.context.ContextLoaderEvent;
import net.jp2p.container.context.IContextLoaderListener;

public class Jp2pContainerBuilder{

	public static final String S_ERR_CONTEXT_FOUND = "The following context was found and registered: ";
	
	private ContextLoader contextLoader;

	private Collection<ServiceInfo> services;
	private Collection<IContainerBuilderListener> listeners;
	private Jp2pBundleActivator activator;
	private Collection<ContextServiceParser> parsers;
	private IJp2pContainer container;
	private IContextLoaderListener listener;

	protected Jp2pContainerBuilder( Jp2pBundleActivator activator, ContextLoader contextLoader ) {
		this.contextLoader = contextLoader;
		this.activator = activator;
		services = new ArrayList<ServiceInfo>();
		listeners = new ArrayList<IContainerBuilderListener>();
		parsers = new ArrayList<ContextServiceParser>();
	}

	
	public IJp2pContainer getContainer() {
		return container;
	}

	public void addContainerBuilderListener( IContainerBuilderListener listener ){
		listeners.add( listener );
	}

	public void removeContainerBuilderListener( IContainerBuilderListener listener ){
		listeners.remove( listener );
	}

	private final void notifyListeners( ContainerBuilderEvent event ){
		for( IContainerBuilderListener listener: listeners )
			listener.notifyContainerBuilt(event);
	}
	
	/**
	 * Build the container
	 */
	public void build(){

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
			public void notifyRegisterContext(ContextLoaderEvent event) {
				activator.getLog().log( LogService.LOG_INFO, S_ERR_CONTEXT_FOUND + event.getContext().getName() );
				for( ServiceInfo info: services ){
					for( String name: event.getContext().getSupportedServices() ){
						if( !info.getName().equals(name))
							continue;
						String context = event.getContext().getName().toLowerCase();
						if(( info.getContext() == null ) || ( context.equals( info.getContext() ))){
							info.setContext( event.getContext().getName());
							info.setFound( true );
							if( buildContainer() )
								return;
						}
					}
				}		
			}
			
			@Override
			public void notifyUnregisterContext(ContextLoaderEvent event) {
				for( String name: event.getContext().getSupportedServices() ){
					for( ServiceInfo info: services ){
						if( !info.getName().equals(name))
							continue;
						if(( info.getContext() != null ) || ( event.getContext().getName().equals( info.getContext() )))
							info.setFound( false );
					}
				}
			}
		};
		
		contextLoader.addContextLoaderListener(listener);		
	}

	public void close(){
		contextLoader.removeContextLoaderListener(listener);
		listener = null;
	}
	
	/* (non-Javadoc)
	 * @see net.osgi.jp2p.chaupal.xml.IFactoryBuilder#isCompleted()
	 */
	private boolean isCompleted() {
		for( ServiceInfo info: services ){
			if( !info.isFound()){
				Logger log = Logger.getLogger( this.getClass().getName() );
				log.info("waiting for: " + info.getName());
				return false;
			}
		}
		return true;
	}

	/**
	 * Try to build the container. Returns false if the container cannot be built
	 */
	private boolean buildContainer(){
		if( !isCompleted())
			return false;

		XMLContainerBuilder builder = new XMLContainerBuilder( activator.getBundleId(), activator.getClass(), contextLoader );
		this.container = builder.build();
		this.notifyListeners( new ContainerBuilderEvent(this, container));
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