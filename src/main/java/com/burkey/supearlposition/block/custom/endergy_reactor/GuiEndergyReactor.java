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
    private final TileEndergyReactorControl reactorControl;

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
            drawString(mc.fontRenderer, "Processing: " + percentage  + "%", guiLeft + 104, guiTop + 80, 0xFF00FF00);
        }
        if(reactorControl.getClientPearlCount() < TileEndergyReactorControl.MIN_PEARL_COUNT){
            drawString(mc.fontRenderer, "STATUS: ", guiLeft + 83, guiTop + 35, 0xFF00FF00);
            drawString(mc.fontRenderer, "OFF", guiLeft + 133, guiTop + 35, 0xFFFF0000);

            drawString(mc.fontRenderer, "Pearls required: " + (TileEndergyReactorControl.MIN_PEARL_COUNT -reactorControl.getClientPearlCount()), guiLeft + 83, guiTop + 50, 0xFF00FF00);
            //drawString(mc.fontRenderer, 100 -reactorControl.getClientPearlCount()+ "", guiLeft + 105, guiTop + 65, 0xFF00FF00);


        }else{
            int percentage = 100 - reactorControl.getReactor_cycle_count() * 100 / TileEndergyReactorControl.REACTOR_CYCLE_LENGTH;
            drawString(mc.fontRenderer, "Cycle Progress: " + percentage, guiLeft + 83, guiTop + 35, 0xFF00FF00);

            int production = getProduction();
            drawString(mc.fontRenderer, "Production speed:", guiLeft + 83, guiTop + 50, 0xFF00FF00);
            drawString(mc.fontRenderer, production + " Pearls/s:", guiLeft + 105, guiTop + 65, 0xFF00FF00);
        }



    }

    private int getProduction() {
        int production;
        if(reactorControl.getClientPearlCount() < 100){
            production = 0;
        }else if (reactorControl.getClientPearlCount() < TileEndergyReactorControl.STAGE1 && reactorControl.getClientPearlCount() > 100) {
            production = 10;
        } else if (reactorControl.getClientPearlCount() < TileEndergyReactorControl.STAGE2) {
            production = 40;
        } else if (reactorControl.getClientPearlCount() < TileEndergyReactorControl.STAGE3) {
            production = 100;
        } else if (reactorControl.getClientPearlCount() < TileEndergyReactorControl.STAGE4) {
            production = 150;
        }else{
            production = 500;
        }
        production = production / (TileEndergyReactorControl.REACTOR_CYCLE_LENGTH / 20);
        return production;
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
        int top = 130;
        int right = 180;
        int bottom = 120;

        // your background
        drawRect(guiLeft + left-2, guiTop + bottom-2, guiLeft + right+2, guiTop + top+2, 0xFF3CFFD8);
        drawRect(guiLeft + left, guiTop + bottom, guiLeft + right, guiTop + top, 0xFF000000);

        int barWidth = right - left;

        int s1 = left + (barWidth * TileEndergyReactorControl.STAGE1  / TileEndergyReactorControl.MAX_PEARL_COUNT);
        int s2 = left + (barWidth * TileEndergyReactorControl.STAGE2  / TileEndergyReactorControl.MAX_PEARL_COUNT);
        int s3 = left + (barWidth * TileEndergyReactorControl.STAGE3  / TileEndergyReactorControl.MAX_PEARL_COUNT);
        int s4 = left + (barWidth * TileEndergyReactorControl.STAGE4  / TileEndergyReactorControl.MAX_PEARL_COUNT);


        int percentage = pearlCount * 100 / TileEndergyReactorControl.MAX_PEARL_COUNT;

        if(pearlCount < 100){
            percentage = pearlCount / 100;
        }else if(pearlCount > 100 &&  pearlCount < TileEndergyReactorControl.STAGE1){
            percentage = pearlCount * 100 / TileEndergyReactorControl.STAGE1;
        }else if(pearlCount > TileEndergyReactorControl.STAGE1 &&  pearlCount < TileEndergyReactorControl.STAGE2){
            percentage = pearlCount * 100 / TileEndergyReactorControl.STAGE2;
        }else if(pearlCount > TileEndergyReactorControl.STAGE2 &&  pearlCount < TileEndergyReactorControl.STAGE3){
            percentage = pearlCount * 100 / TileEndergyReactorControl.STAGE3;
        }else if(pearlCount > TileEndergyReactorControl.STAGE3 &&  pearlCount < TileEndergyReactorControl.STAGE4){
            percentage = pearlCount * 100 / TileEndergyReactorControl.STAGE4;

        }else{
            percentage = pearlCount * 100 / TileEndergyReactorControl.MAX_PEARL_COUNT;
        }
        if(pearlCount < TileEndergyReactorControl.MAX_PEARL_COUNT){
            for (int i = 0 ; i < percentage ; i++) {
                drawVerticalLine(guiLeft + left + 1 + i, guiTop + bottom, guiTop + top,
                        i % 4 == 0 ? 0xFF00FF00 : 0xFF008000);
            }
        } else {
            drawString(mc.fontRenderer, "CRITICAL MASS", guiLeft + left + 15, guiTop + 120, 0xFF00FF00);
        }
    }

}