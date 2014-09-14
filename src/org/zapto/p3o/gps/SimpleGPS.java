package org.zapto.p3o.gps;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public class SimpleGPS {
	
	public interface GPSListener{
		public void onLocationFound(Location location);
		
		public void onLocationTimeOut();
		
		public void cancelLocationListener();
	}
	
	private Context context; // the calling context!
	
	public SimpleGPS(Context context){
		this.context = context;
	}
	
	public void getGPS(int timeoutmills, final GPSListener listener){
		LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

		// Define a listener that responds to location updates
		LocationListener locationListener = new LocationListener() {
		    public void onLocationChanged(Location location) {
		      // Called when a new location is found by the network location provider.
		    	listener.onLocationFound(location);
		    }

		    public void onStatusChanged(String provider, int status, Bundle extras) {}

		    public void onProviderEnabled(String provider) {}

		    public void onProviderDisabled(String provider) {}
		  };

		// Register the listener with the Location Manager to receive location updates
		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
	}
	
}
