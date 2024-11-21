package elocindev.prominent.item.artifacts;

import java.util.List;
import java.util.UUID;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import elocindev.necronomicon.api.text.TextAPI;
import elocindev.prominent.player.artifact.ClientArtifactHolder;
import elocindev.prominent.registry.AttributeRegistry;
import elocindev.prominent.registry.ProminentMaterials;
import elocindev.prominent.soulbinding.Soulbound;
import elocindev.prominent.text.ICONS;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.spell_engine.particle.Particles;
import net.spell_power.api.SpellSchool;
import net.spell_power.api.SpellSchools;
import net.sweenus.simplyswords.util.HelperMethods;

public class Ekavar extends SwordItem implements Artifact, Soulbound {
    private static int stepMod = 0;    

    public Ekavar(Settings settings) {
        super(ProminentMaterials.ARTIFACT, 2, -2.6f, settings);
    }

    // Phases:
    // 0 = None
    // 1 = Arcane
    // 2 = Fire
    // 3 = Frost
    // 4 = Holy
    // 5 = Astral
    public static int getPhase(ItemStack stack) {
        return stack.getOrCreateNbt().getInt("Phase");
    }

    public static ItemStack setPhase(ItemStack stack, int phase) {
        stack.getOrCreateNbt().putInt("Phase", phase);
        return stack;
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (stepMod > 0) {
            --stepMod;
        }

        if (stepMod <= 0) {
            stepMod = 7;
        }

        if (!(entity instanceof LivingEntity livingEntity)) return;

        EntityAttribute phaseAttribute = AttributeRegistry.EKAVAR_PHASE;
        int currentPhase = getPhase(stack);
        int entityPhase = (int) livingEntity.getAttributeInstance(phaseAttribute).getValue();

        if (entityPhase != currentPhase) {
            Ekavar.setPhase(stack, entityPhase);
        }

        DefaultParticleType particle = null;

        switch (getPhase(stack)) {
            case 1:
                particle = Particles.arcane_spell.particleType;
                break;
            case 2:
                particle = ParticleTypes.FLAME;
                break;
            case 3:
                particle = Particles.snowflake.particleType;
                break;
            case 4:
                particle = Particles.holy_spark.particleType;
                break;
            case 5:
                particle = Particles.arcane_hit.particleType;
                break;
        }

        if (particle != null)
        HelperMethods.createFootfalls(entity, stack, world, stepMod, particle, particle, particle, true);
   }

