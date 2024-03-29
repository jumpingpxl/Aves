package one.aves.api.component;

import one.aves.api.util.StringHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Component {

	private final String text;
	private Component parent;
	private Font font;
	private ClickEvent clickEvent;
	private HoverEvent hoverEvent;
	private TextColor color = TextColor.WHITE;
	private List<TextDecoration> decorations;
	private List<Component> children;

	private Component(String text) {
		this.text = text;
	}

	public static Component text(String text) {
		return new Component(text);
	}

	public static Component empty() {
		return new Component("");
	}

	public @Nonnull Component font(Font font) {
		if (font == Font.DEFAULT) {
			this.font = null;
			return this;
		}

		this.font = font;
		return this;
	}

	public @Nonnull Component clickEvent(ClickEvent clickEvent) {
		this.clickEvent = clickEvent;
		return this;
	}

	public @Nonnull Component hoverEvent(HoverEvent hoverEvent) {
		this.hoverEvent = hoverEvent;
		return this;
	}

	public @Nonnull Component color(TextColor color) {
		if (color == null) {
			this.color = TextColor.WHITE;
			return this;
		}

		this.color = color;
		return this;
	}

	public @Nonnull Component addDecorations(TextDecoration... decorations) {
		if (this.decorations == null) {
			this.decorations = new ArrayList<>();
		}

		Collections.addAll(this.decorations, decorations);

		return this;
	}

	public @Nonnull Component removeDecorations(TextDecoration... decorations) {
		if (this.decorations == null) {
			return this;
		}

		for (TextDecoration decoration : decorations) {
			this.decorations.remove(decoration);
		}

		return this;
	}

	public @Nonnull Component append(String text) {
		return this.append(Component.text(text));
	}

	public @Nonnull Component append(Component child) {
		Objects.requireNonNull(child, "Child to append cannot be null");
		if (this.children == null) {
			this.children = new ArrayList<>();
		}

		child.parent = this;
		this.children.add(child);
		return child;
	}

	public @Nullable Component getParent() {
		return this.parent;
	}

	public @Nonnull Component root() {
		if (this.parent == null) {
			return this;
		}

		return this.parent.root();
	}

	public String getText() {
		return this.text;
	}

	public Font getFont() {
		return this.font;
	}

	public ClickEvent getClickEvent() {
		return this.clickEvent;
	}

	public HoverEvent getHoverEvent() {
		return this.hoverEvent;
	}

	public TextColor getColor() {
		return this.color;
	}

	public List<TextDecoration> getDecorations() {
		if (this.decorations == null) {
			return null;
		}

		return Collections.unmodifiableList(this.decorations);
	}

	public List<Component> getChildren() {
		if (this.children == null) {
			return null;
		}

		return Collections.unmodifiableList(this.children);
	}

	public Component copy() {
		Component component = new Component(this.text);
		component.font = this.font;
		component.clickEvent = this.clickEvent;
		component.hoverEvent = this.hoverEvent;
		component.color = this.color;
		component.decorations = this.decorations;
		if (this.children != null) {
			component.children = new ArrayList<>(this.children.size());
			for (Component child : this.children) {
				Component copy = child.copy();
				copy.parent = component;
				component.children.add(copy);
			}
		}

		return component;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || this.getClass() != o.getClass()) {
			return false;
		}
		Component component = (Component) o;
		return this.text.equals(component.text) && Objects.equals(this.font, component.font)
				&& Objects.equals(this.clickEvent, component.clickEvent) && Objects.equals(this.hoverEvent,
				component.hoverEvent) && Objects.equals(this.color, component.color) && Objects.equals(
				this.decorations, component.decorations) && Objects.equals(this.children,
				component.children);
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.text, this.font, this.clickEvent, this.hoverEvent, this.color,
				this.decorations, this.children);
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("Component{");
		sb.append("text='").append(this.text).append('\'');
		if (this.font != null) {
			sb.append(", font=").append(this.font);
		}

		if (this.clickEvent != null) {
			sb.append(", clickEvent=").append(this.clickEvent);
		}

		if (this.hoverEvent != null) {
			sb.append(", hoverEvent=").append(this.hoverEvent);
		}

		if (this.color != null) {
			sb.append(", color=").append(this.color);
		}

		if (this.decorations != null && !this.decorations.isEmpty()) {
			sb.append(", decorations={");
			sb.append(StringHelper.join(this.decorations, ", "));
			sb.append("}");
		}

		if (this.children != null && !this.children.isEmpty()) {
			sb.append(", children={");
			sb.append(StringHelper.join(this.children, ", "));
			sb.append("}");
		}

		sb.append('}');
		return sb.toString();
	}
}
