package com.alamaanah.nisaarnadiadwala;

import android.os.Parcel;
import android.os.Parcelable;

public class SingleNote implements Parcelable{

	public String title;
	public String content;
	public int imageid;

	

	public SingleNote(String t, String c,int i) {
		title = t;
		content = c;
		imageid=i;
	}
	
	private SingleNote(Parcel in) {
        // This order must match the order in writeToParcel()
        title = in.readString();
        content = in.readString();
        imageid=in.readInt();
        // Continue doing this for the rest of your member data
    }
	
	public void writeToParcel(Parcel out, int flags) {
        // Again this order must match the Question(Parcel) constructor
        out.writeString(title);
        out.writeString(content);
        out.writeInt(imageid);
        // Again continue doing this for the rest of your member data
    }
	
	public int describeContents() {
        return 0;
    }
	
	public static final Parcelable.Creator<SingleNote> CREATOR = new Parcelable.Creator<SingleNote>() {
        public SingleNote createFromParcel(Parcel in) {
            return new SingleNote(in);
        }

        public SingleNote[] newArray(int size) {
            return new SingleNote[size];
        }
	};
}


