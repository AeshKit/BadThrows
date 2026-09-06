// lebrawnn ? what heppen'd to you mate ??

package aesh.kai.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang3.RandomUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static aesh.kai.config.Configs.syncedConfig;

@Mixin(LivingEntity.class)
public abstract class BadLivingEntity {

    @Inject(
            method = "createItemStackToDrop(Lnet/minecraft/world/item/ItemStack;ZZ)Lnet/minecraft/world/entity/item/ItemEntity;",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/item/ItemEntity;setDeltaMovement(DDD)V"
            ),
            cancellable = true
    )
    private void aesh$badItemStackDrop(final ItemStack itemStack, final boolean randomly, final boolean thrownFromHand, CallbackInfoReturnable<ItemEntity> cir,
                                       @Local(name = "entity") ItemEntity entity) {

        if(syncedConfig.doLivingEntityLogic && syncedConfig.globalToggle) overwriteBlock(randomly, cir, entity);
        else {
            LivingEntity thisEntity = (LivingEntity)(Object) this;

            if(randomly) {
                float pow = thisEntity.getRandom().nextFloat() * 0.5F;
                float dir = thisEntity.getRandom().nextFloat() * (float) (Math.PI * 2);
                entity.setDeltaMovement(-Mth.sin(dir) * pow, 0.2F, Mth.cos(dir) * pow);
            } else {
                float sinX = Mth.sin(thisEntity.getXRot() * (float) (Math.PI / 180.0));
                float cosX = Mth.cos(thisEntity.getXRot() * (float) (Math.PI / 180.0));
                float sinY = Mth.sin(thisEntity.getYRot() * (float) (Math.PI / 180.0));
                float cosY = Mth.cos(thisEntity.getYRot() * (float) (Math.PI / 180.0));
                float dir = thisEntity.getRandom().nextFloat() * (float) (Math.PI * 2);
                float pow2 = 0.02F * thisEntity.getRandom().nextFloat();
                entity.setDeltaMovement(
                        -sinY * cosX * 0.3F + Math.cos(dir) * pow2,
                        -sinX * 0.3F + 0.1F + (thisEntity.getRandom().nextFloat() - thisEntity.getRandom().nextFloat()) * 0.1F,
                        cosY * cosX * 0.3F + Math.sin(dir) * pow2
                );
            }

            cir.setReturnValue(entity);
        }
    }

    @Unique
    private void overwriteBlock(boolean randomly, CallbackInfoReturnable<ItemEntity> cir, ItemEntity entity) {
        if(randomly) {
            float pow = Math.nextDown(1F) * 0.5F;
            pow -= Math.min(Math.nextDown((float)syncedConfig.magnitudeVariance), Math.nextUp(0.5F));
            /* Range of dir [ from source code ]

                dir == nextFloat() * ((float)Math.PI * 2F);     Given

                ((float)Math.PI * 2F) == 6.2831855F             Calculated in Java env

                = nextFloat() * 6.2831855F                      Substitute

                0F <= nextFloat <= Math.nextDown(1F)            Definition of nextFloat

                Math.nextDown(1.0F) == 0.99999994F              Calculated in Java env

                0F <= nextFloat <= 0.99999994F                  Substitute

                * Global Minimum

                    = 0F * 6.2831855

                    = 0F

                * Global Maximum

                    = 0.99999994F * 6.2831855F

                    = 6.283185F                                 Calculated in Java env

                0F <= nextFloat() * ((float)Math.PI * 2F); <= 6.283185F
                ‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾
             * Find the maxima of abs(xd) + abs(zd)
             Given that we can't use the first derivative method [ Mth.cos() != cos() ], we'll
             have to iterate through every possible value to find the min/max.

             I wrote a lil iterator to find the maxima & minima. note that Math.* and Mth.* are different:

    double max = Double.MIN_VALUE;
    float iAtMax = -1F;
    double xMax = Double.MIN_VALUE;
    double zMax = Double.MIN_VALUE;
    float iAtZMax = -1F;
    float iAtXMax = -1F;

    for(float i = 0F; i < 6.283185F; i = Math.nextUp(i)) {
        double xd = (double)(-Mth.sin((double)i) * pow);
        double zd = (double)(Mth.cos((double)i) * pow);
        double thisVal = Math.abs(xd) + Math.abs(zd);

        if(thisVal > max) {
            max = thisVal;
            iAtMax = i;
        }
        if(xd > xMax) {
            xMax = xd;
            iAtXMax = i;
        }
        if(zd > zMax) {
            zMax = zd;
            iAtZMax = i;
        }
    }

    LOGGER.info("\nmax: {} @i = {}\nxMax: {} @i = {} \nzMax: {} @i = {}",
            max,
            iAtMax,
            xMax,
            iAtXMax,
            zMax,
            iAtZMax
    );

             results:
                 max: 0.707106739282608 @i = 0.78530234
                 xMax: 0.4999999701976776 @i = 4.7121973
                 zMax: 0.4999999701976776 @i = 0.0

             With these values, we can extend it to find every value i with our known maximum points:

    final double totalMax = 0.707106739282608;
    final double xMax = 0.4999999701976776;
    final double zMax = 0.4999999701976776;
    final float pow = Math.nextDown(1F) * 0.5F;

    for(float i = 0F; i < 6.283185F; i = Math.nextUp(i)) {
        double xd = (double)(-sin((double)i) * pow);
        double zd = (double)(cos((double)i) * pow);
        double thisVal = Math.abs(xd) + Math.abs(zd);

        if(xd == xMax) LOGGER.info("x: {}", i);
        if(zd == zMax) LOGGER.info("z: {}", i);
        if(thisVal == totalMax) LOGGER.info("tot: {}", i);
    }

             With these values, the output is in multiple ranges:
                tot: 0.78530234
                tot: 0.7853024
                ......
                tot: 0.7855899

             We get this due to similar values hitting the same entry in the lookup table.
             Honestly, the differnce in distance is negligible, but it's still there.
             That's why those values are preferred, but it can still be anything.
             */

            int rand = RandomUtils.insecure().randomInt(0, 13);
            float dir = switch(rand) {
                case 0 -> 0.7855F; // max total
                case 1 -> 2.3563F; // max total
                case 2 -> 3.9271F; // max total
                case 3 -> 5.4979F; // max total
                case 4 -> 4.7122F; // max X
                default -> RandomUtils.insecure().randomFloat(0, 1) * ((float)Math.PI * 2F);
            };
            entity.setDeltaMovement((double)(-Mth.sin((double)dir) * pow), (double)0.2F, (double)(Mth.cos((double)dir) * pow));

            cir.setReturnValue(entity);
        } else { // Drop
            /*
            Vanilla:
            float pow = 0.3F;
			float sinX = Mth.sin(this.getXRot() * (float) (Math.PI / 180.0));
			float cosX = Mth.cos(this.getXRot() * (float) (Math.PI / 180.0));
			float sinY = Mth.sin(this.getYRot() * (float) (Math.PI / 180.0));
			float cosY = Mth.cos(this.getYRot() * (float) (Math.PI / 180.0));
			float dir = this.random.nextFloat() * (float) (Math.PI * 2);
			float pow2 = 0.02F * this.random.nextFloat();
			entity.setDeltaMovement(
				-sinY * cosX * 0.3F + Math.cos(dir) * pow2,
				-sinX * 0.3F + 0.1F + (this.random.nextFloat() - this.random.nextFloat()) * 0.1F,
				cosY * cosX * 0.3F + Math.sin(dir) * pow2
			);
             */

            LivingEntity thisEntity = (LivingEntity)(Object) this;

            float sinX = Mth.sin(thisEntity.getXRot() * (float) (Math.PI / 180.0));
            float cosX = Mth.cos(thisEntity.getXRot() * (float) (Math.PI / 180.0));
            float sinY = Mth.sin(thisEntity.getYRot() * (float) (Math.PI / 180.0));
            float cosY = Mth.cos(thisEntity.getYRot() * (float) (Math.PI / 180.0));
            float dir;
            // RANGE: [0, 2pi)
            dir = switch(RandomUtils.insecure().randomInt(0, 4)) {
                case 0 -> (float) Math.PI / 2; // max z
                case 1 -> 3 * (float) Math.PI / 2; // min z
                case 2 -> 0F; // max x
                default -> (float) Math.PI; // min x
            };

            float pow2 = 0.02F * Math.nextDown(1F);
            pow2 -= RandomUtils.insecure().randomFloat(
                    0F,
                    Math.min((float)syncedConfig.magnitudeVariance, Math.nextDown(0.02F))
            );

            float triangle = Math.nextDown(1F);
            triangle -= RandomUtils.insecure().randomFloat(
                    0F,
                    Math.min((float)syncedConfig.magnitudeVariance, Math.nextDown(1F))
            );

            if(RandomUtils.insecure().randomInt(0, 2) == 1) triangle *= -1;

            entity.setDeltaMovement(
                    -sinY * cosX * 0.3F + Math.cos(dir) * pow2,
                    -sinX * 0.3F + 0.1F + triangle * 0.1F,
                    cosY * cosX * 0.3F + Math.sin(dir) * pow2
            );

            cir.setReturnValue(entity);
        }
    }
}
