package one.aves.api.component;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import one.aves.api.connection.ProtocolVersion;

import java.util.List;

public class ComponentParser {

	private ComponentParser() {

	}

	public static JsonObject toJson(Component component, ProtocolVersion protocolVersion) {
		return toJson(component, protocolVersion, false);
	}

	private static JsonObject toJson(Component component, ProtocolVersion protocolVersion,
	                                 boolean bare) {
		JsonObject object = new JsonObject();
		object.addProperty("text", component.getText());

		TextColor color = component.getColor();
		if (color != null) {
			object.addProperty("color", color.get());
		}

		Font font = component.getFont();
		if (font != null) {
			object.addProperty("font", font.getActualName());
		}

		List<TextDecoration> decorations = component.getDecorations();
		if (decorations != null) {
			for (TextDecoration decoration : decorations) {
				object.addProperty(decoration.getActualName(), true);
			}
		}

		if (!bare) {
			ClickEvent clickEvent = component.getClickEvent();
			if (clickEvent != null && !clickEvent.getJsonPrimitive().isJsonNull()) {
				JsonObject clickEventObject = new JsonObject();
				clickEventObject.addProperty("action", clickEvent.getAction().getActualName());
				clickEventObject.add("value", clickEvent.getJsonPrimitive());
				object.add("clickEvent", clickEventObject);
			}

			HoverEvent hoverEvent = component.getHoverEvent();
			if (hoverEvent != null) {
				JsonObject hoverEventObject = new JsonObject();
				HoverEvent.Action action = hoverEvent.getAction();
				if (action == HoverEvent.Action.SHOW_ACHIEVEMENT && protocolVersion.isAfter(
						ProtocolVersion.MINECRAFT_1_12_0)) {
					action = HoverEvent.Action.SHOW_TEXT;
				}

				hoverEventObject.addProperty("action", action.getActualName());
				Object value = hoverEvent.getValue();
				if (value instanceof Component) {
					hoverEventObject.add("value", toJson((Component) value, protocolVersion, true));
				} else {
					hoverEventObject.addProperty("value", value.toString());
				}

				object.add("hoverEvent", hoverEventObject);
			}
		}

		List<Component> children = component.getChildren();
		if (children != null && !children.isEmpty()) {
			JsonArray childrenArray = new JsonArray();
			for (Component child : children) {
				childrenArray.add(toJson(child, protocolVersion, bare));
			}

			object.add("extra", childrenArray);
		}

		return object;
	}
}
