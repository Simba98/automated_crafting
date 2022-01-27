- 1.4.3-a1 (27 Jan. 2022): Check Transaction API context in hopes of fixing mod incompatibility show in #30
- 1.4.2 (21 Dec. 2021):

  - Fixed nasty item duping when shift-clicking items out of the template in simple mode (thanks [Sirkilium](https://www.curseforge.com/members/sirkilium/) for reporting!)
  - Fixed strange behavior where trying to shift-click items into the auto crafter in simple mode would fill any empty 
    slots in the template with the shift-clicked item and put the item in the first empty slot of the input. 
    Now items simply can't be shift-clicked into the auto crafter in simple mode. 

- 1.4.1 (13 Dec. 2021): Updated for 1.18.1
- 1.4 (30 Nov. 2021): 
  - Updated for 1.18!
  - Configs can be adjusted through Mod Menu once again
  
- 1.4-b2 (28 Nov. 2021): Fixed critical bug that prevented recipes from crafting in simple mode if they contained any empty spaces.
- 1.4-b1 (28 Nov. 2021): Updated for 1.18 snapshot (pre6). 
  
  - this is a **beta** version; there have been significant internal changes and so there's a higher-than-normal chance that bugs have been introduced
  - configs now take effect without the need for a restart!
  - in simple mode, the template inventory is now virtual: it doesn't actually contain items. It remembers and displays the items you click on it with, but you don't lose those items.
  - because of the change listed above **you will lose any items** placed in the template area *in previous versions*. If you want to keep your template items, you'll need to manually remove them **before updating**. 
  - in simple mode, players can now place items into the input area if they match the template+

- 1.3.24 (9 Jul. 2021): 
  
  - Updated bundled libgui version
  - Marked as compatible with 1.17.1
  
- 1.3.23 (13 Jun. 2021): Now includes CrowdinTranslate again for translation fetching. 
- 1.3.22 (11 Jun. 2021): 

  Updated mod for 1.17. Cloth Config and Autocofig are no longer bundled, instead you must download them separately.
  
  Temporarily disabled automatic translation fetching while I wait for the library that handles this to update. 
  
  I've included the latest versions of translations using the vanilla translation system.  
  
- 1.3.21 (22 Feb. 2021): Hoppers now pull from the output slot before they pull from input slots in non-simple mode. 
  
  Moved Automated Crafting Table to the redstone item group (creative tab). 
  
  Cleaned up+optimized code a little. 
  
- 1.3.20 (15 Jan. 2021): Marked as compatible with 1.16.5. 
- 1.3.19 (10 Jan. 2021): Fixed incorrect output with special crafting recipes (e.g. firework rockets, dyed leather armor). 
- 1.3.18 (9 Jan. 2021): Automated Crafting Table now responds to quick redstone pulses, even 0-ticks! (texture won't change for 0-ticks)
- 1.3.17 (8 Jan. 2021): Added new option "Comparator reads 15 if output isn't empty" that defaults to false/No (this means existing crafting systems won't break with this update). 

  If you use Fabric API `0.28.4` or later, alternate resourcepacks will be automatically made available (disabled by default). 
  
  You can still use older versions of Fabric API, you just won't automatically get the resourcepacks. You can still download the resourcepacks from the repo. 

- 1.3.16 (29 Dec. 2020): Merged a fix by [kas](https://gitlab.com/exactly-one-kas) that makes it so other mods can 
  actually know the autocrafter's max stack size. Sorry I missed that, and thanks to kas for this fix and helping me 
  work out these mod compat issues in general. 
  
- 1.3.15 (28 Dec. 2020): Removed erroneous check for limiting slots to one item per stack. 
  This should improve compatibility with other mods that insert more than one item at a time, 
  but it relies on those other mods respecting vanilla Minecraft's conventions. 
  If a mod inserts more than one item into the input grid, please report the issue to the other mod's developer. 
  
- 1.3.14 (6 Dec. 2020): Changed comparator output (for the last time, promise), it's now equal to the number of filled input slots. 
  Translations are now handled with [CrowdinTranslate](https://github.com/gbl/CrowdinTranslate), go [here](https://crowdin.com/project/automated-crafting) if you'd like to contribute a translation. 
  "Template" and "Input" (in simple mode) can now be translated. 
  
- 1.3.13 (3 Nov. 2020): Marked as compatible with 1.16.4, updated dependencies. 
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
