/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.jxta.peergroup;

import java.net.URI;
import java.util.HashMap;
import java.util.Stack;

import net.jp2p.container.IJp2pContainer;
import net.jp2p.container.builder.IContainerBuilder;
import net.jp2p.container.component.IJp2pComponent;
import net.jp2p.container.component.IJp2pComponentNode;
import net.jp2p.container.component.Jp2pComponentNode;
import net.jp2p.container.factory.IComponentFactory;
import net.jp2p.container.properties.IJp2pProperties;
import net.jp2p.container.properties.IJp2pPropertySource;
import net.jp2p.container.properties.IJp2pWritePropertySource;
import net.jp2p.container.properties.ManagedProperty;
import net.jp2p.container.properties.IJp2pDirectives.Directives;
import net.jp2p.container.properties.IManagedPropertyListener.PropertyEvents;
import net.jp2p.container.utils.SimpleNode;
import net.jp2p.container.utils.Utils;
import net.jp2p.jxta.advertisement.AdvertisementPropertySource;
import net.jp2p.jxta.advertisement.ModuleClassAdvertisementPropertySource;
import net.jp2p.jxta.advertisement.ModuleImplAdvertisementPropertySource;
import net.jp2p.jxta.advertisement.ModuleSpecAdvertisementPropertySource;
import net.jp2p.jxta.advertisement.AdvertisementPropertySource.AdvertisementTypes;
import net.jp2p.jxta.discovery.DiscoveryPropertySource;
import net.jp2p.jxta.factory.AbstractPeerGroupDependencyFactory;
import net.jp2p.jxta.factory.JxtaFactoryUtils;
import net.jp2p.jxta.factory.IJxtaComponents.JxtaComponents;
import net.jp2p.jxta.peergroup.PeerGroupFactory;
import net.jp2p.jxta.peergroup.PeerGroupPropertySource.PeerGroupDirectives;
import net.jp2p.jxta.peergroup.PeerGroupPropertySource.PeerGroupProperties;
import net.jp2p.jxta.pipe.PipeAdvertisementPropertySource;
import net.jp2p.jxta.rendezvous.RendezVousPropertySource;
import net.jxta.id.IDFactory;
import net.jxta.peer.PeerID;
import net.jxta.peergroup.PeerGroup;
import net.jxta.peergroup.PeerGroupID;
import net.jxta.protocol.ModuleClassAdvertisement;
import net.jxta.protocol.ModuleImplAdvertisement;
import net.jxta.protocol.ModuleSpecAdvertisement;
import net.jxta.protocol.PeerGroupAdvertisement;
import net.jxta.protocol.PipeAdvertisement;

public class PeerGroupFactory extends AbstractPeerGroupDependencyFactory<PeerGroup> 
{
	public PeerGroupFactory() {
		super( JxtaComponents.PEERGROUP_SERVICE.toString());
	}
	
	@Override
	protected PeerGroupPropertySource onCreatePropertySource() {
		return new PeerGroupPropertySource( super.getParentSource());
	}

	
	@Override
	public void extendContainer() {
		IContainerBuilder<?> builder = super.getBuilder();
		RendezVousPropertySource rdvps = (RendezVousPropertySource) JxtaFactoryUtils.getOrCreateChildFactory( (IContainerBuilder<Object>) builder, new HashMap<String, String>(), super.getParentSource(), JxtaComponents.RENDEZVOUS_SERVICE.toString(), true ).getPropertySource();
		rdvps.setDirective( Directives.AUTO_START, this.getPropertySource().getDirective( Directives.AUTO_START ));
		super.extendContainer();
	}

	@Override
	protected void onParseProperty( ManagedProperty<IJp2pProperties, Object> property) {
		if(( !ManagedProperty.isCreated(property)) || ( !PeerGroupProperties.isValidProperty(property.getKey())))
			return;
		PeerGroupProperties id = (PeerGroupProperties) property.getKey();
		switch( id ){
		case PEERGROUP_ID:
			String name = PeerGroupPropertySource.getIdentifier( super.getPropertySource() );
			PeerID peerid = IDFactory.newPeerID( PeerGroupID.defaultNetPeerGroupID, name.getBytes() );
			property.setValue( peerid, PropertyEvents.DEFAULT_VALUE_SET );
			property.reset();
			break;
		default:
			break;
		}
		super.onParseProperty(property);
	}

