package one.aves.api.component;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import one.aves.api.network.ProtocolVersion;

import java.util.List;

public class ComponentParser {

	private static final char LEGACY_COLOR_CHARACTER = '\u00A7';

	private ComponentParser() {

	}

	public static String toFormattedText(Component component) {
		StringBuilder stringBuilder = new StringBuilder(getAncientText(component, true));
		if (component.getChildren() != null) {
			for (Component child : component.getChildren()) {
				stringBuilder.append(toFormattedText(child));
			}
		}

		return stringBuilder.toString();
	}

	public static JsonObject toJson(Component component, ProtocolVersion protocolVersion,
	                                boolean onlyText) {
		JsonObject object = new JsonObject();
		if (protocolVersion.isBefore(ProtocolVersion.MC_1_8_0)) {
			applyAncientText(object, component);
		} else {
			applyText(object, component);
		}

		if (!onlyText) {
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
						ProtocolVersion.MC_1_12_0)) {
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
				childrenArray.add(toJson(child, protocolVersion, onlyText));
			}

			object.add("extra", childrenArray);
		}

		return object;
	}

	private static void applyAncientText(JsonObject object, Component component) {
		object.addProperty("text", getAncientText(component, true));
	}

	private static void applyText(JsonObject object, Component component) {
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
	}

	private static String getAncientText(Component component, boolean formatting) {
		if (!formatting) {
			return component.getText();
		}

		StringBuilder stringBuilder = new StringBuilder();
		if (component.getColor() != null) {
			stringBuilder.append(LEGACY_COLOR_CHARACTER);
			Character legacyCharacter = component.getColor().getLegacyCharacter();
			stringBuilder.append(
					legacyCharacter == null ? TextColor.WHITE.getLegacyCharacter() : legacyCharacter);
		}

		List<TextDecoration> decorations = component.getDecorations();
		if (decorations != null) {
			for (TextDecoration decoration : decorations) {
				stringBuilder.append(LEGACY_COLOR_CHARACTER);
				stringBuilder.append(decoration.getLegacyCharacter());
			}
		}

		stringBuilder.append(component.getText());
		return stringBuilder.toString();
	}
}
