package elocindev.prominent.fabric_quilt;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import elocindev.prominent.fabric_quilt.callbacks.CombatCallback;
import elocindev.prominent.fabric_quilt.config.ServerConfig;
import elocindev.prominent.fabric_quilt.config.ServerEntries;
import elocindev.prominent.fabric_quilt.registry.ItemRegistry;
import elocindev.prominent.fabric_quilt.registry.SoundRegistry;

public class ProminentLoader implements ModInitializer {
	public static final String MODID = "prominent";
	public static final Logger LOGGER = LoggerFactory.getLogger(MODID);
	
	public static ServerEntries Config = ServerConfig.loadConfig();

	public static final ItemGroup ProminentTab = FabricItemGroupBuilder.create(
		new Identifier(MODID, "tab"))
		.icon(() -> new ItemStack(ItemRegistry.ICON))
		.appendItems(stacks -> { stacks.add(new ItemStack(ItemRegistry.MAINMENU_DISC));})
		.build();

	@Override
	public void onInitialize() {
		ServerLifecycleEvents.END_DATA_PACK_RELOAD.register((server, resourceManager, success)
		-> Config = ServerConfig.loadConfig());
		LOGGER.info("Loaded Prominent Config");

		SoundRegistry.registerSounds();
		ItemRegistry.registerItems();

		CombatCallback.register();
	}
}
