package com.tues.softdev.movierating;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.ParameterizedType;

/**
 * This AbstractHandler class includes two generic functionality:
 * <p>
 * - Local runner uses getExampleEvent() method to find the example event that you use during your events.
 *
 * @param <T>
 * @link https://github.com/cagataygurturk/aws-lambda-local-runner
 * <p>
 * - It configures automatically Spring IoC. To enable this support Concrete handler classes extends this class with T type
 * which should implement @Configuration interface.
 * @see MovieRatingHandler
 * <p>
 * <p>
 * Every lambda handler class should extend this abstract base class and normally you do not touch this class
 */
@SuppressWarnings("unused")
public abstract class AbstractHandler<T> {

    /**
     * Spring IOC application context
     */
    private ApplicationContext applicationContext;

    /**
     * Every handler can override this String variable to provide an example event JSON for local running
     *
     * @link https://github.com/cagataygurturk/aws-lambda-local-runner
     */
    protected String exampleEvent = "{}";

    public AbstractHandler() {
        System.out.println("++++++++++ executing AbstractHandler....");

        // Gets config class to create an Application context
        Class typeParameterClass = (Class) ((ParameterizedType) getClass()
                .getGenericSuperclass())
                .getActualTypeArguments()[0];

        //Check if T has @Configuration annotation, if no throws an exception
        if (!typeParameterClass.isAnnotationPresent(Configuration.class)) {
            throw new RuntimeException(typeParameterClass + " is not a @Configuration class");
        }

        //Create Spring application context
        applicationContext = new AnnotationConfigApplicationContext(typeParameterClass);
        //applicationContext = new AnnotationConfigApplicationContext("com.tues.softdev.movierating");
        System.out.println("++++++++++ applicationContext created....");
    }

    /**
     * Use this getter to access to Spring Application Context
     *
     * @return ApplicationContext
     */
    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

}