	@Override
	protected IJp2pComponent<PeerGroup> onCreateComponent( IJp2pPropertySource<IJp2pProperties> source ) {
		PeerGroupAdvertisementPropertySource pgps = (PeerGroupAdvertisementPropertySource) AdvertisementPropertySource.findAdvertisementDescendant(source, AdvertisementTypes.PEERGROUP );
		ModuleSpecAdvertisementPropertySource msps = (ModuleSpecAdvertisementPropertySource) AdvertisementPropertySource.findAdvertisementDescendant(source, AdvertisementTypes.MODULE_SPEC );
		ModuleClassAdvertisementPropertySource mcps = (ModuleClassAdvertisementPropertySource) AdvertisementPropertySource.findAdvertisementDescendant(msps, AdvertisementTypes.MODULE_CLASS );
		PipeAdvertisementPropertySource paps = (PipeAdvertisementPropertySource) AdvertisementPropertySource.findAdvertisementDescendant(pgps, AdvertisementTypes.PIPE );

		String name = (String) super.getPropertySource().getProperty( PeerGroupProperties.NAME );
		String description = (String) super.getPropertySource().getProperty( PeerGroupProperties.DESCRIPTION );
		boolean publish = PeerGroupPropertySource.getBoolean(super.getPropertySource(), PeerGroupDirectives.PUBLISH );

		IJp2pComponent<PeerGroup> component = null;
		try {
			PeerGroup peergroup = createPeerGroupFromModuleImpl( super.getPeerGroup(), super.getPropertySource() );
			PeerGroupAdvertisement pgadv = peergroup.getPeerGroupAdvertisement();
			IJp2pWritePropertySource<IJp2pProperties> ws = (IJp2pWritePropertySource<IJp2pProperties>) super.getPropertySource();
			component = new Jp2pComponentNode<PeerGroup>( super.getPropertySource(), peergroup );
			if( publish ){
				peergroup.publishGroup(name, description);
				return component;
			}
		} catch ( Exception e) {
			e.printStackTrace();
		}
		return component;
	}

	/**
	 * Create a peergroup from an implementation advertisement
	 * @param source
	 * @return
	 * @throws Exception
	 */
	public static PeerGroup createPeerGroupFromModuleImpl( PeerGroup parent, IJp2pPropertySource<IJp2pProperties> source ) throws Exception{
		ModuleImplAdvertisement miad = ModuleImplAdvertisementPropertySource.createModuleImplAdvertisement(null, parent );
		PeerGroupID id = (PeerGroupID)source.getProperty( PeerGroupProperties.PEERGROUP_ID );
		boolean publish = PeerGroupPropertySource.getBoolean( source, PeerGroupDirectives.PUBLISH );
		String name = null;
		String description=  null;
		if( source != null ){
			parent.getDiscoveryService().publish(miad);
			id = (PeerGroupID)IDFactory.fromURI( (URI) source.getProperty( PeerGroupProperties.GROUP_ID ));
			name = (String) source.getProperty( PeerGroupProperties.NAME ); 
			description = (String) source.getProperty( PeerGroupProperties.DESCRIPTION );
		}
		PeerGroup peergroup = parent.newGroup( id, miad, name, description, publish);
		return peergroup;
	}


	/**
	 * Create a peergroup from an implementation advertisement
	 * @param source
	 * @return
	 * @throws Exception
	 */
	public static PeerGroup createPeerGroupFromPeerAds( PeerGroup parent, ModuleSpecAdvertisementPropertySource msps, ModuleClassAdvertisementPropertySource mcps, PipeAdvertisementPropertySource paps, PeerGroupAdvertisementPropertySource pgps ) throws Exception{
		ModuleClassAdvertisement mcad = ModuleClassAdvertisementPropertySource.createModuleClassAdvertisement(mcps );
		parent.getDiscoveryService().publish( mcad );
		PipeAdvertisement pipeAdv = PipeAdvertisementPropertySource.createPipeAdvertisement(paps, parent );
		ModuleSpecAdvertisement msad = ModuleSpecAdvertisementPropertySource.createModuleSpecAdvertisement(msps, mcad, pipeAdv);
		parent.getDiscoveryService().publish( msad );
		PeerGroupAdvertisement pgad = PeerGroupAdvertisementPropertySource.createPeerGroupAdvertisement( pgps, msad);
		PeerGroup peergroup = parent.newGroup( pgad );
		return peergroup;
	}
	
