package org.zapto.p3o;

import org.zapto.p3o.database.CannotPreparException;
import org.zapto.p3o.database.NoDatabaseException;
import org.zapto.p3o.database.NotPreparedException;
import org.zapto.p3o.database.QueryListener;
import org.zapto.p3o.database.QueryResult;
import org.zapto.p3o.database.SimpleDatatable;
import org.zapto.p3o.database.TableNotFoundException;
import org.zapto.p3o.http.ConnectionErrorException;
import org.zapto.p3o.http.HttpResponder;
import org.zapto.p3o.http.RequestMaker;
import org.zapto.p3o.http.ServerResponse;

import android.app.ProgressDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		this.testDatabase();
		this.testHttp();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}
	}

	protected void testHttp() {
		final TextView tvGet = (TextView) findViewById(R.id.tvGet);
		final TextView tvPut = (TextView) findViewById(R.id.tvPut);
		final TextView tvPost = (TextView) findViewById(R.id.tvPost);
		final TextView tvDelete = (TextView) findViewById(R.id.tvDelete);

		final ProgressDialog progress;
		progress = ProgressDialog.show(this, "dialog title", "dialog message",
				true);

		// To begin making http request we must create a new RequestMaker
		// object. In this case we name it http. We pass the activity to the
		// constructor so that access to the device's ConnectivityManager is
		// available to ensure connectivity.
		RequestMaker http = new RequestMaker(this);

		try {
			http.get("http://192.168.254.34:2000", new HttpResponder() {

				@Override
				public void onHttpResponse(ServerResponse httpResponse) {
					if (httpResponse != null) {
						Log.d("MainApp", httpResponse.getMessage());
						tvGet.setText(httpResponse.getContent());
						progress.dismiss();
					}
				}

			});

			http.put("http://192.168.254.34:2000",
					"{\"barcode\":\"nu893htn90wehn089w4444q5\"}",
					new HttpResponder() {

						@Override
						public void onHttpResponse(ServerResponse httpResponse) {
							if (httpResponse != null) {
								Log.d("MainApp", httpResponse.getMessage());
								tvPut.setText(httpResponse.getContent());
								progress.dismiss();
							}
						}

					});

			http.post("http://192.168.254.34:2000",
					"{\"barcode\":\"nu893htn90wehn089w4444q5\"}",
					new HttpResponder() {

						@Override
						public void onHttpResponse(ServerResponse httpResponse) {
							if (httpResponse != null) {
								Log.d("MainApp", httpResponse.getMessage());
								tvPost.setText(httpResponse.getContent());
							}
						}

					});

			http.delete("http://192.168.254.34:2000/an_object",
					new HttpResponder() {

						@Override
						public void onHttpResponse(ServerResponse httpResponse) {
							if (httpResponse != null) {
								Log.d("MainApp", httpResponse.getMessage());
								tvDelete.setText(httpResponse.getContent());
							}
						}

					});
		} catch (ConnectionErrorException e) {
			e.printStackTrace();
			progress.dismiss();
		}

	}

	/**
	 * Simple database is an asynchronous library. The order of query execution
	 * is not guaranteed. The running of queries makes use of Android's
	 * AsyncTask.
	 */
	protected void testDatabase() {

		// Create a new database and add the created table to it. If the
		// database already exist these calls make no changes to the database
		TestDatabase db = new TestDatabase(this);

		// Lets try doing an insert operation on our new table. First we prepare
		// the table for insert by calling table.prepareInsert with the required
		// parameters. We immediately invoke execute, passing in our callback
		// that will handle what we do with the results of the query execution.
		for (int i = 0; i < 5; i++) {
			try {
				SimpleDatatable table = db.from("SAMPLE_TABLE");
				table.insert(new String[] { "id" }, new String[] { "" + i })
						.execute(new QueryListener() {

							@Override
							public void onResult(QueryResult result) {
								Log.d("MainApp", result.getMessage());
							}

						});
			} catch (NotPreparedException e) {
				e.printStackTrace();
			} catch (NoDatabaseException e) {
				e.printStackTrace();
			} catch (CannotPreparException e) {
				e.printStackTrace();
			} catch (TableNotFoundException e) {
				e.printStackTrace();
			}
		}

		try {
			db.from("SAMPLE_TABLE").select().where("id=2")
					.execute(new QueryListener() {

						@Override
						public void onResult(QueryResult result) {
							Log.d("MainApp", result.getMessage());
							Cursor c = result.getCursor();
							c.moveToFirst();
							while (true) {
								System.out.println(c.getString(0));
								if (c.isLast())
									break;
								c.moveToNext();
							}
						}

					});
		} catch (CannotPreparException e1) {
			e1.printStackTrace();
		} catch (TableNotFoundException e1) {
			e1.printStackTrace();
		} catch (NotPreparedException e) {
			e.printStackTrace();
		} catch (NoDatabaseException e) {
			e.printStackTrace();
		}

		// Lets test to see how errors are presented and handled in the library
		// by calling execute without preparing the table and omitting a valid
		// callback function.
		try {
			db.from("SAMPLE_TABLE").execute(null);
		} catch (NotPreparedException e) {
			e.printStackTrace();
		} catch (NoDatabaseException e) {
			e.printStackTrace();
		} catch (TableNotFoundException e) {
			e.printStackTrace();
		}

		// Here we will confirm the success of our previous insert by performing
		// a select. This is done just like the insert with us providing a
		// callback and making a prepare call on the table before execution.
		try {
			db.from("SAMPLE_TABLE").select().execute(new QueryListener() {
				@Override
				public void onResult(QueryResult result) {
					Log.d("MainApp", result.getMessage());
					Cursor c = result.getCursor();

					c.moveToFirst();
					while (true) {
						System.out.println(c.getString(0));
						if (c.isLast())
							break;
						c.moveToNext();
					}
				}
			});
		} catch (NotPreparedException e) {
			e.printStackTrace();
		} catch (NoDatabaseException e) {
			e.printStackTrace();
		} catch (CannotPreparException e) {
			e.printStackTrace();
		} catch (TableNotFoundException e) {
			e.printStackTrace();
		}
	}
}
