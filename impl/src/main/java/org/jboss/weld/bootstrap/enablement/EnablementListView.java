/*
 * JBoss, Home of Professional Open Source
 * Copyright 2015, Red Hat, Inc., and individual contributors
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
package org.jboss.weld.bootstrap.enablement;

import static org.jboss.weld.util.Preconditions.checkNotNull;
import org.jboss.weld.logging.BootstrapLogger;

import javax.enterprise.inject.spi.Extension;
import java.util.Collection;
import java.util.List;
import java.util.ListIterator;

import org.jboss.weld.exceptions.UnsupportedOperationException;
import org.jboss.weld.util.collections.ListView;

/**
 *
 * @author Martin Kouba
 *
 */
abstract class EnablementListView extends ListView<Item, Class<?>> {

    protected abstract ViewType getViewType();

    protected abstract Extension getExtension();

    private static final String ADD_OPERATION = "adds";
    private static final String REMOVE_OPERATION = "removes";
    private static final String SET_OPERATION = "sets";
    private static final String RETAIN_OPERATION = "retains";

    @SuppressWarnings("checkstyle:magicnumber")
    private static final int DEFAULT_PRIORITY = javax.interceptor.Interceptor.Priority.APPLICATION + 500;

    @Override
    public boolean add(Class<?> element) {
        checkNotNull(element);
        List<Item> list = getDelegate();
        synchronized (list) {
            if (getExtension() != null) {
                BootstrapLogger.LOG.typeModifiedInAfterTypeDiscovery(getExtension(), element, ADD_OPERATION, getViewType().toString());
            }
            return list.add(createSource(element, list.isEmpty() ? null : list.get(list.size() - 1), null));
        }
    }

    @Override
    public Class<?> set(int index, Class<?> element) {
        checkNotNull(element);
        List<Item> list = getDelegate();
        synchronized (list) {
            if (index < 0 || index >= list.size()) {
                throw new IndexOutOfBoundsException();
            }
            if (getExtension() != null) {
                BootstrapLogger.LOG.typeModifiedInAfterTypeDiscovery(getExtension(), element, SET_OPERATION, getViewType().toString());
            }
            return toView(getDelegate().set(index, createSource(element, list.get(index).getPriority())));
        }
    }

    @Override
    public void add(int index, Class<?> element) {
        checkNotNull(element);
        List<Item> list = getDelegate();
        synchronized (list) {
            if (index < 0 || index >= list.size()) {
                throw new IndexOutOfBoundsException();
            }
            Item previous = (index > 0) ? list.get(index - 1) : null;
            Item next = (index <= (list.size() - 1)) ? list.get(index) : null;
            if (getExtension() != null) {
                BootstrapLogger.LOG.typeModifiedInAfterTypeDiscovery(getExtension(), element, ADD_OPERATION, getViewType().toString());
            }
            list.add(index, createSource(element, previous, next));
        }
    }

    @Override
    public Class<?> remove(int index) {
        Item removedItem = getDelegate().remove(index);
        if (getExtension() != null) {
            BootstrapLogger.LOG.typeModifiedInAfterTypeDiscovery(getExtension(), removedItem.getClass(), REMOVE_OPERATION, getViewType().toString());
        }
        return toView(removedItem);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        if (getExtension() != null) {
            BootstrapLogger.LOG.typeModifiedInAfterTypeDiscovery(getExtension(), c, REMOVE_OPERATION, getViewType().toString());
        }
        return getDelegate().removeAll(c);
    }

    @Override
    public boolean remove(Object o) {
        if (getExtension() != null) {
            BootstrapLogger.LOG.typeModifiedInAfterTypeDiscovery(getExtension(), o, REMOVE_OPERATION, getViewType().toString());
        }
        return getDelegate().remove(o);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        if (getExtension() != null) {
            BootstrapLogger.LOG.typeModifiedInAfterTypeDiscovery(getExtension(), c, RETAIN_OPERATION, getViewType().toString());
        }
        return getDelegate().retainAll(c);
    }

