package elocindev.prominent.item;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.puffish.skillsmod.SkillsMod;

public class ScrollItem extends Item {
    public ScrollItem(Settings settings) {
        super(settings);
    }
    
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (world.isClient()) return TypedActionResult.pass(user.getStackInHand(hand));

        if (user instanceof ServerPlayerEntity player) {
            SkillsMod.getInstance().resetSkills(player, new Identifier("puffish_skills", "prom"));
            
            user.getStackInHand(hand).setCount(0);
            user.sendMessage(Text.translatable("prominent.knowlegemessage"));
        }

        return TypedActionResult.success(user.getStackInHand(hand));
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.translatable("prominent.knowledesc").setStyle(Style.EMPTY.withColor(Formatting.GRAY)));
    }
}