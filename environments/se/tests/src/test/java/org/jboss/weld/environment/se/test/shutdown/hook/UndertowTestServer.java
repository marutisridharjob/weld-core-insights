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
package org.jboss.weld.environment.se.test.shutdown.hook;

import io.undertow.Undertow;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;

public class UndertowTestServer {

    private final Undertow server;

    public UndertowTestServer() {
        server = Undertow.builder().addHttpListener(ShutdownHookTest.UNDERTOW_PORT, "localhost").setHandler(new HttpHandler() {
            @Override
            public void handleRequest(final HttpServerExchange exchange) throws Exception {
                // Any request indicates success
                // System.out.println("REQUEST: " + exchange);
                ShutdownHookTest.IS_FOO_DESTROYED.set(true);
            }
        }).build();
    }

    void start() {
        server.start();
    }

    void stop() {
        server.stop();
    }
}
