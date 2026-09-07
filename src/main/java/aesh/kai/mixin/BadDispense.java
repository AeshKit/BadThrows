package aesh.kai.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.world.entity.item.ItemEntity;
import org.apache.commons.lang3.RandomUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import static aesh.kai.config.Configs.syncedConfig;

@Mixin(DefaultDispenseItemBehavior.class)
public class BadDispense {

    @WrapOperation(
            method = "spawnItem",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/item/ItemEntity;setDeltaMovement(DDD)V"
            )
    )
    private static void aesh$badSpawnItem(
            ItemEntity entity, double x, double y, double z,
            Operation<Void> original,
            @Local(argsOnly = true, name = "accuracy") int accuracy,
            @Local(argsOnly = true, name = "direction") Direction direction
    ) {
        if(syncedConfig.doDropperLogic && syncedConfig.globalToggle) {
            badBlock(entity, accuracy, direction);
        } else {
            original.call(entity, x, y, z);
        }
    }

    @Unique
    private static void badBlock(ItemEntity entity, double accuracy, Direction direction) {
        double x;
        double z;
        double y;

        // Result of Math.nextDown(1.0) * 0.1 + 0.2 is exactly 0.3 in Java
        final double pow = 0.3 - RandomUtils.insecure().randomDouble(
                0D,
                Math.min(syncedConfig.magnitudeVariance, 0.1D)
        );

        double mult = Math.nextDown(1.0);

        // Triangle Dist: Mean + Spread * (double from -1 ~ 1)
        mult = maybeInvert(mult);
        x = ((double) direction.getStepX() * pow) + (0.0172275 * accuracy) * (mult - RandomUtils.insecure().randomDouble(
                0D,
                Math.min(syncedConfig.directionVariance, Math.abs(mult))
        ));

        mult = maybeInvert(mult);
        y = 0.2 + (0.0172275 * accuracy) * (mult - RandomUtils.insecure().randomDouble(
                0D,
                Math.min(syncedConfig.directionVariance, Math.abs(mult))
        ));

        mult = maybeInvert(mult);
        z = ((double) direction.getStepZ() * pow) + (0.0172275 * accuracy) * (mult - RandomUtils.insecure().randomDouble(
                0D,
                Math.min(syncedConfig.directionVariance, Math.abs(mult))
        ));

        entity.setDeltaMovement(x, y, z);
    }

    @Unique
    private static double maybeInvert(double d) {
        if(RandomUtils.insecure().randomInt(0, 2) == 0) return -d;
        return d;
    }
}