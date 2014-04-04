package net.jp2p.chaupal.jxta.builder;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Enumeration;

import org.eclipse.core.runtime.FileLocator;

import net.jp2p.chaupal.jxta.builder.ContainerBuilderExtender;
import net.jp2p.container.builder.ContainerBuilder;
import net.jp2p.container.builder.IContainerBuilder;
import net.jp2p.container.factory.IComponentFactory;

public class ContainerBuilderExtender {

	private static final String S_JP2P_INF = "/JP2P-INF/token.txt";

	private static IContainerBuilder builder = new ContainerBuilder();
	
	private ContainerBuilderExtender() {}

	public static IContainerBuilder getInstance(){
		return builder;
	}
	
	public boolean addFactory( IComponentFactory<?> factory ){
		return builder.addFactory(factory);
	}

	public boolean removeFactory( IComponentFactory<Object> factory ){
		return builder.removeFactory(factory);
	}
	
	public static final String[] listFiles() throws URISyntaxException, IOException{
		Class<?> clss = ContainerBuilderExtender.class;
		Enumeration<URL> enm= clss.getClassLoader().getResources(S_JP2P_INF);
		while( enm.hasMoreElements()){
			System.out.println(FileLocator.resolve( enm.nextElement() ).toURI() );
		}
		URL dirURL = clss.getResource(S_JP2P_INF );
	    File file;
		if (dirURL != null ) {
	    	try {
	    	    file = new File(FileLocator.resolve( dirURL).toURI());
	    	    return file.list();
	    	} catch (URISyntaxException e1) {
	    	    e1.printStackTrace();
	    	} catch (IOException e1) {
	    	    e1.printStackTrace();
	    	}	    }
	    return null;
	}
}
