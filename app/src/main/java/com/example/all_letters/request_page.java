package com.example.all_letters;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
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

public class request_page extends AppCompatActivity {

    int x = 100,y = 100;
    Button generate_Pdf_btn;
    PdfDocument pdfDocument;

    int pageWidth = 1200;
    int pageHeight = 1700;
    private static final int PERMISSION_REQUEST_CODE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_page);
        generate_Pdf_btn = findViewById(R.id.request_submit);
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
        EditText sender_organisation = findViewById(R.id.senderOrg);
        EditText sender_organisation_add = findViewById(R.id.senderAddress);
        EditText sender_department = findViewById(R.id.sender_dept);
        EditText sender_roll_no = findViewById(R.id.roll_no);
        EditText current_date = findViewById(R.id.senderDate);
        EditText recipient_name = findViewById(R.id.RecipientName);
        EditText recipient_des = findViewById(R.id.recipientDesig);
        EditText recipient_org = findViewById(R.id.RecipientOrg);
        EditText recipient_add = findViewById(R.id.RecipientAddress);
        EditText intern_role = findViewById(R.id.intern_role);
        EditText intern_comp = findViewById(R.id.internCompany);
        EditText tenure = findViewById(R.id.tenure);
        EditText start_date = findViewById(R.id.start_date);
        EditText timing = findViewById(R.id.timing);
        pdfDocument = new PdfDocument();

        Paint sender_info = new Paint();

        Paint Recipent_info = new Paint();

        Paint Subject = new Paint();
        Paint letter_body = new Paint();

        sender_info.setTypeface(Typeface.DEFAULT);
        sender_info.setTextSize(24);

        Recipent_info.setTypeface(Typeface.DEFAULT);
        Recipent_info.setTextSize(24);

        Subject.setTypeface(Typeface.DEFAULT_BOLD);
        Subject.setTextSize(24);

        letter_body.setTypeface(Typeface.DEFAULT);
        letter_body.setTextSize(24);

        PdfDocument.PageInfo mypageinfo = new PdfDocument.PageInfo.Builder(pageWidth,pageHeight,1).create();

        PdfDocument.Page letter = pdfDocument.startPage(mypageinfo);

        Canvas canvas = letter.getCanvas();
        drawString(String.valueOf(sender_name.getText())+"\n"+"" +
                sender_organisation.getText()+"\n" +
                sender_organisation_add.getText()+"\n\n" +
                current_date.getText()+"\n \n"
                ,sender_info,canvas);

        drawString(recipient_des.getText()+"\n" +
                recipient_org.getText()+"\n" +
                recipient_add.getText()+"\n \n"+""
                ,Recipent_info,canvas);

        drawString("Subject: Request for permission to attend internship\n \n"+"",Subject,canvas);

        drawString("Respected Sir/Madam\n\n" +
                "With due respect, my name is "+sender_name.getText()+" from the "+sender_department.getText()+" department, having roll number "+sender_roll_no.getText()+".\n" +
                        "\n" +
                        "I am writing this letter to request permission for attending an internship. I recently got selected for "+intern_role.getText()+" by "+intern_comp.getText()+". The timings of the internship are "+timing.getText()+" for "+tenure.getText()+" days from "+start_date.getText()+".\n" +
                        "This internship being a really important and integral part of my learning procedures, I request you to consider my situation and grant me permission for the same.\n" +
                        "Look forward to your kind consideration.\n" +
                        "\n" +
                        "Yours Sincerely,\n\n" +
                        sender_name.getText()
                ,letter_body,canvas);


        pdfDocument.finishPage(letter);


        try {
            createFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void drawString(String text, Paint paint, Canvas canvas)
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
        intent.putExtra(Intent.EXTRA_TITLE,"internship_letter.pdf");
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