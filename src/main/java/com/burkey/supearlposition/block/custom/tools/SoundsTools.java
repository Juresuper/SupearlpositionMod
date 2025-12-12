package com.burkey.supearlposition.block.custom.tools;

import com.burkey.supearlposition.SupearlpositionMod;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class SoundsTools {
    public static SoundEvent ENDERGY_RUNNING;
    public static SoundEvent ENDERGY_UNSTABLE;
    public static SoundEvent PEARL_STORM;

    public static void registerSounds() {
        ENDERGY_RUNNING = registerSound("blocks.endergy_reactor.endergy_running");
        ENDERGY_UNSTABLE = registerSound("blocks.endergy_reactor.endergy_unstable");
        PEARL_STORM = registerSound("entity.meltdownblob.pearl_storm");
    }
    private static SoundEvent registerSound(String name) {
        ResourceLocation location = new ResourceLocation(SupearlpositionMod.MODID, name);
        SoundEvent event = new SoundEvent(location);
        event.setRegistryName(name);
        ForgeRegistries.SOUND_EVENTS.register(event);
        return event;
    }
}
