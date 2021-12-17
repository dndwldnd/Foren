package com.example.application.board;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.application.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;

public class BoardFragment extends Fragment {

    public BoardFragment() {

    }

    private ArrayList<String> list;
    private RequestQueue requestQueue;
    private ArrayAdapter<String> adapter;
    private ListView listView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_board, container, false);
        listView = v.findViewById(R.id.board_list);


        if(requestQueue == null){
            requestQueue = Volley.newRequestQueue(requireActivity());
        }


        list = new ArrayList<>();
        list.add("자유게시판");
        list.add("정보게시판");
        list.add("질문게시판");

        adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, list);
        listView.setAdapter(adapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(getActivity(), BoardActivity.class);
                    intent.putExtra("text", list.get(position));
                    intent.putStringArrayListExtra("list", list);
                    startActivity(intent);
                }
        });

        return v;
    }
}