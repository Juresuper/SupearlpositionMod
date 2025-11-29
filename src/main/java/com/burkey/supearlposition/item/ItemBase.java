package com.burkey.supearlposition.item;

import com.burkey.supearlposition.SupearlpositionMod;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class ItemBase extends Item {
    protected String name;

    public ItemBase(String name) {
        this.name = name;
        setRegistryName(name);
        setTranslationKey(name);
    }

    public void registerItemModel(){
        SupearlpositionMod.proxy.registerItemRenderer(this,0,name);
    }

    @Override
    public ItemBase setCreativeTab(CreativeTabs tab) {
        super.setCreativeTab(tab);
        return this;
    }
}
