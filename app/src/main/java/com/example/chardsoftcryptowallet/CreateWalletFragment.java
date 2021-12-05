package com.example.chardsoftcryptowallet;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.chardsoftcryptowallet.core.SingletonWallet;
import com.example.chardsoftcryptowallet.core.cryptography.AES256CBC;
import com.example.chardsoftcryptowallet.core.cryptography.BIP39;
import com.example.chardsoftcryptowallet.core.database.models.Wallet;

public class CreateWalletFragment extends Fragment {
    private View MainView;
    private String GeneratedBIP39;

    public CreateWalletFragment() {
        // Required empty public constructor
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private Wallet getWalletFromInputs(){
        GeneratedBIP39 = BIP39.generateMnemonic();
        AES256CBC aes = new AES256CBC(((TextView)this.MainView.findViewById(R.id.txtPassword)).getText().toString());
        String cipherBIP39 = aes.encrypt(GeneratedBIP39);

        return new Wallet(
                0,
                ((TextView)this.MainView.findViewById(R.id.txtWalletName)).getText().toString(),
                cipherBIP39
        );
    }

    private boolean validateInputs(){
        String walletName = ((TextView)this.MainView.findViewById(R.id.txtWalletName)).getText().toString();
        if(walletName == null || walletName.isEmpty()){
            MessageBox msg = new MessageBox("Empty input","Please, insert wallet name", this.MainView);
            msg.showMessage(null);
            return false;
        }

        String password1 = ((TextView)this.MainView.findViewById(R.id.txtPassword)).getText().toString();
        String password2 = ((TextView)this.MainView.findViewById(R.id.txtConfirmPassword)).getText().toString();
        if(password1 == null || password2 == null || password1.isEmpty() || password2.isEmpty() || password1.equals(password2) == false){
            MessageBox msg = new MessageBox("Invalid inputs","Please, insert valid passwords", this.MainView);
            msg.showMessage(null);
            return false;
        }

        return true;
    }

    private void createEvents(){
        ((Button)this.MainView.findViewById(R.id.btnBack2)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.Singleton.replaceToFragment(new SelectOpenImportOrCreateWalletFragment());
            }
        });

        ((Button)this.MainView.findViewById(R.id.createWalletBtn)).setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                if(validateInputs()){
                    Wallet wallet = getWalletFromInputs();
                    if(SingletonWallet.getApp().saveNewWallet(wallet)){
                        MessageBox msg = new MessageBox("Success!",GeneratedBIP39, v);
                        msg.showMessage(new Runnable() {
                            @Override
                            public void run() {
                                String password = ((TextView)MainView.findViewById(R.id.txtPassword)).getText().toString();
                                SingletonWallet.getApp().openWalletById(wallet.getID(), password);
                                MainActivity.Singleton.replaceToFragment(new MainWalletPageFragrament());
                            }
                        });
                    }else{
                        MessageBox msg = new MessageBox("Error","Some error occurred on create new wallet!", v);
                        msg.showMessage(new Runnable() {
                            @Override
                            public void run() {
                                MainActivity.Singleton.replaceToFragment(new SelectOpenImportOrCreateWalletFragment());
                            }
                        });
                    }
                }
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.MainView = inflater.inflate(R.layout.fragment_create_wallet, container, false);
        createEvents();
        return this.MainView;
    }
}