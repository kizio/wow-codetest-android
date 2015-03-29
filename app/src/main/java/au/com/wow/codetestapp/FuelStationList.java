package au.com.wow.codetestapp;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Wrapper around a {@link JSONArray} to provide helper methods for accessing the fuel station data.
 * <p>
 * I've gone for a wrapper rather than reading and converting the data because it's less effort. It
 * won't be as efficient as decoding everything once, and then storing the values in a simple class,
 * but that would have taken longer to do. Were this a commercial piece of software then I'd put in
 * the time.
 * </p>
 * <p>
 * The sensible thing would have been to use a library such as
 * <a href="https://code.google.com/p/google-gson/">GSON</a>. However that would have necessitated
 * writing POJOs for the fuel station items, which would probably have been more work than the
 * wrapper. And since I'm not being paid for this...
 * </p>
 *
 * @author Graeme Sutherland
 * @since 29/03/2015.
 */
public final class FuelStationList {

	/**
	 * The application {@link Context}.
	 */
	private final Context context;

	/**
	 * The {@link JSONArray} that contains the fuel station data.
	 */
	private final JSONArray array;

	/**
	 * Constructor.
	 *
	 * @param aContext The {@link Context} the activity is running in
	 * @param object The {@link JSONObject} to display
	 */
	public FuelStationList (final Context aContext, final JSONObject object) {
		super ();

		this.context = aContext.getApplicationContext();

		// TODO Throw an illegal argument exception instead if the object is null. This allows the
		// null checks (below) to be skipped, but adds the need for more exception handling code.
		if (object != null) {
			this.array = object.optJSONArray ("item");
		} else {
			this.array = null;
		}
	}

	/**
	 * Gets the {@link FuelStationItem} at the specified index.
	 *
	 * @param index The position of the item to get in the array
	 * @return The value at that position, wrapped in a {@link FuelStationItem} object
	 */
	public FuelStationItem get (final int index) {
		final JSONObject item = this.array != null ? this.array.optJSONObject(index) : null;

		return new FuelStationItem (this.context, item);
	}

	/**
	 * Gets the size of the item list.
	 *
	 * @return The size of the list
	 */
	public int getSize () {
		return this.array != null ? this.array.length() : 0;
	}
}
