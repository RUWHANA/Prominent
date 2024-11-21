package elocindev.prominent.registry;

import elocindev.prominent.ProminentLoader;
import elocindev.prominent.item.ArtifactUpgrader;
import elocindev.prominent.item.BeaconOfHope;
import elocindev.prominent.item.DescriptiveItem;
import elocindev.prominent.item.MainMenuDisc;
import elocindev.prominent.item.MoltenCore;
import elocindev.prominent.item.artifacts.Ashedar;
import elocindev.prominent.item.artifacts.AshedarSpawner;
import elocindev.prominent.item.artifacts.Azhar;
import elocindev.prominent.item.artifacts.Ekavar;
import elocindev.prominent.item.artifacts.Frostmourne;
import elocindev.prominent.item.artifacts.Fyralath;
import elocindev.prominent.item.artifacts.Supernova;
import elocindev.prominent.item.artifacts.ThousandFists;
import elocindev.prominent.item.artifacts.Thunderwrath;
import elocindev.prominent.item.mythic_bosses.MythicSummoner;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory; 

public class ItemRegistry {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProminentLoader.MODID);

    public static final TagKey<Item> RIDER_WEAPONS = TagKey.of(RegistryKeys.ITEM, new Identifier(ProminentLoader.MODID, "rider_weapons"));
    
    private static Item.Settings discSettings = new Item.Settings().rarity(Rarity.RARE).maxCount(1);
    public static final Item MAINMENU_DISC = reg(new MainMenuDisc(14, SoundRegistry.CONIFERUS_FOREST, discSettings, 128), "music_disc_coniferus_forest");
    
    public static final Item FESTIVITIES_DISC = reg(new MainMenuDisc(14, SoundRegistry.FESTIVITIES, discSettings, 208), "music_disc_festivities");

    public static final Item MOLTEN_CORE = reg(new MoltenCore(new Item.Settings().rarity(Rarity.UNCOMMON)), "molten_core");

    public static final Item ICON = reg(new Item(new Item.Settings()), "icon");

    // NORMAL ARTIFACTS

    public static final Item THUNDERWRATH = 
        reg(new Thunderwrath(ProminentMaterials.ARTIFACT, new Item.Settings().fireproof()), "thunderwrath");
    
    public static final Item SUPERNOVA = 
        reg(new Supernova(ProminentMaterials.ARTIFACT, new Item.Settings().fireproof(), 9, -2.6f), "supernova");

    public static final Item FURY_OF_A_THOUSAND_FISTS =
        reg(new ThousandFists(ProminentMaterials.ARTIFACT, 4, -1.5f, new Item.Settings().fireproof().maxCount(1)), "fury_of_a_thousand_fists");

    public static final Item FROSTMOURNE = 
        reg(new Frostmourne(ProminentMaterials.ARTIFACT, new Item.Settings().fireproof(), 10, -2.6f), "frostmourne");
    
    public static final Item AZHAR = 
        reg(new Azhar(ProminentMaterials.ARTIFACT, new Item.Settings().fireproof()), "azhar");

    public static final Item FYRALATH = 
        reg(new Fyralath(ProminentMaterials.ARTIFACT, new Item.Settings().fireproof()), "fyralath");

    public static final Item ASH =
        reg(new Ashedar(ProminentMaterials.ARTIFACT, new Item.Settings().fireproof(), 9, -2.30f, 0, false), "ash");
    
    public static final Item EDAR =
        reg(new Ashedar(ProminentMaterials.ARTIFACT, new Item.Settings().fireproof(), 6, -2.10f, 1, false), "edar");

    public static final Item EKAVAR =
        reg(new Ekavar(new Item.Settings().fireproof()), "ekavar_staff");
    
    public static final Item ASHEDAR_ESSENCE = 
        reg(new AshedarSpawner(new Item.Settings(), false), "ashedar_essence");


    static List<Text> demon_essence_desc = List.of(
        Text.translatable("item.prominent.demonessencedescription").setStyle(Style.EMPTY.withColor(Formatting.GRAY))
    );

    static List<Text> glory_essence_desc = List.of(
        Text.translatable("item.prominent.gloryessencedescription").setStyle(Style.EMPTY.withColor(Formatting.GRAY))
    );

    static List<Text> wrath_essence_desc = List.of(
        Text.translatable("item.prominent.wrathessencedescription").setStyle(Style.EMPTY.withColor(Formatting.GRAY))
    );

    static List<Text> lesser_mythical_essence_desc = List.of(
        Text.translatable("prominent.10mythicdesc").setStyle(Style.EMPTY.withColor(Formatting.GRAY))
    );

    static List<Text> greater_mythical_essence_desc = List.of(
        Text.translatable("prominent.20mythicdesc").setStyle(Style.EMPTY.withColor(Formatting.GRAY))
    );

    static List<Text> encased_remnant_desc = List.of(
        Text.translatable("item.prominent.encasedremnantdescription").setStyle(Style.EMPTY.withColor(Formatting.GRAY))
    );

    // essences
    public static final Item DEMON_ESSENCE = reg(new DescriptiveItem(new Item.Settings().rarity(Rarity.RARE).fireproof().maxCount(1), demon_essence_desc), "demon_essence");
    public static final Item GLORY_ESSENCE = reg(new DescriptiveItem(new Item.Settings().rarity(Rarity.RARE).fireproof().maxCount(1), glory_essence_desc), "glory_essence");
    public static final Item WRATH_ESSENCE = reg(new DescriptiveItem(new Item.Settings().rarity(Rarity.RARE).fireproof().maxCount(1), wrath_essence_desc), "wrath_essence");
        
    // misc
    public static final Item ENCASED_REMNANT = reg(new DescriptiveItem(new Item.Settings().rarity(Rarity.EPIC).fireproof().maxCount(16), encased_remnant_desc ), "encased_remnant");
    
    public static final Item MOLTEN_REMNANT = reg(new ArtifactUpgrader(new Item.Settings().rarity(Rarity.UNCOMMON), 50, false), "molten_remnant");
    public static final Item INFUSED_MOLTEN_REMNANT = reg(new ArtifactUpgrader(new Item.Settings().rarity(Rarity.UNCOMMON), 100, true), "infused_molten_remnant");

    
    public static final Item RESPEC_SCROLL = reg(new elocindev.prominent.item.ScrollItem(new Item.Settings().rarity(Rarity.UNCOMMON).maxCount(1)), "knowledge_scroll");

    static List<Text> cinderstone_trophy_desc = List.of(
        Text.translatable("item.prominent.cinderstonetrophydesc1").setStyle(Style.EMPTY.withColor(Formatting.GRAY)),
        Text.literal(" "),
        Text.translatable("item.prominent.cinderstonetrophydesc2").setStyle(Style.EMPTY.withColor(Formatting.YELLOW)),
        Text.translatable("item.prominent.cinderstonetrophydesc3").setStyle(Style.EMPTY.withColor(Formatting.YELLOW))
    );
    public static final Item CINDERSTONE_TROPHY = reg(new DescriptiveItem(new Item.Settings().rarity(Rarity.UNCOMMON).fireproof().maxCount(1), cinderstone_trophy_desc), "cinderstone_trophy");

    // mythic stuff
    public static final Item LESSER_MYTHICAL_ESSENCE = reg(new DescriptiveItem(new Item.Settings().rarity(Rarity.EPIC).fireproof().maxCount(64), lesser_mythical_essence_desc), "lesser_mythical_essence");
    public static final Item GREATER_MYTHICAL_ESSENCE = reg(new DescriptiveItem(new Item.Settings().rarity(Rarity.EPIC).fireproof().maxCount(16), greater_mythical_essence_desc), "greater_mythical_essence");
    
    public static final Item EMPTY_VESSEL = reg(new Item(new Item.Settings().fireproof().maxCount(1)), "empty_vessel");
    public static final Item VOID_HOURGLASS = reg(new MythicSummoner(new Item.Settings().maxCount(1).fireproof()), "void_hourglass");

    public static final Item BEACON_OF_HOPE = reg(new BeaconOfHope(new Item.Settings().rarity(Rarity.UNCOMMON).maxCount(1)), "beacon_of_hope");
    public static final Item BROKEN_BEACON_OF_HOPE = reg(new Item(new Item.Settings().rarity(Rarity.COMMON).maxCount(1)), "broken_beacon_of_hope");

    public static void registerItems() { 
        LOGGER.info("Registered Prominent Items");
    }

    public static Item reg(Item instance, String id) {
        return Registry.register(Registries.ITEM, new Identifier(ProminentLoader.MODID, id), instance);
    }

    private static List<Text> getDescription(String text) {
        return List.of(Text.literal(text).setStyle(Style.EMPTY.withColor(Formatting.GRAY)));
    }
}
