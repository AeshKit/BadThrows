package aesh.kai.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.entity.item.ItemEntity;
import org.apache.commons.lang3.RandomUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import static aesh.kai.config.Configs.syncedConfig;

@Mixin(ItemEntity.class)
public class BadItemEntity {
    @ModifyExpressionValue(
            method = "<init>(Lnet/minecraft/world/level/Level;DDDLnet/minecraft/world/item/ItemStack;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/util/RandomSource;nextDouble()D")
    )
    private double aesh$badDouble(double original) {
        if(syncedConfig.doItemEntityLogic && syncedConfig.globalToggle) return badBlock();
        return RandomUtils.insecure().randomDouble(0D, 1D);
    }

    /*
    Original:
		this.setDeltaMovement(this.random.nextDouble() * 0.2 - 0.1, 0.2, this.random.nextDouble() * 0.2 - 0.1);
     */

    @Unique
    private static double badBlock() {
        double out;
        final double diff = RandomUtils.insecure().randomDouble(
                0D,
                Math.min(syncedConfig.magnitudeVariance, Math.nextDown(1D))
        );
        if(RandomUtils.insecure().randomInt(0, 2) == 0) {
            out = 0D + diff;
        } else {
            out = Math.nextDown(1D) - diff;
        }
        return out;
    }
}
