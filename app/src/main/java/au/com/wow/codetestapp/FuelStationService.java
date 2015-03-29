package au.com.wow.codetestapp;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.kizio.reader.JSONReader;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Background process for loading the fuel station data.
 *
 * @author Graeme Sutherland
 * @since 29/03/2015.
 */
public class FuelStationService extends IntentService {

	/**
	 * Parameter for passing the fuel station list URL.
	 */
	public static final String URL = "url";

	/**
	 * Parameter for passing in a refresh request.
	 */
	public static final String IS_REFRESH = "is_refresh";

	/**
	 * Parameter for passing a list of fuel stations back to the UI.
	 */
	public static final String FUEL_STATIONS = "fuel_stations";

	/**
	 * Action for handling an update fuel stations call.
	 */
	public static final String UPDATE_FUEL_STATIONS = "update_fuel_stations";

	/**
	 * Flag to track whether a download has been triggered.
	 */
	private boolean isDownloadTriggered;

	/**
	 * Constructor.
	 */
	public FuelStationService () {
		super (FuelStationService.class.getName ());

		isDownloadTriggered = false;
	}

	/**
	 * Handles an incoming {@link Intent}.
	 *
 	 * @param intent The {@link Intent} used to start the service
	 */
	@Override
	protected void onHandleIntent (final Intent intent) {
		if (intent != null) {
			final String url = intent.getStringExtra (URL);
			this.isDownloadTriggered |= intent.getBooleanExtra (IS_REFRESH, false);

			// Block downloads if no URL is supplied, or no download has been started.
			if (this.isDownloadTriggered && url != null && !url.isEmpty ()) {
				try {
					this.isDownloadTriggered = true;
					final FuelStationList result = new FuelStationList (this, downloadData (url));
					final Intent resultIntent = new Intent (UPDATE_FUEL_STATIONS);

					resultIntent.putExtra (FUEL_STATIONS, result);

					// This could be done with the LocalBroadcastManager, but it's easier to do it
					// this way than add the support library to this project!
					sendBroadcast (resultIntent);
				} catch (final Exception e) {
					// If I was doing this properly then I'd handle the error, and figure some way
					// of closing the app down gracefully. However, I'm not being paid to do this so
					// you'll have to make do with an error message to the logger.
					Log.e ("FuelStationTask", "Failed to download!", e);

					this.isDownloadTriggered = false;
				}
			}
		}
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
	 * <p>
	 * Note that the <code>@SuppressWarnings</code> annotation is to prevent the warning that the
	 * <code>try / catch</code> block can be simplified for Java 7. Unfortunately the version of
	 * Android I'm targetting doesn't support that yet.
	 * </p>
	 *
	 * @param stream The {@link InputStream} from the HTTP connection
	 * @return A {@link String} containing the server's response
	 * @throws IOException If there is an IO error
	 */
	@SuppressWarnings ("all")
	private JSONObject handleResponse (final InputStream stream) throws IOException, JSONException
	{
		final JSONReader reader = new JSONReader (new InputStreamReader (stream));
		JSONObject result = null;

		try {
			result = reader.readJSON();
		} finally {
			reader.close ();
		}

		return result;
	}
}
