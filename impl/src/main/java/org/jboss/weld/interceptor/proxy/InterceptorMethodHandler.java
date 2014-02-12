package org.jboss.weld.interceptor.proxy;

import java.io.Serializable;
import java.lang.reflect.Method;

import org.jboss.weld.bean.proxy.MethodHandler;
import org.jboss.weld.interceptor.spi.model.InterceptionType;
import org.jboss.weld.interceptor.util.InterceptionUtils;

/**
 * @author Marius Bogoevici
 * @author Marko Luksa
 * @author Jozef Hartinger
 */
public class InterceptorMethodHandler implements MethodHandler, Serializable {

    private final InterceptionContext ctx;

    public InterceptorMethodHandler(InterceptionContext ctx) {
        this.ctx = ctx;
    }

    @Override
    public Object invoke(Object self, Method thisMethod, Method proceed, Object[] args) throws Throwable {
        SecurityActions.ensureAccessible(thisMethod);
        if (proceed == null) {
            if (thisMethod.getName().equals(InterceptionUtils.POST_CONSTRUCT)) {
                return executeInterception(self, null, null, InterceptionType.POST_CONSTRUCT);
            } else if (thisMethod.getName().equals(InterceptionUtils.PRE_DESTROY)) {
                return executeInterception(self, null, null, InterceptionType.PRE_DESTROY);
            }
        } else {
            if (isInterceptorMethod(thisMethod)) {
                return proceed.invoke(self, args);
            }
            return executeInterception(self, thisMethod, args, InterceptionType.AROUND_INVOKE);
        }
        return null;
    }

    protected Object executeInterception(Object instance, Method method, Object[] args, InterceptionType interceptionType) throws Throwable {
        SimpleInterceptionChain chain = new SimpleInterceptionChain(instance, method, args, interceptionType, ctx);
        return chain.invokeNextInterceptor(new InterceptorInvocationContext(chain, instance, method, args));
    }

    private boolean isInterceptorMethod(Method method) {
        return ctx.getInterceptionModel().getTargetClassInterceptorMetadata().isInterceptorMethod(method);
    }
}
