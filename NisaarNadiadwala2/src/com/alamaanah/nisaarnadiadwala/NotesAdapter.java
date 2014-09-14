package com.alamaanah.nisaarnadiadwala;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class NotesAdapter extends ArrayAdapter<SingleNote>{

    Context context; 
    int layoutResourceId;    
    SingleNote data[] = null;

    
    public NotesAdapter(Context context, int layoutResourceId, SingleNote[] data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        final NotesHolder holder;
        final SingleNote note = data[position];
        
        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new NotesHolder();
            
            holder.title = (TextView)row.findViewById(R.id.tvTitle);
            
            
            row.setTag(holder);
        }
        else
        {
        	holder= (NotesHolder)row.getTag();
              
        }
       
        
        holder.title.setText(note.title);
        holder.title.setSelected(true);
        return row;
    }
    @Override

    public int getViewTypeCount() {                 

        return getCount();
    }

    @Override
    public int getItemViewType(int position) {

        return position;
    }
    
    static class NotesHolder
    {
        TextView title;
    }
}