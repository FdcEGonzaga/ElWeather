package com.egon.elweather.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.egon.elweather.R;
import com.egon.elweather.activity.MainActivity;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class NoInternetFragment extends Fragment {
    private View view;
    private TextView refresh;
    private ProgressDialog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_no_internet, container, false);
        castComponents();
        setListeners();
        return view;
    }

    private void castComponents() {
        refresh = view.findViewById(R.id.refresh);
        dialog = new ProgressDialog(getActivity());
    }

    private void setListeners() {
        refresh.setOnClickListener(v-> {
            dialog.setMessage("Checking your internet connection.");
            dialog.show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    dialog.dismiss();
                    if (isConnectedToInternet()) {
                        getParentFragmentManager().beginTransaction().remove(NoInternetFragment.this).commit();
                        ((MainActivity)requireActivity()).getWeatherData();
                    } else {
                        Toast.makeText(getActivity(), "Please turn on your wifi or data.", Toast.LENGTH_SHORT).show();
                    }
                }
            }, 5000);

        });
    }

    private boolean isConnectedToInternet() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getApplicationContext().getSystemService(getActivity().CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED
                || connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            return true;
        } else {
            return false;
        }
    }
}