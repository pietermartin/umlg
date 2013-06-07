package org.tuml.runtime.restlet;

import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.service.StatusService;

/**
 * Date: 2013/02/08
 * Time: 3:21 PM
 */
public class ErrorStatusService extends StatusService {

    @Override
    public Representation getRepresentation(Status status, Request request, Response response) {
        StringBuilder sb = new StringBuilder();
        Throwable throwable = status.getThrowable();
        if (throwable != null) {
            sb.append(throwable.getMessage());
            return new StringRepresentation(String.format("Http Status = %s\n + Error = %s", new Object[]{status.toString(), sb.toString()}));
        } else {
            return new StringRepresentation(String.format("Http Status = %s", new Object[]{status.toString()}));
        }
    }

}
