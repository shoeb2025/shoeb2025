package com.alamaanah.nisaarnadiadwala;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;

public class BooksAdapter extends ArrayAdapter<SingleBook> {

	Context context;
	int layoutResourceId;
	SingleBook data[] = null;
	

	public BooksAdapter(Context context, int layoutResourceId, SingleBook[] data) {
		super(context, layoutResourceId, data);
		this.layoutResourceId = layoutResourceId;
		this.context = context;
		this.data = data;
		
	}

	

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		BooksHolder holder = null;

		if (row == null) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			row = inflater.inflate(layoutResourceId, parent, false);

			holder = new BooksHolder();
			holder.book = (ImageView) row.findViewById(R.id.ivBooks);
			holder.buy = (Button) row.findViewById(R.id.bBooksBuy);
			holder.title=(TextView) row.findViewById(R.id.tvBooksTitle);

			row.setTag(holder);
		} else {
			holder = (BooksHolder) row.getTag();
		}
		AQuery aq = new AQuery(row);
		final SingleBook book = data[position];
		holder.title.setText(book.title);
		holder.title.setTag(""+book.title);
		
		aq.id(R.id.ivBooks).image(
				context.getString(R.string.site_url) + "books/im/"
						+ book.bookid + ".jpg");
		holder.book.setTag(book.bookid);
		final String button = setButton(book.bookid);
		holder.buy.setText(button);
		holder.buy.setFocusable(false);
		holder.buy.setClickable(false);

		return row;
	}

	private String setButton(int bookid) {
		// TODO Auto-generated method stub
		TinyDB db = new TinyDB(context);
		ArrayList<Integer> bought = new ArrayList<Integer>();
		bought = db.getListInt("booksBought", context);
		for (int b : bought) {
			if (b == bookid)
				return "Open";
		}
		return "Buy";
	}
	

	static class BooksHolder {
		ImageView book;
		Button buy;
		TextView title;
	}
}