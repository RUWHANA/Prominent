package elocindev.prominent.item.artifacts;

import java.util.List;
import java.util.UUID;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.spellbladenext.items.Starforge;

import elocindev.necronomicon.api.text.TextAPI;
import elocindev.prominent.player.artifact.ClientArtifactHolder;
import elocindev.prominent.soulbinding.Soulbound;
import elocindev.prominent.text.ICONS;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.ToolMaterial;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import net.spell_engine.api.spell.ExternalSpellSchools;
import net.spell_power.api.SpellSchool;
import net.spell_power.api.SpellSchools;

public class Supernova extends Starforge implements Artifact, Soulbound {
    private Multimap<EntityAttribute, EntityAttributeModifier> attributes;
    
    public Supernova(ToolMaterial material, Settings settings, int damage, float speed) {
        super(material, settings, damage, speed, ExternalSpellSchools.PHYSICAL_MELEE);
    }

    @Override
    public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(EquipmentSlot slot) {
        
        if (slot != EquipmentSlot.MAINHAND) return super.getAttributeModifiers(slot);

        Multimap<EntityAttribute, EntityAttributeModifier> modifiers = HashMultimap.create(super.getAttributeModifiers(slot));
        int i = 0;

        SpellSchool[] attributeList = {
            SpellSchools.FIRE,
            SpellSchools.FROST,
            SpellSchools.ARCANE
        };

        for (var attribute : attributeList) {
            modifiers.put(
                attribute.attribute,
                new EntityAttributeModifier(
                    UUID.fromString("697fe278-8f64-11e4-b9d1-0242ac32074"+i), 
                    attribute.id+" Supernova Modifier", 
                    6.0, 
                    EntityAttributeModifier.Operation.ADDITION
                )
            );
        i++;
        }

        this.attributes = modifiers;
        
        return this.attributes;
    }
    
    @Override
    public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext) {
        Style TEXT = Style.EMPTY.withColor(Formatting.GRAY);
        MutableText ARTIFACT = TextAPI.Styles.getGradient(Text.translatable("prominent.itemcosmic"), 1, getGradient(null)[0], getGradient(null)[1], 2.0F);
        MutableText ARTIFACT_TYPE = ARTIFACT.setStyle(ARTIFACT.getStyle().withUnderline(true));

        tooltip.add(Text.literal(ICONS.MOLTEN_CORE+" ").append(ARTIFACT_TYPE).append(ClientArtifactHolder.getPowerText("supernova")));
        tooltip.add(Text.literal(" "));
        tooltip.add(Text.literal(ICONS.ACTIVE_ABILITY+" ").append(Text.translatable("prominent.astral_astral_burst1").setStyle(Style.EMPTY.withColor(0xb078de))));
        tooltip.add(Text.translatable("prominent.astral_astral_burst2").setStyle(TEXT));
        tooltip.add(Text.translatable("prominent.astral_astral_burst3").setStyle(TEXT));
        tooltip.add(Text.literal(" "));
        tooltip.add(Text.literal(ICONS.ACTIVE_ABILITY+" ").append(Text.translatable("prominent.astral_arcane_projection1").setStyle(Style.EMPTY.withColor(0xb078de))));
        tooltip.add(Text.translatable("prominent.astral_arcane_projection2").setStyle(TEXT));
        tooltip.add(Text.translatable("prominent.astral_arcane_projection3").setStyle(TEXT));
        tooltip.add(Text.literal(" "));
        tooltip.add(Text.literal(ICONS.ACTIVE_ABILITY+" ").append(Text.translatable("prominent.astral_frost_projection1").setStyle(Style.EMPTY.withColor(8167382))));
        tooltip.add(Text.translatable("prominent.astral_frost_projection2").setStyle(TEXT));
        tooltip.add(Text.translatable("prominent.astral_frost_projection3").setStyle(TEXT));
        tooltip.add(Text.literal(" "));
        tooltip.add(Text.literal(ICONS.ACTIVE_ABILITY+" ").append(Text.translatable("prominent.astral_fire_projection1").setStyle(Style.EMPTY.withColor(14903072))));
        tooltip.add(Text.translatable("prominent.astral_fire_projection2").setStyle(TEXT));
        tooltip.add(Text.translatable("prominent.astral_fire_projection3").setStyle(TEXT));
        tooltip.add(Text.literal(" "));
        tooltip.add(Text.literal(ICONS.PASSIVE_ABILITY+" ").append(Text.translatable("prominent.astral_astral_echoing1").setStyle(Style.EMPTY.withColor(0xb078de))));
        tooltip.add(Text.translatable("prominent.astral_astral_echoing2").setStyle(TEXT));
        tooltip.add(Text.literal(" "));
    }

    @Override
    public void onCraft(ItemStack stack, World world, PlayerEntity player) {
        Soulbound.onCraft(stack, world, player);

        super.onCraft(stack, world, player);
    }
    
    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);
    }

    @Override
    public boolean canRepair(ItemStack stack, ItemStack ingredient) {
        if (ingredient.getItem() instanceof Starforge || ingredient.getItem() == Items.NETHER_STAR) return true;
    
        return false;
    }

    @Override
    public int[] getGradient(ItemStack stack) {
        return new int[] {0x7e40ad, 0x335eea};
    }
}