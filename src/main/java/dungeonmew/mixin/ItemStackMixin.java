package dungeonmew.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import dungeonmew.feature.Features;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
    @Shadow @Nullable public abstract NbtCompound getNbt();

    @Unique
    @ModifyReturnValue(method = "getTooltip", at = @At("RETURN"))
    private List<Text> showCustomModelData(List<Text> original, @Nullable PlayerEntity player, TooltipContext context) {
        if (Features.SHOW_CUSTOM_MODEL_DATA.getValue() && getNbt() != null && getNbt().contains("CustomModelData")) {
            int customModelData = getNbt().getInt("CustomModelData");

            String formattedCustomModelData = formatCustomModelData(customModelData);

            original.add(
                    Text.literal("CustomModelData: ").formatted(Formatting.YELLOW)
                            .append(Text.literal(formattedCustomModelData).formatted(Formatting.AQUA))
            );
        }
        return original;
    }

    private String formatCustomModelData(int value) {
        String stringValue = String.valueOf(value);
        StringBuilder result = new StringBuilder();

        int length = stringValue.length();
        for (int i = 0; i < length; i++) {
            result.append(stringValue.charAt(i));
            if ((length - i - 1) % 3 == 0 && i < length - 1) {
                result.append('_');
            }
        }

        return result.toString();
    }
}
