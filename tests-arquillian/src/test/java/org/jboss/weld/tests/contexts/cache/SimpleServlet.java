/*
 * JBoss, Home of Professional Open Source
 * Copyright 2014, Red Hat, Inc., and individual contributors
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
package org.jboss.weld.tests.contexts.cache;

import java.io.IOException;
import java.lang.reflect.Field;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jboss.weld.servlet.WeldListener;

@WebServlet(value = "/servlet", asyncSupported = true)
public class SimpleServlet extends HttpServlet {

    private final Field guard;

    public SimpleServlet() throws NoSuchFieldException, SecurityException {
        guard = WeldListener.class.getDeclaredField("nestedInvocationGuard");
        guard.setAccessible(true);
    }

    @Inject
    private RequestScopedBean bean;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        String sequence = req.getParameter("sequence");
        String poison = req.getParameter("poison");
        if ("getAndSet".equals(action)) {
            // the value should always be foo
            String value = bean.getAndSet("bar" + sequence);
            resp.getWriter().println(value);
            if (poison != null) {
                // this is a poisoning request
                // normal applications should never do something like this
                // we just do this to cause the request not to be cleaned up properly
                try {
                    ((ThreadLocal<?>) guard.get(null)).remove();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        } else {
            throw new IllegalArgumentException(action);
        }
    }
}