    @Override
    public void clear() {
        if (getExtension() != null) {
            BootstrapLogger.LOG.typeModifiedInAfterTypeDiscovery(getExtension(), "", REMOVE_OPERATION + " all classes", getViewType().toString());
        }
        getDelegate().clear();
    }

    @Override
    protected Class<?> toView(Item source) {
        return source.getJavaClass();
    }

    @Override
    protected Item createSource(Class<?> view) {
        throw new UnsupportedOperationException();
    }

    private Item createSource(Class<?> view, Item previous, Item next) {
        return createSource(view, getPriority(previous, next));
    }

    private Item createSource(Class<?> view, int priority) {
        return new Item(view, priority);
    }

    private int getPriority(Item previous, Item next) {
        int priority;
        if (previous == null && next == null) {
            // No bounds
            priority = DEFAULT_PRIORITY;
        } else if (previous != null && next != null) {
            int gap = (next.getPriority() - previous.getPriority());
            if (gap == 0) {
                // The items have the same priority
                priority = next.getPriority();
            } else if (gap == 1) {
                // There is no gap - scale the priorities
                for (Item item : getDelegate()) {
                    item.scalePriority();
                }
                priority = getPriority(previous, next);
            } else {
                priority = (gap / 2) + previous.getPriority();
            }
        } else if (previous != null) {
            priority = previous.getPriority() + Item.ITEM_PRIORITY_SCALE_POWER;
        } else {
            priority = next.getPriority() - Item.ITEM_PRIORITY_SCALE_POWER;
        }
        return priority;
    }

    @Override
    public ListIterator<Class<?>> listIterator() {
        return new EnablementListViewIterator(getDelegate().listIterator());
    }

    @Override
    public ListIterator<Class<?>> listIterator(int index) {
        return new EnablementListViewIterator(getDelegate().listIterator(index));
    }

    class EnablementListViewIterator extends ListViewIterator {

        public EnablementListViewIterator(ListIterator<Item> delegate) {
            super(delegate);
        }

        /**
         * The last item returned by a call to <code>next()</code> or <code>previous()</code>.
         */
        private Item lastItem;

        @Override
        public Class<?> next() {
            lastItem = delegate.next();
            return EnablementListView.this.toView(lastItem);
        }

        @Override
        public Class<?> previous() {
            lastItem = delegate.previous();
            return EnablementListView.this.toView(lastItem);
        }

        @Override
        public void set(Class<?> clazz) {
            if (getExtension() != null) {
                BootstrapLogger.LOG.typeModifiedInAfterTypeDiscovery(getExtension(), clazz, SET_OPERATION, getViewType().toString());
            }
            delegate.set(EnablementListView.this.createSource(clazz, lastItem.getPriority()));
        }

        @Override
        public void add(Class<?> clazz) {
            Item previous = hasPrevious() ? EnablementListView.this.getDelegate().get(previousIndex()) : null;
            Item next = hasNext() ? EnablementListView.this.getDelegate().get(nextIndex()) : null;
            if (getExtension() != null) {
                BootstrapLogger.LOG.typeModifiedInAfterTypeDiscovery(getExtension(), clazz, ADD_OPERATION, getViewType().toString());
            }
            delegate.add(EnablementListView.this.createSource(clazz, previous, next));
        }

        @Override
        public void remove() {
            if (getExtension() != null) {
                BootstrapLogger.LOG
                        .typeModifiedInAfterTypeDiscovery(getExtension(), getDelegate().get(delegate.nextIndex()).getJavaClass(), REMOVE_OPERATION, getViewType().toString());
            }
            delegate.remove();
        }

    }

    enum ViewType {
        ALTERNATIVES("getAlternatives()"),
        INTERCEPTORS("getInterceptors()"),
        DECORATORS("getDecorators()");

        private final String name;

        ViewType(String s) {
            name = s;
        }

        public String toString() {
            return this.name;
        }
    }

}
