package com.alamaanah.nisaarnadiadwala;

import java.util.Calendar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

public class EventsAdapter extends ArrayAdapter<SingleEvent>{

    Context context; 
    int layoutResourceId;    
    SingleEvent[] data = null;
    
    public EventsAdapter(Context context, int layoutResourceId, SingleEvent[] data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
       
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        EventsHolder holder = null;
        
        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            
            holder = new EventsHolder();
            holder.title = (TextView)row.findViewById(R.id.tvEventsTitle);
            holder.time = (TextView)row.findViewById(R.id.tvEventsTime);
            holder.venue = (TextView)row.findViewById(R.id.tvEventsVenue);
            holder.description = (TextView)row.findViewById(R.id.tvEventsDescription);
            holder.add=(Button)row.findViewById(R.id.bAddCalendar);
            
            row.setTag(holder);
        }
        else
        {
            holder = (EventsHolder)row.getTag();
        }
        Typeface typeface2 = Typeface.createFromAsset(context.getAssets(), "fonts/robotolight.ttf");
		
        
        final SingleEvent note = data[position];
        holder.title.setText(note.title);
        holder.title.setSelected(true);
        holder.time.setText(String.format(" %1$tb %1$te, %1$tY at %1$TI:%1$TM %1$tp", note.time));
        holder.time.setTypeface(typeface2);
        holder.venue.setText(note.venue);
        holder.venue.setTypeface(typeface2);
        holder.description.setText(note.description);
        holder.description.setTypeface(typeface2);
        holder.add.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Calendar cal = Calendar.getInstance();
				cal.setTimeInMillis(note.time.getTime());
		        Intent intent = new Intent(Intent.ACTION_EDIT);
		        intent.setType("vnd.android.cursor.item/event");
		        intent.putExtra("beginTime", cal.getTimeInMillis());
		        intent.putExtra("allDay", true);
		        intent.putExtra("rrule", "FREQ=YEARLY;COUNT=1");
		        intent.putExtra("endTime", cal.getTimeInMillis()+60*60*1000);
		        intent.putExtra("title", note.title);
		        intent.putExtra("description", note.description);
		        intent.putExtra("eventLocation", note.venue);
		        context.startActivity(intent);
		        
			}
		});
        
        return row;
    }
    
    static class EventsHolder
    {
        TextView title;
        TextView time;
        TextView venue;
        TextView description;
        Button add;
    }
}