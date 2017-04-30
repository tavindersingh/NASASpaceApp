package com.example.tavindersingh.locationdetails;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ViewPlaceDetail extends AppCompatActivity {

    private double latitude, longitude;

    String url="http://www.androidbegin.com";
    ProgressDialog mProgressDialog;

    TextView area_name, country_name, timeTextView, localeTextView, latitudeTextView, longitudeTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_place_detail);

        latitude = getIntent().getExtras().getDouble("latitude");
        longitude = getIntent().getExtras().getDouble("longitude");

        double altitude = getIntent().getExtras().getDouble("altitude");
        long time = getIntent().getExtras().getLong("time");

        area_name = (TextView) findViewById(R.id.area_name);
        country_name = (TextView) findViewById(R.id.country_name);
        timeTextView = (TextView) findViewById(R.id.time);
//        localeTextView = (TextView) findViewById(R.id.locale);
        latitudeTextView = (TextView) findViewById(R.id.latitude);
        longitudeTextView = (TextView) findViewById(R.id.longitude);

//        Toast.makeText(getApplicationContext(), latitude + "", Toast.LENGTH_LONG).show();
//        Toast.makeText(getApplicationContext(), longitude + "", Toast.LENGTH_LONG).show();

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            String cityName = addresses.get(0).getAddressLine(0);
            String stateName = addresses.get(0).getAddressLine(1);
            String countryName = addresses.get(0).getAddressLine(2);

//            Toast.makeText(getApplicationContext(), cityName, Toast.LENGTH_LONG).show();
//            Toast.makeText(getApplicationContext(), stateName, Toast.LENGTH_LONG).show();
//            Toast.makeText(getApplicationContext(), countryName, Toast.LENGTH_LONG).show();
//            Toast.makeText(getApplicationContext(), addresses.get(0).toString(), Toast.LENGTH_LONG).show();
//            Toast.makeText(getApplicationContext(), altitude + "", Toast.LENGTH_LONG).show();
//            Toast.makeText(getApplicationContext(), time + "", Toast.LENGTH_LONG).show();
//            Toast.makeText(getApplicationContext(), placeType1 + "", Toast.LENGTH_LONG).show();

            latitudeTextView.setText(latitude + "");
            longitudeTextView.setText(longitude + "");
            area_name.setText(stateName);
            country_name.setText(countryName);

            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss.SSS");
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(time);
//            format.format(calendar.getTime());
            timeTextView.setText(format.format(calendar.getTime()) + "");

        } catch (IOException e) {
            e.printStackTrace();
        }

        ImageView imageView = (ImageView)findViewById(R.id.imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Image().execute();
            }
        });
    }

    private class Image extends AsyncTask<Void, Void, Void> {
        String imgSrc;
        Bitmap bitmap;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(ViewPlaceDetail.this);
            mProgressDialog.setTitle("Loading Images");
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Document document = Jsoup.connect(url).get();
//                Elements img = document.select("a[class=rg_ic rg_i] img[src]");
                Elements img = document.select("a[class=brand brand-image] img[src]");
                imgSrc = img.attr("src");
                InputStream input = new java.net.URL("data:image/jpeg;base64,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").openStream();
                bitmap = BitmapFactory.decodeStream(input);

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            ImageView imageView = (ImageView) findViewById(R.id.imageView);
//            TextView src = (TextView) findViewById(R.id.src);
//            src.setText(imgSrc);
            imageView.setImageBitmap(bitmap);
        }
    }
}
