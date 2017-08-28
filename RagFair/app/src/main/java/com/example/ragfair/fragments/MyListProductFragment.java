package com.example.ragfair.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.ragfair.MainActivity;
import com.example.ragfair.R;
import com.example.ragfair.adapters.MyListProductAdapter;
import com.example.ragfair.listeners.FireBaseValueEventListener;
import com.example.ragfair.models.ProductModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;

public class MyListProductFragment extends BaseFragment {
    private ListView listProduct;
    private MyListProductAdapter listProductAdapter;
    private FireBaseValueEventListener valueEventListener = new FireBaseValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            ArrayList<ProductModel> productList = new ArrayList<>();
            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                ProductModel product = dataSnapshot1.getValue(ProductModel.class);
                productList.add(product);
            }
            listProductAdapter.setListProduct(productList);
        }
    };

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        listProduct = (ListView) view.findViewById(R.id.main_list_products);
        initAdapter();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        Query myRef = database.getReference(BaseFragment.PRODUCT_REFERENCE).orderByChild("phone").equalTo(((MainActivity)getActivity()).getPhone());
        myRef.addValueEventListener(valueEventListener);
        super.onViewCreated(view, savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.list_product_fragment, container, false);
    }

    private void initAdapter() {
        listProductAdapter = new MyListProductAdapter(getActivity());
        listProduct.setAdapter(listProductAdapter);

    }
}
