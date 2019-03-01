package com.example.anlikmesajlasmauyg;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.core.Context;

public class GirisActivity extends AppCompatActivity {

    EditText kullaniciAdiEditText;
    Button girisYapButton;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giris);
        FirebaseApp.initializeApp(GirisActivity.this);
        tanimla();
    }

    public void tanimla(){
        //xml'de kiler ile eşleştirme yapılacaktır.
        kullaniciAdiEditText = (EditText) findViewById(R.id.kullaniciAdiEditText);
        girisYapButton = (Button) findViewById(R.id.girisYapButton);
        //instance'lar alınır db işlemleri için
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        girisYapButton.setOnClickListener(new View.OnClickListener() { //Giris Butonuna Basıldığında
            @Override
            public void onClick(View v) {
                String username = kullaniciAdiEditText.getText().toString().trim();
                kullaniciAdiEditText.setText("");
                if(username.equals("Bedirhan") || username.equals("Rümeysa")){
                    ekle(username);
                }else{
                    Toast.makeText(getApplicationContext(), "Geçerli Bir Kullanıcı Adı Giriniz.", Toast.LENGTH_LONG).show();
                }
            }
        });

        kullaniciAdiEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {  //Emter tuşuna basıldığında
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(event.getKeyCode() == KeyEvent.KEYCODE_ENTER){
                    girisYapButton.performClick(); //giris yap butonuna tıkla
                }
                return false;
            }
        });
    }

    public void ekle(final String kullaniciAdi){
        databaseReference.child("Kullanıcılar").child(kullaniciAdi).child("kullaniciAdi").setValue(kullaniciAdi).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                Toast.makeText(getApplicationContext(), "Giriş İşlemi Başarılı", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(GirisActivity.this, MainActivity.class); //activity'ler arası geçiş
                intent.putExtra("kullaniciAdi", kullaniciAdi); //diğer activity'ye parametre yolladık inner class olduğu içinde ekle fonk.'daki parametreyi final yaptık.
                startActivity(intent); //geçişi gerçekleştir
            }
        });
    }

}
