package elocindev.prominent.item.artifacts;

import java.util.List;
import java.util.UUID;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import elocindev.necronomicon.api.text.TextAPI;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import elocindev.prominent.player.artifact.ClientArtifactHolder;
import elocindev.prominent.soulbinding.Soulbound;
import elocindev.prominent.text.ICONS;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.Registries;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.spell_power.api.SpellSchool;
import net.spell_power.api.SpellSchools;
import net.sweenus.simplyswords.api.SimplySwordsAPI;
import net.sweenus.simplyswords.util.HelperMethods;

public class Fyralath extends SwordItem implements Artifact, Soulbound {
    private static int stepMod = 0;
    

    public Fyralath(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, 
        (int)9, -3.0F,
        settings);
    }
    
    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (stepMod > 0) {
            --stepMod;
        }
   
        if (stepMod <= 0) {
            stepMod = 7;
        }        

        HelperMethods.createFootfalls(entity, stack, world, stepMod, ParticleTypes.FLAME, ParticleTypes.FLAME, ParticleTypes.FLAME, true);
        SimplySwordsAPI.inventoryTickGemSocketLogic(stack, world, entity, 100, 100);
   }

   @Override
    public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(EquipmentSlot slot) {
        
        if (slot != EquipmentSlot.MAINHAND) return super.getAttributeModifiers(slot);

        Multimap<EntityAttribute, EntityAttributeModifier> modifiers = HashMultimap.create(super.getAttributeModifiers(slot));
        int i = 0;

        SpellSchool[] attributeList = {
            SpellSchools.FIRE,
            SpellSchools.SOUL
        };

        for (var attribute : attributeList) {
            modifiers.put(
                attribute.attribute,
                new EntityAttributeModifier(
                    UUID.fromString("697ae3c8-df64-11e4-b9d1-0242e332074"+i), 
                    attribute.id+" Fyr'alath Modifier", 
                    4,
                    EntityAttributeModifier.Operation.ADDITION
                )
            );
        i++;
        }

        modifiers.put(
                Registries.ATTRIBUTE.get(new Identifier("extraspellattributes:reabsorption")),
                new EntityAttributeModifier(
                    UUID.fromString("697ae3c8-df64-11e4-b9d1-0242e332074"+i), 
                    "Reabsorption Fyr'alath Modifier", 
                    10,
                    EntityAttributeModifier.Operation.ADDITION
                )
            );

        return modifiers;
    }

    @Override
    public void onCraft(ItemStack stack, World world, PlayerEntity player) {
        Soulbound.onCraft(stack, world, player);

        super.onCraft(stack, world, player);
    }

    @Override
    public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext) {
        Style TEXT = Style.EMPTY.withColor(Formatting.GRAY);
        MutableText ARTIFACT = TextAPI.Styles.getGradient(Text.translatable("prominent.itemsoulfire"), 1, getGradient(null)[0], getGradient(null)[1], 2.0F);
        MutableText ARTIFACT_TYPE = ARTIFACT.setStyle(ARTIFACT.getStyle().withUnderline(true));

        tooltip.add(Text.literal(ICONS.MOLTEN_CORE+" ").append(ARTIFACT_TYPE).append(ClientArtifactHolder.getPowerText("fyr'alath")));
        tooltip.add(Text.literal(" "));
        tooltip.add(Text.literal(ICONS.ACTIVE_ABILITY+" ").append(Text.translatable("prominent.fyralath_greater_scorch1").setStyle(Style.EMPTY.withColor(0xe6a667))));
        tooltip.add(Text.translatable("prominent.fyralath_greater_scorch2").setStyle(TEXT));
        tooltip.add(Text.translatable("prominent.fyralath_greater_scorch3").setStyle(TEXT));
        tooltip.add(Text.literal(" "));
        tooltip.add(Text.literal(ICONS.ACTIVE_ABILITY+" ").append(Text.translatable("prominent.fyralath_greater_laceration1").setStyle(Style.EMPTY.withColor(0xe6a667))));
        tooltip.add(Text.translatable("prominent.fyralath_greater_laceration2").setStyle(TEXT));
        tooltip.add(Text.translatable("prominent.fyralath_greater_laceration3").setStyle(TEXT));        
        tooltip.add(Text.literal(" "));
        tooltip.add(Text.literal(ICONS.ACTIVE_ABILITY+" ").append(Text.translatable("prominent.fyralath_greater_meteor1").setStyle(Style.EMPTY.withColor(0xe6a667))));
        tooltip.add(Text.translatable("prominent.fyralath_greater_meteor2").setStyle(TEXT));
        tooltip.add(Text.translatable("prominent.fyralath_greater_meteor3").setStyle(TEXT));        
        tooltip.add(Text.literal(" "));
        tooltip.add(Text.literal(ICONS.PASSIVE_ABILITY+" ").append(Text.translatable("prominent.fyralath_fiery_core1").setStyle(Style.EMPTY.withColor(0xe6a667))));
        tooltip.add(Text.translatable("prominent.fyralath_fiery_core2").setStyle(TEXT));
        tooltip.add(Text.translatable("prominent.fyralath_fiery_core3").setStyle(TEXT));
        tooltip.add(Text.literal(" "));
   }

    @Override
    public int[] getGradient(ItemStack stack) {
        return new int[] {0x953d2e, 0x7e40ad};
    }
}