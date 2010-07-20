/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.hiemadapter.proxy.unsubscribe;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import org.w3c.dom.Element;
import gov.hhs.fha.nhinc.hiem.consumerreference.ReferenceParametersElements;

/**
 *
 * @author rayj
 */
public interface HiemUnsubscribeAdapterProxy {

    public Element unsubscribe(Element unsubscribeElement, ReferenceParametersElements referenceParametersElements, AssertionType assertion, NhinTargetSystemType target);
}