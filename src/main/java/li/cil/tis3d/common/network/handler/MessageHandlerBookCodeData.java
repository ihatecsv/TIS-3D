package li.cil.tis3d.common.network.handler;

import li.cil.tis3d.common.init.Items;
import li.cil.tis3d.common.item.CodeBookItem;
import li.cil.tis3d.common.network.message.MessageBookCodeData;
import net.fabricmc.fabric.networking.PacketContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public final class MessageHandlerBookCodeData extends AbstractMessageHandler<MessageBookCodeData> {
    @Override
    protected void onMessageSynchronized(final MessageBookCodeData message, final PacketContext context) {
        final PlayerEntity player = context.getPlayer();
        if (player != null) {
            final ItemStack stack = player.getStackInHand(message.getHand());
            if (Items.isBookCode(stack)) {
                final CodeBookItem.Data data = CodeBookItem.Data.loadFromNBT(message.getNbt());
                CodeBookItem.Data.saveToStack(stack, data);
            }
        }
    }
}
