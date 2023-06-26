package com.ineffa.trulytreasures.client;

import com.ineffa.trulytreasures.TrulyTreasures;
import com.ineffa.trulytreasures.config.TrulyTreasuresConfig;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = TrulyTreasures.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class ClientEvents {

    @SubscribeEvent
    public static void onConstructMod(FMLConstructModEvent event) {
        ModLoadingContext.get().registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class, () -> new ConfigScreenHandler.ConfigScreenFactory((minecraft, screen) -> AutoConfig.getConfigScreen(TrulyTreasuresConfig.class, screen).get()));
    }
}
