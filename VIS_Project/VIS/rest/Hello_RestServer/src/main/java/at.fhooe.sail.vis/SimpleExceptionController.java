//
// Created by Sandra Hartlauer on 22.01.2024.
//

package at.fhooe.sail.vis;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class SimpleExceptionController implements ExceptionMapper<Throwable> {

    @Override
    public Response toResponse(Throwable exception) {
        String errorMessage = "An error occurred: " + exception.getMessage();
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("<html><body><h1>Error Page</h1><p>" + errorMessage + "</p></body></html>")
                .type(MediaType.TEXT_HTML)
                .build();
    }
}
