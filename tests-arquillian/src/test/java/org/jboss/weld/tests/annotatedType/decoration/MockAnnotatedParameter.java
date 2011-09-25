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
package org.jboss.weld.tests.annotatedType.decoration;

import javax.enterprise.inject.spi.Annotated;
import javax.enterprise.inject.spi.AnnotatedCallable;
import javax.enterprise.inject.spi.AnnotatedParameter;

/**
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 * @version $Revision: 1.1 $
 */
public class MockAnnotatedParameter<X> extends MockAnnotated implements AnnotatedParameter<X> {
    private final AnnotatedCallable<X> callable;

    public MockAnnotatedParameter(Annotated delegate, AnnotatedCallable<X> callable) {
        super(delegate);
        this.callable = callable;
    }

    @Override
    AnnotatedParameter<X> getDelegate() {
        return (AnnotatedParameter<X>) super.getDelegate();
    }

    public AnnotatedCallable<X> getDeclaringCallable() {
        return callable;
    }

    public int getPosition() {
        return getDelegate().getPosition();
    }

}
