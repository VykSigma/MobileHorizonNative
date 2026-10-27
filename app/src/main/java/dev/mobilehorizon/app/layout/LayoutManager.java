package dev.mobilehorizon.app.layout;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import org.json.JSONException;

public class LayoutManager {
    private static final String TAG = "LayoutManager";
    private static final String PREFS_NAME = "mobilehorizon_layouts";
    private static final String KEY_CURRENT_LAYOUT = "current_layout_json";

    private final SharedPreferences prefs;
    private ControlLayout current;

    public LayoutManager(Context context) {
        prefs = context.getApplicationContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        load();
    }

    public ControlLayout getCurrent() { return current; }
    public void setCurrent(ControlLayout layout) { this.current = layout; }

    public void load() {
        String json = prefs.getString(KEY_CURRENT_LAYOUT, null);
        if (json != null && !json.isEmpty()) {
            try {
                current = ControlLayout.fromJsonString(json);
                Log.i(TAG, "Loaded layout: " + current.name + " (" + current.layers.size() + " layers)");
                return;
            } catch (Exception e) {
                Log.w(TAG, "Failed to parse saved layout, using default", e);
            }
        }
        current = ControlLayout.createDefault();
        save();
    }

    public void save() {
        if (current == null) return;
        try {
            prefs.edit().putString(KEY_CURRENT_LAYOUT, current.toJsonString()).apply();
            Log.i(TAG, "Saved layout: " + current.name);
        } catch (JSONException e) {
            Log.e(TAG, "Failed to serialize layout", e);
        }
    }

    public void resetToDefault() { current = ControlLayout.createDefault(); save(); }

    public boolean importFromJson(String json) {
        try {
            ControlLayout imported = ControlLayout.fromJsonString(json);
            if (imported.layers.isEmpty()) return false;
            current = imported; save(); return true;
        } catch (Exception e) {
            Log.e(TAG, "Import failed", e); return false;
        }
    }

    public String exportToJson() {
        try { return current != null ? current.toJsonString() : null; }
        catch (JSONException e) { return null; }
    }

    public boolean toggleLayer(String layerUuid) {
        ControlLayer layer = current != null ? current.findLayer(layerUuid) : null;
        if (layer == null) return false;
        layer.hidden = !layer.hidden;
        return layer.hidden;
    }
}
