package mygrocerylist.listandshop;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ListViewClass extends ArrayAdapter<Items> implements Filterable {

    private Context mContext;
    private ArrayList<Items> itemList, tempItemlist, suggestions;
    /*    private ArrayList<Integer> theIcon = new ArrayList<>();*/
    Items items;


    public ListViewClass(@NonNull Context context, ArrayList<Items> objects) {
        super(context, 0, objects);


        this.itemList = objects;
        this.tempItemlist = new ArrayList<Items>(objects);
        this.suggestions = new ArrayList<Items>(objects);


    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {


        return initView(position, convertView, parent);

    }

//http://www.apnatutorials.com/android/search-a-custom-listview-in-android.php?categoryId=2

    private View initView(int position, View convertView, ViewGroup parent) {
        Items item = (Items) getItem(position);

        if (convertView == null) {
            if (parent == null)
                convertView = LayoutInflater.from(getContext()).inflate(mygrocerylist.listandshop.R.layout.listview, null);  ////inflate view
            else
                convertView = LayoutInflater.from(getContext()).inflate(mygrocerylist.listandshop.R.layout.listview, parent, false);
        }


        TextView text = convertView.findViewById(mygrocerylist.listandshop.R.id.thing);  ////Findview by id
        ImageView icon = (ImageView) convertView.findViewById(mygrocerylist.listandshop.R.id.tomato);

        TypedArray imgs = convertView.getContext().getResources().obtainTypedArray(mygrocerylist.listandshop.R.array.icons);


        if (text != null)
            text.setText(item.getItemName()); //set the text

        if (icon != null)
            icon.setImageResource(imgs.getResourceId(item.getNoOfArray(), -1)); //set the image

        // Now assign alternate color for rows
        if (position % 2 == 0)
            convertView.setBackgroundColor(ContextCompat.getColor(getContext(), mygrocerylist.listandshop.R.color.odd));
        else
            convertView.setBackgroundColor(ContextCompat.getColor(getContext(), mygrocerylist.listandshop.R.color.even));
        /*ivCustomerImage.setImageResource(customer.getProfilePic());*/

        return convertView;
    }

    public int getCount() {
        return itemList.size();
    }

    public Items getItem(int position) {
        return itemList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {

        Filter filter = new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint,
                                          FilterResults results) {

                itemList = (ArrayList<Items>) results.values;
                notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                FilterResults results = new FilterResults();
                ArrayList<Items> FilteredArrayNames = new ArrayList<Items>();

                if (tempItemlist == null) {
                    tempItemlist = new ArrayList<Items>(itemList);
                }
                if (constraint == null || constraint.length() == 0) {
                    results.count = tempItemlist.size();
                    results.values = tempItemlist;
                } else {
                    constraint = constraint.toString().toLowerCase();
                    for (int i = 0; i < tempItemlist.size(); i++) {
                        Items dataNames = tempItemlist.get(i);
                        if (dataNames.getItemName().toLowerCase()
                                .contains(constraint.toString())) {
                            FilteredArrayNames.add(dataNames);
                        }
                    }

                    results.count = FilteredArrayNames.size();
                    // System.out.println(results.count);

                    results.values = FilteredArrayNames;
                    // Log.e("VALUES", results.values.toString());
                }

                return results;
            }
        };

        return filter;
    }
}