/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/

package net.jp2p.jxse.compatibility.utils;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import net.jp2p.jxse.compatibility.service.MessageBoxPetitioner;
import net.jp2p.jxse.compatibility.service.MessageBoxPetitioner.MessageTypes;
import net.jxta.document.Attributable;
import net.jxta.document.Attribute;
import net.jxta.document.Element;
import net.jxta.document.StructuredDocument;
import net.jxta.endpoint.Message;
import net.jxta.endpoint.Message.ElementIterator;
import net.jxta.endpoint.MessageElement;
import net.jxta.peer.PeerID;
import net.jxta.platform.NetworkConfigurator;
import net.jxta.rendezvous.RendezVousService;

public class Tools {

    public Tools() {

    }
    
    public static void popConnectedRendezvous(RendezVousService TheRendezVous, String Name) {
        
        List<PeerID> TheList = TheRendezVous.getLocalRendezVousView();
        Iterator<PeerID> Iter = TheList.iterator();
        int Count = 0;
        
        while (Iter.hasNext()) {
            
            Count = Count + 1;

            PopInformationMessage(Name, "Connected to rendezvous:\n\n"
                    + Iter.next().toString());
            
        }
        
        if (Count==0) {
            
            PopInformationMessage(Name, "No rendezvous connected to this rendezvous!");
            
        }

    }
    
    public static void popConnectedPeers(RendezVousService TheRendezVous, String Name) {
        
        List<PeerID> TheList = TheRendezVous.getLocalRendezVousView();
        Iterator<PeerID> Iter = TheList.iterator();
        int Count = 0;
        
        while (Iter.hasNext()) {
            
            Count = Count + 1;
            
            PopInformationMessage(Name, "Peer connected to this rendezvous:\n\n"
                    + Iter.next().toString());
            
        }
        
        if (Count==0) {
            
            PopInformationMessage(Name, "No peers connected to this rendezvous!");
            
        }
        
    }
    
    public static void CheckForMulticastUsage(String name, NetworkConfigurator TheNC) throws IOException {
        MessageBoxPetitioner petitioner = MessageBoxPetitioner.getInstance();
    	petitioner.petition( MessageTypes.QUESTION, name, "Do you want to enable multicasting?");
        //if (JOptionPane.YES_OPTION==PopYesNoQuestion(Name, "Do you want to enable multicasting?")) {
        //    TheNC.setUseMulticast(true);          
        //} else {          
        //    TheNC.setUseMulticast(false);         
        //}      
    }
    
    public static void CheckForRendezVousSeedAddition(String name, String theSeed, NetworkConfigurator TheNC) {
        
        MessageBoxPetitioner petitioner = MessageBoxPetitioner.getInstance();
    	petitioner.petition( MessageTypes.QUESTION, name, "Do you want to add seed: " + theSeed + "?");
        //if (JOptionPane.YES_OPTION==PopYesNoQuestion(Name, "Do you want to add seed: " + TheSeed + "?")) {
        //    URI LocalSeedingRendezVousURI = URI.create(TheSeed);
        //    TheNC.addSeedRendezvous(LocalSeedingRendezVousURI);
        //}
    };
    
    public static void PopInformationMessage(String name, String message) {
        
        MessageBoxPetitioner petitioner = MessageBoxPetitioner.getInstance();
    	petitioner.petition( MessageTypes.INFO, name, message );
        //JOptionPane.showMessageDialog(null, Message, Name, JOptionPane.INFORMATION_MESSAGE);
        
    }
    
    public static void PopErrorMessage(String name,String message) {
        MessageBoxPetitioner petitioner = MessageBoxPetitioner.getInstance();
        petitioner.petition( MessageTypes.ERROR, name, message );        
    }
    
    public static void PopWarningMessage(String name, String message) {
        MessageBoxPetitioner petitioner = MessageBoxPetitioner.getInstance();
        petitioner.petition( MessageTypes.WARNING, name, message );        
    }
    
    public static int PopYesNoQuestion(String name, String question) {
        MessageBoxPetitioner petitioner = MessageBoxPetitioner.getInstance();
        petitioner.petition( MessageTypes.QUESTION, name, question ); 
        return ( petitioner.getAnswer() != null )? 1: 0; 
    }
    
    public static void CheckForExistingConfigurationDeletion(String name, File ConfigurationFile) throws IOException {
        MessageBoxPetitioner petitioner = MessageBoxPetitioner.getInstance();
        petitioner.petition( MessageTypes.QUESTION, name, "Do you want to delete the existing configuration in:\n\n"  + ConfigurationFile.getCanonicalPath()); 
        
        //if (JOptionPane.YES_OPTION==PopYesNoQuestion(Name, "Do you want to delete the existing configuration in:\n\n"
        //        + ConfigurationFile.getCanonicalPath())) {

//            NetworkManager.RecursiveDelete(ConfigurationFile);
            
        //}
        
    }
    
    public static void DisplayMessageContent(String Name, Message TheMessage) {
        
        try {
            
            String ToDisplay = "--- Message Start ---\n";

            ElementIterator MyElementIterator = TheMessage.getMessageElements();
            
            while (MyElementIterator.hasNext()) {
                
                MessageElement MyMessageElement = MyElementIterator.next();
                
                ToDisplay = ToDisplay + "Element : " +
                        MyElementIterator.getNamespace() + " :: "
                        + MyMessageElement.getElementName() 
                        + "  [" + MyMessageElement + "]\n";
                
            }
            
            ToDisplay = ToDisplay + "--- Message End ---";
            
            PopInformationMessage(Name,ToDisplay);
            
        } catch (Exception Ex) {
            
            PopErrorMessage(Name, Ex.toString());
            
        }
        
    }
    
    public static final void GoToSleep(long Duration) {
        
        long Delay = System.currentTimeMillis() + Duration;

        while (System.currentTimeMillis()<Delay) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException Ex) {
                // We don't care
            }
        }
        
    }

    /**
     *  Recursively copy elements beginnging with <code>from</code> into the
     *  document identified by <code>intoDoc</code>.
     *
     *  @param intoDoc  the document into which the elements which will be
     *  copied.
     *  @param intoElement  the element which will serve as the parent for
     *  the elements being copied.
     *  @param from the root element of the hierarchy which will be copied.
     *  @param copyAttributes whether the elements' attributes should be copied
     *         or not
     **/
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public static void copyElements(StructuredDocument<?> intoDoc, Element intoElement, Element<?> from, boolean recursive, boolean copyAttributes) {

        // Copying current level element
        Element<?> newElement = intoDoc.createElement(from.getKey(), from.getValue());
        intoElement.appendChild(newElement);

        // Copy attributes (eventually)
        if ( copyAttributes )  {

            if ((from instanceof Attributable) && (newElement instanceof Attributable)) {

                Enumeration<?> eachAttrib = ((Attributable) from).getAttributes();

                while (eachAttrib.hasMoreElements()) {
                    Attribute anAttrib = (Attribute) eachAttrib.nextElement();
                    ((Attributable) newElement).addAttribute(anAttrib.getName(), anAttrib.getValue());
                }

            }

        }

        // Looping through the child elements (eventually)
        if ( recursive ) {

            for (Enumeration<?> eachChild = from.getChildren(); eachChild.hasMoreElements();) {

                // recurse to add the children.
                copyElements(intoDoc, newElement, (Element<?>) eachChild.nextElement(), recursive, copyAttributes);

            }

        }

    }

}
