package com.burkey.supearlposition.block.custom.tools;

import com.burkey.supearlposition.SupearlpositionMod;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class SoundsTools {
    public static SoundEvent ENDERGY_RUNNING;

    public static void registerSounds() {
        ENDERGY_RUNNING = registerSound("endergy_running");
    }
    private static SoundEvent registerSound(String name) {
        ResourceLocation location = new ResourceLocation(SupearlpositionMod.MODID, name);
        SoundEvent event = new SoundEvent(location);
        event.setRegistryName(name);
        ForgeRegistries.SOUND_EVENTS.register(event);
        return event;
    }
}
