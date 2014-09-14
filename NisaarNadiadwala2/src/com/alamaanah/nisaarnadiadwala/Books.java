package com.alamaanah.nisaarnadiadwala;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alamaanah.nisaarnadiadwala.inappbilling.util.IabHelper;
import com.alamaanah.nisaarnadiadwala.inappbilling.util.IabResult;
import com.alamaanah.nisaarnadiadwala.inappbilling.util.Inventory;
import com.alamaanah.nisaarnadiadwala.inappbilling.util.Purchase;
import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.google.gson.Gson;
import com.manuelpeinado.fadingactionbar.FadingActionBarHelper;

public class Books extends Activity implements OnItemClickListener {

	private ListView gridView;
	Context context;
	private static final String TAG = "com.alamaanah.nisaarnadiadwala.inappbilling";
	IabHelper mHelper;
	static final String ITEM_SKU = "com.nisaar.book";
	int bookid;
	String book_title;
	ProgressDialog mProgressDialog;
	Button button;
	final Activity activity = this;
	TextView header;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		// setContentView(R.layout.books);

		context = this;
		init();
		String base64EncodedPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAge7/e6E+b0ZKKnLcYO0s/BoLmCnWowqrc3bnSuesbsaXwOnwQb2necuMLoJq0ofVUJWmw7SQa4pLIx4yhOTTNQ1hCHj4psE6jH0tDY5MXuYQ2l/2j02KwWYPr6vjvMSZuEdGLgyxXpeY7OaaTRYlGN5gk2iH8I5hvBU3gJhkpyW8hKxMR56ZQ8QRJhTVY4YBqvU1nwg327Lq5+kpSxZtOuwVjRaD4eytngDXFLSESffu8BqrbsYeP7HfyyDMWVowNFvdXdE/o/g0esPab2OQvTaZI2S4QDYoEgCPAFnRqwT7kA3Mjqy6BrnDIb1pagfuU8gwKmq76af39pcHCxTKrQIDAQAB";

