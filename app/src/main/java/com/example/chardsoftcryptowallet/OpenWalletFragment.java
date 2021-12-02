package com.example.chardsoftcryptowallet;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.chardsoftcryptowallet.core.SingletonWallet;
import com.example.chardsoftcryptowallet.core.database.models.Wallet;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OpenWalletFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OpenWalletFragment extends Fragment {
    private View MainView;

    private final Wallet Wallet;

    public  OpenWalletFragment(){
        this.Wallet = null;
    }

    public OpenWalletFragment(Wallet wallet) {
        this.Wallet = wallet;
    }

    private void createEvents(){

        ((Button)MainView.findViewById(R.id.btnBackOpenWallet)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.Singleton.replaceToFragment(new SelectWalletFragment());
            }
        });

        ((Button)MainView.findViewById(R.id.btnOpenWalletConfirm)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = ((TextView)MainView.findViewById(R.id.txtPasswordOpenWallet)).getText().toString();
                try {
                    SingletonWallet.getApp().openWalletById(Wallet.getID(), password);
                    MainActivity.Singleton.replaceToFragment(new MainWalletPageFragrament());
                }catch (Exception ex){
                    MessageBox msg = new MessageBox("Error","Invalid password!", MainView);
                    msg.showMessage();
                }
            }
        });

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OpenWalletFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OpenWalletFragment newInstance(String param1, String param2) {
        OpenWalletFragment fragment = new OpenWalletFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        MainView = inflater.inflate(R.layout.fragment_open_wallet, container, false);
        createEvents();
        return MainView;
    }
}