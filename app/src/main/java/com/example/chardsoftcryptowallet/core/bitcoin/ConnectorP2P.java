package com.example.chardsoftcryptowallet.core.bitcoin;

import android.os.Build;

import androidx.annotation.RequiresApi;

import org.bitcoinj.core.Address;
import org.bitcoinj.core.Coin;
import org.bitcoinj.core.Context;
import org.bitcoinj.core.InsufficientMoneyException;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.crypto.DeterministicKey;
import org.bitcoinj.crypto.MnemonicCode;
import org.bitcoinj.kits.WalletAppKit;
import org.bitcoinj.script.Script;
import org.bitcoinj.wallet.DeterministicSeed;
import org.bitcoinj.wallet.KeyChain;
import org.bitcoinj.wallet.KeyChainGroup;
import org.bitcoinj.wallet.SendRequest;
import org.bitcoinj.wallet.UnreadableWalletException;
import org.bitcoinj.wallet.Wallet;
import org.bitcoinj.wallet.WalletProtobufSerializer;
import org.bitcoinj.wallet.WalletTransaction;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class ConnectorP2P {
    private static NetworkParameters MAIN_NET = NetworkParameters.fromID(NetworkParameters.ID_MAINNET);
    private static NetworkParameters TEST_NET = NetworkParameters.fromID(NetworkParameters.ID_TESTNET);
    private static NetworkParameters CURRENT_NETWORK = MAIN_NET;

    private Wallet BtcWallet;

    private long bitcoinToSatoshis(float btc){
        long bitcoins = (long)btc * 100000000L;
        return bitcoins;
    }

    private float satoshisToBitcoin(long satoshis){
        float bitcoins = satoshis / 100000000;
        return bitcoins;
    }

    public float getBalance(){
        this.BtcWallet.addWatchedAddress(Address.fromString(CURRENT_NETWORK, getAddress()));
        long satoshis = this.BtcWallet.getBalance().value;

        long b2 = this.BtcWallet.getBalance(Wallet.BalanceType.AVAILABLE).getValue();
        long b3 = this.BtcWallet.getBalance(Wallet.BalanceType.AVAILABLE_SPENDABLE).getValue();
        long b4 = this.BtcWallet.getBalance(Wallet.BalanceType.ESTIMATED).getValue();
        long b5 = this.BtcWallet.getBalance(Wallet.BalanceType.ESTIMATED_SPENDABLE).getValue();

        return satoshisToBitcoin(satoshis);
    }

    public String getAddress(){
        String a1 = this.BtcWallet.currentReceiveAddress().toString();
        String a2 = this.BtcWallet.currentChangeAddress().toString();
        String a3 = this.BtcWallet.freshReceiveAddress().toString();
        String a4 = this.BtcWallet.currentAddress(KeyChain.KeyPurpose.RECEIVE_FUNDS).toString();

        List<Address> issued = this.BtcWallet.getIssuedReceiveAddresses();
        List<Address> watch = this.BtcWallet.getWatchedAddresses();

        return this.BtcWallet.currentAddress(KeyChain.KeyPurpose.RECEIVE_FUNDS).toString();
    }

    public float getSuggestedGas(){
        return 0.0005f;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void updateTransactions(){
        Iterable<org.bitcoinj.wallet.WalletTransaction> transactions = this.BtcWallet.getWalletTransactions();
        ArrayList<WalletTransaction> walletTransactions = new ArrayList<>();

        transactions.forEach(new Consumer<WalletTransaction>() {
            @Override
            public void accept(WalletTransaction walletTransaction) {
                walletTransactions.add(walletTransaction);
            }
        });
    }

    public boolean sendAmount(float btcAmount, String receiver){
        Address address = Address.fromString(CURRENT_NETWORK, receiver);
        Coin coin = Coin.valueOf(bitcoinToSatoshis(btcAmount));
        SendRequest s = SendRequest.to(address, coin);

        try {
            this.BtcWallet.sendCoins(s);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public ConnectorP2P(String BIP39) throws UnreadableWalletException {
        try{
            DeterministicSeed seed = new DeterministicSeed(BIP39, null, "", 0L);

            seed.check();

            this.BtcWallet = Wallet.fromSeed(CURRENT_NETWORK, seed, Script.ScriptType.P2PKH);
        }catch(Exception ex)
        {
            //logger.log(ex)
            throw new IllegalArgumentException("Some error occurred on load wallet from seed");
        }
    }
}