	/**
	 * Extract the peergroup from the given factory, or return null if no peergroup is present 
	 * @param factory
	 * @return
	 */
	public static PeerGroup getPeerGroup( IComponentFactory<?> factory ){
		Object component = factory.createComponent();
		PeerGroup peergroup = null;
		if(  component instanceof PeerGroup )
			peergroup = (PeerGroup) component;
		else if( component instanceof IJp2pComponent ){
			IJp2pComponent<?> comp = (IJp2pComponent<?>) component;
			if( !( comp.getModule() instanceof PeerGroup ))
				return null;
			peergroup = (PeerGroup) comp.getModule();		
		}
		return peergroup;
	}
	
	/**
	 * Returns true if the factory contains the correct peergroup
	 * @param factory
	 * @return
	 */
	public static boolean isCorrectPeerGroup( IJp2pPropertySource<?> current, IComponentFactory<?> factory ){
		if( !isComponentFactory( JxtaComponents.PEERGROUP_SERVICE, factory ) && 
				!isComponentFactory( JxtaComponents.NET_PEERGROUP_SERVICE, factory ))
			return false;
		IJp2pPropertySource<?> ancestor = DiscoveryPropertySource.findPropertySource( current, PeerGroupDirectives.PEERGROUP );
		String peergroupName = null;
		if( ancestor != null ){
			peergroupName = ancestor.getDirective(PeerGroupDirectives.PEERGROUP);
		}
		if( Utils.isNull( peergroupName ))
			peergroupName = PeerGroupPropertySource.S_NET_PEER_GROUP;
		PeerGroup peergroup = PeerGroupFactory.getPeerGroup(factory);
		if( peergroup == null )
			return false;
		return ( peergroupName.toLowerCase().equals( peergroup.getPeerGroupName().toLowerCase()));	
	}

	/**
	 * Return the first peergroup along the ancestors, or the netpeertgroup if none were found
	 * @param current
	 * @param factory
	 * @return
	 */
	public static String findAncestorPeerGroup( IJp2pPropertySource<?> current ){
		String peergroup = PeerGroupPropertySource.findFirstAncestorDirective(current, PeerGroupDirectives.PEERGROUP );
		if( Utils.isNull( peergroup ))
			return PeerGroupPropertySource.S_NET_PEER_GROUP;
		else
			return peergroup;
	}
	
	/**
	 * Create the peergroup structure 
	 * @param container
	 * @return
	 */
	public static PeerGroupNode createPeerGroupTree( IJp2pContainer<?> container){
		Stack<PeerGroupNode> stack = new Stack<PeerGroupNode>();
		findPeerGroups(container, stack);
		for( PeerGroupNode node: stack ){
			if( PeerGroupPropertySource.S_NET_PEER_GROUP.equals( node.getData().getPeerGroupName()))
				return node;
		}
		return null;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private final static void findPeerGroups( IJp2pComponent<?> component, Stack<PeerGroupNode> stack){
		if( component.getModule() instanceof PeerGroup ){
			PeerGroup peergroup = (PeerGroup) component.getModule();
			if( stack.contains( peergroup ))
				return;
			SimpleNode sn = new PeerGroupNode( peergroup );
			for( SimpleNode<PeerGroup, PeerGroup> node: stack ){
				if( node.getData().equals( peergroup.getParentGroup())){
					node.addChild(sn);
					continue;
				}
				PeerGroup parent = node.getData().getParentGroup();
				if( parent == null )
					continue;
				if( parent.equals( peergroup ))
					sn.addChild(node);						
			}
			stack.push((PeerGroupNode) sn);
			if(!( component instanceof IJp2pComponentNode<?,?>) )
				return;
			IJp2pComponentNode<?,?> node = (IJp2pComponentNode<?,?>) component;
			for( IJp2pComponent child: node.getChildren() )
				findPeerGroups(child, stack);
		}
	}

}
