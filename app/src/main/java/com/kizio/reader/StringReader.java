package com.kizio.reader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Reads the contents of an {@link InputStreamReader} into a {@link String}.
 *
 * @author Graeme Sutherland
 * @since 27/03/2015
 */
public class StringReader extends BufferedReader {

	/**
	 * Constructor. Creates the reader based around the supplied {@link InputStreamReader}.
	 *
	 * @param input The {@link InputStreamReader} to be parsed
	 */
	public StringReader (final InputStreamReader input) {
		super (input);
	}

	/**
	 * Reads the contents of the input stream into a single {@link String}. This piece of code is
	 * always repeated, so may as well embed it in a Reader class.
	 *
	 * @return The contents of the input as a {@link String}
	 * @throws IOException If there is an error during the IO process
	 */
	public String readString () throws IOException {
		final StringBuffer buffer = new StringBuffer();
		final String separator = System.getProperty("line.separator");
		String line;

		while ((line = readLine()) != null) {
			buffer.append(line);
			buffer.append(separator);
		}

		return buffer.toString();
	}
}
