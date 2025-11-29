package com.burkey.supearlposition.block.custom.endergy_reactor;

import com.burkey.supearlposition.SupearlpositionMod;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;
import java.util.Collections;

public class GuiEndergyReactor extends GuiContainer{
    public static final int WIDTH = 240;
    public static final int HEIGHT = 256;

    private static final ResourceLocation background = new ResourceLocation(SupearlpositionMod.MODID, "textures/gui/endergy_reactor_gui.png");
    private TileEndergyReactorControl reactorControl;

    public GuiEndergyReactor(TileEndergyReactorControl tileEntity, EndergyReactorControlContainer container) {
        super(container);

        xSize = WIDTH;
        ySize = HEIGHT;

        reactorControl = tileEntity;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        mc.getTextureManager().bindTexture(background);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);

        int reactorCycleProgress = reactorControl.getClientReactorCycleCount();
        //drawCycleProgressBar(reactorCycleProgress)
        drawPearlCountBar(reactorControl.getClientPearlCount());
        if(reactorControl.getClientProgress() > 0){
            int percentage = 100 - reactorControl.getClientProgress() * 100 / TileEndergyReactorControl.MAX_PROGRESS;
            drawString(mc.fontRenderer, "Progress: " + percentage  + "%", guiLeft + 10, guiTop + 30, 0xFFFFFF);
        }

        if(reactorControl.getPearl_count() > 0){
            int pearls= reactorControl.getPearl_count();
            drawString(mc.fontRenderer, "Number of pearls: " + pearls, guiLeft + 10, guiTop + 50, 0xA020F0);
        }
        if(true){
            int percentage = 100 - reactorControl.getReactor_cycle_count() * 100 / TileEndergyReactorControl.REACTOR_CYCLE_LENGTH;
            drawString(mc.fontRenderer, "Reactor cycle progress: " + percentage, guiLeft + 10, guiTop + 70, 0xFFBF00);
        }
        if(true){
            int excess_fuel = reactorControl.getExcess_fuel();
            drawString(mc.fontRenderer, "Excess fuel: " + excess_fuel, guiLeft + 10, guiTop + 90, 0xFFBF00);
        }

    }

    @Override
    public void handleKeyboardInput() throws IOException {
        super.handleKeyboardInput();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        super.renderHoveredToolTip(mouseX, mouseY);
        if(mouseX > guiLeft + 80 && mouseX < guiLeft + 170 && mouseY > guiTop + 5 && mouseY > 110 && mouseY < 120){
            drawHoveringText(Collections.singletonList("Pearl count: "+ reactorControl.getClientPearlCount()), mouseX, mouseY, fontRenderer);
        }
    }
    /*private void drawCycleProgressBar(int cycleProgress) {
        int left = 80;
        int top = 120;
        int right = 170;
        int bottom = 110;
        drawRect(guiLeft + left, guiTop + top, guiLeft + right, guiTop + bottom, 0xff555555);
        int percentage = 100 - cycleProgress * 100 / TileEndergyReactorControl.REACTOR_CYCLE_LENGTH;
        for (int i = 0 ; i < percentage ; i++) {
            drawVerticalLine(guiLeft + left + 1 + i, guiTop + top, guiTop + bottom, i % 2 == 0 ? 0xffff0000 : 0xff000000);
        }
    }*/
    private void drawPearlCountBar(int pearlCount) {
        int left = 80;
        int top = 120;
        int right = 180;
        int bottom = 110;
        drawRect(guiLeft + left, guiTop + bottom, guiLeft + right, guiTop + top, 0xff555555);
        int percentage = pearlCount* 100 / TileEndergyReactorControl.MAX_PEARL_COUNT;
        if(pearlCount < TileEndergyReactorControl.MAX_PEARL_COUNT){
            for (int i = 0 ; i < percentage ; i++) {
                drawVerticalLine(guiLeft + left + 1 + i, guiTop + bottom, guiTop + top, i % 4 == 0 ? 0xff6a1a91 : 0xffb62ff7);
            }
        }else{
            drawString(mc.fontRenderer, "CRITICAL MASS", guiLeft + left, guiTop + 115, 0xFFBF00);
        }


    }
}