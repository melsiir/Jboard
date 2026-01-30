package com.jboard.ime;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    private SettingsManager settingsManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        settingsManager = new SettingsManager(this);

        Switch switchCodingRow = findViewById(R.id.switch_coding_row);
        switchCodingRow.setChecked(settingsManager.isCodingRowEnabled());
        switchCodingRow.setOnCheckedChangeListener((buttonView, isChecked) -> {
            settingsManager.setCodingRowEnabled(isChecked);
        });

        Button btnBackup = findViewById(R.id.btn_backup);
        btnBackup.setOnClickListener(v -> {
            if (settingsManager.backupSettings()) {
                Toast.makeText(this, "Backup successful", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Backup failed", Toast.LENGTH_SHORT).show();
            }
        });

        Button btnRestore = findViewById(R.id.btn_restore);
        btnRestore.setOnClickListener(v -> {
            if (settingsManager.restoreSettings()) {
                switchCodingRow.setChecked(settingsManager.isCodingRowEnabled());
                Toast.makeText(this, "Restore successful", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Restore failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
