package au.com.wow.codetestapp;

import android.app.Activity;
import android.app.ListFragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

/**
 * display whole station list. each item contains icon, address and distance
 */
public class FuelStationListFragment extends ListFragment {

	/**
	 * NewYork Petrol stations info.
	 */
    private final static String URL = "http://www.mshd.net/api/gasprices/10025";

	/**
	 * The broadcast receiver handling updates from the server.
	 */
	private FuelStationBroadcastReceiver receiver = new FuelStationBroadcastReceiver ();

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setHasOptionsMenu(true);
    }

	/**
	 * Invoked when the fragment resumes.
	 */
	@Override
	public void onResume () {
		super.onResume ();

		final IntentFilter filter = new IntentFilter (FuelStationService.UPDATE_FUEL_STATIONS);

		getActivity ().registerReceiver (this.receiver, filter);

		downloadData (false);
	}

	/**
	 * Invoked when the fragment is paused.
	 */
	@Override
	public void onPause () {
		super.onPause ();

		getActivity ().unregisterReceiver (this.receiver);
	}

	@Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        super.onListItemClick(listView, view, position, id);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.add(Menu.NONE, Menu.FIRST, 0, "Refresh").setIcon(R.drawable.ic_refresh).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == Menu.FIRST) {
            refresh();
        }
        return super.onOptionsItemSelected(item);
    }

	/**
	 * Sets the {@link FuelStationList} that is displayed.
	 *
	 * @param list The {@link FuelStationList} that is to be displayed
	 */
	private void setFuelStationList (final FuelStationList list) {
		setListAdapter (new FuelStationListAdapter (getActivity (), list));
	}

	/**
	 * Forces a refresh of the data.
	 */
    private void refresh() {
        downloadData (true);
    }

	/**
	 * Requests that the app downloads data.
	 *
	 * @param isRefresh True if a refresh is to be forced, false otherwise
	 */
	private void downloadData (final boolean isRefresh) {
		final Activity activity = getActivity ();
		final Intent intent = new Intent (activity, FuelStationService.class);

		intent.putExtra (FuelStationService.URL, URL);
		intent.putExtra (FuelStationService.IS_REFRESH, isRefresh);

		activity.startService (intent);
	}

	/**
	 * Handles results from the intent service.
	 */
	private class FuelStationBroadcastReceiver extends BroadcastReceiver {

		/**
		 * Handles receiving the results from the download service.
		 *
		 * @param context The {@link Context} the result is coming from
		 * @param intent An {@link Intent} containing additional information
		 */
		@Override
		public void onReceive (final Context context, final Intent intent) {
			if (intent != null && intent.hasExtra (FuelStationService.FUEL_STATIONS)) {
				setFuelStationList ((FuelStationList) intent.getParcelableExtra
						(FuelStationService.FUEL_STATIONS));
			}
		}
	}
}
