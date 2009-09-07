/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2009 Sun Microsystems, Inc. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * JBoss, Home of Professional Open Source
 * Copyright 2008, Red Hat Middleware LLC, and individual contributors
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
package org.jboss.webbeans.servlet;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletRequestEvent;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSessionEvent;

import org.jboss.webbeans.Container;
import org.jboss.webbeans.context.ContextLifecycle;
import org.jboss.webbeans.jsp.JspInitialization;
import org.jboss.webbeans.servlet.api.helpers.AbstractServletListener;

/**
 * The Web Beans listener
 * 
 * Listens for context/session creation/destruction.
 * 
 * Delegates work to the ServletLifeCycle.
 * 
 * @author Nicklas Karlsson
 *
 */
public class WebBeansListener extends AbstractServletListener
{
   
   private ServletLifecycle lifecycle;
   
   private ServletLifecycle getLifecycle()
   {
      if (lifecycle == null)
      {
         this.lifecycle = new ServletLifecycle(Container.instance().deploymentServices().get(ContextLifecycle.class));
      }
      return lifecycle;
   }
   
   @Override
   public void contextInitialized(ServletContextEvent sce)
   {
      // JBoss AS will still start the deployment even if WB fails
      if (Container.instance() != null && Container.instance().isInitialized())
      {
         new JspInitialization().init(sce.getServletContext());
      }
   }  

   /**
    * Called when the session is created
    * 
    * @param event The session event
    */
   @Override
   public void sessionCreated(HttpSessionEvent event) 
   {
      // JBoss AS will still start the deployment even if WB fails
      if (Container.instance() != null && Container.instance().isInitialized())
      {
         getLifecycle().beginSession(event.getSession());
      }
   }

   /**
    * Called when the session is destroyed
    * 
    * @param event The session event
    */
   @Override
   public void sessionDestroyed(HttpSessionEvent event) 
   {
      // JBoss AS will still start the deployment even if WB fails
      if (Container.instance() != null && Container.instance().isInitialized())
      {
         getLifecycle().endSession(event.getSession());
      }
   }

   /**
    * Called when the request is destroyed
    * 
    * @param event The request event
    */
   @Override
   public void requestDestroyed(ServletRequestEvent event)
   {
      // JBoss AS will still start the deployment even if WB fails
      if (Container.instance() != null && Container.instance().isInitialized())
      {
         if (event.getServletRequest() instanceof HttpServletRequest)
         {
            getLifecycle().endRequest((HttpServletRequest) event.getServletRequest());
         }
         else
         {
            throw new IllegalStateException("Non HTTP-Servlet lifecycle not defined");
         }
      }
   }

   /**
    * Called when the request is initialized
    * 
    * @param event The request event
    */
   @Override
   public void requestInitialized(ServletRequestEvent event)
   {
      // JBoss AS will still start the deployment even if WB fails
      if (Container.instance() != null && Container.instance().isInitialized())
      {
         if (event.getServletRequest() instanceof HttpServletRequest)
         {
            getLifecycle().beginRequest((HttpServletRequest) event.getServletRequest());
         }
         else
         {
            throw new IllegalStateException("Non HTTP-Servlet lifecycle not defined");
         }
      }
   }

}
