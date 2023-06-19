package com.example.apptest;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class MainActivity extends AppCompatActivity{

    private static final int GALERY_INTENT = 1;
    private StorageReference mStorage;
    Button btnGuardar,btnMostrar,mSubir;
    EditText txtTitulo,txtContenido;
    static final int READ_BLOCK_SIZE=100;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnGuardar=findViewById(R.id.button2);
        btnMostrar=findViewById(R.id.button);
        txtTitulo=findViewById(R.id.txtTitulo);
        txtContenido=findViewById(R.id.txtContenido);
        mSubir = findViewById(R.id.button4);

        mStorage = FirebaseStorage.getInstance().getReference();
        mSubir.setOnClickListener(new View.OnClickListener() {
            @Override
            //SUBIR IMAGEN
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,GALERY_INTENT);
            }
        });
    }

    public void escribirBtn(View view){
        try {
            String titulo = txtTitulo.getText().toString().trim()+".txt";
            if(txtTitulo.getText().toString()!=""){

                FileOutputStream fileout = null;

                fileout = openFileOutput(titulo, MODE_PRIVATE);

                OutputStreamWriter outputWriter = new OutputStreamWriter(fileout);
                outputWriter.append("\r\n");
                outputWriter.write(txtContenido.getText().toString());
                outputWriter.close();
                //txtmsg.setText("")

                Toast.makeText(getBaseContext(), "NOTA GUARDADA CORRECTAMENTE!",
                        Toast.LENGTH_SHORT).show();

            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void leerBtn(View view){
        try {
            String titulo = txtTitulo.getText().toString().trim()+".txt";

            FileInputStream fileIn=openFileInput(titulo);
            InputStreamReader InputRead= new InputStreamReader(fileIn);
            char[] inputBuffer = new char[READ_BLOCK_SIZE];
            String s="";
            int charRead;

            while ((charRead=InputRead.read(inputBuffer))>0){
                String readstring = String.copyValueOf(inputBuffer,0,charRead);
                s +=readstring;
            }
            InputRead.close();
            txtContenido.setText(s);
        }catch(Exception e){
            Toast.makeText(getBaseContext(), "ESTA NOTA NO EXISTE",
                    Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public void limpiar(View view){
        txtContenido.setText("");
        txtTitulo.setText("");
    }

    //SUBIR IMAGEN
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALERY_INTENT && resultCode == RESULT_OK){

            Uri uri = data.getData();

            StorageReference filePath = mStorage.child("fotos").child(uri.getLastPathSegment());

            filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(MainActivity.this, "Se subio exitosamente la foto.", Toast.LENGTH_SHORT).show();

                }
            });

        }
    }

    public void volver(View view){
        Intent intent = new Intent(MainActivity.this, Inicio.class);
        startActivity(intent);
    }
}





