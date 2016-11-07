package org.jboss.weld.environment.se.logging;

import org.jboss.logging.Logger;
import org.jboss.logging.Logger.Level;
import org.jboss.logging.annotations.LogMessage;
import org.jboss.logging.annotations.Message;
import org.jboss.logging.annotations.Message.Format;
import org.jboss.logging.annotations.MessageLogger;
import org.jboss.weld.environment.logging.Category;
import org.jboss.weld.environment.logging.WeldEnvironmentLogger;

/**
 * A source of localized log/bundle messages and exceptions. Note that this interface extends {@link WeldEnvironmentLogger} so that regular logger methods are
 * available.
 *
 * Message IDs: 002000 - 002099
 *
 * @author Matej Briškár
 * @author Martin Kouba
 * @author Kirill Gaevskii
 */
@MessageLogger(projectCode = WeldEnvironmentLogger.WELD_ENV_PROJECT_CODE)
public interface WeldSELogger extends WeldEnvironmentLogger {

    WeldSELogger LOG = Logger.getMessageLogger(WeldSELogger.class, Category.BOOTSTRAP.getName());

    @Message(id = 2000, value = "Weld SE container {0} is already running!", format = Format.MESSAGE_FORMAT)
    IllegalStateException weldContainerAlreadyRunning(Object id);

    @LogMessage(level = Level.INFO)
    @Message(id = 2001, value = "Weld SE container {0} shut down", format = Format.MESSAGE_FORMAT)
    void weldContainerShutdown(Object id);

    @Message(id = 2002, value = "Weld SE container {0} was already shut down", format = Format.MESSAGE_FORMAT)
    IllegalStateException weldContainerAlreadyShutDown(Object id);

    @LogMessage(level = Level.INFO)
    @Message(id = 2003, value = "Weld SE container {0} initialized", format = Format.MESSAGE_FORMAT)
    void weldContainerInitialized(Object id);

    @LogMessage(level = Level.DEBUG)
    @Message(id = 2004, value = "Scanning package {0}, class resource: {1}", format = Format.MESSAGE_FORMAT)
    void scanningPackage(Object packageName, Object resource);

    @LogMessage(level = Level.WARN)
    @Message(id = 2005, value = "Package intended for scanning was not found: {0}", format = Format.MESSAGE_FORMAT)
    void packageNotFound(Object packageName);

    @LogMessage(level = Level.INFO)
    @Message(id = 2006, value = "Multiple containers running - CDI.current() may not work properly: {0}", format = Format.MESSAGE_FORMAT)
    void multipleContainersRunning(Object ids);

    @LogMessage(level = Level.DEBUG)
    @Message(id = 2007, value = "Resource URL procotol not supported: {0}", format = Format.MESSAGE_FORMAT)
    void resourceUrlProtocolNotSupported(Object resource);

    @LogMessage(level = Level.WARN)
    @Message(id = 2008, value = "Bean class {0} found in multiple bean archives - this may result in incorrect behavior: {1}", format = Format.MESSAGE_FORMAT)
    void beanClassDeployedInMultipleBeanArchives(Object beanClass, Object bdas);

    @Message(id = 2009, value = "Weld SE container cannot be initialized - no bean archives found")
    IllegalStateException weldContainerCannotBeInitializedNoBeanArchivesFound();

    // 2010 - 2012 used in Weld 3

    // 2013 used to warn on deprecated org.jboss.weld.environment.se.contexts.interceptors.ActivateThreadScope

    @Message(id = 2014, value = "Weld SE container {0} not initialized completely", format = Format.MESSAGE_FORMAT)
    IllegalStateException weldContainerNotInitializedCompletely(Object id);

}
