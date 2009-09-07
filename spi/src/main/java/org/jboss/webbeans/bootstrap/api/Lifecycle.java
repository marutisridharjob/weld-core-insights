package org.jboss.webbeans.bootstrap.api;

import javax.enterprise.context.spi.Context;

import org.jboss.webbeans.context.api.BeanStore;

/**
 * Note Lifecycle is not complete, and the API may change.
 * 
 * {@link Lifecycle} is a per-deployment service.
 * 
 * @author pmuir
 *
 */
public interface Lifecycle
{

   public void restoreSession(String id, BeanStore sessionBeanStore);

   public void endSession(String id, BeanStore sessionBeanStore);

   public void beginRequest(String id, BeanStore requestBeanStore);

   public void endRequest(String id, BeanStore requestBeanStore);

   public boolean isRequestActive();

   public void beginApplication(BeanStore applicationBeanStore);

   public void endApplication();

   public Context getApplicationContext();

   public Context getSessionContext();

   public Context getConversationContext();

   public Context getRequestContext();

   public Context getDependentContext();

}
