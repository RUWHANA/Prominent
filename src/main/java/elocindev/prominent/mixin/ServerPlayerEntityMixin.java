package elocindev.prominent.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.glisco.things.items.ThingsItems;

import elocindev.prominent.item.artifacts.Artifact;
import elocindev.prominent.item.artifacts.IPartOfSet;
import elocindev.prominent.mythicbosses.MythicBosses;
import elocindev.prominent.soulbinding.Soulbound;
import io.wispforest.accessories.api.slot.SlotEntryReference;
import io.wispforest.accessories.pond.AccessoriesAPIAccess;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Entity.RemovalReason;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
// import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin implements AccessoriesAPIAccess {
    @Inject(method = "onDeath", at = @At("HEAD"))
    public void prominent$removeMythicBosses(DamageSource damageSource, CallbackInfo ci) {
        ServerPlayerEntity player = (ServerPlayerEntity) (Object) this;
        if (!(player.getWorld() instanceof ServerWorld)) return;

        if (damageSource.getSource() instanceof LivingEntity possibleBoss) {
            if (MythicBosses.isMythicBoss(possibleBoss)) {

                if (MythicBosses.isOwnedByBoss(possibleBoss)) {
                    LivingEntity owner = MythicBosses.getOwnerOf(possibleBoss);
                    
                    if (owner != null) {
                        owner.remove(RemovalReason.DISCARDED);
                    }
                }

                possibleBoss.remove(RemovalReason.DISCARDED);

                player.sendMessage(Text.translatable("prominent.whisperface").setStyle(Style.EMPTY.withColor(0xb86bbf).withItalic(true)));
                
                int finalLevel = MythicBosses.getMythicLevel(possibleBoss) - 1;
                if (finalLevel < 1) finalLevel = 1;

                ItemEntity item = new ItemEntity(player.getWorld(), player.getX(), player.getY() + 1, player.getZ(), MythicBosses.getVoidHourglass(finalLevel));
                possibleBoss.getWorld().spawnEntity(item);
            }
        }
    }

    @Inject(method = "tick", at = @At("TAIL"))
    public void prominent$tickSoulboundDamage(CallbackInfo ci) {
        ServerPlayerEntity player = (ServerPlayerEntity) (Object) this;
        if (!(player.getWorld() instanceof ServerWorld)) return;
        World world = player.getWorld();
    
        int ownedArtifactCount = 0;
        boolean hasDualArtifacts = false;

        ArrayList<ItemStack> inventory = new ArrayList<>();
        for (int i = 0; i < player.getInventory().size(); i++) {
            inventory.add(player.getInventory().getStack(i));
        }

        // if (!player.getOffHandStack().isEmpty())
        //     inventory.add(player.getOffHandStack());
    
        // TODO: Implement this directly to item obliterator later
        if (!world.isClient() && world.getTime() % 20 == 0) {
            List<SlotEntryReference> equippedStacks = this.accessoriesCapability().getEquipped(stack -> (stack.getItem().equals(ThingsItems.BROKEN_WATCH) || Registries.ITEM.getId(stack.getItem()).toString().equals("artifacts:feral_claws")));
        
            for (SlotEntryReference equippedStack : equippedStacks) {
                if (equippedStack != null) equippedStack.stack().setCount(0);
            }
            

            for (ItemStack stack : inventory) {
                if (!(stack.getItem() instanceof Soulbound))
                    continue;
    
                if (Soulbound.isSoulbinded(stack)) {
                    if (player.isCreative()) continue;
    
                    if (!Soulbound.isSoulbindedTo(stack, player)) {
                        player.sendMessage(Text.empty().append(stack.getName()).append(Text.translatable("prominent.otheritemsoulbound").setStyle(Style.EMPTY.withColor(Formatting.RED))), true);
    
                        player.damage(player.getDamageSources().genericKill(), player.getMaxHealth() * 0.40f);
                    } else if (stack.getItem() instanceof Artifact) {
                        if (stack.getItem() instanceof IPartOfSet) hasDualArtifacts = true;
    
                        ownedArtifactCount++;
                    }
                } else if (!world.isClient() && !Soulbound.isSoulbinded(stack)) {
                    Soulbound.soulbind(stack, player);
                    player.sendMessage(Text.empty().append(stack.getName()).append(Text.translatable("prominent.itemsoulbound").setStyle(Style.EMPTY.withColor(Formatting.GOLD))), false);
                }
            }
    
            if (ownedArtifactCount > 1 && !player.isCreative()) {
                if (hasDualArtifacts && ownedArtifactCount <= 2) return;
    
                player.sendMessage(Text.translatable("prominent.overitemsoulbound").setStyle(Style.EMPTY.withColor(Formatting.RED)), true);
                player.damage(player.getDamageSources().genericKill(), player.getMaxHealth() * 0.45f);
            }
        }
    }
}