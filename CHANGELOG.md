- 1.3.7 (31 Aug. 2020): Fixed compatibility with LibBlockAttributes, should improve overall compatibility as well. 
- 1.3.6 (26 Aug. 2020): Added 语言 (Simplified Chinese, zh_cn) translation from [lechiny](https://www.curseforge.com/members/lechiny/projects) on CurseForge. 
- 1.3.5 (26 Aug. 2020): Added Español (Spanish, es_es) translation from [Luis Llanos](https://gitlab.com/llrluis) on GitLab and русский язык (Russian, ru_ru) translation from [kazmurenko](https://www.curseforge.com/members/kazmurenko/projects) on CurseForge. 
- 1.3.4 (26 Aug. 2020): Switched from using `ItemStack.getRecipeRemainder` to `CraftingRecipe.getRemainingStacks`. 
  This fixes compatibility with [Flamin' Hot](https://www.curseforge.com/minecraft/mc-mods/flamin-hot), and other mods with recipes that extend `CraftingRecipe` and `@Override getRecipeRemainder`.
