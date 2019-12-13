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
package org.jboss.weld.tests.builtinBeans.ee.servlet;

import java.io.IOException;

import jakarta.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(value = "/", name = "testServlet")
@SuppressWarnings("serial")
public class Servlet extends HttpServlet {

    @Inject
    private ServletBuiltinBeanInjectingBean bean;
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getRequestURI().contains("/request")) {
            bean.verifyRequest();
        }
        if (req.getRequestURI().endsWith("/session")) {
            req.getSession().setAttribute("foo", "bar");
            bean.verifySession();
        }
        if (req.getRequestURI().endsWith("/context")) {
            req.getSession().getServletContext().setAttribute("foo", "bar");
            bean.verifyServletContext();
        }
    }
}
