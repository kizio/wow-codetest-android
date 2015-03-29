package au.com.wow.codetestapp;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
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
public final class FuelStationList implements Parcelable {

	/**
	 * Creates a new {@link FuelStationList} from a {@link Parcelable}.
	 */
	public static final Creator<FuelStationList> CREATOR = new Creator<FuelStationList>() {

		/**
		 * Creates a new {@link FuelStationList} from a {@link Parcelable}.
		 *
		 * @param in The {@link Parcelable} to recreate the object from
		 * @return The recreated {@link FuelStationList}
		 */
		public FuelStationList createFromParcel (final Parcel in) {
			final String json = in.readString ();
			final String notApplicable = in.readString ();
			JSONArray array = null;

			if (!json.isEmpty ()) {
				try {
					array = new JSONArray (json);
				} catch (final JSONException jse) {
					Log.e (FuelStationList.class.getName (), "Failed to recreate JSONArray", jse);
				}
			}

			return new FuelStationList (array, notApplicable);
		}

		/**
		 * Creates an empty array of {@link FuelStationList} of the specified size.
		 *
		 * @param size The size of the array to create
		 * @return The newly created array
		 */
		@Override
		public FuelStationList[] newArray (final int size) {
			return new FuelStationList[size];
		}
	};

	/**
	 * The {@link String} to use if no value is available.
	 */
	private final String notApplicable;

	/**
	 * The {@link JSONArray} that contains the fuel station data.
	 */
	private final JSONArray array;

	/**
	 * Constructor.
	 *
	 * @param context The {@link Context} the app is running in
	 * @param object The {@link JSONObject} to display
	 */
	public FuelStationList (final Context context, final JSONObject object) {
		this (object != null ? object.optJSONArray ("item") : null,
				context.getString (R.string.not_applicable));
	}

	/**
	 * Constructor. Creates the object from the supplied {@link JSONArray}. Used for recreating the
	 * object from a {@link Parcel}.
	 *
	 * @param anArray The {@link JSONArray} to display
	 * @param notApplicableString The text to display when there's no value set
	 */
	private FuelStationList (final JSONArray anArray, final String notApplicableString) {
		super ();

		this.array = anArray;
		this.notApplicable = notApplicableString;
	}

	/**
	 * Gets the {@link FuelStationItem} at the specified index.
	 *
	 * @param index The position of the item to get in the array
	 * @return The value at that position, wrapped in a {@link FuelStationItem} object
	 */
	public FuelStationItem get (final int index) {
		final JSONObject item = this.array != null ? this.array.optJSONObject(index) : null;

		return new FuelStationItem (item, this.notApplicable);
	}

	/**
	 * Gets the size of the item list.
	 *
	 * @return The size of the list
	 */
	public int getSize () {
		return this.array != null ? this.array.length() : 0;
	}

	/**
	 * Gets a set of flags describing this object's contents.
	 *
	 * @return Always returns 0
	 */
	@Override
	public int describeContents () {
		return 0;
	}

	/**
	 * Writes the result to a {@link Parcel}.
	 *
	 * @param destination The {@link Parcel} to write to
	 * @param flags The flags to apply to the parcel
	 */
	@Override
	public void writeToParcel (final Parcel destination, final int flags) {
		destination.writeString (this.array != null ? this.array.toString () : "");
		destination.writeString (this.notApplicable);
	}
}
