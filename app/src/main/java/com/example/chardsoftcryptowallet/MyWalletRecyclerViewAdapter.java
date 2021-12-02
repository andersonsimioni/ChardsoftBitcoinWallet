package com.example.chardsoftcryptowallet;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.chardsoftcryptowallet.core.SingletonWallet;
import com.example.chardsoftcryptowallet.core.database.models.Wallet;

import java.util.ArrayList;
import java.util.List;

public class MyWalletRecyclerViewAdapter extends RecyclerView.Adapter<MyWalletRecyclerViewAdapter.ViewHolder> {

    private final ArrayList<Wallet> mValues;

    public MyWalletRecyclerViewAdapter(ArrayList<Wallet> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_select_wallet, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mContentView = holder.mItem.getName();
        holder.initialize();
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public String mContentView;
        public Wallet mItem;

        private void createEvents(){
            ((Button)mView.findViewById(R.id.btnOpenWalletItem)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainActivity.Singleton.replaceToFragment(new OpenWalletFragment(mItem));
                }
            });

            ((Button)mView.findViewById(R.id.btnDeleteWallet)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MessageBox msg = new MessageBox("Delete wallet", "Do you want to delete this wallet?", mView, true);
                    msg.showMessage(new Runnable() {
                        @Override
                        public void run() {
                            SingletonWallet.getApp().deleteWallet(mItem);
                            MainActivity.Singleton.replaceToFragment(new SelectWalletFragment());
                        }
                    });
                }
            });
        }

        private void initialize(){
            createEvents();
            ((TextView)mView.findViewById(R.id.txtWalletContent)).setText(mItem.getName());
        }

        public ViewHolder(View view) {
            super(view);
            mView = view;
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView + "'";
        }
    }
}