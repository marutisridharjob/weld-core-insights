/*
 * JBoss, Home of Professional Open Source
 * Copyright 2017, Red Hat, Inc., and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.weld.tests.builtinBeans.ee.servlet.session;

import static org.junit.Assert.assertFalse;

import java.net.URL;

import jakarta.servlet.http.HttpSession;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.weld.test.util.Utils;
import org.jboss.weld.tests.category.Integration;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;

/**
 * Test that an invocation of an injected {@link HttpSession} (provided by {@link org.jboss.weld.module.web.HttpSessionBean}) triggers session creation.
 *
 * @author Martin Kouba
 * @see WELD-2346
 */
@RunWith(Arquillian.class)
@Category(Integration.class)
public class EagerHttpSessionTest {

    @ArquillianResource
    private URL url;

    @Deployment(testable = false)
    public static WebArchive createTestArchive() {
        return ShrinkWrap
                .create(WebArchive.class,
                        Utils.getDeploymentNameAsHash(EagerHttpSessionTest.class,
                                Utils.ARCHIVE_TYPE.WAR))
                .addClasses(TestServlet.class, EagerHttpSessionTest.class)
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @Test
    public void testHttpSession() throws Exception {
        WebClient client = new WebClient();
        client.setThrowExceptionOnFailingStatusCode(true);
        Page page = client.getPage(url + "/test");
        String id = page.getWebResponse().getContentAsString();
        assertFalse(id.isEmpty());
    }

}
