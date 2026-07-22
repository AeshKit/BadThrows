package aesh.kai.mixin;

import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Projectile.class)
public abstract class BadThrow {
	@Inject(method = "getMovementToShoot", at = @At(value = "RETURN"), cancellable = true)
	private void aesh$badThrow(double xd,
							   double yd,
							   double zd,
							   float  pow,
							   float  uncertainty,
	                           CallbackInfoReturnable<Vec3> cir) {

		Vec3 base = new Vec3(xd, yd, zd).normalize();
		double magnification = 0.0172275 * uncertainty;

		double[] offset = { magnification, magnification, magnification };
		for(int i = 0; i < 3; i++) {
			if(Math.random() >= 0.5) {
				offset[i] *= -1.0;
			}
		}

		cir.setReturnValue(base.add(offset[0], offset[1], offset[2]).scale(pow));
	}
}