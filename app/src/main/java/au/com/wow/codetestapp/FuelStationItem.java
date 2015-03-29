package au.com.wow.codetestapp;

import android.content.Context;

import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * Wrapper for a {@link JSONObject} so that its contents can be queried and formatted to be
 * displayed correctly in the fuel guide list view.
 *
 * @author Graeme Sutherland
 * @since 27/03/2015.
 */
public class FuelStationItem {

	/**
	 * The {@link JSONObject} whose data is to be displayed.
	 */
	private final JSONObject fuelStation;

	/**
	 * {@link NumberFormat} to convert the price value into a Dollar {@link String}.
	 */
	private final NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(Locale.US);

	/**
	 * The {@link String} to use if no value is available.
	 */
	private final String notApplicable;

	/**
	 * Constructor.
	 *
	 * @param context The {@link Context} the app is being displayed in
	 * @param object The {@link JSONObject} representing the fuel station
	 */
	public FuelStationItem (final Context context, final JSONObject object) {
		super ();

		this.fuelStation = object;
		this.notApplicable = context.getString(R.string.not_applicable);
	}

	/**
	 * Gets the brand of petrol.
	 *
	 * @return The fuel station's brand
	 */
	public String getBrand () {
		return getField("brand");
	}

	/**
	 * Gets the address of the fuel station.
	 *
	 * @return The fuel station's address
	 */
	public String getAddress () {
		return getField ("address");
	}

	/**
	 * Gets the distance to the fuel station.
	 *
	 * @return The distance to the fuel station
	 */
	public String getDistance () {
		return getField ("distance");
	}

	/**
	 * Gets the URL for the image to be displayed by this entry.
	 *
	 * @return The URL of the image to be displayed
	 */
	public String getImageUrl () {
		return this.fuelStation != null ? fuelStation.optString ("img") : null;
	}

	/**
	 * Gets the price of <em>Regular</em> fuel as a formatted string, or "N/A" if it's not
	 * available.
	 *
	 * @return The price of regular fuel
	 */
	public String getRegularPrice () {
		return getPrice("regular");
	}

	/**
	 * Gets the price of <em>Plus</em> fuel as a formatted string, or "N/A" if it's not available.
	 *
	 * @return The price of plus fuel
	 */
	public String getPlusPrice () {
		return getPrice("plus");
	}

	/**
	 * Gets the price of <em>Premium</em> fuel as a formatted string, or "N/A" if it's not
	 * available.
	 *
	 * @return The price of premium fuel
	 */
	public String getPremiumPrice () {
		return getPrice("regular");
	}

	/**
	 * Gets the price of <em>Diesel</em> as a formatted string, or "N/A" if it's not available.
	 *
	 * @return The price of diesel
	 */
	public String getDieselPrice () {
		return getPrice("diesel");
	}

	/**
	 * Helper method to get a {@link String} containing the contents of a field, or "N/A" if it's
	 * not mapped, or the JSON object is null.
	 *
	 * @param field The JSON field to extract
	 * @return The contents of the field as a {@link String}
	 */
	private String getField (final String field) {
		final Object value;

		if (this.fuelStation != null) {
			value = this.fuelStation.opt(field);
		} else {
			value = null;
		}

		// As it stands, this may return NULL for some empty values.
		return value != null ? value.toString() : this.notApplicable;
	}

	/**
	 * Helper method to extract the price of the specified fuel from the JSON, and format it into a
	 * Dollar string.
	 * <p>
	 * The JSON source appears to use <code>false</code> to indicate that a fuel is missing, so any
	 * value that can't be coerced into a <code>double</code> is replaced by "N/A".
	 * </p>
	 *
	 * @param field The JSON field that should be holding a number
	 * @return The corresponding price (in USD), or N/A if it can't be translated
	 */
	private String getPrice (final String field) {
		final String price;

		if (this.fuelStation != null && this.fuelStation.optDouble(field) != Double.NaN) {
			price = this.currencyFormatter.format(this.fuelStation.optDouble(field, 0));
		} else {
			price = this.notApplicable;
		}

		return price;
	}
}
