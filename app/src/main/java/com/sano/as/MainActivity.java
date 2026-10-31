package com.sano.as;
import android.content.Intent; import android.net.Uri; import android.os.Build; import android.os.Bundle; import android.provider.Settings; import android.widget.Button; import android.widget.CompoundButton; import android.widget.Switch; import android.widget.TextView; import android.widget.Toast; import android.view.View; import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private Switch swFloating;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); setContentView(R.layout.activity_main);
        Toast.makeText(this, "Apk By Sano", Toast.LENGTH_SHORT).show();
        swFloating = findViewById(R.id.swFloating); Button btnAcc = findViewById(R.id.btnAccessibility); Button btnOverlay = findViewById(R.id.btnOverlay); TextView tvGithub = findViewById(R.id.tvGithub);
        swFloating.setChecked(FloatingService.isRunning);
        swFloating.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override public void onCheckedChanged(CompoundButton b, boolean isChecked) {
                if (isChecked) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(MainActivity.this)) {
                        Toast.makeText(MainActivity.this, "Beri izin overlay!", Toast.LENGTH_SHORT).show(); swFloating.setChecked(false);
                        startActivity(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName())));
                    } else { startService(new Intent(MainActivity.this, FloatingService.class)); }
                } else { stopService(new Intent(MainActivity.this, FloatingService.class)); }
            }
        });
        btnAcc.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) { startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)); }
        });
        btnOverlay.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) { startActivity(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()))); }
        });
        tvGithub.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) { startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/LordSanoID"))); }
        });
    }
}