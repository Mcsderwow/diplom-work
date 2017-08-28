package com.example.ragfair.fragments;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.ragfair.MainActivity;
import com.example.ragfair.R;
import com.example.ragfair.models.ProductModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import id.zelory.compressor.FileUtil;

import static android.app.Activity.RESULT_OK;

public class MakeProductFragment extends BaseFragment {
    private static final int SELECT_PICTURE_REQUEST = 101;
    EditText name;
    EditText description;
    EditText price;
    EditText phone;
    ImageView setImageProduct;
    Button loadImage;
    Button confirmBtn;
    Button declineBtn;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private String imageString;
    private View.OnClickListener loadImageListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT).setType("image/*");
            startActivityForResult(Intent.createChooser(intent, getString(R.string.select_picture)), SELECT_PICTURE_REQUEST);
        }
    };
    private View.OnClickListener confirmListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (checkField()) {
                ProductModel productModel = new ProductModel();
                productModel.setId(new Random().nextInt());
                productModel.setName(name.getText().toString());
                productModel.setDescription(description.getText().toString());
                double priceDouble = 0;
                try {
                    priceDouble = Double.parseDouble(price.getText().toString());
                } catch (Exception e) {

                }
                productModel.setPrice(priceDouble);
                productModel.setImage(imageString);
                productModel.setPhone(phone.getText().toString());
                Map<String, Object> map = new HashMap<>();
                map.put(String.valueOf(productModel.getId()), productModel);
                myRef.updateChildren(map);
                ((MainActivity) getActivity()).replaceFragment(new ListProductFragment());
            }
        }
    };

    private View.OnClickListener declineListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
      //      ((MainActivity) getActivity()).replaceFragment(new ListProductFragment());
            getActivity().onBackPressed();
        }
    };

    private boolean checkField() {
        if (name.getText().toString().isEmpty()) {
            name.setError("Please enter a valid name");
            return false;
        }
        if (price.getText().toString().isEmpty()) {
            price.setError("Please enter a valid price");
            return false;
        }
        if (phone.getText().toString().isEmpty()) {
            phone.setError("Please enter a valid phone");
            return false;
        }
        return true;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.make_product_fragment, container, false);
        name = (EditText) view.findViewById(R.id.set_name);
        description = (EditText) view.findViewById(R.id.set_description);
        price = (EditText) view.findViewById(R.id.set_price);

        phone = (EditText) view.findViewById(R.id.set_phone);
        setImageProduct = (ImageView) view.findViewById(R.id.set_image);
        loadImage = (Button) view.findViewById(R.id.load_image_btn);
        loadImage.setOnClickListener(loadImageListener);
        confirmBtn = (Button) view.findViewById(R.id.confirmBtn);
        confirmBtn.setOnClickListener(confirmListener);
        declineBtn = (Button) view.findViewById(R.id.declineBtn);
        declineBtn.setOnClickListener(declineListener);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference(BaseFragment.PRODUCT_REFERENCE);
        phone.setText(((MainActivity)getActivity()).getPhone());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == SELECT_PICTURE_REQUEST) {
            Glide.with(getActivity()).load(data.getData()).into(setImageProduct);
            imageString = getStringFromImage(data.getData());
        }
    }

    private String getStringFromImage(Uri imageUri) {
        File imageFile = null;
        try {
            imageFile = FileUtil.from(getActivity(), imageUri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream);
        byte[] image = stream.toByteArray();
        String img_str = Base64.encodeToString(image, 0);
        return img_str;
    }
}
