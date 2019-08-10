package hu.stewe.UpgradeLite;

import hu.stewe.UpgradeLite.UpgradeItem;
import hu.stewe.UpgradeLite.R;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

public class UpgradeMenuAdapter extends BaseAdapter
{
	private final List<UpgradeItem> upgrades;
	
	public UpgradeMenuAdapter(final Context context, final ArrayList<UpgradeItem> vUpgrades) {
    	upgrades = vUpgrades;
    }

	public int getCount()
	{
		return upgrades.size();
	}

	public Object getItem(int i)
	{
		return upgrades.get(i);
	}

	public long getItemId(int position)
	{
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent)
	{
		final UpgradeItem item = upgrades.get(position);


		LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	        
		View itemView = inflater.inflate(R.layout.upgrade_menu_item, null);
	        
        TextView uName = (TextView) itemView.findViewById(R.id.upgradeItem);
        //textViewTitle.setText(todo.getTitle());
        uName.setText(item.getName());
        
        RatingBar uLvl = (RatingBar) itemView.findViewById(R.id.upgradeItemBar);
        //textViewDueDate.setText(todo.getDueDate());
        uLvl.setProgress(item.getLvl());
        
        return itemView;
	}

}
