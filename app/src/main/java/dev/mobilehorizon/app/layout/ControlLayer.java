package dev.mobilehorizon.app.layout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ControlLayer {
    public enum VisibilityType { ALWAYS, IN_GAME, IN_MENU, MANUAL }

    public String uuid, name = "layer";
    public boolean hidden = false;
    public VisibilityType visibilityType = VisibilityType.ALWAYS;
    public List<ControlButton> buttons = new ArrayList<>();
    public ControlJoystick joystick = null;

    public ControlLayer() { uuid = UUID.randomUUID().toString().replace("-", "").substring(0, 12); }
    public ControlLayer(String name) { this(); this.name = name; }

    public JSONObject toJson() throws JSONException {
        JSONObject o = new JSONObject();
        o.put("uuid", uuid); o.put("name", name); o.put("hidden", hidden);
        o.put("visibilityType", visibilityType.name());
        JSONArray btns = new JSONArray();
        for (ControlButton b : buttons) btns.put(b.toJson());
        o.put("buttons", btns);
        if (joystick != null) o.put("joystick", joystick.toJson());
        return o;
    }

    public static ControlLayer fromJson(JSONObject o) throws JSONException {
        ControlLayer layer = new ControlLayer();
        layer.uuid = o.optString("uuid", layer.uuid);
        layer.name = o.optString("name", "layer");
        layer.hidden = o.optBoolean("hidden", false);
        try { layer.visibilityType = VisibilityType.valueOf(o.optString("visibilityType", "ALWAYS")); } catch (Exception ignored) {}
        JSONArray btns = o.optJSONArray("buttons");
        if (btns != null) for (int i = 0; i < btns.length(); i++) {
            JSONObject bj = btns.optJSONObject(i);
            if (bj != null) layer.buttons.add(ControlButton.fromJson(bj));
        }
        JSONObject js = o.optJSONObject("joystick");
        if (js != null) layer.joystick = ControlJoystick.fromJson(js);
        return layer;
    }

    public ControlButton findButton(String buttonUuid) {
        for (ControlButton b : buttons) if (b.uuid.equals(buttonUuid)) return b;
        return null;
    }
}
