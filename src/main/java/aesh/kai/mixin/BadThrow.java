package aesh.kai.mixin;

import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.lang3.RandomUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static aesh.kai.config.Configs.syncedConfig;

@Mixin(Projectile.class)
public abstract class BadThrow {
	@Inject(method = "getMovementToShoot", at = @At(value = "RETURN"), cancellable = true)
	private void aesh$badThrow(double xd,
							   double yd,
							   double zd,
							   float  pow,
							   float  uncertainty,
	                           CallbackInfoReturnable<Vec3> cir) {

		if(syncedConfig.doProjectileLogic && syncedConfig.globalToggle)
			badBlock(xd, yd, zd, pow, uncertainty, cir);
	}

	@Unique
	private static void badBlock(double xd, double yd, double zd, float pow, float uncertainty, CallbackInfoReturnable<Vec3> cir) {
		/*
		ORIGINAL:
		return new Vec3(xd, yd, zd)
			.normalize()
			.add(
				this.random.triangle(0.0, 0.0172275 * uncertainty), this.random.triangle(0.0, 0.0172275 * uncertainty), this.random.triangle(0.0, 0.0172275 * uncertainty)
			)
			.scale(pow);
		 */
		Vec3 base = new Vec3(xd, yd, zd).normalize();
		double randSpread = Math.nextDown(0.0172275 * uncertainty);
		// Magnitude of force vector determined by pow

		double[] offset = {randSpread, randSpread, randSpread};
		for(int i = 0; i < 3; ++i) {
			offset[i] -= RandomUtils.insecure().randomDouble(
					0D,
					Math.min(syncedConfig.directionVariance, Math.nextDown(randSpread))
			);

			if(RandomUtils.insecure().randomInt(0, 2) == 0)
				offset[i] *= -1;
		}

		cir.setReturnValue(base.add(offset[0], offset[1], offset[2]).scale(pow));
	}
}