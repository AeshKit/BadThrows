package aesh.kai;

import aesh.kai.config.Configs;
import net.fabricmc.api.ModInitializer;

import net.minecraft.resources.Identifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BadThrows implements ModInitializer {
	public static final String MOD_ID = "bad-throws";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		Configs.init();
		LOGGER.info("Is dat lebrawnn jaems?");
	}

	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}
}
