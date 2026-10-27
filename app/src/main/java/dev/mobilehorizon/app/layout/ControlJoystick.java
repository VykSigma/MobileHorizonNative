package dev.mobilehorizon.app.layout;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.UUID;

public class ControlJoystick {
    public String uuid;
    public float x = 0.18f, y = 0.72f, radius = 0.11f, deadzone = 0.18f, opacity = 0.75f;
    public boolean fixed = false, visible = true;

    public ControlJoystick() {
        uuid = UUID.randomUUID().toString().replace("-", "").substring(0, 12);
    }

    public JSONObject toJson() throws JSONException {
        JSONObject o = new JSONObject();
        o.put("uuid", uuid); o.put("x", x); o.put("y", y); o.put("radius", radius);
        o.put("deadzone", deadzone); o.put("opacity", opacity); o.put("fixed", fixed); o.put("visible", visible);
        return o;
    }

    public static ControlJoystick fromJson(JSONObject o) throws JSONException {
        ControlJoystick j = new ControlJoystick();
        j.uuid = o.optString("uuid", j.uuid);
        j.x = (float) o.optDouble("x", 0.18); j.y = (float) o.optDouble("y", 0.72);
        j.radius = (float) o.optDouble("radius", 0.11); j.deadzone = (float) o.optDouble("deadzone", 0.18);
        j.opacity = (float) o.optDouble("opacity", 0.75);
        j.fixed = o.optBoolean("fixed", false); j.visible = o.optBoolean("visible", true);
        return j;
    }
}
