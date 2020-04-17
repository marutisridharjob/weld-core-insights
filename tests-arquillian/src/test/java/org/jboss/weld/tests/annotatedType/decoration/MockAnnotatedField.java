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

import jakarta.enterprise.inject.spi.AnnotatedField;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Set;

/**
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 * @version $Revision: 1.1 $
 */
class MockAnnotatedField<X> extends MockAnnotatedMember<X> implements AnnotatedField<X> {
    MockAnnotatedField(AnnotatedField<? super X> delegate) {
        super(delegate);
    }

    @Override
    AnnotatedField<X> getDelegate() {
        return (AnnotatedField<X>) super.getDelegate();
    }

    public Field getJavaMember() {
        return getDelegate().getJavaMember();
    }

    private boolean isDecoratedField() {
        return getJavaMember().getName().equals("fromField");
    }

    @Override
    public <T extends Annotation> T getAnnotation(Class<T> annotationType) {
        if (isDecoratedField())
            return super.getAnnotation(annotationType);
        return null;
    }

    @Override
    public Set<Annotation> getAnnotations() {
        if (isDecoratedField())
            return super.getAnnotations();
        return Collections.emptySet();
    }

    @Override
    public boolean isAnnotationPresent(Class<? extends Annotation> annotationType) {
        if (isDecoratedField())
            return super.isAnnotationPresent(annotationType);
        return false;
    }
}
