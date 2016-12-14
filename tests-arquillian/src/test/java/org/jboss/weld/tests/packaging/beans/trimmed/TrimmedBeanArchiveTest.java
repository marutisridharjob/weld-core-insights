/*
 * JBoss, Home of Professional Open Source
 * Copyright 2016, Red Hat, Inc., and individual contributors
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
package org.jboss.weld.tests.packaging.beans.trimmed;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.Extension;
import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.weld.test.util.Utils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class TrimmedBeanArchiveTest {

    @Inject
    TestExtension extension;

    @Inject
    BeanManager bm;

    @Inject
    Instance<Bike> bikeInstance;

    @Inject
    Instance<Segway> segwayInstance;

    @Deployment
    public static WebArchive createTestArchive() {
        return ShrinkWrap.create(WebArchive.class, Utils.getDeploymentNameAsHash(TrimmedBeanArchiveTest.class, Utils.ARCHIVE_TYPE.WAR))
                .addPackage(TrimmedBeanArchiveTest.class.getPackage())
                .addAsServiceProvider(Extension.class, TestExtension.class)
                .addAsWebInfResource(TrimmedBeanArchiveTest.class.getPackage(), "beans.xml", "beans.xml");
    }

    @Test
    public void testDiscoveredBean() {
        Assert.assertEquals(1, extension.getVehiclePBAinvocations().get());
        Bean<MotorizedVehicle> vehicleBean = (Bean<MotorizedVehicle>) bm.getBeans(MotorizedVehicle.class).iterator().next();
        CreationalContext<MotorizedVehicle> cc = bm.createCreationalContext(vehicleBean);
        MotorizedVehicle vehicle = (MotorizedVehicle) bm.getReference(vehicleBean, MotorizedVehicle.class, cc);
        Assert.assertEquals(vehicle.start(), Bus.class.getSimpleName());
    }

    @Test
    public void testProducerNotDsicovered() {
        Assert.assertTrue(extension.isBikerProducerPATFired());
        Assert.assertFalse(extension.isBikerProducerPBAFired());
        Assert.assertFalse(bikeInstance.isAmbiguous());
    }

    @Test
    public void testDiscoveredBeanWithStereoType() {
        Assert.assertFalse(segwayInstance.isUnsatisfied());
    }
}
