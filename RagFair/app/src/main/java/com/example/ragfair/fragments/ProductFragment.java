package com.example.ragfair.fragments;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ragfair.MainActivity;
import com.example.ragfair.R;
import com.example.ragfair.models.ProductModel;

public class ProductFragment extends BaseFragment {

    private static final String ARGUMENT_PRODUCT = "product";

    public static ProductFragment newInstance(ProductModel product) {
        Bundle args = new Bundle();
        args.putSerializable(ARGUMENT_PRODUCT, product);
        ProductFragment fragment = new ProductFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private ImageView imageProduct;
    private TextView nameTextView;
    private TextView descriptionTextView;
    private TextView priceTextView;
    private TextView phoneTextView;
    private Button backBtn;
    private View.OnClickListener backListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            ((MainActivity) getActivity()).replaceFragment(new ListProductFragment());
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.product_fragment, container, false);
        imageProduct = (ImageView) view.findViewById(R.id.viewImageProduct);
        nameTextView = (TextView) view.findViewById(R.id.txtViewName);
        descriptionTextView = (TextView) view.findViewById(R.id.set_description);
        priceTextView = (TextView) view.findViewById(R.id.txtViewPrice);
        phoneTextView = (TextView) view.findViewById(R.id.txtViewPhone);
        backBtn = (Button) view.findViewById(R.id.backBtn);
        backBtn.setOnClickListener(backListener);
        setDataInView();
        return view;
    }

    private void setDataInView() {
        Bundle bundle = getArguments();
        ProductModel product = (ProductModel) bundle.getSerializable(ARGUMENT_PRODUCT);
        if (product.getImage() != null) {
            byte[] decodedString = Base64.decode(product.getImage(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            imageProduct.setImageBitmap(decodedByte);
        }
        nameTextView.setText(product.getName());
        descriptionTextView.setText(product.getDescription());
        priceTextView.setText(String.valueOf(product.getPrice()));
        phoneTextView.setText(product.getPhone());
    }
}
