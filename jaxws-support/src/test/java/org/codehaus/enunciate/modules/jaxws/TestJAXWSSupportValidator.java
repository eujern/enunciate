/*
 * Copyright 2006-2008 Web Cohesion
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.codehaus.enunciate.modules.jaxws;

import org.codehaus.enunciate.InAPTTestCase;
import org.codehaus.enunciate.contract.jaxws.*;

import java.util.HashSet;

import com.sun.mirror.declaration.ClassDeclaration;
import junit.framework.Test;

/**
 * @author Ryan Heaton
 */
public class TestJAXWSSupportValidator extends InAPTTestCase {

  /**
   * Tests the behavior of conflicting bean names.
   */
  public void testConflictingBeanNames() throws Exception {
    EndpointInterface ei = new EndpointInterface(getDeclaration("org.codehaus.enunciate.samples.jaxws.BasicEndpointInterface"));
    JAXWSSupportValidator validator = new JAXWSSupportValidator();
    WebMethod alreadyExisting = null;
    WebMethod notConflictingMethod = null;
    for (WebMethod webMethod : ei.getWebMethods()) {
      if ("alreadyExisting".equals(webMethod.getSimpleName())) {
        alreadyExisting = webMethod;
      }
      else if ("notConflictingMethod".equals(webMethod.getSimpleName())) {
        notConflictingMethod = webMethod;
      }
    }

    RequestWrapper requestWrapper = new RequestWrapper(alreadyExisting);
    HashSet<String> visited = new HashSet<String>();
//    assertTrue("Bean names conflicting with alrady existing classes should not be valid.", validator.validateRequestWrapper(requestWrapper, visited).hasErrors());
//    assertTrue(visited.contains(requestWrapper.getRequestBeanName()));

    ResponseWrapper responseWrapper = new ResponseWrapper(alreadyExisting);
//    visited.clear();
//    assertTrue("Bean names conflicting with alrady existing classes should not be valid.", validator.validateResponseWrapper(responseWrapper, visited).hasErrors());
//    assertTrue(visited.contains(responseWrapper.getResponseBeanName()));

    WebFault webFault = new WebFault((ClassDeclaration) getDeclaration("org.codehaus.enunciate.samples.jaxws.BasicFault"));
//    visited.clear();
//    assertTrue("Bean names conflicting with alrady existing classes should not be valid.", validator.validateWebFault(webFault, visited).hasErrors());
//    assertTrue(visited.contains(webFault.getImplicitFaultBeanQualifiedName()));

    visited.clear();
    requestWrapper = new RequestWrapper(notConflictingMethod);
    responseWrapper = new ResponseWrapper(notConflictingMethod);
    webFault = new WebFault((ClassDeclaration) getDeclaration("org.codehaus.enunciate.samples.jaxws.NonConflictingFault"));

    assertFalse(validator.validateRequestWrapper(requestWrapper, visited).hasErrors());
    assertFalse(validator.validateResponseWrapper(responseWrapper, visited).hasErrors());
    assertFalse(validator.validateWebFault(webFault, visited).hasErrors());
    assertEquals(3, visited.size());
    assertTrue("Beans names that have already been used shouldn't be valid.", validator.validateRequestWrapper(requestWrapper, visited).hasErrors());
    assertTrue("Beans names that have already been used shouldn't be valid.", validator.validateResponseWrapper(responseWrapper, visited).hasErrors());
    assertTrue("Beans names that have already been used shouldn't be valid.", validator.validateWebFault(webFault, visited).hasErrors());

  }

  public static Test suite() {
    return createSuite(TestJAXWSSupportValidator.class);
  }
}
