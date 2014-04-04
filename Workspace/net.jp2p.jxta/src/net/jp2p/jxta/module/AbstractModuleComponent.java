/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.jxta.module;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Scanner;
import java.util.logging.Logger;

import net.jp2p.container.component.Jp2pComponent;
import net.jp2p.container.properties.IJp2pProperties;
import net.jp2p.container.properties.IJp2pPropertySource;
import net.jp2p.container.utils.IOUtils;
import net.jp2p.container.utils.Utils;
import net.jxse.module.IJxtaModuleService;
import net.jxta.document.Advertisement;
import net.jxta.document.MimeMediaType;
import net.jxta.document.XMLElement;
import net.jxta.impl.loader.CompatibilityUtils;
import net.jxta.impl.protocol.PlatformConfig;
import net.jxta.platform.Module;
import net.jxta.platform.ModuleClassID;
import net.jxta.platform.ModuleSpecID;
import net.jxta.protocol.ModuleImplAdvertisement;

public abstract class AbstractModuleComponent<T extends Module> extends Jp2pComponent<T> implements IJxtaModuleService<T> {

	public static final String S_UTF_8 = "UTF-8";
	public static final String S_HASH = "#";

	public static final String S_RESOURCE_LOCATION = "/services/net.jxta.platform.Module";

    private ModuleImplAdvertisement implAdv;
     
    protected AbstractModuleComponent( IJp2pPropertySource<IJp2pProperties> source, T module  ) {
		super(source, module );
		this.init();
	}
 
    /**
     * Create the advertisement
     * @return
     */
    protected abstract ModuleImplAdvertisement onCreateAdvertisement();
    
    private final void init(){
    	this.implAdv = this.onCreateAdvertisement();
    }
    
	@Override
	public String getIdentifier() {
		return super.getPropertySource().getComponentName();
	}

	@Override
	public abstract ModuleClassID getModuleClassID();

	@Override
	public ModuleSpecID getModuleSpecID() {
		return implAdv.getModuleSpecID();
	}

	@Override
	public ModuleImplAdvertisement getModuleImplAdvertisement() {
		return implAdv;
	}

	@Override
	public String getDescription() {
		return implAdv.getDescription();
	}

	
	@Override
	public String getRepresentedClassName() {
		return implAdv.getCode();
	}

	/**
	 * Get the XML document that describes the module
	 * @return
	 */
	public abstract Advertisement getAdvertisement( PlatformConfig config );

   /**
     * Register instance classes given a URL to a file containing modules which
     * must be found on the current class path. Each line of the file contains a 
     * module spec ID, the class name and the Module description. The fields are 
     * separated by whitespace. Comments are marked with a '#', the pound sign. 
     * Any text following # on any line in the file is ignored.
     *
     * @param specID ModuleSpecID that we are seeking implementations of
     * @param providers URL to a resource containing a list of providers.
     * @return list of discovered ModuleImplAdvertisements for the specified
     *  ModuleSpecID, or null if no results were found.
     */
    protected static Collection<ModuleImplAdvertisement> locateModuleImplementations( URL providers) {

        Logger logger = Logger.getLogger( AbstractModuleComponent.class.getName());

        Collection<ModuleImplAdvertisement> result = new ArrayList<ModuleImplAdvertisement>();
        InputStream urlStream = null;
        try {
            urlStream = providers.openStream();
   		}
		catch( Exception ex ){
			ex.printStackTrace();
			IOUtils.closeInputStream( urlStream);
			return null;
		}
        String provider;
        Scanner scanner = new Scanner( urlStream );
        try{
        	while( scanner.hasNextLine() ){
        		provider = scanner.nextLine();
        		if( Utils.isNull( provider ))
        			continue;

        		int comment = provider.indexOf( S_HASH );
        		if (comment != -1) {
        			provider = provider.substring(0, comment);
        		}
        		provider = provider.trim();
        		if (0 == provider.length())
        			continue;
        		try {
        			ModuleImplAdvertisement mAdv = null;
        			String[] parts = provider.split("\\s", 3);
        			if (parts.length == 1) {
        				// Standard Jar SPI format:  Class name
        				mAdv = locateModuleImplAdvertisement( parts[0]);
        			} else if (parts.length == 3) {
        				// Current non-standard format: MSID, Class name, Description
        				ModuleSpecID msid = ModuleSpecID.create(URI.create(parts[0]));
        				String code = parts[1];
        				String description = parts[2];
        				mAdv = locateModuleImplAdvertisement( code);
        				if (mAdv == null) {
        					mAdv = CompatibilityUtils.createModuleImplAdvertisement(msid, code, description);
        				}
        		        XMLElement<?> paramElement = (XMLElement<?>) mAdv.getDocument(MimeMediaType.XMLUTF8);
        		        mAdv.setParam(paramElement);
        			} else {
        				logger.severe( "Failed to register: " + provider );
        			}
        			if (mAdv != null) {
        				result.add(mAdv);
        			}
        		} catch (Exception allElse) {
        			logger.severe( "Failed to register:" + provider );
        		}
        	}
        }
        catch( Exception ex ){
        	ex.printStackTrace();
        }
        finally{
        	scanner.close();
        	IOUtils.closeInputStream( urlStream);
        }
        return result;
    }

    /**
     * Attempts to locate the ModuleImplAdvertisement of a module by
     * the use of reflection.
     * 
     * @param className class name to examine
     * @return ModuleImplAdvertisement found by introspection, or null if
     *  the ModuleImplAdvertisement could not be discovered in this manner
     */
    private static ModuleImplAdvertisement locateModuleImplAdvertisement( String className) {
        Logger logger = Logger.getLogger( AbstractModuleComponent.class.getName());
        try {
            Class<?> moduleClass = (Class<?>) Class.forName(className);
            Class<? extends Module> modClass = verifyAndCast(moduleClass);
            Method getImplAdvMethod = modClass.getMethod("getDefaultModuleImplAdvertisement");
            return (ModuleImplAdvertisement) getImplAdvMethod.invoke(null);
        } catch(Exception ex) {
            logger.severe( ": Could not introspect Module for MIA: " + className );
        }
        return null;
    }

    /**
     * Checks that a class is a Module.  If not, it raises a an exception.
     * If it is, it casts the generic class to the subtype.
     * 
     * @param clazz generic class to verify
     * @return Module subtype class
     * @throws ClassNotFoundException if class was not of the proper type
     */
    private static Class<? extends Module> verifyAndCast(Class<?> clazz)
    throws ClassNotFoundException {
        try {
            return clazz.asSubclass(Module.class);
        } catch (ClassCastException ccx) {
            throw(new ClassNotFoundException(
                    "Class found but was not a Module class: " + clazz));
        }
    }
    
    /**
     * get an advertisement from an URL
     * @param url
     * @return
     */
    public static ModuleImplAdvertisement getAdvertisementFromResource( URL url, ModuleClassID id  ){
		Collection<ModuleImplAdvertisement> advs = locateModuleImplementations( url );
		if(( advs == null ) || ( advs.isEmpty() ))
			return null;
		
		for( ModuleImplAdvertisement adv: advs ){
			if( adv.getModuleSpecID().isOfSameBaseClass( id ))
				return adv;
		}
		return null;   	
    }
}
