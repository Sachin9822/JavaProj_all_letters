package com.example.all_letters;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.FileOutputStream;

public class notes extends AppCompatActivity {

    int x = 100,y = 100;
    Button generate_Pdf_btn;
    PdfDocument pdfDocument;

    int pageWidth = 1200;
    int pageHeight = 1700;
    private static final int PERMISSION_REQUEST_CODE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);
        generate_Pdf_btn = findViewById(R.id.button);

        if(checkPermission()){
            Toast.makeText(this,"Permission Granted",Toast.LENGTH_SHORT).show();
        }
        else{
            requestPermissions();
        }
        generate_Pdf_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                generatePdf();
            }
        });
    }
    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
    }
    private boolean checkPermission() {
        // checking of permissions.
        int permission1 = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int permission2 = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        return permission1 == PackageManager.PERMISSION_GRANTED && permission2 == PackageManager.PERMISSION_GRANTED;
    }
    public void generatePdf(){
        EditText notes = findViewById(R.id.editTextTextMultiLine);
        pdfDocument = new PdfDocument();

        Paint notes_paint = new Paint();
        notes_paint.setTypeface(Typeface.DEFAULT);
        notes_paint.setTextSize(24);


        PdfDocument.PageInfo mypageinfo = new PdfDocument.PageInfo.Builder(pageWidth,pageHeight,1).create();

        PdfDocument.Page letter = pdfDocument.startPage(mypageinfo);

        Canvas canvas = letter.getCanvas();

//        canvas.drawText("here is the sender info \n and address",200,200,sender_info);

        drawString(String.valueOf(notes.getText()),notes_paint,canvas);
        pdfDocument.finishPage(letter);


        try {
            createFile();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public void drawString(String text, Paint paint,Canvas canvas)
    {
        if (text.contains("\n"))
        {
            // spliting wiht \n as delimeter
            String new_text = "";
            String temp[] = text.split("\n");
            for(String t:temp){
                StringBuilder new_t = new StringBuilder();
                int count = 0;
                while(count<t.length()){
                    new_t.append(t.charAt(count));
                    if(count%80==0 && count != 0){
                        new_t.append("\n");
                    }
                    count++;
                }

                new_text += new_t+"\n";
            }
            String[] texts = new_text.split("\n");

            for (String txt : texts)
            {
                canvas.drawText(txt, x, y, paint);

                y += paint.getTextSize();
            }
        }
        else
        {
            canvas.drawText(text, x, y, paint);
        }
    }
    private void createFile() {
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/pdf");
        intent.putExtra(Intent.EXTRA_TITLE,"notes.pdf");
        startActivityForResult(intent,1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode,Intent resutData) {

        super.onActivityResult(requestCode, resultCode, resutData);
        if(resultCode == Activity.RESULT_OK && requestCode == 1){
            Uri uri = null;
            if(resutData != null){
                uri = resutData.getData();
                if(pdfDocument != null){
                    ParcelFileDescriptor pfd = null;
                    try {
                        pfd = getContentResolver().openFileDescriptor(uri,"w");
                        FileOutputStream fileOutputStream = new FileOutputStream(pfd.getFileDescriptor());
                        pdfDocument.writeTo(fileOutputStream);
                        Toast.makeText(this,"Saved Successfully",Toast.LENGTH_SHORT).show();
                        pdfDocument.close();
                    } catch (Exception e) {
                        Toast.makeText(this,e.getMessage(),Toast.LENGTH_LONG).show();
                    }
                }
            }
        }
    }
}