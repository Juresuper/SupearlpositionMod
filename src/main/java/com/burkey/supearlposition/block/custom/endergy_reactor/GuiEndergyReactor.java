package com.burkey.supearlposition.block.custom.endergy_reactor;

import com.burkey.supearlposition.SupearlpositionMod;
import com.burkey.supearlposition.config.GeneralConfig;
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
        if(reactorControl.getClientPearlCount() < GeneralConfig.MIN_PEARL_COUNT){
            drawString(mc.fontRenderer, "STATUS: ", guiLeft + 83, guiTop + 35, 0xFF00FF00);
            drawString(mc.fontRenderer, "OFF", guiLeft + 133, guiTop + 35, 0xFFFF0000);

            drawString(mc.fontRenderer, "Pearls required: " + (GeneralConfig.MIN_PEARL_COUNT -reactorControl.getClientPearlCount()), guiLeft + 83, guiTop + 50, 0xFF00FF00);

        }else{
            int percentage = 100 - reactorControl.getReactor_cycle_count() * 100 / GeneralConfig.REACTOR_CYCLE_LENGTH;
            drawString(mc.fontRenderer, "Cycle Progress: " + percentage, guiLeft + 83, guiTop + 35, 0xFF00FF00);

            double production = Math.round(getProduction() * 100.0) / 100.0;
            drawString(mc.fontRenderer, "Production speed:", guiLeft + 83, guiTop + 50, 0xFF00FF00);
            drawString(mc.fontRenderer, production + " Pearls/s:", guiLeft + 105, guiTop + 65, 0xFF00FF00);
        }



    }

    private double getProduction() {
        int pearls = reactorControl.getClientPearlCount();
        int fuelPerCycle;

        if (pearls < GeneralConfig.MIN_PEARL_COUNT) {
            fuelPerCycle = 0;
        } else fuelPerCycle = TileEndergyReactorControl.getFuelProduced(pearls);

        return fuelPerCycle * (20.0 / (double) GeneralConfig.REACTOR_CYCLE_LENGTH);
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
        if(mouseX > guiLeft + 80 && mouseX < guiLeft + 180 && mouseY > guiTop + 5 && mouseY > 118 && mouseY < 132){
            drawHoveringText(Collections.singletonList("Pearl count: "+ reactorControl.getClientPearlCount()), mouseX, mouseY, fontRenderer);
        }
    }
    private void drawPearlCountBar(int pearlCount) {
        int left = 80;
        int top = 130;
        int right = 180;
        int bottom = 120;

        drawRect(guiLeft + left-2, guiTop + bottom-2, guiLeft + right+2, guiTop + top+2, 0xFF3CFFD8);
        drawRect(guiLeft + left, guiTop + bottom, guiLeft + right, guiTop + top, 0xFF000000);

        int barWidth = right - left;

        int s1 = left + (barWidth * GeneralConfig.STAGE1  / GeneralConfig.MAX_PEARL_COUNT);
        int s2 = left + (barWidth * GeneralConfig.STAGE2  / GeneralConfig.MAX_PEARL_COUNT);
        int s3 = left + (barWidth * GeneralConfig.STAGE3  / GeneralConfig.MAX_PEARL_COUNT);
        int s4 = left + (barWidth * GeneralConfig.STAGE4  / GeneralConfig.MAX_PEARL_COUNT);


        int percentage = getPercentage(pearlCount);
        if(pearlCount < GeneralConfig.MAX_PEARL_COUNT){
            for (int i = 0 ; i < percentage ; i++) {
                drawVerticalLine(guiLeft + left + 1 + i, guiTop + bottom, guiTop + top,
                        i % 4 == 0 ? 0xFF00FF00 : 0xFF008000);
            }
        } else {
            drawString(mc.fontRenderer, "CRITICAL MASS", guiLeft + left + 15, guiTop + 120, 0xFF00FF00);
        }
    }

    private static int getPercentage(int pearlCount) {
        int percentage = 0;

        if(pearlCount < GeneralConfig.STAGE1){
            percentage = pearlCount * 100 / GeneralConfig.STAGE1;
        }else if(pearlCount > GeneralConfig.STAGE1 &&  pearlCount < GeneralConfig.STAGE2){
            percentage = pearlCount * 100 / GeneralConfig.STAGE2;
        }else if(pearlCount > GeneralConfig.STAGE2 &&  pearlCount < GeneralConfig.STAGE3){
            percentage = pearlCount * 100 / GeneralConfig.STAGE3;
        }else if(pearlCount > GeneralConfig.STAGE3 &&  pearlCount < GeneralConfig.STAGE4){
            percentage = pearlCount * 100 / GeneralConfig.STAGE4;

        }else{
            percentage = pearlCount * 100 / GeneralConfig.MAX_PEARL_COUNT;
        }
        return percentage;
    }

}