package com.example.chardsoftcryptowallet;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.chardsoftcryptowallet.core.SingletonWallet;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReceiveFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReceiveFragment extends Fragment {

   private View MainView;

    public ReceiveFragment() {
        // Required empty public constructor
    }

    Bitmap encodeAsBitmap(String str) {
        int white = 0xFFFFFFFF;
        int black = 0xFF000000;
        int WIDTH = 500;

        BitMatrix result;
        Bitmap bitmap=null;
        try
        {
            result = new MultiFormatWriter().encode(str,
                    BarcodeFormat.QR_CODE, WIDTH, WIDTH, null);

            int w = result.getWidth();
            int h = result.getHeight();
            int[] pixels = new int[w * h];
            for (int y = 0; y < h; y++) {
                int offset = y * w;
                for (int x = 0; x < w; x++) {
                    pixels[offset + x] = result.get(x, y) ? black : white;
                }
            }
            bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, WIDTH, 0, 0, w, h);
        } catch (Exception iae) {
            iae.printStackTrace();
            return null;
        }
        return bitmap;
    }

    private void createEvents() {
        String publicAddress = SingletonWallet.getApp().getUserWalletBtcNetworkConnection().getAddress();

        ((Button)this.MainView.findViewById(R.id.btnCopyAddress)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.content.ClipboardManager clipboard = (android.content.ClipboardManager) MainView.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", publicAddress);
                clipboard.setPrimaryClip(clip);
            }
        });

        ((Button)this.MainView.findViewById(R.id.btnBackReceive)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.Singleton.replaceToFragment(new MainWalletPageFragrament());
            }
        });

        ((TextView)this.MainView.findViewById(R.id.txtPublicAddress)).setText(publicAddress);

        ((ImageView)this.MainView.findViewById(R.id.imgReceiveQrCode)).setImageBitmap(encodeAsBitmap(publicAddress));
    }

    private void initializeComponent() {
        createEvents();
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ReceiveFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ReceiveFragment newInstance(String param1, String param2) {
        ReceiveFragment fragment = new ReceiveFragment();
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
        this.MainView = inflater.inflate(R.layout.fragment_receive, container, false);
        initializeComponent();
        return this.MainView;
    }
}