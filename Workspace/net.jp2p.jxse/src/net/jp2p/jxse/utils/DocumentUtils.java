/**
 * The JXTA protocols often need to refer to peers, peer groups, pipes and other JXTA resources. These references are presented in
 * the protocols as JXTA IDs. JXTA IDs are a means for uniquely identifying specific peer groups, peers, pipes, codat and service
 * instances. JXTA IDs provide unambiguous references to the various JXTA entities. There are six types of JXTA entities which
 * have JXTA ID types defined: peergroups, peers, pipes, codats, module classes and module specifications. Additional JXTA ID
 * types may be defined in the future.
 * JXTA IDs are normally presented as URNs. URNs are a form of URI that ‘... are intended to serve as persistent, locationindependent,
 * resource identifiers’. Like other forms of URI, JXTA IDs are presented as text. See IETF RFC 2141 RFC2141 for
 * more information on URNs.
 *
 *  THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
 *  INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND 
 *  FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL CHAUPAL 
 *  MICROSYSTEMS OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, 
 *  INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT 
 *  LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, 
 *  OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF 
 *  LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING 
 *  NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, 
 *  EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * 
 * @See: JXTA v2.0 Protocols Specification, Chapter 1
 * @author keesp
 * @Organisation: chaupal.org 
 * 
 *******************************************************************************
 * Copyright (c) 2014-2021 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************
*/
package net.jp2p.jxse.utils;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.ws.rs.core.MediaType;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import net.jp2p.chaupal.id.IJp2pID;
import net.jp2p.jxse.core.id.Jp2pID;
import net.jxta.document.MimeMediaType;
import net.jxta.document.StructuredDocument;
import net.jxta.document.StructuredDocumentFactory;
import net.jxta.id.ID;

public class DocumentUtils {

	public enum Attributes{
		MIME_MEDIA_TYPE;
	}

	public static MimeMediaType convert( MediaType type ){
		return null;
	}
	
	public static Document createDomDocument( StructuredDocument<?> sd ) throws ParserConfigurationException {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = dbf.newDocumentBuilder();
		Document doc = builder.newDocument();    
		createDomDocument(doc, sd);
		return doc;
	}

	private static void createDomDocument(  Document doc, StructuredDocument<?> sd ) {

		// create the root element node
		org.w3c.dom.Element element = doc.createElement( sd.getKey().toString() );
		if( sd.getValue() != null )
			element.setNodeValue( sd.getValue().toString() );
		if( sd.getMimeType() != null )
			element.setAttribute( Attributes.MIME_MEDIA_TYPE.name(), sd.getMimeType().getMimeMediaType());
		Enumeration<?> enm = sd.getChildren();
		while( enm.hasMoreElements() ) {
			createDomDocument( doc, (StructuredDocument<?>) enm.nextElement() );
		}
	}

	public static StructuredDocument<?> createJxseDocument( Document doc ) throws ParserConfigurationException, IOException {
		StructuredDocument<?> sd = StructuredDocumentFactory.newStructuredDocument(null);
		createJxseDocument(sd, doc);
		return sd;
	}

	private static void createJxseDocument(  StructuredDocument<?> sd, Document elem ) {

		// create the root element node
		//org.w3c.dom.Element element = doc.createElement( sd.getKey().toString() );
	}

	public static org.w3c.dom.Element convertToDom( net.jxta.document.Element<?> elem ){
		return null;
	}

	@SuppressWarnings("unchecked")
	public static Map<ID, net.jxta.document.Element<?>>  convertToJxse( Hashtable<IJp2pID, org.w3c.dom.Element> params ){
		Map<ID, net.jxta.document.Element<?>> result = new HashMap<>();
		Iterator<?> iterator = params.entrySet().iterator();
		while( iterator.hasNext()) {
			Map.Entry<IJp2pID, org.w3c.dom.Element> entry = (Entry<IJp2pID, Element>) iterator.next();
			Jp2pID<ID> id = (Jp2pID<ID>) entry.getKey();
			result.put( id.getID(), convertToJxse(entry.getValue()));
		}
		return result;
	}

	public static Map<IJp2pID, org.w3c.dom.Element> convertToDom( Hashtable<ID, net.jxta.document.Element<?>> params ){
		Map<IJp2pID, org.w3c.dom.Element> result = new HashMap<>();
		Iterator<Map.Entry<ID, net.jxta.document.Element<?>>> iterator = params.entrySet().iterator();
		while( iterator.hasNext()) {
			Map.Entry<ID, net.jxta.document.Element<?>> entry = iterator.next();
			result.put( new Jp2pID<ID>( entry.getKey() ), convertToDom(entry.getValue()));
		}
		return result;
	}

	public static Hashtable<IJp2pID, org.w3c.dom.Document> convertDocToDom( Hashtable<ID, StructuredDocument<?>> params ){
		Hashtable<IJp2pID, org.w3c.dom.Document> result = new Hashtable<>();
		Iterator<Map.Entry<ID, StructuredDocument<?>>> iterator = params.entrySet().iterator();
		while( iterator.hasNext()) {
			Map.Entry<ID, StructuredDocument<?>> entry = iterator.next();
			try {
				result.put( new Jp2pID<ID>( entry.getKey() ), createDomDocument(entry.getValue()));
			} catch (ParserConfigurationException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	public static net.jxta.document.Element<?> convertToJxse( org.w3c.dom.Element elem ){
		return null;
	}

	protected boolean isEmpty( String str ) {
		return ( str == null ) || (str.trim().length() == 0 );
	}
}
