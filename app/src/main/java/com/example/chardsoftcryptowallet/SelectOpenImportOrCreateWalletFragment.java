package com.example.chardsoftcryptowallet;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.chardsoftcryptowallet.core.SingletonWallet;

public class SelectOpenImportOrCreateWalletFragment extends Fragment {
    private View MainView;

    public SelectOpenImportOrCreateWalletFragment() {
        // Required empty public constructor
    }

    private void createEvents(){
        ((Button)this.MainView.findViewById(R.id.btnImportWallet)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.Singleton.replaceToFragment(new ImportWalletFragment());
            }
        });

        ((Button)this.MainView.findViewById(R.id.btnCreateWallet)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.Singleton.replaceToFragment(new CreateWalletFragment());
            }
        });

        ((Button)this.MainView.findViewById(R.id.btnOpenWallet)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.Singleton.replaceToFragment(new SelectWalletFragment());
            }
        });

        if(SingletonWallet.getApp().haveSomeWallet()){
            ((Button)this.MainView.findViewById(R.id.btnOpenWallet)).setVisibility(View.VISIBLE);
        }else
        {
            ((Button)this.MainView.findViewById(R.id.btnOpenWallet)).setVisibility(View.GONE);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        this.MainView = inflater.inflate(R.layout.fragment_select_open_import_or_create_wallet, container, false);
        createEvents();
        return this.MainView;
    }
}