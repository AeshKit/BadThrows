<p align="center">
  <img 
    src="https://raw.githubusercontent.com/AeshKit/BadThrows/main/iconBIG.png"
    width="360"
    style="image-rendering: pixelated;">
</p>

# Bad Throws

A Fabric mod that always returns extreme projectile & item force vector values.

### How ?

When choosing what velocity to give a projectile or item entity, Minecraft has random variance for each initial force vector given to the object, creating a cube of possible vector end-points where values closer to the center are linearly more likely.

This mod affects the force vectors of:
* *Player death item scatters*
* *Dispenser & dropper item & projectile drops*
* *Item & tile drops*
* *Thrown projectiles*

This mod chooses the most extreme, theoretically possible values for these vector end-points, with some variation according to the config if so desired. Each aspect of the mod is toggleable per-server. Note that players require permission level 2 to modify the server config in the client-side mod menu.

### Why ?

Most pearl or arrow lineups, as well as systems using dropped items are luck-dependent. With a bad throw, the whole machine can break. When using these mechanics in-game, make sure that it works, even with a bad throw.

# Installation

To install, grab the latest .jar from the [releases page](https://github.com/AeshKit/BadThrows/releases/) [ NOT sources ], and copy it into the `/mods/` folder of your Minecraft instance

You can also install this mod directly on [Modrinth](https://modrinth.com/mod/badthrows) and [CurseForge](https://www.curseforge.com/minecraft/mc-mods/bad-throws/)

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
