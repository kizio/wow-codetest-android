package com.kizio.reader;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Reads in an input as a JSON object.
 * <p>
 * I'm using the native library rather than GSON or Jackson because the petrol price can be either
 * a number or a boolean. I'm not sure how to handle this discrepancy with the third party APIs,
 * and it's probably as quick to roll my own solution.
 * </p>
 *
 * @author Graeme Sutherland
 * @since 27/03/2015.
 */
public class JSONReader extends StringReader {

	/**
	 * Constructor.
	 *
	 * @param input The {@link InputStreamReader} to parse
	 */
	public JSONReader(final InputStreamReader input) {
		super (input);
	}

	/**
	 * Convenience method that reads the contents of the {@link InputStreamReader} into a
	 * {@link JSONObject}.
	 *
	 * @return The contents of the input as a {@link JSONObject}
	 * @throws IOException If there's an IO error
	 * @throws JSONException If there's an error parsing the JSON
	 */
	public JSONObject readJSON () throws IOException, JSONException{
		return new JSONObject(readString());
	}
}
