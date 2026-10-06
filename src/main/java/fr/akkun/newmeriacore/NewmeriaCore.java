package fr.akkun.newmeriacore;

import com.mojang.logging.LogUtils;
import fr.akkun.newmeriacore.block.ModBlockEntities;
import fr.akkun.newmeriacore.block.ModBlocks;
import fr.akkun.newmeriacore.block.ModCauldronInteractions;
import fr.akkun.newmeriacore.creativemodetab.ModCreativeModeTabs;
import fr.akkun.newmeriacore.data.ModDataComponents;
import fr.akkun.newmeriacore.effect.ModEffects;
import fr.akkun.newmeriacore.entity.ModEntityTypes;
import fr.akkun.newmeriacore.worldgen.ModFeatures;
import fr.akkun.newmeriacore.fluid.ModFluids;
import fr.akkun.newmeriacore.frying.FryingAttachments;
import fr.akkun.newmeriacore.item.ModItems;
import fr.akkun.newmeriacore.potion.ModPotions;
import fr.akkun.newmeriacore.recipe.ModRecipeSerializers;
import fr.akkun.newmeriacore.rpg.RpgAttachments;
import fr.akkun.newmeriacore.rpg.companion.CompanionAttachments;
import fr.akkun.newmeriacore.rpg.network.RpgNetworking;
import fr.akkun.newmeriacore.sound.ModSounds;
import fr.akkun.newmeriacore.stat.ModStats;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(NewmeriaCore.MOD_ID)
public class NewmeriaCore {
    // Define mod id in a common place for everything to reference
    public static final String MOD_ID = "newmeriacore";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();

    // The constructor for the mod class is the first code that is run when your mod is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
    public NewmeriaCore(IEventBus modEventBus, ModContainer modContainer) {
        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        ModCreativeModeTabs.register(modEventBus);

        ModFluids.register(modEventBus);
        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModCauldronInteractions.register(modEventBus);

        FryingAttachments.register(modEventBus);

        ModDataComponents.register(modEventBus);
        ModStats.register(modEventBus);

        ModSounds.register(modEventBus);
        ModEffects.register(modEventBus);

        ModPotions.register(modEventBus);

        ModEntityTypes.register(modEventBus);
        ModFeatures.register(modEventBus);
        ModRecipeSerializers.register(modEventBus);

        RpgAttachments.register(modEventBus);
        CompanionAttachments.register(modEventBus);
        RpgNetworking.register(modEventBus);

        NeoForge.EVENT_BUS.register(this);
        // Register the item to a creative tab
        modEventBus.addListener(this::addCreative);
        // Register our mod's ModConfigSpec so that FML can create and load the config file for us
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(FMLCommonSetupEvent event) {

    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {

    }
}
