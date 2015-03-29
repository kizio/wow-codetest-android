package au.com.wow.codetestapp;

import android.app.ListFragment;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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
	 * The {@link FuelStationTask} used to load the list of fuel stations.
	 */
	private FuelStationTask task = null;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);

	    task = new FuelStationTask (this);

	    task.execute (URL);
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

	public void setFuelStationList (final FuelStationList list) {
		this.task = null;

		setListAdapter (new FuelStationListAdapter (getActivity (), list));
	}

    private void refresh() {
        //refresh the list
    }
}
