package com.snugzy.trulytreasures;

import com.snugzy.trulytreasures.config.TrulyTreasuresConfig;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.loading.FMLPaths;

@Mod("trulytreasures")
public class TrulyTreasures {
    public static final String MOD_ID = "trulytreasures";

    public TrulyTreasures() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, TrulyTreasuresConfig.config);

        TrulyTreasuresConfig.loadConfig(TrulyTreasuresConfig.config, FMLPaths.CONFIGDIR.get().resolve(MOD_ID + "-common.toml").toString());

        MinecraftForge.EVENT_BUS.register(this);
    }
}
