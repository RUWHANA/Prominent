package elocindev.prominent.item.artifacts;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import elocindev.necronomicon.api.text.TextAPI;
import elocindev.prominent.corruption.ICorruptable;
import elocindev.prominent.registry.ItemRegistry;
import elocindev.prominent.soulbinding.Soulbound;
import elocindev.prominent.text.ICONS;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class AshedarSpawner extends Item implements Artifact, Soulbound, ICorruptable {

    private boolean corrupted;

    public AshedarSpawner(Settings settings, boolean corrupted) {
        super(settings
            .maxCount(1)
            .fireproof()
        );

        this.corrupted = corrupted;
    }
    
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (world.isClient()) return TypedActionResult.pass(user.getStackInHand(hand));

        if (!Soulbound.isSoulbindedTo(user.getStackInHand(hand), user))
            return TypedActionResult.fail(user.getStackInHand(hand));

        if (!(hand == Hand.MAIN_HAND) || !user.getStackInHand(Hand.OFF_HAND).isEmpty()) {
            user.sendMessage(Text.translatable("prominent.arcanesunfireloot").setStyle(Style.EMPTY.withColor(Formatting.RED)));
            
            return TypedActionResult.fail(user.getStackInHand(hand));
        }

        ItemStack ASH; ItemStack EDAR;

        if (!isCorrupted()) {
            ASH = new ItemStack(ItemRegistry.ASH); EDAR = new ItemStack(ItemRegistry.EDAR);
        } else {
            ASH = new ItemStack(ItemRegistry.ASH); EDAR = new ItemStack(ItemRegistry.EDAR); //TODO: REPLACE THIS
        }

        user.setStackInHand(Hand.MAIN_HAND, ASH);
        user.setStackInHand(Hand.OFF_HAND, EDAR);

        Soulbound.soulbind(ASH, user);
        Soulbound.soulbind(EDAR, user);

        MutableText name = TextAPI.Styles.getStaticGradient(Text.translatable("item.prominent.ashedar"), getGradient(null)[0], getGradient(null)[1]);
        user.sendMessage(name.append(Text.translatable("prominent.itemsoulbound").setStyle(Style.EMPTY.withColor(Formatting.GOLD))), false);
        
        return TypedActionResult.success(user.getStackInHand(hand));
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        MutableText ARTIFACT = TextAPI.Styles.getGradient(Text.translatable("prominent.arcanesunfire"), 1, getGradient(null)[0], getGradient(null)[1], 2.0F);
        MutableText ARTIFACT_TYPE = ARTIFACT.setStyle(ARTIFACT.getStyle().withUnderline(true));

        tooltip.add(Text.literal(ICONS.MOLTEN_CORE+" ").append(ARTIFACT_TYPE));
        tooltip.add(Text.literal(" "));
        
        tooltip.add(Text.literal(ICONS.ACTIVE_ABILITY+" ").append(Text.translatable("prominent.arcanesunfireloot2").setStyle(Style.EMPTY.withColor(Formatting.GRAY))));
    }

    @Override
    public void onCraft(ItemStack stack, World world, PlayerEntity player) {
        Soulbound.onCraft(stack, world, player);

        super.onCraft(stack, world, player);
    }

    @Override
    public int[] getGradient(ItemStack stack) {
        return new int[] { 0xECB464, 0x8A6E9E };
    }

    
    @Override
    public boolean isCorrupted() {
        return this.corrupted;
    }

    @Override
    public int corruptionAmount() {
        return 0;
    }

    @Override
    public EquipmentSlot[] getSlots() {
        return null;
    }
}
