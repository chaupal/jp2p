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
package net.jp2p.chaupal.protocol;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import net.jp2p.chaupal.id.IJp2pID;

public interface IJp2pModuleImplAdvertisement {

	/**
	 * Returns the unique ID of that advertisement for indexing purposes.
	 * In that case we do not have any particular one to offer. Let the indexer
	 * hash the document.
	 *
	 * @return ID the unique id
	 */
	IJp2pID getID();

	/**
	 * Returns the opaque compatibility statement for this advertisement. Each
	 * JXTA implementation has the ability to recognize and evaluate it's own
	 * compatibility statements (even though it may not be able to evaluate the
	 * compatibility statements of other implementations).
	 *
	 * @return The compatibility statement as a StructuredDocument of 
	 * unspecified content.
	 */
	Document getCompat();

	/**
	 * returns the code; a reference to or representation of the executable code
	 * advertised by this advertisement.
	 * <p/>
	 * The appropriate interpretation of the code value is dependant upon the 
	 * compatibility statement. Any compatible consumer of this advertisement
	 * will be able to correctly interpret code value. The standard group 
	 * implementations of the JXSE reference implementation expect it to be a 
	 * reference to a jar file.
	 *
	 * @return A reference to the executable code described by this 
	 * advertisement.
	 */
	String getCode();

	/**
	 * Sets the reference for the executable code described by this 
	 * advertisement.
	 *
	 * @param code A reference to the executable code described by this 
	 * advertisement.
	 */
	void setCode(String code);

	/**
	 * returns the uri; that is a reference to or representation of a package 
	 * from which the executable code referenced by the getCode method may be 
	 * loaded.
	 * <p/>
	 * The appropriate interpretation of the URI value is dependant upon the 
	 * compatibility statement. Any compatible consumer of this advertisement
	 * will be able to correctly interpret the URI value. The standard group 
	 * implementations of the JXSE reference implementation expect it to be a 
	 * reference to a jar file.
	 *
	 * @return Location URI for the code described by this advertisement.
	 */
	String getUri();

	/**
	 * Sets the uri
	 *
	 * @param uri Location URI for the code described by this advertisement.
	 */
	void setUri(String uri);

	/**
	 * returns the provider
	 *
	 * @return String the provider
	 */
	String getProvider();

	/**
	 * sets the provider
	 *
	 * @param provider the provider
	 */
	void setProvider(String provider);

	/**
	 * returns the param element.
	 *
	 * The interpretation of the param element is entirely up to the code
	 * that this advertises. One valid use of it is to enable the code to
	 * be configured so that multiple specs or multiple implementations of
	 * one spec may use the same code.
	 *
	 * @return A standalone structured document of unspecified content.
	 */
	Document getParam();

	/**
	 * Sets the module param
	 *
	 * @param param Element of an unspecified content.
	 */
	void setParam(Element param);

}