package one.aves.api.component;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonPrimitive;

import java.util.Objects;

public class ClickEvent {

	private final Action action;
	private final Object value;
	private JsonElement jsonPrimitive;

	private ClickEvent(Action action, Object value) {
		this.action = action;
		this.value = value;
	}

	public static ClickEvent of(Action action, boolean value) {
		return new ClickEvent(action, value);
	}

	public static ClickEvent of(Action action, Number value) {
		return new ClickEvent(action, value);
	}

	public static ClickEvent of(Action action, String value) {
		return new ClickEvent(action, value);
	}

	public static ClickEvent of(Action action, char value) {
		return new ClickEvent(action, value);
	}

	public Action getAction() {
		return action;
	}

	public Object getValue() {
		return value;
	}

	public JsonElement getJsonPrimitive() {
		if (this.jsonPrimitive == null) {
			if (this.value instanceof Boolean) {
				this.jsonPrimitive = new JsonPrimitive((Boolean) this.value);
			} else if (this.value instanceof Number) {
				this.jsonPrimitive = new JsonPrimitive((Number) this.value);
			} else if (this.value instanceof String) {
				this.jsonPrimitive = new JsonPrimitive((String) this.value);
			} else if (this.value instanceof Character) {
				this.jsonPrimitive = new JsonPrimitive((Character) this.value);
			} else {
				this.jsonPrimitive = JsonNull.INSTANCE;
			}
		}

		return jsonPrimitive;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		ClickEvent that = (ClickEvent) o;
		return action == that.action && value.equals(that.value);
	}

	@Override
	public int hashCode() {
		return Objects.hash(action, value);
	}

	@Override
	public String toString() {
		return "ClickEvent{" + "action=" + action + ", value=" + value + '}';
	}

	public enum Action {
		OPEN_URL,
		RUN_COMMAND,
		SUGGEST_COMMAND,
		CHANGE_PAGE,
		COPY_TO_CLIPBOARD;

		private final String actualName;

		Action() {
			this.actualName = this.name().toLowerCase();
		}

		public String getActualName() {
			return actualName;
		}
	}
}
