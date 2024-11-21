package elocindev.prominent.registry.client;

import elocindev.prominent.ProminentLoader;
import elocindev.prominent.registry.ItemRegistry;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.util.Identifier;

public class ModelPredicateRegistry {
    // Thank you mojang for clamping the predicates to 0-1f so I can't do this in a single predicate due to float rounding errors :D
    public static void registerEkavar() {
        String[] phases = {"arcane", "fire", "frost", "holy", "astral"};
    
        for (int i = 0; i < phases.length; i++) {
            final int phase = i + 1;
            ModelPredicateProviderRegistry.register(ItemRegistry.EKAVAR, new Identifier(ProminentLoader.MODID, "ekavar_"+phases[i]), (stack, world, entity, seed) -> {    
                return stack.getOrCreateNbt().getInt("Phase") == phase ? 1 : 0;
            });
        }
    }
}
