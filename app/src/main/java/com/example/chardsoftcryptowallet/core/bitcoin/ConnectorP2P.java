package com.example.chardsoftcryptowallet.core.bitcoin;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.chardsoftcryptowallet.core.cryptography.BytesOperator;
import com.google.common.collect.ImmutableList;
import com.google.common.util.concurrent.SettableFuture;
import com.google.errorprone.annotations.Immutable;

import org.bitcoinj.core.Address;
import org.bitcoinj.core.Coin;
import org.bitcoinj.core.Context;
import org.bitcoinj.core.InsufficientMoneyException;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.core.PeerGroup;
import org.bitcoinj.core.Transaction;
import org.bitcoinj.core.TransactionBroadcast;
import org.bitcoinj.core.TransactionBroadcaster;
import org.bitcoinj.crypto.ChildNumber;
import org.bitcoinj.crypto.DeterministicKey;
import org.bitcoinj.crypto.MnemonicCode;
import org.bitcoinj.kits.WalletAppKit;
import org.bitcoinj.script.Script;
import org.bitcoinj.wallet.DeterministicKeyChain;
import org.bitcoinj.wallet.DeterministicSeed;
import org.bitcoinj.wallet.KeyChain;
import org.bitcoinj.wallet.KeyChainGroup;
import org.bitcoinj.wallet.SendRequest;
import org.bitcoinj.wallet.UnreadableWalletException;
import org.bitcoinj.wallet.Wallet;
import org.bitcoinj.wallet.WalletProtobufSerializer;
import org.bitcoinj.wallet.WalletTransaction;

import java.io.File;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
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
        Coin satoshis = this.BtcWallet.getBalance();
        Coin b2 = this.BtcWallet.getBalance(Wallet.BalanceType.AVAILABLE);
        Coin b3 = this.BtcWallet.getBalance(Wallet.BalanceType.AVAILABLE_SPENDABLE);
        Coin b4 = this.BtcWallet.getBalance(Wallet.BalanceType.ESTIMATED);
        Coin b5 = this.BtcWallet.getBalance(Wallet.BalanceType.ESTIMATED_SPENDABLE);

        return 0;
    }

    public String getAddress(){
        return this.BtcWallet.currentReceiveAddress().toString();
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

    public void sendAmount(float btcAmount, String receiver) throws InsufficientMoneyException, ExecutionException, InterruptedException, Exception {
        SettableFuture<Transaction> future = SettableFuture.create();
        this.BtcWallet.setTransactionBroadcaster(new TransactionBroadcaster() {
            @Override
            public TransactionBroadcast broadcastTransaction(Transaction transaction) {
                return TransactionBroadcast.createMockBroadcast(transaction, future);
            }
        });

        Address dest = Address.fromString(CURRENT_NETWORK, receiver);
        Transaction t = new Transaction(CURRENT_NETWORK);
        t.addOutput(Coin.valueOf(bitcoinToSatoshis(btcAmount)), dest);
        SendRequest request = SendRequest.forTx(t);
        this.BtcWallet.sendCoins(request);

        //this.BtcWallet.setTransactionBroadcaster(new TransactionBroadcast(t));
        //Wallet.SendResult result = this.BtcWallet.sendCoins(request);
        //Transaction endTransaction = result.broadcastComplete.get();
    }

    public ConnectorP2P(String BIP39) throws UnreadableWalletException {
        try{
            DeterministicSeed seed = new DeterministicSeed(BIP39, null, "", System.currentTimeMillis() / 1000l);
            seed.check();
            this.BtcWallet = Wallet.fromSeed(CURRENT_NETWORK, seed, Script.ScriptType.P2WPKH);
        }catch(Exception ex)
        {
            //logger.log(ex)
            throw new IllegalArgumentException("Some error occurred on load wallet from seed");
        }
    }
}
