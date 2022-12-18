package com.skin.minicraft.pe.skinmeloforminecraft.buttomsheet;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.skin.minicraft.pe.skinmeloforminecraft.ItemSkin;
import com.skin.minicraft.pe.skinmeloforminecraft.R;
import com.skin.minicraft.pe.skinmeloforminecraft.databinding.FragmentBottomSheetFullBinding;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.List;

public class BottomSheetFullFragment extends BottomSheetDialogFragment {

    BottomSheetDialog dialog;
    BottomSheetBehavior<View> bottomSheetBehavior;
    View rootView;
    public  static String TAG = BottomSheetFullFragment.class.getSimpleName();
    public int heightActionBar;
    private FragmentBottomSheetFullBinding binding;
    private ItemSkin itemSkin;

    private OnShowAds onShowAds;

    public BottomSheetFullFragment newInstance(int height, ItemSkin itemSkin) {
        BottomSheetFullFragment myFragment = new BottomSheetFullFragment();

        Bundle args = new Bundle();
        args.putInt("height", height);
        args.putParcelable("item",itemSkin);
        myFragment.setArguments(args);

        return myFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        return  dialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding  = FragmentBottomSheetFullBinding.inflate(getLayoutInflater(),container,false);
       // rootView = inflater.inflate(R.layout.fragment_bottom_sheet_full,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bottomSheetBehavior =  BottomSheetBehavior.from((View) view.getParent());
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        heightActionBar = getArguments().getInt("height",0);
        itemSkin = getArguments().getParcelable("item");
        CoordinatorLayout layout = dialog.findViewById(R.id.photoBottomSheet);
        assert  layout != null;
        int height =  (Resources.getSystem().getDisplayMetrics().heightPixels);
        if (heightActionBar == 0 ){
            height = height * 90 /100;
        }else {
            height = height - heightActionBar;
        }
        layout.setMinimumHeight(height);
        initView();
        initOnClick();
    }

    private void initView() {
        if (itemSkin !=  null){
            Glide.with(this)
                    .load(itemSkin.getSkinView())
                    .into(binding.imgSkin);
        }
    }

    public  void initOnClick(){
        binding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        binding.imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onShowAds.callAds();
                dismiss();
            }
        });
        binding.btnDownload.setOnClickListener(view -> {
            new DownloadFile().execute(itemSkin);
        });
    }

    class DownloadFile extends AsyncTask<ItemSkin, String, String> {

        @Override
        protected String doInBackground(ItemSkin... itemSkins) {
            int count;
            try {
                String path = itemSkins[0].getSkinDownload();
                URL url = new URL(path);
                URLConnection connection = url.openConnection();
                connection.connect();
                InputStream input = new BufferedInputStream(url.openStream(), 8192);
                String folderPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/"+ getResources().getString(R.string.app_name_dir);
                File folderSave = new File(folderPath);
                if (!folderSave.exists()){
                    folderSave.mkdir();
                }

                List<String> stringList= Arrays.asList(itemSkins[0].getSkinDownload().split("/"));
                String filename = stringList.get(stringList.size()-1);
                File file = new File(
                        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) +"/"
                                +getResources().getString(R.string.app_name_dir)+"/"+ filename );
                Log.d(TAG, "doInBackground: "+ file.getAbsolutePath());
                OutputStream output = new FileOutputStream(file);

                byte data[] = new byte[1024];

                while ((count = input.read(data)) != -1) {
                    output.write(data, 0, count);
                }

                output.flush();
                output.close();
                input.close();

            } catch (Exception e) {
                Log.e("Error: ", e.toString());
            }

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            binding.progress.setVisibility(View.VISIBLE);

            binding.btnDownload.setEnabled(false);
            binding.btnDownload.setVisibility(View.GONE);
        }

        @Override
        protected void onPostExecute(String s) {
            binding.btnDownload.setEnabled(true);
            binding.progress.setVisibility(View.GONE);
            binding.btnDownload.setVisibility(View.VISIBLE);
            String folderPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/"+ getResources().getString(R.string.app_name_dir);
            File file = new File(folderPath);
            String message = "success save to  "+  file.getAbsolutePath();
            Toast.makeText(getActivity(),message,Toast.LENGTH_LONG).show();
            onShowAds.callAds();
        }
    }

    public interface OnShowAds {
        void callAds();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            onShowAds = (OnShowAds) getActivity();
        }catch (Exception e){
            Log.d(TAG, "onAttach: ");
        }
    }
}
