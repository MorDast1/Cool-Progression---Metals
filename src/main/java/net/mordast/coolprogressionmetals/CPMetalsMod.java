package net.mordast.coolprogressionmetals;

import net.minecraft.world.item.CreativeModeTabs;
import net.mordast.coolprogressionmetals.Item.ModCreaviveModeTabs;
import net.mordast.coolprogressionmetals.Item.ModItems;

import net.mordast.coolprogressionmetals.block.Modblocks;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;


// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(CPMetalsMod.MOD_ID)
public class CPMetalsMod {
    // Define mod id in a common place for everything to reference
    public static final String MOD_ID = "coolprogressionmetalsmod";
    public static final Logger LOGGER = LogUtils.getLogger();

    // The constructor for the mod class is the first code that is run when your mod is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
    public CPMetalsMod(IEventBus modEventBus, ModContainer modContainer) {
        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        // Register ourselves for server and other game events we are interested in.
        // Note that this is necessary if and only if we want *this* class (ExampleMod) to respond directly to events.
        // Do not add this line if there are no @SubscribeEvent-annotated functions in this class, like onServerStarting() below.
        NeoForge.EVENT_BUS.register(this);

        ModCreaviveModeTabs.register(modEventBus);

        ModItems.register(modEventBus);
        Modblocks.register(modEventBus);

        // Register the item to a creative tab
        modEventBus.addListener(this::addCreative);

        // Register our mod's ModConfigSpec so that FML can create and load the config file for us
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(FMLCommonSetupEvent event) {

    }

    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event) {
 //       if(event.getTabKey() == CreativeModeTabs.INGREDIENTS) {
        //           event.accept(ModItems.MITHRIL_INGOT);
        //           event.accept(ModItems.ADAMANT_INGOT);
        //           event.accept(ModItems.NIXTRIS_INGOT);
        //           event.accept(ModItems.MITHRIL);
        //           event.accept(ModItems.ADAMANT);
        //          event.accept(ModItems.NIXTRIS);
        //          event.accept(ModItems.MITHRIL_MIXTURE);
        //          event.accept(ModItems.ADAMANT_MIXTURE);
        //          event.accept(ModItems.NIXTRIS_POLYMER);
        //     }
        //     if(event.getTabKey() == CreativeModeTabs.INGREDIENTS){
        //         event.accept(Modblocks.MITHRIL_BLOCK);
        //         event.accept(Modblocks.ADAMANT_BLOCK);
        //         event.accept(Modblocks.NIXTRIS_BLOCK);
        //              event.accept(Modblocks.MITHRIL_ORE);
        //         event.accept(Modblocks.ADAMANT_ORE);
        //        event.accept(Modblocks.NIXTRIS_ORE);
//        }
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
    }
}
