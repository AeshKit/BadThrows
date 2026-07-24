<p align="center">
  <img 
    src="https://raw.githubusercontent.com/AeshKit/BadThrows/main/iconBIG.png"
    width="360"
    style="image-rendering: pixelated;">
</p>

# Bad Throws

A Fabric mod that always returns extreme projectile vector values.

### How ?

When choosing what velocity to give a projectile, Minecraft does the following:
```
public Vec3 getMovementToShoot(final double xd, final double yd, final double zd, final float pow, final float uncertainty) {
    return (new Vec3(xd, yd, zd)).normalize().add(this.random.triangle((double)0.0F, 0.0172275 * (double)uncertainty), this.random.triangle((double)0.0F, 0.0172275 * (double)uncertainty), this.random.triangle((double)0.0F, 0.0172275 * (double)uncertainty)).scale((double)pow);
}
```
The above code adds an offset with a triangle distribution of bounds *0.0172275 \* uncertainty* in each axis. See the [Minecraft Wiki](https://minecraft.wiki/w/Projectile#Shot_from_dispenser) for uncertainty values per-projectile.

This creates a cube of possible vector end-points, where values closer to the center are linearly more likely. With the mod, every projectile throw's angle vector will be on one of the corners of the cube, emulating a theoretical unlucky throw.

### Why ?

Most pearl or arrow lineups are luck-dependent. With a bad throw, the whole machine can break. When creating throw lineups or capturing shot / dispensed arrows, make sure that it works, even with a bad throw.

# Installation

To install, grab the latest .jar from the [releases page](https://github.com/AeshKit/BadThrows/releases/) [ NOT sources ], and copy it into the `/mods/` folder of your Minecraft instance

You can also install this mod directly on [CurseForge](https://www.curseforge.com/minecraft/mc-mods/bad-throws/) and Modrinth [ Once approved ]

Please note that this mod is only compatable with the Fabric Loader, for now.

# License

```
Copyright (C) 2026  AeshKit

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; version 2.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, see
<https://www.gnu.org/licenses/>.
```