   @Override
    public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(EquipmentSlot slot) {
        
        if (slot != EquipmentSlot.MAINHAND) return super.getAttributeModifiers(slot);

        Multimap<EntityAttribute, EntityAttributeModifier> modifiers = HashMultimap.create(super.getAttributeModifiers(slot));
        int i = 0;

        SpellSchool[] attributeList = {
            SpellSchools.FIRE,
            SpellSchools.ARCANE,
            SpellSchools.FROST,
            SpellSchools.HEALING
        };

        for (var attribute : attributeList) {
            modifiers.put(
                attribute.attribute,
                new EntityAttributeModifier(
                    UUID.fromString("697ae3c8-8f54-11e4-b9d1-0242e332042"+i), 
                    attribute.id+" Ekavar Modifier", 
                    10.0,
                    EntityAttributeModifier.Operation.ADDITION
                )
            );
        i++;
        }

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
        Style SUBTEXT = Style.EMPTY.withColor(Formatting.DARK_GRAY);
        MutableText ARTIFACT = TextAPI.Styles.getGradient(Text.translatable("prominent.cinderstoneartifact"), 1, getGradient(itemStack)[0], getGradient(itemStack)[1], 2.0F);
        MutableText ARTIFACT_TYPE = ARTIFACT.setStyle(ARTIFACT.getStyle().withUnderline(true));

        tooltip.add(Text.literal(ICONS.MOLTEN_CORE+" ").append(ARTIFACT_TYPE).append(ClientArtifactHolder.getPowerText("ekavar")));
        tooltip.add(Text.literal(" "));

        switch (getPhase(itemStack)) {
            case 0:
            tooltip.add(Text.literal(ICONS.PASSIVE_ABILITY+" ").append(Text.translatable("prominent.cinderstonecore").setStyle(Style.EMPTY.withColor(0x564b63))));
            tooltip.add(Text.translatable("prominent.ekabarpassive1").setStyle(TEXT));
            tooltip.add(Text.translatable("prominent.ekabarpassive2").setStyle(TEXT));
            break;

            case 1:
            tooltip.add(Text.literal(ICONS.PASSIVE_ABILITY+" ").append(Text.translatable("prominent.arcaneinfusion").setStyle(Style.EMPTY.withColor(0xd147ff))));
            tooltip.add(Text.translatable("prominent.arcaneinfusion2").setStyle(TEXT));
            break;

            case 2:
            tooltip.add(Text.literal(ICONS.PASSIVE_ABILITY+" ").append(Text.translatable("prominent.fireinfusion").setStyle(Style.EMPTY.withColor(0xff6a00))));
            tooltip.add(Text.translatable("prominent.fireinfusion2").setStyle(TEXT));
            break;

            case 3:
            tooltip.add(Text.literal(ICONS.PASSIVE_ABILITY+" ").append(Text.translatable("prominent.frostinfusion").setStyle(Style.EMPTY.withColor(0x00d1ff))));
            tooltip.add(Text.translatable("prominent.frostinfusion2").setStyle(TEXT));
            break;

            case 4:
            tooltip.add(Text.literal(ICONS.PASSIVE_ABILITY+" ").append(Text.translatable("prominent.holyinfusion").setStyle(Style.EMPTY.withColor(0xffd100))));
            tooltip.add(Text.translatable("prominent.holyinfusion2").setStyle(TEXT));
            break;

            case 5:
            tooltip.add(Text.literal(ICONS.PASSIVE_ABILITY+" ").append(Text.translatable("prominent.astralinfusion").setStyle(Style.EMPTY.withColor(0xe76eff))));
            tooltip.add(Text.translatable("prominent.astralinfusion2").setStyle(TEXT));
            break;
        }
        tooltip.add(Text.literal(" "));
        tooltip.add(Text.translatable("prominent.spellbook").setStyle(TEXT));
        tooltip.add(Text.literal(" "));

        switch(getPhase(itemStack)) {
            case 1:
            tooltip.add(Text.translatable("prominent.ekabararcane"));
            break;
            case 2:
            tooltip.add(Text.translatable("prominent.ekabarfire"));
            break;
            case 3:
            tooltip.add(Text.translatable("prominent.ekabarfrost"));
            break;
            case 4:
            tooltip.add(Text.translatable("prominent.ekabarholy"));
            break;
            case 5:
            tooltip.add(Text.translatable("prominent.ekabarastral"));
            break;
        }
   }

    @Override
    public int[] getGradient(ItemStack stack) {
        switch(getPhase(stack)) {
            case 1:
            return new int[] {0xd147ff, 0x634b5e};
            case 2:
            return new int[] {0xff6a00, 0x634b5e};
            case 3:
            return new int[] {0x00d1ff, 0x634b5e};
            case 4:
            return new int[] {0xfcba03, 0xfce303};
            case 5:
            return new int[] {0xc603fc, 0x8003fc};
        }

        return new int[] {0x564b63, 0x634b5e};
    }

    @Override
    public Text getName(ItemStack stack) {
        if (getPhase(stack) > 0) {
            return Text.translatable(this.getTranslationKey(stack)+".phase."+getPhase(stack));
        }

        return Text.translatable(this.getTranslationKey(stack));
    }

    public TypedActionResult<ItemStack> debug_use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        int phase = getPhase(stack);
        
        phase = (phase + 1) > 5 ? 0 : phase + 1;
        setPhase(stack, phase);
    
        return super.use(world, user, hand);
    }
}
