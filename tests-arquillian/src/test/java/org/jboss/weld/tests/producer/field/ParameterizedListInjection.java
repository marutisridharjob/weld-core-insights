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
package org.jboss.weld.tests.producer.field;

import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import java.util.List;

@Dependent
public class ParameterizedListInjection {

    private List<String> value;

    @Inject
    private List<String> fieldInjection;

    private List<String> setterInjection;

    @Inject
    public void init(List<String> setterInjection) {
        this.setterInjection = setterInjection;
    }

    @Inject
    public ParameterizedListInjection(List<String> com) {
        this.value = com;
    }

    public java.util.List<String> getValue() {
        return value;
    }

    public List<String> getFieldInjection() {
        return fieldInjection;
    }

    public List<String> getSetterInjection() {
        return setterInjection;
    }

}
