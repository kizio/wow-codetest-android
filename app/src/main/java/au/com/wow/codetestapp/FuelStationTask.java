package au.com.wow.codetestapp;

import android.os.AsyncTask;
import android.util.Log;

import com.kizio.reader.JSONReader;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Invoked to download the details of the fuel stations for a given location.
 *
 *  @author Graeme Sutherland
 *  @since 27/03/15
 */
public class FuelStationTask extends AsyncTask <String, Void, FuelStationList> {

	/**
	 * The {@link FuelStationListFragment} this task is running in.
	 */
	private final FuelStationListFragment fragment;

	/**
	 * Constructor.
	 *
	 * @param aFragment The {@link FuelStationListFragment} the task is being run in
	 */
	public FuelStationTask (final FuelStationListFragment aFragment) {
		super ();

		this.fragment = aFragment;
	}

	/**
	 * Downloads the list of fuel stations on a background thread.
	 *
	 * @param urls The URLs to download the data from
	 * @return A {@link List} of fuel stations
	 */
	@Override
	protected FuelStationList doInBackground (final String... urls) {
		FuelStationList result = null;

		if (urls != null && urls.length > 0) {
			try {
				result = new FuelStationList (this.fragment.getActivity (), downloadData (urls[0]));
			} catch (final Exception e) {
				// TODO If I was doing this properly then I'd handle the error, and figure some way
				// of closing the app down gracefully. However, I'm not being paid to do this so
				// you'll have to make do with an error message to the logger.
				Log.e("FuelStationTask", "Failed to download!", e);
			}
		}

		return result;
	}

	/**
	 * Invoked when the task is complete. Sets the fuel station list to display.
	 *
	 * @param fuelStationList The downloaded {@link FuelStationList} that is to be displayed
	 */
	@Override
	protected void onPostExecute (final FuelStationList fuelStationList) {
		this.fragment.setFuelStationList (fuelStationList);
	}

	/**
	 * Downloads the JSON data from the specified URL.
	 *
	 * @param urlString The URL of the web service from which data is to be downloaded
	 * @return A {@link JSONObject} containing the downloaded data
	 * @throws IOException If there's an IO error
	 * @throws JSONException If there's a JSON processing error
	 */
	private JSONObject downloadData (final String urlString) throws IOException, JSONException {
		HttpURLConnection connection = null;
		JSONObject result = null;

		try {
			final URL url = new URL (urlString);
			connection = (HttpURLConnection) url.openConnection ();

			result = handleResponse(connection.getInputStream());
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}

		return result;
	}

	/**
	 * Reads the response from the HTTP connection, and parses it into a{@link JSONObject}.
	 *
	 * @param stream The {@link InputStream} from the HTTP connection
	 * @return A {@link String} containing the server's response
	 * @throws IOException If there is an IO error
	 */
	private JSONObject handleResponse (final InputStream stream) throws IOException, JSONException
	{
		final JSONReader reader = new JSONReader (new InputStreamReader(stream));
		JSONObject result = null;

		try {
			result = reader.readJSON();
		} finally {
			reader.close ();
		}

		return result;
	}
}
