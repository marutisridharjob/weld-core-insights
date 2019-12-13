/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc., and individual contributors
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
package org.jboss.weld.tests.contexts.passivating.injection;

import java.io.Serializable;

import jakarta.enterprise.context.SessionScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.enterprise.inject.TransientReference;
import jakarta.inject.Inject;

@SuppressWarnings("serial")
@SessionScoped
public class Farm implements Serializable {

    protected Farm() {
    }

    @Inject
    public Farm(Truck truck, @TransientReference Pasture pasture) {
    }

    @Inject
    public void init(Truck truck, @TransientReference Pasture pasture) {
    }

    @Produces
    @Random
    @SessionScoped
    public Sheep chooseRandomSheepFromDelivery(Truck truck,  @TransientReference Pasture pasture) {
        return truck.getSheeps().get(0);
    }

}
