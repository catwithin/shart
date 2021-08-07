package com.gamesofni.shart;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.gamesofni.shart.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Example of a call to a native method
        TextView tv = binding.sampleText;
        tv.setText(stringFromJNI());

        binding.searchActivity.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), SearchActivity.class);
            v.getContext().startActivity(intent);
        });

        binding.addActivity.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), AddImageActivity.class);
            v.getContext().startActivity(intent);
        });

    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();
}
