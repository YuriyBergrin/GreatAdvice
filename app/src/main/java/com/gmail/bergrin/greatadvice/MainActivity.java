package com.gmail.bergrin.greatadvice;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Random;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    Button adviceButton;
    ImageView imageView;
    ImageView imageView2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        adviceButton = findViewById(R.id.adviceButton);
        showAdvice();
        imageView = findViewById(R.id.imageView);
        imageView = findViewById(R.id.imageView2);
        imageView.animate().alpha(1).setDuration(2000);
    }

    public void showAdvice() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://fucking-great-advice.ru/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        AdviceApi adviceApi = retrofit.create(AdviceApi.class);

        Call<Advice> advice = adviceApi.getRandomAdvice();

        advice.enqueue(new Callback<Advice>() {
            @Override
            public void onResponse(Call<Advice> call, Response<Advice> response) {
                adviceButton.setText(response.body().getText());
            }

            @Override
            public void onFailure(Call<Advice> call, Throwable t) {
                adviceButton.setText("ОТСУТСВТУЕТ СЕТЬ ИНТЕРНЕТ");
                Log.d("LOG ", "ERROR " + t.getMessage());
            }
        });
    }

    public void showImage(String number) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://fucking-great-advice.ru/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ImageApi service = retrofit.create(ImageApi.class);

        Call<ResponseBody> call = service.getImage(number);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    Log.d("LOG", "Response came from server");
                    boolean fileDownLoaded = DownloadImage(response.body());
                    Log.d("LOG", "Image downloaded and saved  - " + fileDownLoaded);
                } catch (Exception e) {
                    Log.d("LOG", "There is an error ");
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("LOG", "ON FAILURE " + t.toString());
            }
        });
    }

    private boolean DownloadImage(ResponseBody body) {
        try {
            Log.d("LOG", "Reading and writing file");
            InputStream inputStream = null;
            FileOutputStream fileOutputStream = null;

            try {
                inputStream = body.byteStream();
                fileOutputStream = new FileOutputStream(getExternalFilesDir(null) + File.separator + "50.jpg");
                int c;
                while ((c = inputStream.read()) != -1) {
                    fileOutputStream.write(c);
                }
            } catch (Exception e) {
                Log.d("LOG", "Download image " + e.toString());
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
            }

            int width, height;

            //ImageView imageView = findViewById(R.id.imageView);
            Bitmap bitmap = BitmapFactory.decodeFile(getExternalFilesDir(null) + File.separator + "50.jpg");
            width = bitmap.getWidth();
            height = bitmap.getHeight();
            Bitmap bitmapTwo = Bitmap.createScaledBitmap(bitmap, width, height, false);
            imageView.setImageBitmap(bitmapTwo);
            return true;

        } catch (Exception e) {
            Log.d("LOG", "Download image  to view" + e.toString());
            return false;
        }
    }

    public void advice(View view) {
        showImage(getNumber());
        showAdvice();
    }

    private String getNumber() {
        Random random = new Random();
        int result = random.nextInt(60 - 1) + 1;
        Log.d("LOG", result + "");
        if (result < 10) {
            return "0" + result;
        } else {
            return "" + result;
        }

    }
}