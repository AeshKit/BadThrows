package aesh.kai.mixin;

import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.apache.commons.lang3.RandomUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import static aesh.kai.config.Configs.syncedConfig;

@Mixin(DefaultDispenseItemBehavior.class)
public class BadDispense {
    @Redirect(
            method = "spawnItem",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/item/ItemEntity;setDeltaMovement(DDD)V"
            )
    )
    private static void aesh$badSpawnItem(
            ItemEntity entity, double x, double y, double z,
            Level level, ItemStack itemStack, int accuracy, Direction direction, Position position
    ) {
        if(syncedConfig.doDropperLogic && syncedConfig.globalToggle)
            badBlock(entity, accuracy, direction);
        else {
            RandomSource random = level.getRandom();
            double pow = random.nextDouble() * 0.1 + 0.2;
            entity.setDeltaMovement(
                    random.triangle(direction.getStepX() * pow, 0.0172275 * accuracy),
                    random.triangle(0.2, 0.0172275 * accuracy),
                    random.triangle(direction.getStepZ() * pow, 0.0172275 * accuracy)
            );
        }
    }

    @Unique
    private static void badBlock(ItemEntity entity, double accuracy, Direction direction) {
        double x;
        double z;
        double y;
        //        original:
//        double pow = random.nextDouble() * 0.1 + 0.2;
//        itemEntity.setDeltaMovement(
//                        random.triangle((double)direction.getStepX() * pow, 0.0172275 * (double)accuracy),
//                        random.triangle(0.2, 0.0172275 * (double)accuracy),
//                        random.triangle((double)direction.getStepZ() * pow, 0.0172275 * (double)accuracy));

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