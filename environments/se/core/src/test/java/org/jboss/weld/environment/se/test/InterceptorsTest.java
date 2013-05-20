/**
 * JBoss, Home of Professional Open Source
 * Copyright 2009, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
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
package org.jboss.weld.environment.se.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.jboss.weld.environment.se.test.beans.InterceptorTestBean;
import org.jboss.weld.environment.se.test.interceptors.AggregatingInterceptor;
import org.jboss.weld.environment.se.test.interceptors.RecordingInterceptor;
import org.junit.Test;

/**
 * @author Peter Royle
 */
public class InterceptorsTest extends WeldSETest {

    /**
     * Test that interceptors work as expected in SE.
     */
    @Test
    public void testInterceptors() {

        InterceptorTestBean intTestBean = container.instance().select(InterceptorTestBean.class).get();
        assertNotNull(intTestBean);

        intTestBean.doSomethingRecorded();
        System.out.println(RecordingInterceptor.methodsRecorded);
        System.out.println(AggregatingInterceptor.methodsCalled);
        assertTrue(RecordingInterceptor.methodsRecorded.contains("doSomethingRecorded"));

        intTestBean.doSomethingRecordedAndAggregated();
        System.out.println(RecordingInterceptor.methodsRecorded);
        System.out.println(AggregatingInterceptor.methodsCalled);

        assertEquals(1, AggregatingInterceptor.methodsCalled);
    }

}
