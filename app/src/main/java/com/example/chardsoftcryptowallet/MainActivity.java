package com.example.chardsoftcryptowallet;

import android.os.Bundle;

import com.example.chardsoftcryptowallet.core.SingletonWallet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class MainActivity extends AppCompatActivity {
    public static MainActivity Singleton;
    private FragmentManager MainFragmentManager;


    public void replaceToFragment(Fragment fragment){
        this.MainFragmentManager.beginTransaction().replace(R.id.content_fragment, fragment).commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MainActivity.Singleton = this;
        SingletonWallet.initializeSingletonApp(getBaseContext());
        this.MainFragmentManager = getSupportFragmentManager();

        replaceToFragment(new SelectOpenImportOrCreateWalletFragment());
    }

}