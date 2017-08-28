package com.example.ragfair.adapters;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ragfair.MainActivity;
import com.example.ragfair.R;
import com.example.ragfair.fragments.BaseFragment;
import com.example.ragfair.fragments.ProductFragment;
import com.example.ragfair.models.ProductModel;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class MyListProductAdapter extends BaseAdapter {
    private List<ProductModel> listProduct = new ArrayList<>();
    private Context context;

    public MyListProductAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return listProduct.size();
    }

    @Override
    public Object getItem(int i) {
        return listProduct.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(context).inflate(R.layout.list_product_item, null, false);
        ImageView image = (ImageView) view.findViewById(R.id.image);
        TextView name = (TextView) view.findViewById(R.id.name_text_view);
        TextView price = (TextView) view.findViewById(R.id.price_text_view);
        ImageButton removeBtn = (ImageButton) view.findViewById(R.id.removeBtn);
        name.setText(listProduct.get(i).getName());
        price.setText(String.valueOf(listProduct.get(i).getPrice()));
        if (listProduct.get(i).getImage()!=null){
            byte[] decodedString = Base64.decode(listProduct.get(i).getImage(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            image.setImageBitmap(decodedByte);
        }
        else {
            image.setImageResource(R.drawable.ic_menu_manage);
        }
        removeBtn.setVisibility(View.VISIBLE);
        removeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase.getInstance().getReference(BaseFragment.PRODUCT_REFERENCE).child(String.valueOf(listProduct.get(i).getId())).removeValue();
            }
        });

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)context).replaceFragment(ProductFragment.newInstance(listProduct.get(i)));
                notifyDataSetChanged();
            }
        });
        return view;
    }
    public void setListProduct(List <ProductModel> listProduct){
        this.listProduct=listProduct;
        notifyDataSetChanged();
    }
}
