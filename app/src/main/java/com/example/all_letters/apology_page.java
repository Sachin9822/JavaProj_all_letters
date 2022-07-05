package com.example.all_letters;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.text.TextPaint;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class apology_page extends AppCompatActivity {

    int x = 100,y = 100;
    Button generate_Pdf_btn;
    PdfDocument pdfDocument;

    int pageWidth = 1200;
    int pageHeight = 1700;

    Bitmap bmp, scaledbmp;
    private static final int PERMISSION_REQUEST_CODE = 200;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apology_page);

        generate_Pdf_btn = findViewById(R.id.generate_pdf);
        bmp = BitmapFactory.decodeResource(getResources(),12);

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
        EditText sender_name = findViewById(R.id.sendername);
        EditText sender_addr = findViewById(R.id.senderAddress);
        EditText sender_class = findViewById(R.id.senderClass);
        EditText sender_date = findViewById(R.id.senderDate);
        EditText recipent_name = findViewById(R.id.RecipientName);
        EditText recipent_Designation = findViewById(R.id.recipientDesig);
        EditText recipent_Organisation = findViewById(R.id.RecipientOrg);
        EditText recipent_addr = findViewById(R.id.RecipientAddress);
        EditText period_of_absence = findViewById(R.id.leavedates);
        EditText cause_of_absence = findViewById(R.id.cause);
        pdfDocument = new PdfDocument();

        Paint sender_info = new Paint();

        Paint Recipent_info = new Paint();

        Paint Subject = new Paint();
        Paint letter_body = new Paint();

        PdfDocument.PageInfo mypageinfo = new PdfDocument.PageInfo.Builder(pageWidth,pageHeight,1).create();

        PdfDocument.Page letter = pdfDocument.startPage(mypageinfo);

        Canvas canvas = letter.getCanvas();

        sender_info.setTypeface(Typeface.DEFAULT);
        Recipent_info.setTypeface(Typeface.DEFAULT);
        sender_info.setTextSize(26);
        Recipent_info.setTextSize(26);

        Subject.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        Subject.setTextSize(26);

        letter_body.setTypeface(Typeface.DEFAULT);
        letter_body.setTextSize(26);
//        canvas.drawText("here is the sender info \n and address",200,200,sender_info);
        drawString( sender_name.getText()+"\n"+
                        sender_addr.getText()+"\n\n"+
                        sender_date.getText()+" "
                ,sender_info,canvas);
        drawString("\n"+recipent_name.getText()+"\n"+
                        recipent_Designation.getText()+"\n"+
                        recipent_Organisation.getText()+"\n"+
                        recipent_addr.getText()+"\n\n" +
                        "Dear Mr/Mrs "+recipent_name.getText()+"\n \n"
                ,Recipent_info,canvas);
        drawString("Subject: Asking apology for the absence"+"\n \n",Subject,canvas);
        String period[] = period_of_absence.getText().toString().split("-");
        drawString("I am "+sender_name.getText()+", a student of "+sender_class.getText()+" . I am writing this letter to apologise for my absence for a period of "+period_of_absence.getText()+". Hence, I was advised to stay at home and rest.\n" +
                "Coming to school could have caused severe discomfort to my health. Thus, the only way to handle this situation was by being absent for a few days. I have already taken notes from my friends for the missing classes. And I have enclosed my medical certificate for your reference. Hope you allow me to appear for the upcoming test.\n" +
                "\n" +
                "I am anticipating your positive response.\n" +
                "\n" +
                "Yours obediently,\n" +
                "\n" +
                "\n" +
                "Signature\n"+
                sender_name.getText()
                ,letter_body,canvas);

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
        intent.putExtra(Intent.EXTRA_TITLE,"apology_letter.pdf");
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