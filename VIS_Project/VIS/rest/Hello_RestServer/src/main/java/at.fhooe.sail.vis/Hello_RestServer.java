//
// Created by Sandra Hartlauer on 22.01.2024.
//

package at.fhooe.sail.vis;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@ApplicationPath("/rest")
@Path("/")
public class Hello_RestServer {
    @GET
    @Produces(MediaType.TEXT_HTML)
    public String getIndex() {
        return "<html><body><h1>Hello_RestServer</h1><a href=\"rest\">Go to REST landing page</a></body></html>";
    }

    @GET
    @Path("/rest")
    @Produces(MediaType.TEXT_HTML)
    public Response getRestLandingPage() {
        Response.ResponseBuilder bob = Response.ok();
        bob.type(MediaType.TEXT_HTML);
        bob.entity("<html><head><title>Hello_RestServer</title> </head>"
                + "<body>"
                + "<h1>REST Landing Page</h1>"
                + "</body> </html>");
        return bob.build();
    }

    @GET
    @Path("/test")
    @Produces(MediaType.APPLICATION_XML)
    public Response getTestXml() {
        Response.ResponseBuilder bob = Response.ok();
        bob.type(MediaType.APPLICATION_XML);
        bob.entity("<message>This is a Test</message>");
        return bob.build();
    }

    /*@GET
    @Path("/exception")
    @Produces(MediaType.TEXT_HTML)
    public String getExceptionLandingPage() {
        return "<html><body><h1>Exception Branch Landing Page</h1><p>Available paths and parameters:</p><ul><li><a href=\"test\">/test</a></li></ul></body></html>";
    }

    @GET
    @Path("/exception/test")
    @Produces(MediaType.TEXT_HTML)
    public String testException() {
        // Simulate a random exception occurrence
        if (Math.random() < 0.5) {
            throw new RuntimeException("Simulated exception occurred!");
        } else {
            return "<html><body><h1>No exception occurred</h1><p>Test successful!</p></body></html>";
        }
    }*/
}

/*
Index: http://localhost:8443/Hello_RestServer/rest
REST Landing Page: http://localhost:8443/Hello_RestServer/rest/rest
Test XML: http://localhost:8443/Hello_RestServer/rest/rest/test
Exception Landing Page: http://localhost:8443/Hello_RestServer/rest/exception
Test Exception: http://localhost:8443/Hello_RestServer/rest/exception/test
*/