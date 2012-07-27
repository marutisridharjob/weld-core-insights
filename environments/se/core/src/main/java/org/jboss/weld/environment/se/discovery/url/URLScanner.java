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
package org.jboss.weld.environment.se.discovery.url;

import org.jboss.weld.bootstrap.api.Bootstrap;
import org.jboss.weld.bootstrap.spi.BeanDeploymentArchive;
import org.jboss.weld.environment.se.discovery.ImmutableBeanDeploymentArchive;
import org.jboss.weld.resources.spi.ResourceLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;

/**
 * Scan the classloader
 *
 * @author Thomas Heute
 * @author Gavin King
 * @author Norman Richards
 * @author Pete Muir
 * @author Peter Royle
 * @author Marko Luksa
 */
public class URLScanner {

    private static final Logger log = LoggerFactory.getLogger(URLScanner.class);
    private final String[] resources;
    private final ResourceLoader resourceLoader;
    private final Bootstrap bootstrap;

    public URLScanner(ResourceLoader resourceLoader, Bootstrap bootstrap, String... resources) {
        this.resources = resources;
        this.resourceLoader = resourceLoader;
        this.bootstrap = bootstrap;
    }

    public BeanDeploymentArchive scan() {
        FileSystemURLHandler handler = new FileSystemURLHandler();
        for (String resourceName : resources) {
            // grab all the URLs for this resource
            for (URL url : resourceLoader.getResources(resourceName)) {
                handler.handle(getUrlPath(resourceName, url));
            }
        }
        return new ImmutableBeanDeploymentArchive("classpath", handler.getDiscoveredClasses(), bootstrap.parse(handler.getDiscoveredBeansXmlUrls()));
    }

    private String getUrlPath(String resourceName, URL url) {
        String urlPath = url.toExternalForm();
        String urlType = getUrlType(urlPath);
        log.debug("URL Type: " + urlType);
        // Extra built-in support for simple file-based resources
        if ("file".equals(urlType) || "jar".equals(urlType)) {
            // switch to using getPath() instead of toExternalForm()
            urlPath = url.getPath();

            if (urlPath.indexOf('!') > 0) {
                urlPath = urlPath.substring(0, urlPath.indexOf('!'));
            } else {
                // hack for /META-INF/beans.xml
                File dirOrArchive = new File(urlPath);
                if ((resourceName != null) && (resourceName.lastIndexOf('/') > 0)) {
                    dirOrArchive = dirOrArchive.getParentFile();
                }
                urlPath = dirOrArchive.getParent();
            }
        }

        return decode(urlPath);
    }

    private String decode(String urlPath) {
        try {
            urlPath = URLDecoder.decode(urlPath, "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            throw new ClasspathScanningException("Error decoding URL using UTF-8");
        }
        return urlPath;
    }

    /**
     * determine resource type (eg: jar, file, bundle)
     */
    private String getUrlType(String urlPath) {
        String urlType = "file";
        int colonIndex = urlPath.indexOf(":");
        if (colonIndex != -1) {
            urlType = urlPath.substring(0, colonIndex);
        }
        return urlType;
    }

}
