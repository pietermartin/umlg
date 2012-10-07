package org.tuml.ui;

import java.io.IOException;
import java.io.Writer;

import org.restlet.data.MediaType;
import org.restlet.representation.WriterRepresentation;

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
