package com.greenpepper.extensions.demo;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.greenpepper.document.Document;
import com.greenpepper.systemunderdevelopment.DefaultSystemUnderDevelopment;
import com.sun.grizzly.http.SelectorThread;
import com.sun.jersey.api.container.grizzly.GrizzlyWebContainerFactory;

public class RestSystemUnderDevelopmentSample extends DefaultSystemUnderDevelopment {
	private SelectorThread threadSelector;

	@Override
	public void onEndDocument(Document document) {
		threadSelector.stopEndpoint();
	}

	@Override
	public void onStartDocument(Document document) {
		try {
			final String baseUri = "http://localhost:9998/";
			final Map<String, String> initParams = new HashMap<String, String>();
			initParams.put("com.sun.jersey.config.property.packages", "com.greenpepper.extensions.demo.resource");
			threadSelector = GrizzlyWebContainerFactory.create(baseUri, initParams);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


}
