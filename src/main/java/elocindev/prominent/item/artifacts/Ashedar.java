package elocindev.prominent.item.artifacts;

import java.util.List;
import java.util.UUID;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import elocindev.necronomicon.api.text.TextAPI;
import elocindev.prominent.ProminentLoader;
import elocindev.prominent.corruption.ICorruptable;
import elocindev.prominent.player.artifact.ClientArtifactHolder;
import elocindev.prominent.registry.EffectRegistry;
import elocindev.prominent.soulbinding.Soulbound;
import elocindev.prominent.text.ICONS;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.spell_power.api.SpellSchools;

public class Ashedar extends SwordItem implements Artifact, Soulbound, ICorruptable, IPartOfSet {
    private Multimap<EntityAttribute, EntityAttributeModifier> attributes;
    private int id;
    private boolean corrupted;

    public static final TagKey<Item> IS_ASHEDAR = TagKey.of(RegistryKeys.ITEM, new Identifier(ProminentLoader.MODID, "ashedar"));
    
    public static boolean isUsingBoth(LivingEntity player) {
        return player.getMainHandStack().isIn(IS_ASHEDAR) && player.getOffHandStack().isIn(IS_ASHEDAR);
    }

    public static boolean isEclipsed(LivingEntity player) {
        return player.hasStatusEffect(EffectRegistry.SOLAR_ECLIPSE) || player.hasStatusEffect(EffectRegistry.LUNAR_ECLIPSE) 
        && isUsingBoth(player);
    }

    public static boolean isAffectedByLunar(LivingEntity player) {
        if (player.hasStatusEffect(EffectRegistry.LUNAR_ECLIPSE) && isUsingBoth(player)) return true;

        return false;
    }

    public static boolean isAffectedBySolar(LivingEntity player) {
        if (player.hasStatusEffect(EffectRegistry.SOLAR_ECLIPSE) && isUsingBoth(player)) return true;

        return false;
    }

    // 0 = Ash
    // 1 = Edar
    public int getType() {
        return this.id;
    }

    public boolean isAsh() {
        return getType() == 0;
    }

    public boolean isEdar() {
        return getType() == 1;
    }

    public Ashedar(ToolMaterial material, Settings settings, int damage, float speed, int id, boolean corrupted) {
        super(material, damage, speed, settings);

        this.id = id;
        this.corrupted = corrupted;
    }

    @Override
    public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(EquipmentSlot slot) {
        
        if (slot != EquipmentSlot.MAINHAND) return super.getAttributeModifiers(slot);

        Multimap<EntityAttribute, EntityAttributeModifier> modifiers = HashMultimap.create(super.getAttributeModifiers(slot));

        if (isAsh())
        modifiers.put(
            SpellSchools.FIRE.attribute,
            new EntityAttributeModifier(
                UUID.fromString("c57ee278-8f64-115a-b9d1-02a2be32023"+getType()), 
                "Ashedar Fire Modifier", 
                7.0, 
                EntityAttributeModifier.Operation.ADDITION
            )
        );

        if (isEdar())
        modifiers.put(
                SpellSchools.ARCANE.attribute,
                new EntityAttributeModifier(
                    UUID.fromString("c57ee278-8f64-115a-b9d1-0242ac32a23"+getType()), 
                    "Ashedar Arcane Modifier", 
                    7.0, 
                    EntityAttributeModifier.Operation.ADDITION
                )
            );

        this.attributes = modifiers;
        
        return this.attributes;
    }
    
