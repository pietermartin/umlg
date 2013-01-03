package org.tuml.runtime.restlet;

import org.restlet.data.MediaType;
import org.restlet.representation.WriterRepresentation;

import java.io.IOException;
import java.io.Writer;

public class ErrorJsonRepresentation extends WriterRepresentation {

	private String json;

	public ErrorJsonRepresentation(String json) {
		super(MediaType.APPLICATION_JSON);
		this.json = json;
	}

	@Override
	public void write(Writer writer) throws IOException {
		writer.write(this.json);
	}
}
