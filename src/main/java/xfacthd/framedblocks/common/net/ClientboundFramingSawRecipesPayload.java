package xfacthd.framedblocks.common.net;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import xfacthd.framedblocks.common.crafting.FramingSawRecipe;
import xfacthd.framedblocks.common.crafting.FramingSawRecipeSerializer;

import java.util.List;

public record ClientboundFramingSawRecipesPayload(List<RecipeHolder<FramingSawRecipe>> recipes) implements CustomPacketPayload
{
    public static final CustomPacketPayload.Type<ClientboundFramingSawRecipesPayload> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("framedblocks", "framing_saw_recipes"));
    private static final StreamCodec<RegistryFriendlyByteBuf, RecipeHolder<FramingSawRecipe>> HOLDER_STREAM_CODEC = StreamCodec.composite(
            ResourceKey.streamCodec(Registries.RECIPE),
            RecipeHolder::id,
            FramingSawRecipeSerializer.STREAM_CODEC,
            RecipeHolder::value,
            RecipeHolder::new
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundFramingSawRecipesPayload> STREAM_CODEC =
            HOLDER_STREAM_CODEC.apply(ByteBufCodecs.list())
                    .map(ClientboundFramingSawRecipesPayload::new, ClientboundFramingSawRecipesPayload::recipes);

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type()
    {
        return TYPE;
    }
}