		mHelper = new IabHelper(context, base64EncodedPublicKey);
		purchase();

	}

	private void purchase() {
		// TODO Auto-generated method stub

		mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
			public void onIabSetupFinished(IabResult result) {
				if (!result.isSuccess()) {
					Log.d(TAG, "In-app Billing setup failed: " + result);
					Toast.makeText(context,
							(String) "In-App Purchases not working",
							Toast.LENGTH_SHORT).show();
				} else {
					Log.d(TAG, "In-app Billing is set up OK");
					Toast.makeText(context,
							(String) "In-App Purchases working",
							Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	private void init() {
		// TODO Auto-generated method stub

		// new
		FadingActionBarHelper helper = new FadingActionBarHelper()
				.actionBarBackground(
						new ColorDrawable(Color.parseColor("#1a237e")))
				.headerLayout(R.layout.books_header)
				.contentLayout(R.layout.books)
				.headerOverlayLayout(R.layout.books_header_overlay);
		setContentView(helper.createView(this));
		helper.initActionBar(this);

		// end

		android.app.ActionBar ab = getActionBar();
		ab.setDisplayHomeAsUpEnabled(true);
		// ab.setBackgroundDrawable(new
		// ColorDrawable(Color.parseColor("#22A7F0")));
		ab.setIcon(android.R.color.transparent);

		header = (TextView) findViewById(R.id.tvBooksHeader);
		Typeface typeface = Typeface.createFromAsset(getAssets(),
				"fonts/robotoregular.ttf");
		header.setTypeface(typeface);

		// instantiate it within the onCreate method
		mProgressDialog = new ProgressDialog(Books.this);
		mProgressDialog.setMessage("Downloading...");
		mProgressDialog.setIndeterminate(false);
		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		mProgressDialog.setCancelable(true);
		mProgressDialog.setMax(100);
		

		gridView = (ListView) findViewById(android.R.id.list);

		ConnectionDetector cd = new ConnectionDetector(getApplicationContext());
		Boolean isInternetPresent = cd.isConnectingToInternet();
		if (isInternetPresent == false) {
			Toast.makeText(context, (String) "Please connect to the Internet",
					Toast.LENGTH_SHORT).show();
			FadingActionBarHelper helper2 = new FadingActionBarHelper()
					.actionBarBackground(
							new ColorDrawable(Color.parseColor("#1a237e")))
					.headerLayout(R.layout.books_header)
					.contentLayout(R.layout.empty_view)
					.headerOverlayLayout(R.layout.books_header_overlay);
			setContentView(helper2.createView(context));
			TextView emp = (TextView) findViewById(R.id.tvEmptyView);
			emp.setText("No books");
		} else
			activity.setProgressBarIndeterminateVisibility(true);
		getBooks();

	}

	private void getBooks() {

		AQuery aq = new AQuery(this);
		String url = "http://216.12.194.26/~alamaana/nisaar/api/getBooks.php";

		aq.ajax(url, String.class, new AjaxCallback<String>() {

			@Override
			public void callback(String url, String apiResponse,
					AjaxStatus status) {

				Log.d("books", apiResponse);
				Gson gson = new Gson();
				SingleBook[] booksArray = gson.fromJson(apiResponse,
						SingleBook[].class);

				BooksAdapter adapter = new BooksAdapter(context,
						R.layout.grid_single, booksArray);

				if (booksArray == null || booksArray.length == 0) {
					FadingActionBarHelper helper = new FadingActionBarHelper()
							.actionBarBackground(
									new ColorDrawable(Color
											.parseColor("#1a237e")))
							.headerLayout(R.layout.books_header)
							.contentLayout(R.layout.empty_view)
							.headerOverlayLayout(R.layout.books_header_overlay);
					setContentView(helper.createView(context));
					TextView emp = (TextView) findViewById(R.id.tvEmptyView);
					emp.setText("No books");
					activity.setProgressBarIndeterminateVisibility(false);
					return;

				}

				gridView.setAdapter(adapter);
				// if(adapter.isEmpty())
				// Toast.makeText(context, "No books available",
				// Toast.LENGTH_SHORT).show();
				activity.setProgressBarIndeterminateVisibility(false);
				gridView.setOnItemClickListener(Books.this);

			}
		});

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View v, int arg2, long arg3) {
		// TODO Auto-generated method stub

		button = (Button) v.findViewById(R.id.bBooksBuy);
		String buy = (String) button.getText();
		bookid = (int) v.findViewById(R.id.ivBooks).getTag();
		book_title = (String) v.findViewById(R.id.tvBooksTitle).getTag();

		if (buy.equalsIgnoreCase("Buy")) {
			mHelper.launchPurchaseFlow(this, ITEM_SKU, 10001,
					mPurchaseFinishedListener, "mypurchasetoken");
		} else if (buy.equalsIgnoreCase("Open")) {

			Intent pdf = new Intent(this, ViewPDF.class);
			pdf.putExtra("bookid", "" + bookid);
			startActivity(pdf);
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (!mHelper.handleActivityResult(requestCode, resultCode, data)) {
			super.onActivityResult(requestCode, resultCode, data);
		}
	}

	IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
		public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
			if (result.isFailure()) {
				// Handle error
				return;
			} else if (purchase.getSku().equals(ITEM_SKU)) {
				consumeItem();
			}

		}
	};

	public void consumeItem() {
		mHelper.queryInventoryAsync(mReceivedInventoryListener);
	}

	IabHelper.QueryInventoryFinishedListener mReceivedInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
		public void onQueryInventoryFinished(IabResult result,
				Inventory inventory) {

			if (result.isFailure()) {
				// Handle failure
			} else {
				mHelper.consumeAsync(inventory.getPurchase(ITEM_SKU),
						mConsumeFinishedListener);
			}
		}
	};

	static String getEmail(Context context) {
		AccountManager accountManager = AccountManager.get(context);
		Account account = getAccount(accountManager);

		if (account == null) {
			return null;
		} else {
			return account.name;
		}
	}

	private static Account getAccount(AccountManager accountManager) {
		Account[] accounts = accountManager.getAccountsByType("com.google");
		Account account;
		if (accounts.length > 0) {
			account = accounts[0];
		} else {
			account = null;
		}
		return account;
	}

	IabHelper.OnConsumeFinishedListener mConsumeFinishedListener = new IabHelper.OnConsumeFinishedListener() {
		public void onConsumeFinished(Purchase purchase, IabResult result) {

			if (result.isSuccess()) {
				TinyDB db = new TinyDB(context);
				ArrayList<Integer> b = new ArrayList<Integer>();
				b = db.getListInt("booksBought", context);
				b.add(bookid);
				db.putListInt("booksBought", b, context);
				button.setText("Open");
				Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();

				// download

				final AQuery aq = new AQuery(context);
				String url = "http://nisaar.al-amaanah.com/books/bo/" + bookid
						+ ".pdf";

				File target = new File(context.getDir("nisaar",
						Context.MODE_PRIVATE) + "/" + bookid + ".pdf");

				aq.progress(mProgressDialog).download(url, target,
						new AjaxCallback<File>() {

							public void callback(String url, File file,
									AjaxStatus status) {

								if (file != null) {
									Toast.makeText(context,
											(String) "Download Done",
											Toast.LENGTH_SHORT).show();
								} else {
									Toast.makeText(context,
											(String) "Download failed",
											Toast.LENGTH_SHORT).show();
								}
							}

						});

				String email = getEmail(context);
				
				new MyAsyncTask().execute(email,book_title);		
			
			} else {
				// handle error
			}
		}
	};

	public void onRestart() {
		init();
	}

	public void onDestroy() {
		super.onDestroy();
		if (mHelper != null)
			mHelper.dispose();
		mHelper = null;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// app icon in action bar clicked; go home
			Intent intent = new Intent(this, Home.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			overridePendingTransition(R.anim.slide_in_left,
					R.anim.slide_out_right);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
	}
	
	private class MyAsyncTask extends AsyncTask<String, Integer, Double> {

		@Override
		protected Double doInBackground(String... params) {
			// TODO Auto-generated method stub
			postData(params[0],params[1]);
			return null;
		}

		protected void onPostExecute(Double result) {
		}

		protected void onProgressUpdate(Integer... progress) 
		{
		}

		public void postData(String email,String title) {
			// Create a new HttpClient and Post Header
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost("http://216.12.194.26/~alamaana/nisaar/api/addPurchase.php");

			try {
				// Add your data
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				nameValuePairs.add(new BasicNameValuePair("email",email));
				nameValuePairs.add(new BasicNameValuePair("title",title));
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

				// Execute HTTP Post Request
				HttpResponse response = httpclient.execute(httppost);

			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
			} catch (IOException e) {
				// TODO Auto-generated catch block
			}
		}

	}

}
