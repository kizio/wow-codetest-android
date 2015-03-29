package au.com.wow.codetestapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Adapter for displaying data in the fuel station list.
 *
 * @author Graeme Sutherland
 * @since 29/03/2015.
 */
public class FuelStationListAdapter extends BaseAdapter {

	/**
	 * The {@link FuelStationList} containing the data to display.
	 */
	private final FuelStationList list;

	/**
	 * The {@link LayoutInflater} used to inflate new views.
	 */
	private final LayoutInflater inflater;

	/**
	 * Constructor.
	 *
	 * @param context The {@link Context} in which the list is being displayed
	 * @param fuelStationList The {@link FuelStationList} to display
	 */
	public FuelStationListAdapter (final Context context, final  FuelStationList fuelStationList) {
		super ();

		this.list = fuelStationList;
		this.inflater = LayoutInflater.from (context);
	}

	/**
	 * Gets the number of items to display.
	 *
	 * @return The number of items in the list
	 */
	@Override
	public int getCount () {
		return this.list.getSize ();
	}

	/**
	 * Gets the item at the specified position. This returns a {@link FuelStationItem}, so as to
	 * save casting an {@link Object} when used as an internal method.
	 *
	 * @param position The position of the item in the list
	 * @return The {@link FuelStationItem} at the specified position
	 */
	@Override
	public FuelStationItem getItem (final int position) {
		return this.list.get (position);
	}

	/**
	 * Gets the unique ID for an item in the list. This is defined as its position.
	 *
	 * @param position The position of the item in the list
	 * @return The unique ID
	 */
	@Override
	public long getItemId (final int position) {
		return position;
	}

	/**
	 * Gets the {@link View} at the specified position.
	 * <p>
	 * TODO Use a view holder class instead of looking up the child views each time.
	 * </p>
	 *
	 * @param position The position of the item in the list
	 * @param convertView The {@link View} to recycle if possible
	 * @param parent The parent {@link ViewGroup} that it's being added to
	 * @return The {@link View} to display
	 */
	@Override
	public View getView (final int position, final View convertView, final ViewGroup parent) {
		final FuelStationItem item = getItem (position);
		final View view;

		// Resetting parameters is bad programming practice, so a new variable is used instead.
		if (convertView == null) {
			view = this.inflater.inflate (R.layout.fuel_station_list_item, null);
		} else {
			view = convertView;
		}

		((TextView) view.findViewById (R.id.brand)).setText (item.getBrand ());
		((TextView) view.findViewById (R.id.address)).setText (item.getAddress ());
		((TextView) view.findViewById (R.id.distance)).setText (item.getDistance ());

		return view;
	}
}
