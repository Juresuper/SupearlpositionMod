package com.burkey.supearlposition.config;

import com.burkey.supearlposition.SupearlpositionMod;
import net.minecraftforge.common.config.Config;

@Config(modid = SupearlpositionMod.MODID)
public class GeneralConfig {

    @Config.Comment(value="Number of pearls needed to start the reactor")
    public static int MIN_PEARL_COUNT = 32;
    @Config.Comment(value="The length of one reactor cycle 20 ticks -> 1 second, so default is set to 4 seconds")
    public static int REACTOR_CYCLE_LENGTH = 80;
    @Config.Comment(value="The threshold at which the reactor will explode")
    public static int MAX_PEARL_COUNT = 100000;
    @Config.Comment(value="The upper limit for each stage of the reactor (stage five starts at over 75000)")
    public static int STAGE1 = 1000;
    public static int STAGE2 = 10000;
    public static int STAGE3 = 25000;
    public static int STAGE4 = 75000;
    @Config.Comment(value="The number of pearls produced in one cycle at each stage")
    public static int STAGE1_PRODUCTION = 10;
    public static int STAGE2_PRODUCTION = 40;
    public static int STAGE3_PRODUCTION = 100;
    public static int STAGE4_PRODUCTION = 150;
    public static int STAGE5_PRODUCTION = 500;
    @Config.Comment(value="The length of the meltdown, default is 30 seconds")
    public static int COUNTDOWN_LENGTH = 600;
}
