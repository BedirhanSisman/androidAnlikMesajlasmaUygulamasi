package com.example.anlikmesajlasmauyg;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    List<String> list = new ArrayList<>();
    String kullaniciAdi;
    RecyclerView userRecyclerView;
    UserAdapter userAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tanimla();
        listele();
    }

    public void girisEkraninaGec(){
        Intent intent = new Intent(MainActivity.this, GirisActivity.class);
        startActivity(intent);
    }

    public void tanimla(){
        kullaniciAdi = getIntent().getExtras().getString("kullaniciAdi"); //gönderilen activity'de ki key değeri
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(MainActivity.this, 2); //bir satırda iki adet kullanıcının listelenmesini istiyorsak
        userAdapter = new UserAdapter(MainActivity.this, list, MainActivity.this, kullaniciAdi);
        userRecyclerView = (RecyclerView) findViewById(R.id.userRecyclerView);
        userRecyclerView.setLayoutManager(layoutManager);
        userRecyclerView.setAdapter(userAdapter);
    }

    public void listele(){
        databaseReference.child("Kullanıcılar").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                //Log.i("Kullanıcı : " , dataSnapshot.getKey()); //Logcat'e basacak.
                if(!dataSnapshot.getKey().equals(kullaniciAdi)){
                    list.add(dataSnapshot.getKey());
                    Log.i("Kullanıcı : " , dataSnapshot.getKey()); //kendisi dışındaki kullanıcıları Logcat'e basacak.
                    userAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



}
