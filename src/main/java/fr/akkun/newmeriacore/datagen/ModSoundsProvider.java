package fr.akkun.newmeriacore.datagen;

import fr.akkun.newmeriacore.NewmeriaCore;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.SoundDefinitionsProvider;

public class ModSoundsProvider extends SoundDefinitionsProvider {
    public ModSoundsProvider(PackOutput output) {
        super(output, NewmeriaCore.MOD_ID);
    }

    @Override
    public void registerSounds() {
    }
}
