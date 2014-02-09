package com.intrivix.android.busman;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;

public class MapPane extends Activity implements LocationListener {

	private static final String LOG_TAG = "BusMan";

	private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/directions";
	private static final String OUT_JSON = "/json";
	private static final String API_KEY = "AIzaSyD9QUYAyQd2lOiysKGoqgMPsvKTqAhPDp4";

	private GoogleMap map;
	private LocationManager locationManager;
	private static final long MIN_TIME = 400;
	private static final float MIN_DISTANCE = 1000;
	private final static String LINE = "m|brFl{~aQo_Hac@qw@hOklFjlJybGtwI{}BtdLmxQph`@}sCbd@utCf~BmnCn}C}_DbhIs~BffIw|ErbI_nJxzKgaFffAwiF~}E}}QpzIywGhsAufMxyHsdNbyS}bIbeG_fAvvAkeE~OioI_iAmoLe^ijLLwyIrf@g_Kd{A_eDdz@oaEkq@etItAmyEjB{w@fz@}{@j{@eEl|Dkl@hnD_iCrb@kqAsbBqb@ewBihCgVkkIxiEo~TkG{_FfNgoCh`BgcGzQmnEznGezDhsFyuF`mEu|Hb~@aoFgCqjFdh@ujFhkEagBxdB{oHzQwePt]onO|aKarTjuKexHxj@ceKj}H{kPzcL{|DnWeoEj_AivEteBcj@l~BsMtvGqFjsBy|AzmBeyDxl@wrGxaH}tKp~O}dDhwHofA|lAi|CzDipDf`FwiHjlJk`\\vO_wp@jDwgy@_LgqIksAahJqx@miJ~i@srGjTkeQe@siG_PecDe_C{fVlCyybAkAkjSmI_oYzd@}oEbj@ycHl}F{vO|hKoyNdaUisH|gLklLpH}lo@|Dsnd@mt@keHitGw~DgyAwiPBo{TaKmcKw_FgjLanIofFi}DwbD}Lm|HoBydHymD{iSsFkea@eFeqm@_V{rIzQe{EucAkwHfD}yHvo@mfUnq@exYFsyFnnBuxEzwDycLz_Iu`TzfF_eQb|F_dHxy@k|E~uC}pHbp@mjCviBy{IqD_{M~mBuuAuz@ssDkDa_^uQcxS|MewB`m@_gDvAckFxa@gxJqCk|Cf}CmrJf|HmvBvCkeCjgC{j]mN_tb@z]iuWmr@ifHi]krFjJkgJhiAapCfR{jCb_BiiFluBobFoQkgDdCmiGjuBwqD~{@elEyD_cC}vAwy@_f@_pAlu@yxG~vAw_MtkGwv@tdAkuRt_CudCp`BmdFvn@u`EfdAqvBsZikCse@u~LarLq_HwlDopBw`B}pCc}C}lFcwEnn@sbCKkyW@qtE_r@}kCi`FPbCyaY`Eif^gk@wb@sbCha@yz@oMoyEgiCqfC}eCfzEieP`C{|_@|j@cqYkk@yh[rd@qnIbm@{{_@bG}p]zlCelFMezG{t@}vUoFqbJyt@mvBgaHguUg}Ac{Je|AuwAs`A}tAyd@e{E_f@yeRlqAojOtu@ayXrc@amDaCayErBooJaO_yIa}AuzGcbBqsD_kBmRa_Aa~@}d@{eDgRepAoa@mHa|@viA~n@~bFep@hdFqVdqMexAbuEk`A~z@{HzgAflA|xDpIf_B}f@t{@y@`P";
	private String theString = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		JSONObject jObject;

		try {
			theString = getInternetData();
			jObject = new JSONObject(theString);
			theString = jObject.getJSONObject("points").toString();
		
		} catch (IOException e) {
			System.out.println(e);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(theString);
		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
				.getMap();

		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		locationManager.requestLocationUpdates(
				LocationManager.NETWORK_PROVIDER, MIN_TIME, MIN_DISTANCE, this);

		map.setMyLocationEnabled(true);
		map.setTrafficEnabled(true);
		map.setBuildingsEnabled(true);

		//List<LatLng> decodedPath = PolyUtil.decode(theString);

		//map.addPolyline(new PolylineOptions().addAll(decodedPath).color(
			//	Color.BLUE));
		// map.addPolyline(new
		// PolylineOptions().addAll(PolyUtil.decode("{dleI~cvLdAcNYi@MAmA{@}@m@I_@Go@Fe@Py@Pc@^q@xBeEbAkCnE_LzB}Bz@gAnAqBr@cAb@g@`@[dCg@t@Yz@o@`Am@bDeC|BsB\\a@^k@r@_ApAwAl@q@l@aArAaBTOX]jCuCtAaBjJ_MlCaDvAiBFSp@}@J?hBaC^g@`AmArCoDj@q@rHaJdB}BfFqGjC_DhEiFpFaHj@w@^m@~AwCrBwExHsPnD}Ht@{AlDjWr@jFHlEJvAj@nERnBBf@?tADn@bApJ~Au@n@c@nA{@zDaD`@[dAk@rCaAbBw@r@i@fBeCz@sA\\_@\\Sh@UlA_@j@Gt@BxBTtDv@W|D")));

	}

	@Override
	public void onLocationChanged(Location location) {
		LatLng latLng = new LatLng(location.getLatitude(),
				location.getLongitude());
		CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng,
				14);
		map.animateCamera(cameraUpdate);
		locationManager.removeUpdates(this);
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

	public String getInternetData() throws Exception {

		BufferedReader in = null;
		String data = null;

		try {
			HttpClient client = new DefaultHttpClient();

			URI website = new URI(
					"http://maps.googleapis.com/maps/api/directions/json?origin=39.833193,-94.862794&destination=49.833193,-94.862794&sensor=true");
			HttpGet request = new HttpGet();
			request.setURI(website);
			HttpResponse response = client.execute(request);
			response.getStatusLine().getStatusCode();

			in = new BufferedReader(new InputStreamReader(response.getEntity()
					.getContent()));
			StringBuffer sb = new StringBuffer("");
			String l = "";
			String nl = System.getProperty("v.separator");
			while ((l = in.readLine()) != null) {
				sb.append(l + nl);
			}
			in.close();
			data = sb.toString();
			return data;
		} finally {
			if (in != null) {
				try {
					in.close();
					return data;
				} catch (Exception e) {
					Log.e("GetMethodEx", e.getMessage());
				}
			}
		}
	}

}