    @Override
    public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext) {
        Style TEXT = Style.EMPTY.withColor(Formatting.GRAY);
        MutableText TYPE; if (isAsh()) TYPE = Text.translatable("prominent.ashedarsunfire"); else TYPE = Text.translatable("prominent.ashedararcane");
        MutableText ARTIFACT = TextAPI.Styles.getGradient(TYPE.append(Text.translatable("prominent.itemartifact")), 1, getGradient(itemStack)[0], getGradient(itemStack)[1], 2.0F);

        MutableText ARTIFACT_TYPE = ARTIFACT.setStyle(ARTIFACT.getStyle().withUnderline(true));

        MutableText Ability1; MutableText Ability1Desc; MutableText Ability2; MutableText Ability2Desc; MutableText Ability3; MutableText Ability3Desc;
        MutableText Ability1Passive1; MutableText Ability1Passive2; MutableText Ability2Passive1; MutableText Ability2Passive2;


        if (isAsh()) {
            Ability1 = Text.translatable("prominent.ashedarsunfiredesc1");
            Ability1Desc = Text.translatable("prominent.ashedarsunfiredesc2");
            Ability1Passive1 = Text.translatable("prominent.ashedarsunfiredesc3");
            Ability1Passive2 = Text.translatable("prominent.ashedarsunfiredesc4");

            Ability2 = Text.translatable("prominent.ashedarsunfire2");
            Ability2Desc = Text.translatable("prominent.ashedarsunfire2desc1");
            Ability2Passive1 = Text.translatable("prominent.ashedarsunfire2desc2");
            Ability2Passive2 = Text.translatable("prominent.ashedarsunfire2desc3");

            Ability3 = Text.translatable("prominent.sunfire3");
            Ability3Desc = Text.translatable("prominent.sunfire3desc");
        } else {
            Ability1 = Text.translatable("prominent.lunar1");
            Ability1Desc = Text.translatable("prominent.lunar1desc");
            Ability1Passive1 = Text.translatable("prominent.lunar1passive1");
            Ability1Passive2 = Text.translatable("prominent.lunar1passive2");

            Ability2 = Text.translatable("prominent.lunar2");
            Ability2Desc = Text.translatable("prominent.lunar2desc");
            Ability2Passive1 = Text.translatable("prominent.lunar2passive1");
            Ability2Passive2 = Text.translatable("prominent.lunar2passive2");

            Ability3 = Text.translatable("prominent.lunar3");
            Ability3Desc = Text.translatable("prominent.lunar3desc");
        }

        tooltip.add(Text.literal(ICONS.MOLTEN_CORE+" ").append(ARTIFACT_TYPE).append(ClientArtifactHolder.getPowerText("ashedar_essence")));
        tooltip.add(Text.literal(" "));
        tooltip.add(Text.literal(ICONS.ACTIVE_ABILITY+" ").append(Ability1.setStyle(Style.EMPTY.withColor(getGradient(itemStack)[0]))));
        tooltip.add(Ability1Desc.setStyle(TEXT));
        tooltip.add(Ability1Passive1.setStyle(Style.EMPTY.withColor(Formatting.DARK_GRAY)));
        tooltip.add(Ability1Passive2.setStyle(Style.EMPTY.withColor(Formatting.DARK_GRAY)));

        tooltip.add(Text.literal(" "));

        tooltip.add(Text.literal(ICONS.ACTIVE_ABILITY+" ").append(Ability2.setStyle(Style.EMPTY.withColor(getGradient(itemStack)[0]))));
        tooltip.add(Ability2Desc.setStyle(TEXT));
        tooltip.add(Ability2Passive1.setStyle(Style.EMPTY.withColor(Formatting.DARK_GRAY)));
        tooltip.add(Ability2Passive2.setStyle(Style.EMPTY.withColor(Formatting.DARK_GRAY)));

        tooltip.add(Text.literal(" "));

        tooltip.add(Text.literal(ICONS.ACTIVE_ABILITY+" ").append(Ability3.setStyle(Style.EMPTY.withColor(getGradient(itemStack)[0]))));
        tooltip.add(Ability3Desc.setStyle(TEXT));
        tooltip.add(Text.translatable("prominent.lunarsolar").setStyle(Style.EMPTY.withColor(Formatting.DARK_GRAY)));

        tooltip.add(Text.literal(" "));

        tooltip.add(Text.literal(ICONS.PASSIVE_ABILITY+" ").append(Text.translatable("prominent.astralattunement").setStyle(Style.EMPTY.withColor(getGradient(itemStack)[0]))));
        tooltip.add(Text.translatable("prominent.astralattunement2").setStyle(TEXT));
        tooltip.add(Text.translatable("prominent.astralattunement3").setStyle(Style.EMPTY.withColor(Formatting.DARK_GRAY)));
        tooltip.add(Text.literal(" "));        
    }

    @Override
    public void onCraft(ItemStack stack, World world, PlayerEntity player) {
        Soulbound.onCraft(stack, world, player);

        super.onCraft(stack, world, player);
    }

    @Override
    public boolean canRepair(ItemStack stack, ItemStack ingredient) {
        if (ingredient.getItem() == Items.NETHER_STAR) return true;
    
        return false;
    }

    @Override
    public int[] getGradient(ItemStack stack) {
        if (this.isAsh()) return new int[] { 0xECB464, 0xEACF34 };
        else return new int[] { 0x6A4E9E, 0x8A6E9E };
    }

    @Override
    public boolean isCorrupted() {
        return this.corrupted;
    }

    @Override
    public int corruptionAmount() {
        return 15;
    }

    @Override
    public EquipmentSlot[] getSlots() {
        return new EquipmentSlot[] { EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND };
    }
}