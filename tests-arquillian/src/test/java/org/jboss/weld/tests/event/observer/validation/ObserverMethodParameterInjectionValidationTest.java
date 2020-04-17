/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc., and individual contributors
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

package org.jboss.weld.tests.event.observer.validation;

import jakarta.enterprise.inject.spi.DeploymentException;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.ShouldThrowException;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Dan Allen
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
@RunWith(Arquillian.class)
public class ObserverMethodParameterInjectionValidationTest {
    @Deployment
    @ShouldThrowException(DeploymentException.class)
    public static JavaArchive getDeployment() {
        return ShrinkWrap.create(JavaArchive.class, "test.jar")
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml")
                .addClasses(SimpleTarget.class, SimpleObserver.class);
    }

    @Test
    public void testDeployment() {
        // Arquillian ShouldThrowException marks it as allowed, does not stop @Test from execution
        //Assert.fail();
    }

    /**
     * This test should not run, but if it does, it shows Weld reporting the ambiguous error in WELD-870:
     * WELD-001324 Argument bean must not be null
     *
     * @param beanManager the bean manager
    public void testNullInjectionOnObserverMethod(BeanManager beanManager)
    {
    beanManager.fireEvent("message");
    }
     */
}

