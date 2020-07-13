package com.example.somespring.springconfiguration.beanpostprocessors;

import com.example.somespring.entity.History;
import com.example.somespring.service.AlgorithmService;
import com.example.somespring.service.HistoryService;
import com.example.somespring.springconfiguration.annotations.SaveHistory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@Component
public class SaveHistoryAnnotationBeanPostProcessor implements BeanPostProcessor {
    Map<String, Class> map = new HashMap<>();


    @Autowired
    private AlgorithmService algorithmService;

    @Autowired
    private HistoryService historyService;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Class<?> beanClass = bean.getClass();
        for (Method method : beanClass.getMethods()) {
            if (method.isAnnotationPresent(SaveHistory.class)) {
                map.put(beanName, beanClass);
            }
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> beanClass = map.get(beanName);

        if (beanClass != null) {
            for (Method someMethod : beanClass.getMethods()) {
                if (someMethod.isAnnotationPresent(SaveHistory.class)) {
                    /*return Proxy.newProxyInstance(beanClass.getClassLoader(), beanClass.getInterfaces(), new InvocationHandler() {
                        @Override
                        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                            System.out.println("Arguments : " + Arrays.toString(args));
                            return method.invoke(bean, args);
                        }
                    });*/


                    MethodInterceptor handler = (o, method, objects, methodProxy) -> {
                        History historyAdd = (History) objects[0];
                        String algorithmName = (String) objects[2];
                        historyAdd.setAlgorithm(algorithmService.findByName(algorithmName));
                        Object res = methodProxy.invoke(bean, objects);
                        historyService.save(historyAdd);
                        return res;
                    };
                    return Enhancer.create(beanClass, beanClass.getInterfaces(), handler);
                }
            }
        }
        return bean;
    }
}


