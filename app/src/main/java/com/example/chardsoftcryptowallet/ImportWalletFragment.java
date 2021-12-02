package com.example.chardsoftcryptowallet;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class ImportWalletFragment extends Fragment {
    private View MainView;

    public ImportWalletFragment() {
        // Required empty public constructor
    }

    private void createEvents(){
        ((Button)this.MainView.findViewById(R.id.btnBack)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.Singleton.replaceToFragment(new SelectOpenImportOrCreateWalletFragment());
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        this.MainView = inflater.inflate(R.layout.fragment_import_wallet, container, false);
        createEvents();
        return this.MainView;
    }
}