package elocindev.prominent.mixin;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import elocindev.necronomicon.api.text.TextAPI;
import elocindev.prominent.item.artifacts.Artifact;
import elocindev.prominent.item.artifacts.IMoltenItem;

@Mixin(ItemStack.class)
public abstract class CustomItemNameMixin {
    @Inject(method="getName", at = @At(value = "HEAD"), cancellable = true)
    private void prominent$getName(CallbackInfoReturnable<Text> cir) {
        ItemStack stack = (ItemStack)(Object)this;

        var name = Text.empty().append(stack.getItem().getName(stack));
        var bold = name.setStyle(name.getStyle().withBold(true));
        MutableText gradient = Text.empty();
        Object item = (Object)stack.getItem();

        NbtCompound nbtCompound = stack.getSubNbt("display");

        if (item instanceof Artifact artifact)
            gradient = TextAPI.Styles.getGradient(bold, 1, artifact.getGradient(stack)[0], artifact.getGradient(stack)[1], 1.0F);
        else if (item instanceof IMoltenItem)
            gradient = TextAPI.Styles.getGradient(name.setStyle(name.getStyle().withBold(false)), 1, 0xd1611f, 0xd1802a, 1.0F);
        else return;
        

        if (nbtCompound != null && nbtCompound.contains("Name", 8)) {
            try {
                Text text = Text.Serializer.fromJson(nbtCompound.getString("Name"));

                if (text != null) {
                    cir.setReturnValue(gradient.setStyle(gradient.getStyle().withBold(true)));
                    return;
                }

                nbtCompound.remove("Name");
            } catch (Exception e) {
                nbtCompound.remove("Name");
            }
        }

        cir.setReturnValue(gradient);
    }
}