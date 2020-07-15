This mod is for the [Fabric modloader](https://www.fabricmc.net/) and Minecraft 1.16.1. It depends on the [Fabric API](https://www.curseforge.com/minecraft/mc-mods/fabric-api/files/all?filter-game-version=1738749986%3a70886). 

Download the mod on [CurseForge](https://www.curseforge.com/minecraft/mc-mods/automated-crafting)!

It's required the mod be installed on both client and server. 

It adds a single block: the Automated Crafting Table

![Automated Crafting Table](https://shft.cl/img/l/lh4.googleusercontent.com-3729709478633236.png)

Basically, you insert items (with hoppers and droppers) so that a recipe pattern is in its inventory, then give it a redstone signal and it crafts the recipe. 

It has two configs:

- simple mode (default = true)

- enable quasi-connectivity(default = false)

These configs can be changed either using Mod Menu (if it's installed), or by editing '.minecraft/config/automated_crafting.json'

In simple mode, the crafting bench has two 3x3 grids

![Simple Mode GUI](https://shft.cl/img/l/lh3.googleusercontent.com-3729351876759961.png)

- The left grid is the 'Template' grid. Only players can interact with it. This is where your recipe pattern goes. 

- The right grid is the 'Input' grid. Only non-players (hoppers+droppers etc.) can insert to it, and it limits item stacking to 1. Items will be inserted so that they match the template. 

In non-simple mode, there's only one 3x3 grid. Hoppers and players can both insert+extract from the grid. 

![Less-simple Mode GUI](https://shft.cl/img/l/lh6.googleusercontent.com-3729755174563747.png)

This mode is very similar to the autocrafter [showcased here](https://www.youtube.com/watch?v=2_HL309IZ0M).

Both modes feature a single output slot that hoppers can pull from. This is the only slot hoppers can pull from in simple mode. 

![quasi-connectivity](https://shft.cl/img/l/lh3.googleusercontent.com-3729789011976985.png)

Quasi-connectivity allows the Automated Crafting Table to be powered from blocks farther above it, in the same way pistons, droppers, and dispensers behave in vanilla. 

The slabs mark positions that can only power the Crafter if quasi-connectivity is enabled. 

Download the [Fabric API for Minecraft 1.16.1 here](https://www.curseforge.com/minecraft/mc-mods/fabric-api/files/all?filter-game-version=1738749986%3a70886)

![Requires the Fabric API](https://i.imgur.com/Ol1Tcf8.png)

If you'd like to translate Automated Crafting to another language (there are only a few lines of text), learn how on this wiki page. 