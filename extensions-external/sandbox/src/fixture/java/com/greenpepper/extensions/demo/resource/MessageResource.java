package com.greenpepper.extensions.demo.resource;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HEAD;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Response.Status;

@Path("/messages")
public class MessageResource {
	private static final Response OK = Response.ok().build();
	private static final Response NOT_FOUND = Response.status(Status.NOT_FOUND).build();
	private static Map<Integer, String> messageStorage = new HashMap<Integer, String>();

	@Context
	private UriInfo uriInfo;
	
	@POST
	public Response create(String message) throws URISyntaxException {
		int generatedId = generateId();
		messageStorage.put(generatedId, message);
		return Response.created(buildLocationUriForMessageId(generatedId)).build();
	}

	@HEAD
	@Path("/{id}")
	@Produces("text/plain")
	public Response exists(@PathParam("id")Integer id) {
		return messageExists(id) ? OK : NOT_FOUND;
	}
	
	@GET
	@Path("/{id}")
	@Produces("text/plain")
	public Response getByIdAsText(@PathParam("id")Integer id) {
		return messageExists(id) ? Response.ok(messageStorage.get(id)).build() : NOT_FOUND;
	}

	@GET
	@Path("/{id}")
	@Produces("application/xml")
	public Response getByIdAsXml(@PathParam("id")Integer id) {
		if (messageExists(id)) {
			String messageAsXml = String.format("<?xml version=\"1.0\"><message id=\"%d\" value=\"%s\" />",
					 id, messageStorage.get(id));
			return Response.ok(messageAsXml).build();
		}
		return NOT_FOUND;
	}
	
	@DELETE
	@Path("/{id}")
	public Response delete(@PathParam("id")Integer id) throws URISyntaxException {
		return messageStorage.remove(id) == null ? NOT_FOUND : OK;
	}
	
	@PUT
	@Path("/{id}")
	public Response put(@PathParam("id")Integer id, String message) throws URISyntaxException {
		if (messageExists(id)) {
			messageStorage.put(id, message);
			return OK;
		}
		return NOT_FOUND;
	}
	
	private URI buildLocationUriForMessageId(int id) throws URISyntaxException {
		return new URI(uriInfo.getAbsolutePath().toString() + "/" + id);
	}
	
	private boolean messageExists(Integer id) {
		return messageStorage.containsKey(id);
	}
	
	private int generateId() {
		return messageStorage.size();
	}
}
