package dev.mobilehorizon.app.layout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class ControlLayout {
    public static final int CURRENT_VERSION = 1;
    public int version = CURRENT_VERSION;
    public String name = "Custom Layout", author = "", description = "";
    public float globalScale = 1.0f, globalOpacity = 0.85f;
    public List<ControlLayer> layers = new ArrayList<>();

    public ControlLayout() {}
    public ControlLayout(String name) { this.name = name; }

    public JSONObject toJson() throws JSONException {
        JSONObject root = new JSONObject();
        root.put("version", version);
        JSONObject info = new JSONObject();
        info.put("name", name); info.put("author", author); info.put("description", description);
        root.put("info", info);
        root.put("globalScale", globalScale); root.put("globalOpacity", globalOpacity);
        JSONArray layersArr = new JSONArray();
        for (ControlLayer layer : layers) layersArr.put(layer.toJson());
        root.put("layers", layersArr);
        return root;
    }

    public String toJsonString() throws JSONException { return toJson().toString(2); }

    public static ControlLayout fromJson(JSONObject root) throws JSONException {
        ControlLayout layout = new ControlLayout();
        layout.version = root.optInt("version", 1);
        JSONObject info = root.optJSONObject("info");
        if (info != null) {
            layout.name = info.optString("name", "Custom Layout");
            layout.author = info.optString("author", "");
            layout.description = info.optString("description", "");
        }
        layout.globalScale = (float) root.optDouble("globalScale", 1.0);
        layout.globalOpacity = (float) root.optDouble("globalOpacity", 0.85);
        JSONArray layersArr = root.optJSONArray("layers");
        if (layersArr != null) for (int i = 0; i < layersArr.length(); i++) {
            JSONObject lj = layersArr.optJSONObject(i);
            if (lj != null) layout.layers.add(ControlLayer.fromJson(lj));
        }
        return layout;
    }

    public static ControlLayout fromJsonString(String json) throws JSONException {
        return fromJson(new JSONObject(json));
    }

    public ControlLayer findLayer(String layerUuid) {
        for (ControlLayer l : layers) if (l.uuid.equals(layerUuid)) return l;
        return null;
    }

    public ControlLayer findLayerByName(String name) {
        for (ControlLayer l : layers) if (l.name.equals(name)) return l;
        return null;
    }

    /** Default Minecraft-oriented layout. */
    public static ControlLayout createDefault() {
        ControlLayout layout = new ControlLayout("Default");
        layout.author = "MobileHorizon";
        layout.description = "Layout padrao inspirado no Zalith";

        ControlLayer game = new ControlLayer("game");
        game.visibilityType = ControlLayer.VisibilityType.IN_GAME;

        ControlJoystick joy = new ControlJoystick();
        joy.x = 0.17f; joy.y = 0.73f; joy.radius = 0.115f;
        game.joystick = joy;

        ControlButton jump = new ControlButton("Pulo", 0.84f, 0.72f, ControlButton.ActionType.KEY, 57);
        jump.width = 0.13f; jump.height = 0.09f; jump.shape = ControlButton.Shape.CIRCLE;
        game.buttons.add(jump);

        ControlButton sneak = new ControlButton("Agachar", 0.84f, 0.85f, ControlButton.ActionType.KEY, 59);
        sneak.width = 0.11f; sneak.height = 0.07f;
        game.buttons.add(sneak);

        ControlButton attack = new ControlButton("Atacar", 0.70f, 0.68f, ControlButton.ActionType.MOUSE_BUTTON, 0);
        attack.mouseButton = 0; attack.width = 0.11f; attack.height = 0.08f;
        game.buttons.add(attack);

        ControlButton use = new ControlButton("Usar", 0.70f, 0.80f, ControlButton.ActionType.MOUSE_BUTTON, 1);
        use.mouseButton = 1; use.width = 0.11f; use.height = 0.08f;
        game.buttons.add(use);

        ControlButton inv = new ControlButton("Inv", 0.92f, 0.28f, ControlButton.ActionType.KEY, 33);
        inv.width = 0.09f; inv.height = 0.065f;
        game.buttons.add(inv);

        ControlButton chat = new ControlButton("Chat", 0.92f, 0.38f, ControlButton.ActionType.KEY, 20);
        chat.width = 0.09f; chat.height = 0.065f;
        game.buttons.add(chat);

        ControlButton f5 = new ControlButton("F5", 0.92f, 0.48f, ControlButton.ActionType.KEY, 63);
        f5.width = 0.08f; f5.height = 0.06f;
        game.buttons.add(f5);

        layout.layers.add(game);

        ControlLayer gui = new ControlLayer("gui");
        gui.visibilityType = ControlLayer.VisibilityType.IN_MENU;
        ControlButton esc = new ControlButton("Esc", 0.08f, 0.08f, ControlButton.ActionType.KEY, 111);
        esc.width = 0.10f; esc.height = 0.07f;
        gui.buttons.add(esc);
        layout.layers.add(gui);

        return layout;
    }
}
