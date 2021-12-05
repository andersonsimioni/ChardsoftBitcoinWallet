package com.example.chardsoftcryptowallet;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.chardsoftcryptowallet.core.SingletonWallet;

import org.bitcoinj.core.InsufficientMoneyException;

import java.util.concurrent.ExecutionException;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SendFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SendFragment extends Fragment {

    private View MainView;
    private float SuggestedBtcGas;

    public SendFragment() {
        // Required empty public constructor
    }

    private void readSuggestGas(){
        this.SuggestedBtcGas = SingletonWallet.getApp().getUserWalletBtcNetworkConnection().getSuggestedGas();
    }

    private void send(){
        String txtBtcAmount = ((TextView)this.MainView.findViewById(R.id.txtSendAmount)).getText().toString();
        String txtBtcToAddress = ((TextView)this.MainView.findViewById(R.id.txtSendPublicAddress)).getText().toString();

        float btcAmount = Float.parseFloat(txtBtcAmount);

        try {
            SingletonWallet.getApp().getUserWalletBtcNetworkConnection().sendAmount(btcAmount, txtBtcToAddress);
            MessageBox msg = new MessageBox("Success", "Success to send btc amount", this.MainView, false);
            msg.showMessage(new Runnable() {
                @Override
                public void run() {
                    MainActivity.Singleton.replaceToFragment(new MainWalletPageFragrament());
                }
            });
        } catch (InsufficientMoneyException e) {
            MessageBox msg = new MessageBox("Error", "Insufficient Money", this.MainView, false);
            msg.showMessage();
        } catch (ExecutionException e) {
            MessageBox msg = new MessageBox("Error", "Unknown error", this.MainView, false);
            msg.showMessage();
        } catch (InterruptedException e) {
            MessageBox msg = new MessageBox("Error", "Unknown error", this.MainView, false);
            msg.showMessage();
        }
        catch (Exception e) {
            MessageBox msg = new MessageBox("Error", "Unknown error", this.MainView, false);
            msg.showMessage();
        }
    }

    private void setMaxAmount(){
        float amount = SingletonWallet.getApp().getUserWalletBtcNetworkConnection().getBalance();
        ((TextView)this.MainView.findViewById(R.id.txtSendAmount)).setText(String.valueOf(amount));
    }

    private void createEvents(){
        ((Button)this.MainView.findViewById(R.id.btnBackSend)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.Singleton.replaceToFragment(new MainWalletPageFragrament());
            }
        });

        ((Button)this.MainView.findViewById(R.id.btnConfirmSend)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send();
            }
        });

        ((Button)this.MainView.findViewById(R.id.btnSetMaxAmountSend)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setMaxAmount();
            }
        });

        ((TextView)this.MainView.findViewById(R.id.txtSendGas)).setText(String.valueOf(this.SuggestedBtcGas));
    }

    private void initializeComponent(){
        readSuggestGas();
        createEvents();
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SendFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SendFragment newInstance(String param1, String param2) {
        SendFragment fragment = new SendFragment();
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
        this.MainView = inflater.inflate(R.layout.fragment_send, container, false);
        initializeComponent();
        return this.MainView;
    }
}