package com.greenpepper.extensions.grails;

import static java.util.Arrays.asList;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.StringUtils;

import com.greenpepper.reflect.DefaultFixture;
import com.greenpepper.reflect.Fixture;
import com.greenpepper.systemunderdevelopment.DefaultSystemUnderDevelopment;

public class GrailsSystemUnderDevelopment
		extends DefaultSystemUnderDevelopment
		implements ApplicationContextAware {

	private BeanFactory beanFactory;

	public GrailsSystemUnderDevelopment() {
	}

	public GrailsSystemUnderDevelopment(BeanFactory beanFactory) {
		this.beanFactory = beanFactory;
	}

	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {

		beanFactory = applicationContext;
	}

	@Override
	public Fixture getFixture(String fixtureName, String... params)
			throws Throwable {
		if (params.length == 0) {
			Object target;

			try {
				String grailsFixtureName = flattenForGrails(fixtureName);

				target =
						getBean(withSuffix(grailsFixtureName), withSuffix(fixtureName), grailsFixtureName, fixtureName);
			}
			catch (NoSuchBeanDefinitionException ex) {
				target = instantiateAsAutowiredBean(fixtureName);
			}

			// @todo : check for null target

			return new DefaultFixture(target);
		}
		else {
			// When params are used, we use the PlainOldSystemUnderDevelopment to instantiate the fixture
			// these params are passed to the constructor
			return super.getFixture(fixtureName, params);
			// TODO : We still probably can inject members
		}
	}

	private static String withSuffix(String fixtureName) {
		return fixtureName + "Fixture";
	}

	private static String flattenForGrails(String fixtureName) {
		String newFixtureName = StringUtils.uncapitalize(fixtureName);
		newFixtureName = newFixtureName.replaceAll("\\s?", "");
		return newFixtureName;
	}

	private Object getBean(String... fixtureNames) {
		for (String fixtureName : fixtureNames) {
			if (beanFactory.containsBean(fixtureName)) {
				return beanFactory.getBean(fixtureName);
			}
		}

		throw new NoSuchBeanDefinitionException(asList(fixtureNames).toString());
	}

	private Object instantiateAsAutowiredBean(String fixtureName) {

		Class fixtureClass = loadType(fixtureName).getUnderlyingClass();

		String simpleFixtureName = flattenForGrails(fixtureClass.getSimpleName());

		BeanDefinition beanDef = new RootBeanDefinition(fixtureClass, RootBeanDefinition.AUTOWIRE_AUTODETECT);

		DefaultListableBeanFactory fallFactory = new DefaultListableBeanFactory(beanFactory);
		fallFactory.registerBeanDefinition(simpleFixtureName, beanDef);

		return fallFactory.getBean(simpleFixtureName);
	}

}