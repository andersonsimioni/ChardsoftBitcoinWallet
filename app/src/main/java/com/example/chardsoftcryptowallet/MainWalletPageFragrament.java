package com.example.chardsoftcryptowallet;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.chardsoftcryptowallet.core.SingletonWallet;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainWalletPageFragrament#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainWalletPageFragrament extends Fragment {
    private View MainView;

    private Thread UpdateBalanceTransactionsThread;
    private FragmentManager MainFragmentManager;

    public MainWalletPageFragrament() {
    }

    private void updateBalance(){
        float balance = SingletonWallet.getApp().getUserWalletBtcNetworkConnection().getBalance();
        ((TextView)this.MainView.findViewById(R.id.txtBalance)).setText(String.valueOf(balance) + " BTC");
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void updateTransactions(){
    }

    private void initializeFragmentCoreProcessing(){
        this.UpdateBalanceTransactionsThread = new Thread(){
            @RequiresApi(api = Build.VERSION_CODES.N)
            public void run(){
                while (true){
                    try {
                        sleep(1000L);
                        updateTransactions();
                        updateBalance();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        this.UpdateBalanceTransactionsThread.start();
    }

    private void createEvents(){
        ((Button)this.MainView.findViewById(R.id.btnReceive)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.Singleton.replaceToFragment(new ReceiveFragment());
            }
        });

        ((Button)this.MainView.findViewById(R.id.btnSend)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.Singleton.replaceToFragment(new SendFragment());
            }
        });

        ((Button)this.MainView.findViewById(R.id.btnEnterShowSecretFragment)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.Singleton.replaceToFragment(new SendFragment());
            }
        });

        ((Button)this.MainView.findViewById(R.id.btnEnterShowSecretFragment)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.Singleton.replaceToFragment(new ShowSecretFragment());
            }
        });

        ((Button)this.MainView.findViewById(R.id.btnLockWallet)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateBalanceTransactionsThread.interrupt();
                SingletonWallet.getApp().lock();
                MainActivity.Singleton.replaceToFragment(new SelectOpenImportOrCreateWalletFragment());
            }
        });
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MainWalletPageFragrament.
     */
    // TODO: Rename and change types and number of parameters
    public static MainWalletPageFragrament newInstance(String param1, String param2) {
        MainWalletPageFragrament fragment = new MainWalletPageFragrament();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        this.MainView = inflater.inflate(R.layout.fragment_main_wallet_page_fragrament, container, false);
        createEvents();
        initializeFragmentCoreProcessing();
        return this.MainView;
    }
}