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
package net.jp2p.jxse.core.id;

import net.jp2p.chaupal.id.IJp2pID;
import net.jp2p.jxse.core.peergroup.Jp2pModuleClassID;
import net.jxta.id.ID;

public interface IJp2pModuleID<I extends ID> extends IJp2pID{

	public I getID();

	/**
	 * Returns true if this ModuleClassID is of the same class than the
	 * the given ModuleSpecID.
	 *
	 * @param id Module spec id to compare with
	 * @return boolean true if equals
	 */

	boolean isOfSameBaseClass( I id);

	/**
	 * Returns true if this ModuleClassID is of the same base class than the
	 * given class.
	 * Note: This method is NOT named "isOfClass" because a ModuleClassID
	 * may have two UUID; one that denotes a "base" class proper,
	 * and an optional second one that denotes a "Role", or subclass.
	 * Compatibility between ClassIDs is based on the "base" portion, hence the
	 * "isOfSame" naming. This routine can be used for comparison with a base class
	 * since a base class is just a class which role portion happens to be zero.
	 *
	 * @param id Module class id to compare with
	 * @return boolean true if equals
	 */
	boolean isOfSameBaseClass(Jp2pModuleClassID id);

	/**
	 * Return a ModuleClassID of the same base class but with the role portion
	 * set to zero. aka "the base class".
	 *
	 * @return ModuleClassID the base class.
	 */
	Jp2pModuleClassID getBaseClass();

}