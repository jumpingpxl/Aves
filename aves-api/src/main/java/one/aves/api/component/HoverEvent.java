package one.aves.api.component;

import java.util.Objects;

public class HoverEvent {

	private final Action action;
	private final Object value;

	private HoverEvent(Action action, Object value) {
		this.action = action;
		this.value = value;
	}

	public static HoverEvent showText(String value) {
		return new HoverEvent(Action.SHOW_TEXT, value);
	}

	public static HoverEvent showText(Component value) {
		return new HoverEvent(Action.SHOW_TEXT, value);
	}

	public static HoverEvent showAchievement(String value) {
		return new HoverEvent(Action.SHOW_ACHIEVEMENT, value);
	}

	//TODO implement ItemComponent
	//	public static HoverEvent showItem( value) {
	//		return new HoverEvent(Action.SHOW_ITEM, value);
	//	}

	//TODO implement EntityComponent
	//	public static HoverEvent showEntity( value) {
	//		return new HoverEvent(Action.SHOW_ENTITY, value);
	//	}

	public Action getAction() {
		return action;
	}

	public Object getValue() {
		return value;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		HoverEvent that = (HoverEvent) o;
		return action == that.action && value.equals(that.value);
	}

	@Override
	public int hashCode() {
		return Objects.hash(action, value);
	}

	@Override
	public String toString() {
		return "HoverEvent{" + "action=" + action + ", value=" + value + '}';
	}

	public enum Action {
		SHOW_TEXT,
		SHOW_ITEM,
		SHOW_ENTITY,
		SHOW_ACHIEVEMENT;

		private final String actualName;

		Action() {
			this.actualName = this.name().toLowerCase();
		}

		public String getActualName() {
			return actualName;
		}
	}
}
