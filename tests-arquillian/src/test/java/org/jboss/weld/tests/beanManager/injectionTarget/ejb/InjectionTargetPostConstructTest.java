/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc., and individual contributors
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

package org.jboss.weld.tests.beanManager.injectionTarget.ejb;

import static org.junit.Assert.assertTrue;

import javax.enterprise.inject.spi.Extension;
import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.BeanArchive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.weld.tests.category.Integration;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;

/**
 * @author <a href="mailto:mluksa@redhat.com">Marko Luksa</a>
 */
@RunWith(Arquillian.class)
@Category(Integration.class)
@Ignore("WFLY-168")
public class InjectionTargetPostConstructTest {

    @Inject
    private TheCDIBean cdiBean;

    @Inject
    private TheEJB ejb;

    @Deployment
    public static Archive<?> getDeployment() {
        return ShrinkWrap.create(BeanArchive.class)
                .addAsServiceProvider(Extension.class, InjectionTargetWrappingExtension.class)
                .addPackage(InjectionTargetPostConstructTest.class.getPackage());
    }

    @Test
    public void testPostConstructInvokedOnCDIBean() {
        cdiBean.foo();
        assertTrue("postConstruct not invoked", cdiBean.isPostConstructInvoked());
    }

    @Test
    public void testPostConstructInvokedOnEJB() {
        ejb.foo();
        assertTrue("postConstruct not invoked", ejb.isPostConstructInvoked());
    }

    @Test
    public void testInjectionTargetPostConstructInvokedForCDIBean() {
        cdiBean.foo();
        assertTrue("InjectionTarget.postConstruct not invoked", InjectionTargetWrappingExtension.invokedPostConstructs.contains(TheCDIBean.class.getName()));
    }

    @Test
    public void testInjectionTargetPostConstructInvokedForEJB() {
        ejb.foo();
        assertTrue("InjectionTarget.postConstruct not invoked", InjectionTargetWrappingExtension.invokedPostConstructs.contains(TheEJB.class.getName()));
    }
}
