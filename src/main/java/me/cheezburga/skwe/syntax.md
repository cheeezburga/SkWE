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

To a clipboard? Should a clipboard be a Skript type?
Probably not, but something to consider I suppose?

### Pasting a schematic

### Saving a schematic

As a certain file type? To a certain directory?

### Deleting a schematic file


## Unsorted stuff atm
WALLS? SHOULDNT GO IN SHAPES SURELY?
```applescript
		# schematics | should be undoable too?
		load schematic "schem" into {_x}
		paste schematic {_x} as player

		save blocks within {1} and {2} as schematic "asdf"
		delete schematic "asdf" # will this be a problem if its called in the same tick?

		# history
		send last 5 global operations
		send last 5 operations of player
		clear history of player

		# properties of player's selection
		send volume of player's selection
		send world of player's selection
		send center of player's selection
		send chunks of player's selection
		send height/length/width of player's selection
```

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