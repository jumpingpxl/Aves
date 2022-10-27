package one.aves.api.connection;

import com.google.common.base.Preconditions;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

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

public class ServerPing {

	private final String versionName;
	private final int versionProtocol;

	private final int maxPlayers;
	private final int onlinePlayers;
	private final String description;
	private String[] playerSample;
	private Favicon favicon;

	public ServerPing() {
		versionName = "Test";
		versionProtocol = -1;
		maxPlayers = 1337;
		onlinePlayers = 69;
		description = "Hello, I am Aves.";
	}

	public String toJsonString(ProtocolVersion protocolVersion) {
		JsonObject statusObject = new JsonObject();
		JsonObject versionObject = new JsonObject();
		versionObject.addProperty("name", versionName);
		versionObject.addProperty("protocol",
				versionProtocol == -1 ? protocolVersion.getProtocol() : versionProtocol);
		statusObject.add("version", versionObject);
		JsonObject playersObject = new JsonObject();
		playersObject.addProperty("max", maxPlayers);
		playersObject.addProperty("online", onlinePlayers);
		if (Objects.nonNull(playerSample)) {
			JsonArray sampleArray = new JsonArray();
			for (String sample : playerSample) {
				JsonObject sampleObject = new JsonObject();
				sampleObject.addProperty("name", sample);
				sampleObject.addProperty("id", UUID.randomUUID().toString());
				sampleArray.add(sampleObject);
			}

			playersObject.add("sample", sampleArray);
		}

		statusObject.add("players", playersObject);
		if (protocolVersion.getProtocol() >= ProtocolVersion.MC_1_16_0.getProtocol()) {
			JsonObject descriptionObject = new JsonObject();
			descriptionObject.addProperty("text", description);
			statusObject.add("description", descriptionObject);
		} else {
			statusObject.addProperty("description", description);
		}

		statusObject.addProperty("favicon", Objects.isNull(favicon) ? "" : favicon.getBase64Url());
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
			return base64Url;
		}
	}
}
