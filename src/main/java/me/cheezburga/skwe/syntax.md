# Syntax ideas (v1)
> **NOTE** - all proposed syntax will be prefixed with a mandatory `(use (world[ ]edit|we) to|world[ ]edit|we)`, so as to avoid conflicts.

There aren't any ways to create or define a region in these syntax yet.
See v2 for a more proper implementation that had more thought go in to it.

## Blocks

### Setting blocks in a region
Proposed syntax: `set [all] blocks (within|from) %location% (to|and) %location% to %itemtype/string% [as %-player%]`

Proposed example: `set blocks within {1} and {2} to "5%%oak_planks,5%%diamond_block" as player("cheezburga")`

* Will probably use an EditSession and call `setBlocks(region, pattern)`
* Should being able to create a region and pattern be an option (maybe an effect section or smth?)

### Replacing blocks in a region
Proposed syntax: `replace [all] blocks (within|from) %location% (to|and) %location% that match [mask] %itemtype/string% with [pattern] %itemtype/string% [as %-player%]`

Proposed example: `replace all blocks within {1} and {2} that match stone with "5%%oak_planks,5%%diamond_block"`

* Mostly the same as setting blocks in a region in terms of implementation, but just accepts a mask too
* So just call `replaceBlocks(region, mask, pattern)` instead
* If being able to create a region and pattern is going to be an option, the same should be available for masks

### Undo/redo operations
Proposed syntax: `(un|re)do [the] last [%-number%] [worldedit] (operations|edits) [[performed] by %-player%]`

Proposed example: `undo the last 5 operations performed by player("cheeezburga")`

* Currently, all operations will be remembered by the addon's LocalSession, so un/redoing globally is a possibility
* Is this even wanted tho? Seems like it could be unsafe

## Shapes

Shapes, to my knowledge, also just use an EditSession and call their respective methods (e.g. `makeCylinder(loc, pattern, radiusX, radiusZ, height, hollow)` for a cylinder).

Thinking that maybe each shape should have its own effect section and each parameter to its respective function would be included as an entry.

### Generic
Proposed syntax: `create [a] %*worldeditshape% %directions% %locations%`

Proposed example: `create a circle at player("cheeezburga")`

* These should be effect sections which have entries based on which shape is passed in as the literal shape
* If it's not used as a section, then default values for each of the entries should be assigned (maybe as config options?)
* If it is, then the appropriate entries should be accepted and it should use them when calling the respective method
* Not sure if the `%directions%` part is super necessary, only included it because that's what EffSecSpawn does
* For shapes that have a radius (which I think is all of them), maybe allow multiple numbers for x,y,z?

### Specific - Sphere

Proposed syntax: `create [a] [:hollow] sphere [([made] out of|with [pattern]) %-itemtype/string%] [with radius %-number%] %directions% %locations%`

Proposed example: `create a hollow sphere made out of stone with radius 10 at player`

Parameters to the `EditSession#makeSphere` method: `location`, `pattern`, `radius (x,y,z)` and `hollow`

### Specific - Cylinder

Proposed syntax: `create [a] [:hollow] cylinder [([made] out of|with [pattern]) %-itemtype/string%] [with radius %-number%] [with height %-number%] [with thickness %-number%] %directions% %locations%`

Proposed example: `create a hollow cylinder out of stone with radius 10 with height 50 with thickness 2 at player`

Parameters to the `EditSession#makeCylinder` method: `location`, `pattern`, `radius (x,y,z)`, `height`, `thickness` and `hollow`

### Specific - Cone

Proposed syntax: `create [a] [:hollow] cone [([made] out of|with [pattern]) %-itemtype/string%] [with radius %-number%] [with height %-number%] [with thickness %-number%] %directions% %locations%`

Proposed example: `create a hollow cone made out of stone with radius 5 with height 10 with thickness 1 at player`

Parameters to the `EditSession#makeCone` method: `location`, `pattern`, `radius (x,z)`, `height`, `thickness` and `hollow`

### Specific - Pyramid

Proposed syntax: `create [a] [:hollow] pyramid [([made] out of|with [pattern]) %-itemtype/string%] [with size %-number%] %directions% %locations%`

Proposed example: `create a hollow pyramid made out of stone with size 3 at player`

Parameters to the `EditSession#makePyramid` method: `location`, `pattern`, `size` and `hollow`

## Schematics

### Loading a schematic

Proposed syntax: `load schem[atic] %string% (in|to|into) (%object%|%players%'[s] clipboard)`

Proposed example: `load schematic "asdf" into {_asdf}`

* Should loading a schematic into a variable cache it, so it could available at a later time?
* Maybe there should be a separate effect to cache them, if the user wants (e.g. if they want to use them often)?
* But what happens then if the schematic changes, or is deleted, etc.?

### Pasting a schematic

Proposed syntax: `(paste|place) schem[atic] %string% at %locations% [as %-player%]`

Proposed example: `paste schematic "asdf" at player's location as player`

* Should probably allow rotations and masks and stuff
* Should be remembered by the global LocalSession as well?
* Maybe even have a 2nd global LocalSession specifically for schematics?

### Saving a schematic

Proposed syntax: `save blocks within %location% and %location% to [a] [new] schem[atic] named %string%`

Proposed example: `save blocks within {1} and {2} to a new schematic named "adsf"`

* As a certain file type? Will research this
* To a certain directory? Will research this
* What happens though if there's already a schematic with that name? Should it silently fail, should it override, should it override but save a copy of the one being overwritten elsewhere?

### Deleting a schematic file

Proposed syntax: `delete schem[atic] %string%`

Proposed example: `delete schematic "asdf"`

* Should mostly be fine, but what if we go down the caching path, what if it's been cached?

## Selection

Most (if not all) of these are just getters on a player's selection.

The properties that should be gettable are: `volume`, `world`, `center`, `chunks` and `height/length/width`.

These should all probably be in the one expression, similar to how SkBee handles BossBar properties.

## Clipboard manipulation

### Make player copy their selection

Proposed syntax: `make %players%'[s] copy their selection[s]`

Proposed example: `make player copy their selection`

* Should just silently fail if their selection is incomplete

### Load schematic into clipboard

See [Loading a schematic](#loading-a-schematic). Might need some further thought but that's how its currently planned.

### Clear a player's clipboard

Should this just be an expression which has the reset changer allowed?
Might require that `clipboard` be a Skript type?

Proposed syntax: `clear %players%'[s] clipboard`

Proposed example: `clear player's clipboard`

## History manipulation

Should you be able to like, get a player's history as a Skript type? Could allow for some cool comparisons maybe, and would allow for debugging stuff like `send last 5 operations by player`.

### Clear a player's history

Similar to the clipboard one, should this also be an expression?

Proposed syntax: `clear %players%'[s] history`

Proposed example: `clear player's history`

## Unsorted

### Walls

Just not really sure where this belongs at the moment. Should definitely be a thing though.

Most of the syntax should allow a specified player(s) to remember the actions. Some of these are missing at the moment, but stuff like shapes should, etc.

# Not related to syntax

#### A function to (maybe?) test a certain effects' async-ness.

```applescript
# function to test its async-ness?

on load:
    set {sends::*} to "a", "as", "asd", "asdf"

function send():
    loop 250 times: # should be 1000 in total with 4 in {sends::*}
    loop {sends::*}:
    broadcast loop-value-2
    wait 1 tick
```