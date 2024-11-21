package elocindev.prominent.item;

import java.util.List;

import elocindev.prominent.soulbinding.Soulbound;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.world.World;

public class MoltenCore extends Item implements Soulbound {

    public MoltenCore(Settings settings) {
        super(settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.translatable("prominent.moltendesc"));
        tooltip.add(Text.translatable("prominent.moltendesc2"));
        tooltip.add(Text.empty());
        tooltip.add(Text.translatable("prominent.moltendesc3"));
        tooltip.add(Text.empty());
        tooltip.add(Text.translatable("prominent.moltendesc4"));
    }
    
}
