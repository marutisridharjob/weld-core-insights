/*
 * JBoss, Home of Professional Open Source
 * Copyright 2021, Red Hat, Inc., and individual contributors
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

package org.jboss.weld.tests.discovery;

import jakarta.enterprise.inject.Instance;
import jakarta.enterprise.inject.spi.CDI;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.weld.test.util.Utils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Starting with CDI 4.0, empty beans.xml should be interpreted as discovery mode {@code annotated}.
 */
@RunWith(Arquillian.class)
public class EmptyBeansXmlTest {

    @Deployment
    public static Archive<?> getDeployment() {
        return ShrinkWrap.create(JavaArchive.class, Utils.getDeploymentNameAsHash(EmptyBeansXmlTest.class))
                .addPackage(EmptyBeansXmlTest.class.getPackage())
                // add completely empty beans.xml
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @Test
    public void testDiscoveryMode() {
        Instance<Object> instance = CDI.current().getBeanManager().createInstance();
        // assert Bar is resolvable because it is annotated
        Assert.assertTrue(instance.select(Bar.class).isResolvable());
        // assert Foo is not resolvable
        Assert.assertFalse(instance.select(Foo.class).isResolvable());
    }
}
