package com.example.chardsoftcryptowallet;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.chardsoftcryptowallet.core.SingletonWallet;

import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.script.Script;
import org.bitcoinj.wallet.DeterministicSeed;
import org.bitcoinj.wallet.KeyChain;
import org.bitcoinj.wallet.UnreadableWalletException;
import org.bitcoinj.wallet.Wallet;

import java.math.BigInteger;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ShowSecretFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShowSecretFragment extends Fragment {
    private View MainView;
    private String Secret;

    public ShowSecretFragment() {
        // Required empty public constructor
    }

    private String getPrivateKeyFromSecret(){
        DeterministicSeed seed = null;
        String privateKey = "";
        try {
            seed = new DeterministicSeed(this.Secret, null, "", 0L);
        } catch (UnreadableWalletException e) {
            e.printStackTrace();
        }
        Wallet temp = Wallet.fromSeed(NetworkParameters.fromID(NetworkParameters.ID_MAINNET), seed, Script.ScriptType.P2PKH);

        BigInteger b = temp.currentKey(KeyChain.KeyPurpose.AUTHENTICATION).getPrivKey();
        privateKey = b.toString(16);

        return privateKey;
    }

    private void tryPassword(){
        String password = ((TextView)this.MainView.findViewById(R.id.txtPasswordShowSecret)).getText().toString();
        Secret = SingletonWallet.getApp().getOpenedWalletSecret(password);
        if(Secret == null || Secret.isEmpty()){
            MessageBox msg = new MessageBox("Password error","Incorrect password!", this.MainView);
            msg.showMessage();
        }else {
            ((TextView)this.MainView.findViewById(R.id.txtSecretPhraseShowSecret)).setText(Secret);
            ((LinearLayout)this.MainView.findViewById(R.id.secretPhraseLayout)).setVisibility(View.VISIBLE);
            ((LinearLayout)this.MainView.findViewById(R.id.layoutEnterPasswordShowSecret)).setVisibility(View.GONE);

            String privateKey = getPrivateKeyFromSecret();
        }
    }

    private void createEvents(){
        ((Button)this.MainView.findViewById(R.id.btnBackShowSecretPhrase)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.Singleton.replaceToFragment(new MainWalletPageFragrament());
            }
        });

        ((Button)this.MainView.findViewById(R.id.btnConfirmShowSecret)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tryPassword();
            }
        });

        ((Button)this.MainView.findViewById(R.id.btnCopySecretShowSecret)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.content.ClipboardManager clipboard = (android.content.ClipboardManager) MainView.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", Secret);
                clipboard.setPrimaryClip(clip);
            }
        });
    }

    private void initializeComponent(){
        createEvents();
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ShowSecretFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ShowSecretFragment newInstance(String param1, String param2) {
        ShowSecretFragment fragment = new ShowSecretFragment();
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
        // Inflate the layout for this fragment
        this.MainView = inflater.inflate(R.layout.fragment_show_secret, container, false);
        initializeComponent();
        return this.MainView;
    }
}