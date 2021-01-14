This mod is for the [Fabric modloader](https://www.fabricmc.net/) and Minecraft 1.16.1. It depends on the [Fabric API](https://www.curseforge.com/minecraft/mc-mods/fabric-api/files/all?filter-game-version=1738749986%3a70886). 

Download the mod on [CurseForge](https://www.curseforge.com/minecraft/mc-mods/automated-crafting)!

It's required the mod be installed on both client and server. 

It adds a single block: the Automated Crafting Table

![Automated Crafting Table](https://shft.cl/img/l/lh6.googleusercontent.com-4257019419025668.png)

Basically, you insert items (with hoppers and droppers) so that a recipe pattern is in its inventory, then give it a redstone signal and it crafts the recipe. 

It has three configs:
- simple mode (default = true)
- enable quasi-connectivity(default = false)
- redirect redstone dust (default = false)


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

Enabling redstone dust redirection make redstone dust next to the Automated Crafting Table point into the table. 

![redstone redirection](https://lh3.googleusercontent.com/htZi6YomFySjcSAnroeg9vps6a4smwcjK-Tw_a8ONQpXVSnd4Sr731sryXJFixfU9kyROxpdE1qfVLVdTm2oLsQxDf-tvaAca8Yp0TvXpp52gLlAS_K9AlievEYPfQOnjREBocxXmWP6JWy-DwbHbQEuZ4emWITJwS2TrG2OOUjTZjQAjp15TY0GI1CXacWEHXbpx4RajjH_e-1P9VCFpwPXn5oDfwMBgKSYu8ejNRLPrYAP2821rmvb8CXnwpZHwkOPX86uqviD534bcZVrkHZom4WNolpS9Ga9cbByBTO054htkFN5kBfDzwXTQx8SteFecEU20j8kVnvOO79IWKnFMUbFwycfVfbAKKMYh-HY9brfv965dC1ngWi3VCGZlgstXxc6BbmdMdRKsBVZj8RdGuwFg-zeZ8RhIbPYyu7pzpIz2x9kN1LVmt2ENwxFMgvNzewcUDXZ4WyD6YP8N-aVqIU6yFYZVkOJGtO7TUc_e8Nw-30QgUBXRvXb3DVW-OKm1cZM0xHNgH4lFzn6lCeIm82cksojeLDuUFNFRg2bVPSmBWwWH4oYAjxK-kLceezwVNNux2UudbuLpFEaJ67Feq4gVjHmMrgqmtDN8Pud39BzUE9kB9UcMcoxR0atW7rNh26WJiJEKj4v2tNpnxU2zTjLsl8GgOhdtNVZ55SbJ0_xxw6VeXDCwakeTRHwlOG1=w800-h500-k-ft)

Here is the recipe. Any stripped wood or logs may be substituted for those shown. 

![recipe](https://lh3.googleusercontent.com/yMvEWd7sOwhxDpsvlEQBXdFwX-fpwG_T3tlITGYNfR6CM2s3s55X9sudybASEHdABbx5g2D1TiUaM0Iv2BbMgYoD2WrjN3pY5LPYOZooKUfJ8nHv6KZQYHvBTeJxoRR9OmNuEPp1PZ14cGbeFe8HWWVKOfThhd3spf_Xr2qmjnD8kX8I0ig_x83IyRwQd6kg7adwtqyPWw7TPkmqZS6Mt5WMQTjVvPJJI1MB5ay5vx4oer-hR_W69_qAiGgpNrL9vCsM841SzFLz6hmWKhwpJHb56vM8T_4QOH2j1TdJUpQBl_Z0mocSdKp2OZbK0mmgKc_h6EY5mnkDD8VE3a_QaYWo_-BemUGUeUdcrjK0FtBX_VTkckVTN4InuOGd4HhkiyUSgbDjspuhoJJRP3HbOB0CPwXmgIVpTcPJRwBTYYWED0EaxEB_f68SyfXquifo-Xd51CDdFMMvaRKYY7atVm0YJWkdA61hxKcaeQSoJ2ecjGyJ6chtMaAbOOaPYI-OwYRRvjQ7ZNeIQCCEY6XJFGqjAAF3RI0KoUJvNZJtYIYkJmL6ZZk88Dr2SeH3w8-kmHUiT-ElGtSIVVPmfWDdVijYZ-0IJOvS6aB2msOUa2qgTzTMgdscwpa4ez97gF9ryosw_bcqQML0ejzaJhRa2Ep-LC_e7gLnxk3KL4ztlUR9An7E8lq4-2yOlBu6gM4qTOB9=w800-h500-k-ft)

Download the [Fabric API for Minecraft 1.16.1 here](https://www.curseforge.com/minecraft/mc-mods/fabric-api/files/all?filter-game-version=1738749986%3a70886)

![Requires the Fabric API](https://i.imgur.com/Ol1Tcf8.png)

If you'd like to translate Automated Crafting to another language (there are only a few lines of text), learn how on this wiki page. 

This mod is only for Fabric and I won't be porting it to Forge. The license is [MIT](https://will-lucic.mit-license.org/), however, so anyone else is free to port it. 

I'd appreciate links back to this page if you port or otherwise modify this project, but links aren't required. 
