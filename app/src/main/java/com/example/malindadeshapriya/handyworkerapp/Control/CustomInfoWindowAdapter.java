package com.example.malindadeshapriya.handyworkerapp.Control;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.malindadeshapriya.handyworkerapp.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.IncompleteAnnotationException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by malindadeshapriya on 3/27/18.
 */

public class CustomInfoWindowAdapter  implements GoogleMap.InfoWindowAdapter{

    private final View mWindow;
    private Context mContext;

    private String name;
    private String mdistance;
    private String phoneN;
    private String email;
    private String imageU;





    public CustomInfoWindowAdapter(Context mContext, String name, String distance,String phone,String email, String imageUrl) {
        this.mContext = mContext;
        mWindow = LayoutInflater.from(mContext).inflate(R.layout.custom_info_window,null );
        this.name = name;
        this.mdistance = distance;
        this.phoneN = phone;
        this.email = email;
        this.imageU = imageUrl;

    }

    private void rendowWindowText(Marker marker,View v) throws IOException {

        ImageView imageView = (ImageView)v.findViewById(R.id.profImage);

        Picasso.with(v.getContext()).load(imageU).into(imageView);

        String title =  marker.getTitle();
        TextView tvTitle = (TextView)v.findViewById(R.id.title);

        if(!title.equals("")){
            tvTitle.setText     ("Name:\t\t\t\t\t\t\t\t\t "+title);
        }

        String phoneNo =  phoneN;
        TextView tvSnippet = (TextView)v.findViewById(R.id.phone);

        if(!phoneNo.equals("")){
            tvSnippet.setText   ("Contact No:\t\t\t\t\t"+phoneNo);
        }

        String emailAd =  email;
        TextView tvEmail = (TextView)v.findViewById(R.id.email);

        if(!emailAd.equals("")){
            tvEmail.setText     ("Email:\t\t\t\t\t\t\t\t\t\t "+emailAd);
        }

        String distanceC =  mdistance;
        TextView tvDistance = (TextView)v.findViewById(R.id.distance);

        if(!distanceC.equals("")){
            tvDistance.setText     ("Distance:\t\t\t\t\t\t\t\t"+mdistance+ " KM");
        }


    }


    @Override
    public View getInfoWindow(Marker marker) {
        try {
            rendowWindowText(marker,mWindow);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mWindow;
    }

    @Override
    public View getInfoContents(Marker marker) {
        try {
            rendowWindowText(marker,mWindow);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
