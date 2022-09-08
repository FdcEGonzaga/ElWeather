package com.egon.elweather.fragment;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.egon.elweather.R;

public class NoInternetFragment extends Fragment {
    private View view;
    private ImageView fragmentClose;

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
        fragmentClose = view.findViewById(R.id.fragment_close);
    }

    private void setListeners() {
        fragmentClose.setOnClickListener(v -> {
            getParentFragmentManager().beginTransaction().remove(this).commit();
        });
    }
}