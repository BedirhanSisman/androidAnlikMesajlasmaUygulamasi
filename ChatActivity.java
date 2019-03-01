package com.example.anlikmesajlasmauyg;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {

    String kullaniciAdi;
    String otherName;
    TextView chatUserName;
    ImageView backImage;
    EditText chatEditText;
    ImageView sendImage;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    RecyclerView chatRecyclerView;
    MesajAdapter mesajAdapter;
    List<MesajModel> list;

    public void tanimla(){

        //tanımlamalar
        kullaniciAdi = getIntent().getExtras().getString("kullaniciAdi");
        otherName = getIntent().getExtras().getString("otherName");
        Log.i("kullanicilar : ", kullaniciAdi + " ve " + otherName);//logcat'e basacak
        chatUserName = (TextView) findViewById(R.id.chatUserName);
        chatEditText = (EditText) findViewById(R.id.chatEditText);
        backImage = (ImageView) findViewById(R.id.backImage);
        sendImage = (ImageView) findViewById(R.id.sendImage);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        list = new ArrayList<>();
        chatRecyclerView = (RecyclerView) findViewById(R.id.chatRecyclerView);

        //layout işlemleri
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(ChatActivity.this, 1); //her satırda 1 mesaj
        chatRecyclerView.setLayoutManager(layoutManager);
        mesajAdapter = new MesajAdapter(ChatActivity.this, list, ChatActivity.this, kullaniciAdi);
        chatRecyclerView.setAdapter(mesajAdapter);

        //işlemler
        chatUserName.setText(otherName);
        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChatActivity.this, MainActivity.class);
                intent.putExtra("kullaniciAdi", kullaniciAdi); //MainActivity için giriş yaparken kullanici adı key'i verdiğimiz için buradada vermek zorundayız
                startActivity(intent);
            }
        });
        sendImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String gonderilecekMesaj = chatEditText.getText().toString();
                if(!gonderilecekMesaj.equals(null)){
                    mesajGonder(gonderilecekMesaj);
                }
                chatEditText.setText("");
            }
        });
    }

    public void mesajGonder(String text){
        final String ID = databaseReference.child("Mesajlar").child(kullaniciAdi).child(otherName).push().getKey(); //gonderilen mesajın id'si - eğer göndermezsek mesajlerda data kaybı yaşarız.
        final Map messageMap = new HashMap();
        messageMap.put("text", text);
        messageMap.put("from", kullaniciAdi);
        databaseReference.child("Mesajlar").child(kullaniciAdi).child(otherName).child(ID).setValue(messageMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    databaseReference.child("Mesajlar").child(otherName).child(kullaniciAdi).child(ID).setValue(messageMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                        }
                    });
                }
            }
        });
    }

    public void loadMessage(){
        databaseReference.child("Mesajlar").child(kullaniciAdi).child(otherName).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                MesajModel mesajModel = dataSnapshot.getValue(MesajModel.class);
                list.add(mesajModel);
                mesajAdapter.notifyDataSetChanged();
                chatRecyclerView.scrollToPosition(list.size()-1);
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        tanimla();
        loadMessage();
    }
}
