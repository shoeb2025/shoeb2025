package com.alamaanah.nisaarnadiadwala;

public class SingleBook {

	public int imageid;
	public int bookid;
	public String title;
	public boolean bought;

	public SingleBook() {
		super();
	}

	public SingleBook( int b, String s) {
		bought=false;
		bookid = b;
	}
}
