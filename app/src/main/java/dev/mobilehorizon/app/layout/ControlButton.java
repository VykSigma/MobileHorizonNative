package dev.mobilehorizon.app.layout;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.UUID;

public class ControlButton {
    public enum Shape { CIRCLE, ROUNDED_RECT, RECT }
    public enum Visibility { ALWAYS, IN_GAME, IN_MENU, NEVER }
    public enum ActionType { KEY, MOUSE_BUTTON, HOTBAR_SLOT, TOGGLE_CONTROLS, SWITCH_LAYER, CUSTOM }

    public String uuid;
    public String label;
    public float x = 0.5f, y = 0.5f;
    public float width = 0.12f, height = 0.08f;
    public float scale = 1.0f, opacity = 0.85f;
    public Shape shape = Shape.ROUNDED_RECT;
    public Visibility visibility = Visibility.ALWAYS;
    public ActionType actionType = ActionType.KEY;
    public int keyCode = 0, mouseButton = 0, hotbarSlot = 0;
    public String targetLayerUuid = null;
    public boolean toggleable = false, penetrable = false;
    public int styleColor = 0xCC1A2434, textColor = 0xFFFFFFFF;

    public ControlButton() {
        uuid = UUID.randomUUID().toString().replace("-", "").substring(0, 16);
        label = "";
    }

    public ControlButton(String label, float x, float y, ActionType type, int code) {
        this(); this.label = label; this.x = x; this.y = y; this.actionType = type; this.keyCode = code;
    }

    public JSONObject toJson() throws JSONException {
        JSONObject o = new JSONObject();
        o.put("uuid", uuid); o.put("label", label);
        o.put("x", x); o.put("y", y); o.put("width", width); o.put("height", height);
        o.put("scale", scale); o.put("opacity", opacity);
        o.put("shape", shape.name()); o.put("visibility", visibility.name());
        o.put("actionType", actionType.name());
        o.put("keyCode", keyCode); o.put("mouseButton", mouseButton); o.put("hotbarSlot", hotbarSlot);
        o.put("targetLayerUuid", targetLayerUuid != null ? targetLayerUuid : JSONObject.NULL);
        o.put("toggleable", toggleable); o.put("penetrable", penetrable);
        o.put("styleColor", styleColor); o.put("textColor", textColor);
        return o;
    }

    public static ControlButton fromJson(JSONObject o) throws JSONException {
        ControlButton b = new ControlButton();
        b.uuid = o.optString("uuid", b.uuid);
        b.label = o.optString("label", "");
        b.x = (float) o.optDouble("x", 0.5); b.y = (float) o.optDouble("y", 0.5);
        b.width = (float) o.optDouble("width", 0.12); b.height = (float) o.optDouble("height", 0.08);
        b.scale = (float) o.optDouble("scale", 1.0); b.opacity = (float) o.optDouble("opacity", 0.85);
        try { b.shape = Shape.valueOf(o.optString("shape", "ROUNDED_RECT")); } catch (Exception ignored) {}
        try { b.visibility = Visibility.valueOf(o.optString("visibility", "ALWAYS")); } catch (Exception ignored) {}
        try { b.actionType = ActionType.valueOf(o.optString("actionType", "KEY")); } catch (Exception ignored) {}
        b.keyCode = o.optInt("keyCode", 0); b.mouseButton = o.optInt("mouseButton", 0);
        b.hotbarSlot = o.optInt("hotbarSlot", 0);
        String tl = o.optString("targetLayerUuid", null);
        b.targetLayerUuid = (tl == null || "null".equals(tl)) ? null : tl;
        b.toggleable = o.optBoolean("toggleable", false); b.penetrable = o.optBoolean("penetrable", false);
        b.styleColor = o.optInt("styleColor", 0xCC1A2434); b.textColor = o.optInt("textColor", 0xFFFFFFFF);
        return b;
    }
}
