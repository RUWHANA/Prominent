package elocindev.prominent.player.artifact;

import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.puffish.skillsmod.client.data.ClientCategoryData;
import net.minecraft.util.Formatting;

public class ClientArtifactHolder {
    public static int artifactPower = 0;
    public static String artifactName = "";

    public static ClientCategoryData INSTANCE = null;

    public static MutableText getPowerText(String artifact) {
        if (ClientArtifactHolder.artifactPower > 0 && ClientArtifactHolder.artifactName.equals(artifact)) {            
            return Text.literal("                                        ").append(ClientArtifactHolder.artifactPower+ " ").append(Text.translatable("prominent.moltencoredesc2")).setStyle(Style.EMPTY.withColor(Formatting.GOLD));
        }
        return Text.empty();
    }
}