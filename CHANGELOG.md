- 1.3.12-beta.1 (15 Oct. 2020): Fixed comparator output so that it gives equal weight to input and output in both simple and complex mode. 
  Known issue: Settings do not sync from server to client, leading to strange behavior when client settings don't match. For now just make sure they match; I'm working on a solution. 
- 1.3.11 (18 Sep. 2020): Marked as compatible with 1.16.3. 
- 1.3.10 (2 Sep. 2020): Auto crafter now actually requires input to match template. 
- 1.3.9 (1 Sep. 2020): Fixed `indexOutOfBounds` exception that occurred when 'Simple Mode' was disabled. 
  If 'Craft continuously while powered' is disabled and an auto crafter is powered continuously, the auto crafter will no longer try to craft every time it receives a block update. 
  If you have a resourcepack that changes the autocrafter's texture, it's likely it will need to be adjusted after this update. 
  I've created two alternate resourcepacks, see 'additional files' for this update if you're interested in these. 
- 1.3.8 (1 Sep. 2020): Fixed startup crash. Made sure the mod works outside my dev environment (sorry about that). 
- 1.3.7 (31 Aug. 2020): Fixed compatibility with LibBlockAttributes, should improve overall compatibility as well. 
- 1.3.6 (26 Aug. 2020): Added 语言 (Simplified Chinese, zh_cn) translation from [lechiny](https://www.curseforge.com/members/lechiny/projects) on CurseForge. 
- 1.3.5 (26 Aug. 2020): Added Español (Spanish, es_es) translation from [Luis Llanos](https://gitlab.com/llrluis) on GitLab and русский язык (Russian, ru_ru) translation from [kazmurenko](https://www.curseforge.com/members/kazmurenko/projects) on CurseForge. 
- 1.3.4 (26 Aug. 2020): Switched from using `ItemStack.getRecipeRemainder` to `CraftingRecipe.getRemainingStacks`. 
  This fixes compatibility with [Flamin' Hot](https://www.curseforge.com/minecraft/mc-mods/flamin-hot), and other mods with recipes that extend `CraftingRecipe` and `@Override getRecipeRemainder`.
