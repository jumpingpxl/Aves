package one.aves.api.connection;

import com.google.common.base.Preconditions;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import one.aves.api.component.Component;
import one.aves.api.component.ComponentParser;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;
import java.util.Objects;
import java.util.UUID;

public class ServerInfo {

	private String versionName;
	private int versionProtocol;

	private int maxPlayers;
	private int onlinePlayers;
	private Component description;
	private String[] playerSample;
	private Favicon favicon;

	public ServerInfo() {
		this.versionName = "Test";
		this.versionProtocol = -1;
		this.maxPlayers = 1337;
		this.onlinePlayers = 69;
		this.description = Component.text("Hello, I am Aves.");
	}

	public String getVersionName() {
		return this.versionName;
	}

	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}

	public int getVersionProtocol() {
		return this.versionProtocol;
	}

	public void setVersionProtocol(int versionProtocol) {
		this.versionProtocol = versionProtocol;
	}

	public int getMaxPlayers() {
		return this.maxPlayers;
	}

	public void setMaxPlayers(int maxPlayers) {
		this.maxPlayers = maxPlayers;
	}

	public int getOnlinePlayers() {
		return this.onlinePlayers;
	}

	public void setOnlinePlayers(int onlinePlayers) {
		this.onlinePlayers = onlinePlayers;
	}

	public Component getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = Component.text(description);
	}

	public void setDescription(Component component) {
		this.description = component;
	}

	public String[] getPlayerSample() {
		return this.playerSample;
	}

	public void setPlayerSample(String[] playerSample) {
		this.playerSample = playerSample;
	}

	public Favicon getFavicon() {
		return this.favicon;
	}

	public void setFavicon(Favicon favicon) {
		this.favicon = favicon;
	}

	public String toJsonString(ProtocolVersion protocolVersion) {
		JsonObject statusObject = new JsonObject();
		JsonObject versionObject = new JsonObject();
		versionObject.addProperty("name", this.versionName);
		versionObject.addProperty("protocol",
				this.versionProtocol == -1 ? protocolVersion.getProtocol() : this.versionProtocol);
		statusObject.add("version", versionObject);
		JsonObject playersObject = new JsonObject();
		playersObject.addProperty("max", this.maxPlayers);
		playersObject.addProperty("online", this.onlinePlayers);
		if (Objects.nonNull(this.playerSample)) {
			JsonArray sampleArray = new JsonArray();
			for (String sample : this.playerSample) {
				JsonObject sampleObject = new JsonObject();
				sampleObject.addProperty("name", sample);
				sampleObject.addProperty("id", UUID.randomUUID().toString());
				sampleArray.add(sampleObject);
			}

			playersObject.add("sample", sampleArray);
		}

		statusObject.add("players", playersObject);
		if (protocolVersion.isAfter(ProtocolVersion.MC_1_8_0)) {
			statusObject.add("description",
					ComponentParser.toJson(this.description, protocolVersion, true));
		} else {
			statusObject.addProperty("description", ComponentParser.toFormattedText(this.description));
		}

		statusObject.addProperty("favicon",
				Objects.isNull(this.favicon) ? "" : this.favicon.getBase64Url());
		return statusObject.toString();
	}

	public static class Favicon {

		private final String base64Url;

		public Favicon(String base64Url) {
			this.base64Url = Preconditions.checkNotNull(base64Url, "base64Url");
		}

		public static Favicon create(Path path) throws IOException {
			try (InputStream stream = Files.newInputStream(path)) {
				BufferedImage image = ImageIO.read(stream);
				if (Objects.isNull(image)) {
					throw new IOException("Unable to read the image.");
				}

				return create(image);
			}
		}

		public static Favicon create(BufferedImage bufferedImage) {
			Preconditions.checkNotNull(bufferedImage, "image");
			Preconditions.checkArgument(bufferedImage.getWidth() == 64 && bufferedImage.getHeight() == 64,
					"Image is not 64x64 (found %sx%s)", bufferedImage.getWidth(), bufferedImage.getHeight());
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			try {
				ImageIO.write(bufferedImage, "PNG", byteArrayOutputStream);
			} catch (IOException e) {
				throw new AssertionError(e);
			}

			String base64 = Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray());
			return new Favicon("data:image/png;base64," + base64);
		}

		public String getBase64Url() {
			return this.base64Url;
		}
	}
}
