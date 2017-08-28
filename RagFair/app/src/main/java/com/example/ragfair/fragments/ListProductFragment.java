package com.example.ragfair.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.ragfair.MainActivity;
import com.example.ragfair.R;
import com.example.ragfair.adapters.ListProductAdapter;
import com.example.ragfair.listeners.FireBaseValueEventListener;
import com.example.ragfair.models.ProductModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ListProductFragment extends BaseFragment {
    private ListView listProduct;
    private ListProductAdapter listProductAdapter;
    private SearchView searchView;
    private ArrayList <ProductModel>productList=new ArrayList();
    private FireBaseValueEventListener valueEventListener = new FireBaseValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
             productList = new ArrayList<>();
            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                ProductModel product = dataSnapshot1.getValue(ProductModel.class);
                productList.add(product);
            }
            listProductAdapter.setListProduct(productList);
        }
    };
    private SearchView.OnQueryTextListener onQueryTextListener=new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String s) {
            return false;
        }

        @Override
        public boolean onQueryTextChange(String s) {
            if (s.isEmpty()){
                listProductAdapter.reset();
                return true;
            }
            ArrayList <ProductModel> filterList=new ArrayList<>();
            for (int i = 0; i <productList.size() ; i++) {
                if (productList.get(i).getName().toUpperCase().trim().contains(s.toUpperCase().trim())){
                    filterList.add(productList.get(i));
                }
            }
            listProductAdapter.setFilterList(filterList);
            return true;
        }
    };

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        listProduct = (ListView) view.findViewById(R.id.main_list_products);
        searchView = ((MainActivity) getActivity()).getSearchView();
        searchView.setVisibility(View.VISIBLE);
        searchView.setOnQueryTextListener(onQueryTextListener);
        initAdapter();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(BaseFragment.PRODUCT_REFERENCE);
        myRef.addValueEventListener(valueEventListener);
        super.onViewCreated(view, savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.list_product_fragment, container, false);
    }

    private void initAdapter() {
        listProductAdapter = new ListProductAdapter(getActivity());
        listProduct.setAdapter(listProductAdapter);
    }

    @Override
    public void onDestroyView() {
        searchView.setVisibility(View.GONE);
        super.onDestroyView();
    }
}
