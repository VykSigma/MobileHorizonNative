package dev.mobilehorizon.app.layout;

import java.util.ArrayList;
import java.util.List;

/**
 * Bridges the new Zalith-inspired ControlLayout system with the existing
 * NativeTouchControlsView rendering/input model.
 *
 * Converts layers/buttons into a flat list of renderable control descriptors
 * that the view can draw and hit-test without a full rewrite.
 */
public class LayoutBridge {

    /** Flat descriptor used by the view for drawing and hit-testing. */
    public static class RenderItem {
        public enum Kind { BUTTON, JOYSTICK }

        public Kind kind;
        public String uuid;
        public String label;
        public float x, y;           // normalized 0-1
        public float width, height;  // normalized (buttons)
        public float radius;         // normalized (joystick)
        public float scale;
        public float opacity;
        public ControlButton.Shape shape;
        public ControlButton.ActionType actionType;
        public int keyCode;
        public int mouseButton;
        public int hotbarSlot;
        public String targetLayerUuid;
        public boolean toggleable;
        public int styleColor;
        public int textColor;
        public String layerUuid;
        public ControlLayer.VisibilityType layerVisibility;
        public boolean layerHidden;

        // runtime state
        public boolean pressed;
    }

    /**
     * Flatten the current layout into render items, filtering by game/menu mode.
     *
     * @param layout      current layout
     * @param gameplayMode true when player is in world (not inventory/menu)
     */
    public static List<RenderItem> flatten(ControlLayout layout, boolean gameplayMode) {
        List<RenderItem> items = new ArrayList<>();
        if (layout == null) return items;

        for (ControlLayer layer : layout.layers) {
            if (layer.hidden) continue;

            // Visibility filter
            switch (layer.visibilityType) {
                case IN_GAME:
                    if (!gameplayMode) continue;
                    break;
                case IN_MENU:
                    if (gameplayMode) continue;
                    break;
                case MANUAL:
                    // shown only when not hidden (already checked)
                    break;
                case ALWAYS:
                default:
                    break;
            }

            // Joystick
            if (layer.joystick != null && layer.joystick.visible) {
                RenderItem ji = new RenderItem();
                ji.kind = RenderItem.Kind.JOYSTICK;
                ji.uuid = layer.joystick.uuid;
                ji.label = "JOY";
                ji.x = layer.joystick.x;
                ji.y = layer.joystick.y;
                ji.radius = layer.joystick.radius;
                ji.opacity = layer.joystick.opacity * layout.globalOpacity;
                ji.scale = layout.globalScale;
                ji.layerUuid = layer.uuid;
                ji.layerVisibility = layer.visibilityType;
                ji.layerHidden = layer.hidden;
                items.add(ji);
            }

            // Buttons
            for (ControlButton b : layer.buttons) {
                if (b.visibility == ControlButton.Visibility.NEVER) continue;
                if (b.visibility == ControlButton.Visibility.IN_GAME && !gameplayMode) continue;
                if (b.visibility == ControlButton.Visibility.IN_MENU && gameplayMode) continue;

                RenderItem ri = new RenderItem();
                ri.kind = RenderItem.Kind.BUTTON;
                ri.uuid = b.uuid;
                ri.label = b.label;
                ri.x = b.x;
                ri.y = b.y;
                ri.width = b.width * b.scale * layout.globalScale;
                ri.height = b.height * b.scale * layout.globalScale;
                ri.scale = b.scale * layout.globalScale;
                ri.opacity = b.opacity * layout.globalOpacity;
                ri.shape = b.shape;
                ri.actionType = b.actionType;
                ri.keyCode = b.keyCode;
                ri.mouseButton = b.mouseButton;
                ri.hotbarSlot = b.hotbarSlot;
                ri.targetLayerUuid = b.targetLayerUuid;
                ri.toggleable = b.toggleable;
                ri.styleColor = b.styleColor;
                ri.textColor = b.textColor;
                ri.layerUuid = layer.uuid;
                ri.layerVisibility = layer.visibilityType;
                ri.layerHidden = layer.hidden;
                items.add(ri);
            }
        }
        return items;
    }

    /** Hit-test a point (normalized 0-1) against render items. Returns topmost match or null. */
    public static RenderItem hitTest(List<RenderItem> items, float nx, float ny) {
        // reverse order so later (top) items win
        for (int i = items.size() - 1; i >= 0; i--) {
            RenderItem item = items.get(i);
            if (item.kind == RenderItem.Kind.JOYSTICK) {
                float dx = nx - item.x;
                float dy = ny - item.y;
                float r = item.radius * item.scale;
                if (dx * dx + dy * dy <= r * r) return item;
            } else {
                float hw = item.width / 2f;
                float hh = item.height / 2f;
                if (nx >= item.x - hw && nx <= item.x + hw
                        && ny >= item.y - hh && ny <= item.y + hh) {
                    return item;
                }
            }
        }
        return null;
    }
}
