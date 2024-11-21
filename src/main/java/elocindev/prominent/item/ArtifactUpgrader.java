package elocindev.prominent.item;

import java.util.List;

import elocindev.prominent.player.artifact.ArtifactAPI;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class ArtifactUpgrader extends Item {
    private int artifactPower;
    private boolean glint;

    public ArtifactUpgrader(Settings settings, int artifactPower, boolean glint) {
        super(settings.maxCount(1).fireproof());

        this.artifactPower = artifactPower;
        this.glint = glint;
    }

    @Override
    public boolean hasGlint(ItemStack stack) {
        return glint;
    }

    @Override
    public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.translatable("prominent.moltencoredesc"));
        tooltip.add(Text.translatable("prominent.moltencoredesc3").append("§6" + artifactPower + " ").append(Text.translatable("prominent.moltencoredesc4")));
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (world.isClient() || !(user instanceof ServerPlayerEntity player)) return TypedActionResult.pass(user.getStackInHand(hand));

        if (!ArtifactAPI.isArtifactUpgradeable(player)) {
            user.sendMessage(Text.translatable("prominent.alreadymoltencore").setStyle(Style.EMPTY.withColor(Formatting.RED)), true);
            return TypedActionResult.fail(user.getStackInHand(hand));
        }

        user.sendMessage(Text.translatable("prominent.upgmoltencore").append(" " + artifactPower + " ").append(Text.translatable("prominent.upgmoltencore2")).setStyle(Style.EMPTY.withColor(Formatting.GOLD).withItalic(true)), false);
    
        ArtifactAPI.addPoints(player, artifactPower);
        
        user.getStackInHand(hand).setCount(0);
        return TypedActionResult.success(user.getStackInHand(hand));
    }
}
