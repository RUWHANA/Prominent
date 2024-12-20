package elocindev.prominent.dialogue;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Formatting;
import toni.immersivemessages.api.ImmersiveMessage;
import net.minecraft.text.Text;

public class Dialogue {
    public ServerPlayerEntity receiver;

    public Dialogue(ServerPlayerEntity receiver) {
        this.receiver = receiver;
    }

    public static ImmersiveMessage getGumasDialogue(String dialogue) {
        return getGumasDialogue(dialogue, 70);
    }

    public static ImmersiveMessage getGumasDialogue(String dialogue, int durationTicks) {
        return ImmersiveMessage.builder((float) (durationTicks / 20), dialogue)
            .slideUp()
            .shadow(true)
            .fadeOut()
            .fadeIn()
            .typewriter(1.5f, true)
            .color(0xc9af57)
            
            .subtext(0f, ("낯익은 목소리가 속삭입니다."), 10, subtext -> subtext
                .fadeIn()
                .shadow(true)
                .fadeOut(0.15f)
                .color(Formatting.DARK_GRAY)
                .italic()
            );
    }
}
