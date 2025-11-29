package com.burkey.supearlposition.network;

import com.burkey.supearlposition.SupearlpositionMod;
import com.burkey.supearlposition.block.custom.endergy_reactor.EndergyReactorControlContainer;
import com.burkey.supearlposition.block.custom.endergy_reactor.TileEndergyReactorControl;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketSyncPearls implements IMessage {
    private int numOfPearls;
    private int progress;
    private int reactorCycleCount;
    private int excessFuel;

    //TODO OPTIMIZE PACKET HANDLING
    /*enum PacketType{
        PEARLS,
        PROGRESS,
        REACTORCYCLE,
        EXCESSFUEL
    }

    private PacketType packetType;
*/


    @Override
    public void fromBytes(ByteBuf byteBuf) {
        numOfPearls = byteBuf.readInt();
        progress = byteBuf.readInt();
        reactorCycleCount = byteBuf.readInt();
        excessFuel = byteBuf.readInt();
    }

    @Override
    public void toBytes(ByteBuf byteBuf) {
        byteBuf.writeInt(numOfPearls);
        byteBuf.writeInt(progress);
        byteBuf.writeInt(reactorCycleCount);
        byteBuf.writeInt(excessFuel);
    }
    public PacketSyncPearls() {}
    /*
    public PacketSyncPearls(int numOfPearls) {
        this.numOfPearls = numOfPearls;
        //packetType = PacketType.PEARLS;

    }
    public PacketSyncPearls(int numOfPearls, int progress) {
        this.numOfPearls = numOfPearls;
        this.progress = progress;
        //packetType = PacketType.PROGRESS;

    }
    public PacketSyncPearls(int numOfPearls, int progress, int reactorCycleCount) {
        this.numOfPearls = numOfPearls;
        this.progress = progress;
        this.reactorCycleCount = reactorCycleCount;
        //packetType = PacketType.REACTORCYCLE;

    }*/
    public PacketSyncPearls(int numOfPearls, int progress, int reactorCycleCount, int excessFuel) {
        this.numOfPearls = numOfPearls;
        this.progress = progress;
        this.reactorCycleCount = reactorCycleCount;
        this.excessFuel = excessFuel;
        //packetType = PacketType.PEARLS;
    }

    public static class Handler implements IMessageHandler<PacketSyncPearls, IMessage> {

        @Override
        public IMessage onMessage(PacketSyncPearls message, MessageContext ctx) {
            SupearlpositionMod.proxy.addScheduledTaskClient(() -> handle(message,ctx));
            return null;
        }
        private void handle(PacketSyncPearls message, MessageContext ctx) {
            EntityPlayer player = SupearlpositionMod.proxy.getClientPlayer();
            if(player.openContainer instanceof EndergyReactorControlContainer){
                ((EndergyReactorControlContainer) player.openContainer).syncNumOfPearls(message.numOfPearls,message.progress,message.reactorCycleCount,message.excessFuel);
            }
        }
    }
}
