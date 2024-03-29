package org.openconfig.factory.impl;

import com.google.inject.Inject;
import com.google.inject.Injector;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.apache.log4j.Logger;
import org.openconfig.Configurator;
import org.openconfig.core.ConfiguratorProxy;
import org.openconfig.core.DataProviderToConfiguratorAdapter;
import org.openconfig.core.bean.ConfiguratorProxyInvocationHandler;
import org.openconfig.core.bean.PropertyNormalizer;
import org.openconfig.core.bean.ProxyInvocationHandler;
import org.openconfig.event.EventListener;
import org.openconfig.event.EventPublisher;
import org.openconfig.factory.ConfiguratorFactory;
import org.openconfig.providers.DataProvider;

import java.lang.reflect.Method;

/**
 * @author Richard L. Burton III
 * @author Dushyanth (Dee) Inguva
 */
@SuppressWarnings("unchecked")
public class DefaultConfiguratorFactory implements ConfiguratorFactory {

    private static final Logger LOGGER = Logger.getLogger(DefaultConfiguratorFactory.class);

    @Inject
    private Injector injector;

    @Inject
    private DataProvider dataProvider;

    @Inject
    private EventPublisher eventPublisher;

    @Inject
    private PropertyNormalizer propertyNormalizer;

    /**
     * @see ConfiguratorFactory#(Class, boolean, EventListener)
     */
    public <T> T newInstance(final Class clazz, EventListener... eventListeners) {
        Enhancer enhancer = new Enhancer();

        if (clazz.isInterface()) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Creating a proxy for the following interface: " + clazz.getName());
            }
            enhancer.setInterfaces(new Class[]{clazz});
        } else {

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Creating a proxy for the following class: " + clazz.getName());
            }
            enhancer.setSuperclass(clazz);
        }


        final ConfiguratorProxy proxy = newConfiguratorProxy(clazz, eventListeners);

        enhancer.setCallback(new MethodInterceptor() {
            public Object intercept(Object source, Method method, Object[] arguments, MethodProxy methodProxy) throws Throwable {
                return proxy.intercept(source, method, arguments, methodProxy);
            }
        });
        return (T) enhancer.create();
    }

    public Configurator newInstance(EventListener... eventListeners) {
        return new DataProviderToConfiguratorAdapter(dataProvider);
    }

    private ConfiguratorProxy newConfiguratorProxy(Class configuratorInterface, EventListener... eventListeners) {
        ConfiguratorProxy proxy = new ConfiguratorProxy(configuratorInterface);
        ProxyInvocationHandler returnHandler = new ConfiguratorProxyInvocationHandler(proxy);
        dataProvider.registerEventListeners(configuratorInterface.getSimpleName(), eventListeners);
        proxy.setDataProvider(dataProvider);
        proxy.setPropertyNormalizer(propertyNormalizer);
        proxy.setProxyInvocationHandler(returnHandler);
        return proxy;
    }

    public void setDataProvider(DataProvider dataProvider) {
        this.dataProvider = dataProvider;
    }

    public void setEventPublisher(EventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    public void setPropertyNormalizer(PropertyNormalizer propertyNormalizer) {
        this.propertyNormalizer = propertyNormalizer;
    }

    public void setInjector(Injector injector) {
        this.injector = injector;
    }
}
