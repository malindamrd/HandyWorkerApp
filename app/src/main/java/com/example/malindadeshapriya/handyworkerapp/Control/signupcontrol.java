package com.example.malindadeshapriya.handyworkerapp.Control;

import android.text.TextUtils;
import android.widget.Toast;

/**
 * Created by malindadeshapriya on 12/5/17.
 */

public class signupcontrol {
    public static boolean validateForm(String name, String email, String password, String confirm) {
        boolean valid = true;


        if (TextUtils.isEmpty(email)) {
            Toast.makeText(null,"Error",Toast.LENGTH_SHORT);
            valid = false;
        } else {

        }


        if (TextUtils.isEmpty(password)) {
            Toast.makeText(null,"Error",Toast.LENGTH_SHORT);
            valid = false;
        } else {

        }

        if (TextUtils.isEmpty(name)) {
            Toast.makeText(null,"Error",Toast.LENGTH_SHORT);
            valid = false;
        } else {

        }

        if (TextUtils.isEmpty(confirm)) {
            Toast.makeText(null,"Error",Toast.LENGTH_SHORT);
            valid = false;
        } else {

        }

        return valid;
    }







}
