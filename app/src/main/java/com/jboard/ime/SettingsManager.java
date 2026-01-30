package com.jboard.ime;

import android.content.Context;
import android.content.SharedPreferences;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class SettingsManager {
    private static final String PREFS_NAME = "JboardPrefs";
    private static final String KEY_CODING_ROW = "coding_row_enabled";
    private static final String BACKUP_FILE = "jboard_settings.backup";

    private final SharedPreferences prefs;
    private final Context context;

    public SettingsManager(Context context) {
        this.context = context;
        this.prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public boolean isCodingRowEnabled() {
        return prefs.getBoolean(KEY_CODING_ROW, true);
    }

    public void setCodingRowEnabled(boolean enabled) {
        prefs.edit().putBoolean(KEY_CODING_ROW, enabled).apply();
    }

    public boolean backupSettings() {
        Properties props = new Properties();
        props.setProperty(KEY_CODING_ROW, String.valueOf(isCodingRowEnabled()));

        File backupFile = new File(context.getExternalFilesDir(null), BACKUP_FILE);
        try (FileOutputStream out = new FileOutputStream(backupFile)) {
            props.store(out, "Jboard Settings Backup");
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean restoreSettings() {
        File backupFile = new File(context.getExternalFilesDir(null), BACKUP_FILE);
        if (!backupFile.exists()) return false;

        Properties props = new Properties();
        try (FileInputStream in = new FileInputStream(backupFile)) {
            props.load(in);
            boolean codingRowEnabled = Boolean.parseBoolean(props.getProperty(KEY_CODING_ROW, "true"));
            setCodingRowEnabled(codingRowEnabled);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
