/*
 * JBoss, Home of Professional Open Source
 * Copyright 2012, Red Hat, Inc., and individual contributors
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
package org.jboss.weld.tests.contexts.request.postConstruct.singleton.complicated;

import static org.junit.Assert.assertEquals;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;

@Singleton
@Startup
public class Initializer {

    @Inject
    private Foo foo;
    @Inject
    private Bar bar;

    @Inject
    private Instance<ValueHolder> value;

    @PostConstruct
    void init() {
        assertEquals(null, value.get().getValue());
        value.get().setValue("singleton");
    }
}
