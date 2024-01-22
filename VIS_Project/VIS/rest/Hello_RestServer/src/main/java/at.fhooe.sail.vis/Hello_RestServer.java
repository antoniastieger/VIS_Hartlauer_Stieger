package at.fhooe.sail.vis;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@ApplicationPath("/rest")
@Path("/")
public class Hello_RestServer {
    @GET
    @Produces(MediaType.TEXT_HTML)
    public String getIndex() {
        return "<html><body><h1>Welcome to HelloRestServer</h1><a href=\"rest\">Go to REST landing page</a></body></html>";
    }

    @GET
    @Path("/rest")
    @Produces(MediaType.TEXT_HTML)
    public String getRestLandingPage() {
        return "<html><body><h1>REST Landing Page</h1><p>Available paths and parameters:</p><ul><li><a href=\"test\">/test</a></li></ul></body></html>";
    }

    @GET
    @Path("/rest/test")
    @Produces(MediaType.APPLICATION_XML)
    public String getTestXml() {
        return "<data><message>Hello from Test XML</message></data>";
    }
}

/*
Index: http://localhost:8080/HelloRestServer/
REST Landing Page: http://localhost:8080/HelloRestServer/rest
Test XML: http://localhost:8080/HelloRestServer/rest/test
 */