# SkWE Syntax

---

> Note that most proposed syntax that *might* conflict will be prefixed with a mandatory `(use (world[ ]edit|we) to|world[ ]edit|we)` to avoid conflicts.

> Also note that all the syntax, currently, is supported by both WorldEdit and FAWE. The only difference between the two is that SkWE will run the operations async if FAWE is found.

---

## Regions

Regions are *very* useful. They're essentially 3D shapes that most operations use to define where they work. They allow for much more flexibility than Skript's simple `blocks` expression, with new complex shapes.

They can be created by using the respective expression. So far, the implemented regions are: `cuboid`, `cylinder`, `ellipsoid` and `convex polyhedral`. These seem to be the most useful ones, and both WE and FAWE support them.

Cuboid regions have a couple of additional syntax that the other region types don't - there are expressions to get the walls and faces of a cuboid region, which actually return another region.

There seems to be a bug somewhere with serializing a convex polyhedral region (i.e. storing it in a global variable). While they do *sometimes* work, I wouldn't rely on SkWE being able to do this reliably.

For any more information on the following syntax, or just regions in general, the [WorldEdit region docs](https://worldedit.enginehub.org/en/latest/usage/regions/) would be the best resource. Just note that there is some stuff which SkWE doesn't support yet (e.g. WorldEdit CUI stuff, some of the operations, etc.).

### Constructor - Cuboid

Pattern(s): `[a] [new] [cuboid] region (between|within|from) %location% (and|to) %location%`

Example(s): `a new cuboid region from {_loc1} to {_loc2}`

### Constructor - Cylinder

Pattern(s): `[a] [new] cyl[ind(er|rical)] region at %location% with radi(i|us) %numbers% [and] with height %number%`

Example(s): `a new cylindrical region at player with radii (10,9) and with height 50`

### Constructor - Ellipsoid

Pattern(s): `[a] [new] (ellips(e|oid)|spher(e|ical)) region at %location% with radi(i|us) %numbers%"`

Example(s): `a new spherical region at player with radii (10,9,8)`

### Constructor - Convex Polyhedral

Pattern(s): `[a] [new] convex [poly[hedral]] region (at|with|using) %locations%`

Example(s): `a new convex polyhedral region using {_locs::*}`

### Operation - Center

Sets the block at the middle of the given region using a given pattern

Pattern(s): `create [a] block at [the] center of %worldeditregion% with [pattern] %itemtype/string/worldeditpattern%`

Example(s): `create a block at the center of {_region} with stone`

### Operation - Faces

Creates the faces (walls + ceiling + floor) of a given region using a given pattern. This effect works best with cuboid regions.

Pattern(s): `create [the] faces (of|around) %worldeditregion% ([made] out of|with [pattern]) %itemtype/string/worldeditpattern%`

Example(s): `create the faces of {_region} made out of "5%%stone,5%%oak_planks"`

### Operation - Flora

Creates flora (grass, flowers, etc.) in a given region with a given density (default is 5).

Pattern(s): `make flora within %worldeditregion% [with density %-number%]`

Example(s): `make flora within {_region} with density 10`

### Operation - Hollow

Hollows out a given region, leaving a border with a given thickness (default is 1) and replacing the hollowed out blocks with a given pattern (default is air).

Pattern(s): `hollow out %worldeditregion% [with thickness %-number%] [(with pattern|leaving behind) %-itemtype/string/worldeditpattern%]`

Example(s): `hollow out {_region} with thickness 2 with pattern glass block`

### Operation - Move

Moves the blocks in a given region in a given direction a certain distance (default is 1). Can choose to only move blocks that match a certain mask (default is none, i.e. the whole region), and can also allow moved blocks to be replaced using a given pattern (default is air). Can also choose to ignore air, copy entities, and copy biomes.

Pattern(s): `move [mask:blocks that match %-itemtype/string/worldeditmask% in] %worldeditregion% (0:up|1:down|2:north|3:south|4:east|5:west) [%-number% (time|block)[s]] [and fill the area with %-itemtype/string/worldeditpattern%] [air:while ignoring air] [entities:while copying entities] [biomes:while copying biomes]`

Example(s): `move blocks that match stone in {_region} up 50 blocks and fill the area with glass block while ignoring air while copying entities while copying biomes`

### Operation - Naturalize

Naturalizes a given region. This just sets the top 3 layers to grass/dirt, and sets the bottom layers to stone.

Pattern(s): `naturalize %worldeditregion%`

Example(s): `naturalize {_region}`

### Operation - Overlay

Places a layer of blocks using a given pattern on top of the ground blocks in a given region.

Pattern(s): `overlay [the] [top level of] blocks (in|of) %worldeditregion% with [pattern] %itemtype/string/worldeditpattern%`

Example(s): `overlay the top level of blocks in {_region} with diamond block`

### Operation - Regenerate

Regenerates the area within a given region. Can accept a seed and whether or not it should also regenerate biomes.

Pattern(s): `regen[erate] %worldeditregion% [with seed %-number%] [biomes:while regen[erat]ing biomes]`

Example(s): `regenerate {_region} with seed 123456789 while regenerating biomes`

### Operation - Smooth

Smooths a given region a given number of times. Can choose to only smooth blocks that match a certain mask.

Pattern(s): `smooth %worldeditregion% [%-number% time[s]] [with mask %-itemtype/string/worldeditmask%]`

Example(s): `smooth {_region} 10 times with mask stone`

### Operation - Walls

Creates the walls of a given region using a given pattern.

Pattern(s): `create [the] walls (of|around) %worldeditregion% ([made] out of|with [pattern]) %itemtype/string/worldeditpattern%`

Example(s): `create the walls around {_region} made out of stone`

### Property - Center

Gets the centre of a given region as a location.

Pattern(s):
- `[the] region cent(re|er) of %worldeditregion%`
- `[the] %worldeditregion%'[s] region cent(re|er)`

Example(s):
- `send region centre of {_region}`
- `add {_region}'s centre to {centres::*}`

### Property - Vertices

Gets the vertices of a given region as a list of locations.

Pattern(s):
- `[the] region vert(ices|exes) of %worldeditregion%`
- `[the] %worldeditregion%'[s] region vert(ices|exes)`

Example(s):
- `send region vertices of {_region}`
- `add {_region}'s vertexes to {vertexes::*}`

### Property - Volume

Gets the volume of a given region.

Pattern(s):
- `[the] region volume of %worldeditregion%`
- `[the] %worldeditregion%'[s] region volume`

Example(s):
- `send region volume of {_region}`
- `add {_region}'s volume to {totalVolume}`

### Property - Faces

Gets the faces of a given region as another region.

Pattern(s):
- `[the] [region] faces of %worldeditregion%`
- `[the] %worldeditregion%'[s] [region] faces`

Example(s):
- `send the region faces of {_region}`
- `use we to set blocks in ({_region}'s faces) to stone`

### Property - Walls

Gets the walls of a given region.

Pattern(s):
- `[the] [region] walls of %worldeditregion%`
- `[the] %worldeditregion%'[s] [region] walls`

Example(s):
- `send the region walls of {_region}`
- `use we to replace all stone in ({_region}'s walls) with air`

---

## Patterns

Patterns are also very useful. Pretty much any syntax which modifies blocks in any way will use a pattern to define what the blocks will be changed to.

There is an expression to create a pattern object from a string or itemtype, which allows for both simple patterns (e.g. just single block patterns), but also more complex patterns (e.g. `"5%stone,5%oak_planks`).

The only limitation of this, to my knowledge, are clipboard patterns, which just don't work currently.

For more information regarding patterns, I'd recommend having a look at the [WorldEdit pattern docs](https://worldedit.enginehub.org/en/latest/usage/general/patterns/).

For help creating a pattern, this [Game Patterning System](https://gps.enginehub.org/) (by the same people that made WorldEdit) is simple and useful.

---

## Masks

Masks are useful too, but are used less. They define which blocks in an operation will actually be modified.

Like patterns, there is an expression to get a mask object.

Again, the [WorldEdit mask docs](https://worldedit.enginehub.org/en/latest/usage/general/masks/) are the best source for information on these.

---

## Blocks

### Setting blocks in a region

Sets the blocks in a given region using a given pattern.

Pattern(s): `set blocks in %worldeditregion% to %itemtype/string/worldeditpattern%`

Example(s): `set blocks in {region} to "5%%oak_planks,5%%diamond_block"`

### Replacing blocks in a region

Replaces the blocks in a given region that match a given mask using a given pattern.

Pattern(s):
- `replace [all] blocks in %worldeditregion% that match [mask] %itemtype/string/worldeditmask% with [pattern] %itemtype/string%`
- `replace all %itemtype/string/worldeditmask% in %worldeditregion% with %itemtype/string/worldeditpattern%`

Example(s):
- `replace all blocks in {region} that match stone with coal block`
- `replace all stone in {region} with coal block`

---

## Shapes

### Generic
Pattern(s): `create [a] %*worldeditshape% at %locations%`

Example(s): `create a circle at player`

* This is a section which has entries based on which literal shape is passed in
* There are also, as seen just below, effects to create each shape which more explicitly outline the parameters
* Shapes that have potentially multiple radii allow a list of numbers to facilitate that option
* **Note for later**: all the default values for undefined properties should be changed to a config option later (or just fall through silently)

### Specific - Sphere

Pattern(s): `create [a] [:hollow] (sphere|ellipsoid) ([made] out of|with [pattern]) %itemtype/string/worldeditpattern% [with radi(us|i) %-numbers%] at %locations%`

Example(s): `create a hollow sphere made out of stone with radius (10,9,8) at player`

If no radii are provided, the effect will use a default of 5

### Specific - Cylinder

Pattern(s): `create [a] [:hollow] cylinder ([made] out of|with [pattern]) %itemtype/string/worldeditpattern% [with radi(us|i) %-numbers%] [with height %-number%] at %locations%`

Proposed example: `create a hollow cylinder out of stone with radii (10,9) with height 50 at player`

If no radii are provided, the effect will default to 5\
If no height is provided, the effect will default to 1

### Specific - Pyramid

Proposed syntax: `create [a] [:hollow] pyramid ([made] out of|with [pattern]) %itemtype/string/worldeditpattern% [with size %-number%] at %locations%`

Proposed example: `create a hollow pyramid made out of stone with size 3 at player`

If no size is provided, the effect will default to 5

---

## Coming soon

-[ ] Schematic support (saving, pasting, deleting, maybe more?)
-[ ] More region stuff (moving, contracting, shifting, creating lines/curves, etc.)
-[ ] More flexibility with some of the already-existing region effects (e.g. choosing to adjust the region after using EffMove)
-[ ] Generating arbitrary shapes (i.e. ones defined by a WorldEdit expression)
-[ ] Generating forests and pumpkin patches
-[ ] Support for some FAWE-only features (e.g. some new shape and region types, etc.)
-[ ] Get a player's selection as a region
-[ ] Navigation syntax (i.e. thru, ascend, descend)
-[ ] Utility syntax (i.e. drain, simulate snowfall, fill, fix water/lava, thaw, simulate grass, and extinguish)