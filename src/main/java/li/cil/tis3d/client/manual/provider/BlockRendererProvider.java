package li.cil.tis3d.client.manual.provider;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import li.cil.tis3d.api.manual.RendererProvider;
import li.cil.tis3d.api.manual.ContentRenderer;
import li.cil.tis3d.client.manual.Strings;
import li.cil.tis3d.client.manual.segment.render.ItemStackContentRenderer;
import li.cil.tis3d.client.manual.segment.render.MissingItemRenderer;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.command.arguments.BlockStateParser;
import net.minecraft.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistryEntry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public final class BlockRendererProvider extends ForgeRegistryEntry<RendererProvider> implements RendererProvider {
    private static final Logger LOGGER = LogManager.getLogger();

    private static final String PREFIX = "block:";
    private static final Map<String, BlockState> BLOCK_STATE_CACHE = new HashMap<>();

    @Override
    public boolean matches(final String path) {
        return path.startsWith(PREFIX);
    }

    @Override
    public ContentRenderer getRenderer(final String path) {
        final String data = path.substring(PREFIX.length());
        final BlockState state = Objects.requireNonNull(BLOCK_STATE_CACHE.computeIfAbsent(data, (string) -> {
            try {
                return new BlockStateParser(new StringReader(string), false)
                    .parse(false)
                    .getState();
            } catch (final CommandSyntaxException e) {
                LOGGER.error("Failed parsing block state.", e);
                return Blocks.AIR.defaultBlockState();
            }
        }));

        if (state.getBlock() != Blocks.AIR) {
            return new ItemStackContentRenderer(new ItemStack(state.getBlock()));
        } else {
            return new MissingItemRenderer(Strings.WARNING_BLOCK_MISSING);
        }
    }
}