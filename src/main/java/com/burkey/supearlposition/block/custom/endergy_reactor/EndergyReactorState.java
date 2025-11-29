package com.burkey.supearlposition.block.custom.endergy_reactor;

import net.minecraft.util.IStringSerializable;

public enum EndergyReactorState implements IStringSerializable {
    OFF("off"),
    WORKING("working"),
    CRITICAL("critical"),
    NEEDS_CATALYST("catalyst");
    public static final EndergyReactorState[] VALUES = EndergyReactorState.values();
    private final String name;
    EndergyReactorState(String name) {this.name = name;}
    @Override
    public String getName() {
        return name;
    }
}
