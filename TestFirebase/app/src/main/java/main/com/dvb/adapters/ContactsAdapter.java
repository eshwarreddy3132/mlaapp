package main.com.dvb.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import main.com.dvb.R;
import main.com.dvb.pojos.Contacts;

/**
 * Created by artre on 12/23/2016.
 */

public class ContactsAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<Contacts> contactsItemslist = null;
    private ArrayList<Contacts> arraylist;

    public ContactsAdapter(Context context, List<Contacts> contactsItemsItems) {
        this.context = context;
        this.contactsItemslist = contactsItemsItems;
       inflater = LayoutInflater.from(context);
       this.arraylist = new ArrayList<Contacts>();
       this.arraylist.addAll(contactsItemslist);
      //  this.arraylist= (ArrayList<Contacts>) contactsItemsItems;
    }

    public class ViewHolder {
        TextView name;
        TextView number;
        TextView designation;

    }

    @Override
    public int getCount() {
        return contactsItemslist.size();
    }

    @Override
    public Contacts getItem(int position) {
        return contactsItemslist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.contactslist_item, null);
            // Locate the TextViews in listview_item.xml
            holder.name = (TextView) view.findViewById(R.id.name);
            holder.designation = (TextView) view.findViewById(R.id.designantion);
            holder.number = (TextView) view.findViewById(R.id.number);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Set the results into TextViews
        holder.name.setText(contactsItemslist.get(position).getName());
        holder.designation.setText(contactsItemslist.get(position).getDesignation());
        holder.number.setText(contactsItemslist.get(position).getPhoneNumber());


        return view;
    }

    // Filter Class
    public void filter(String charText) {
        Log.e("arraylist",""+arraylist.toString());
        Log.e("contactrsitre",""+contactsItemslist.toString());
        charText = charText.toLowerCase(Locale.getDefault());
        contactsItemslist.clear();
        if (charText.length() == 0) {
            contactsItemslist.addAll(arraylist);
        } else {
            for (Contacts wp : arraylist) {
                if (wp.getName().toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    contactsItemslist.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }


}