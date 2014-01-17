package com.greenpepper.extensions.spring;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;

import com.greenpepper.reflect.DefaultFixture;
import com.greenpepper.reflect.Fixture;
import com.greenpepper.systemunderdevelopment.DefaultSystemUnderDevelopment;

public class SpringSystemUnderDevelopment extends DefaultSystemUnderDevelopment
{
    private BeanFactory beanFactory;

    public SpringSystemUnderDevelopment(String... applicationCtxes)
    {
        this.beanFactory = new GreenPepperXMLAplicationContext(applicationCtxes).getBeanFactory();
    }
	
    public SpringSystemUnderDevelopment(BeanFactory beanFactory)
    {
        this.beanFactory = beanFactory;
    }

    @Override
    public Fixture getFixture(String fixtureName, String... params) throws Throwable
    {
		Fixture fixture;
		if (params.length != 0)
        {
		    // When params are used, we use the PlainOldSystemUnderDevelopment to instantiate the fixture
			// these params are passed to the constructor
            fixture = super.getFixture(fixtureName, params);
            // TODO : We still probably can inject members
        }
		else
		{
	        try
	        {
	            fixture = new DefaultFixture(beanFactory.getBean(fixtureName));
	        }
	        catch (NoSuchBeanDefinitionException e)
	        {
	            fixture = new DefaultFixture(instantiateAsAutowiredBean(fixtureName));
	        }
		}
		
		return fixture;
    }

    private Object instantiateAsAutowiredBean(String fixtureName) throws Exception
    {
        Class fixtureClass = loadType(fixtureName).getUnderlyingClass();

        BeanDefinition beanDef = new RootBeanDefinition(fixtureClass, RootBeanDefinition.AUTOWIRE_AUTODETECT);
        
        DefaultListableBeanFactory fallFactory = new DefaultListableBeanFactory(beanFactory);
        fallFactory.registerBeanDefinition(fixtureName, beanDef);

        return fallFactory.getBean(fixtureName);
    }
}