<p align="center">
  <img src="assets/preview.gif" alt="Potions Preview GIF">
</p>
<h1 align="center">
  Potions
</h1>
<br>
<p align="center">
  <a href="https://github.com/hello-andrew-yan/paper-potions/commits/master">
    <img alt="GitHub last commit" src="https://img.shields.io/github/last-commit/hello-andrew-yan/paper-potions?style=flat-square""></a>
  <a href="https://github.com/hello-andrew-yan/paper-potions/issues">
    <img alt="GitHub issues" src="https://img.shields.io/github/issues-raw/hello-andrew-yan/paper-potions?style=flat-square""></a>
  <a href="https://github.com/hello-andrew-yan/paper-potions/pulls">
    <img alt="GitHub pull requests" src="https://img.shields.io/github/issues-pr-raw/hello-andrew-yan/paper-potions?style=flat-square""></a>
</p>
<p align="center">
 This is an independant and refactored implementation of a potions plugin I worked on in a previous server. This project prioritises improved modular design and configuration from its previous iteration.
</p>

---

## Implementation
### JSON Schema
This plugin utilises JSON schemas defined in the resources folder to ensure correct configuration files. There are two different objects you can incorporate: `Ingredient` and `Recipe`.

### Ingredient
The `Ingredient` class represents an ingredient that the Potions plugin can use during brewing. You can define an ingredient by implementing a `.json` file that corresponds with the defined `ingredient.schema.json`.

At its core, ingredients have an assigned `name`, `description`, `hex` colour, Minecraft `material` to represent the ingredient in-game, `customModelData` and an optional `subingredient`.

The name of an existing ingredient can be linked as a subingredient to the parent ingredient via the three enums: `CUT`, `CRUSH` and `STEW`. If the specified ingredient name doesn't exist or an existing ingredient with the specified enum is defined already, this won't be added as a linked subingredient.

#### Example Ingredient
```json
{
    "name": "Standard Ingredient",
    "description": "Nothing exciting. Just your normal ingredient...",
    "hex": "#5add6e",
    "material": "REDSTONE",
    "customModelData": 1,
    "subingredient": {
        "method": "CRUSH",
        "ingredient": "Crushed Standard Ingredient"
    }
}
```

### Recipe
The `Recipe` class represents a list of `Steps` that the Potions plugin follows to create a `Potion`. You can define an recipe by implementing a `.json` file that corresponds with the defined `recipe.schema.json`.

At its core, recipes have an assigned `name`, `description`, `hex` colour and a combination of `steps`. There are currently four step types to choose from: `INGREDIENT_STEP`, `HEAT_STEP`, `STIR_STEP` and `WAND_STEP`.

#### Ingredient Step
Requires the name of the `ingredient` and quantity `count` needed to proceed. In-game, ingredients a thrown into the cauldron automatically when the player's crosshair is selecting the cauldron block. You can either throw the items individually or in stacks.
```json
{
  "type": "INGREDIENT_STEP",
  "ingredient": "Lethe River Water",
  "count": 2
}
```
#### Heat Step
Requires a value from the following `heat` enum: `LOW`, `MEDIUM` and `HIGH`. In-game, changing the heat involves right-clicking the cauldron with a free hand. The heat will toggle through the enums linearly starting with a hidden `OFF` enum. 
```json
{
  "type": "HEAT_STEP",
  "heat": "HIGH"
}
```
#### Stir Step
Requires the stir `direction` from `CLOCKWISE` and `ANTICLOCKWISE` as well as the number of `rotations`. In-game, stiring the cauldron involves right-clicking the cauldron with a stick to stir `CLOCKWISE`. If you stir the cauldron while crouching, this will stir the cauldron `COUNTERCLOCKWISE`.
```json
{
  "type": "STIR_STEP",
  "direction": "CLOCKWISE",
  "rotations": 2
}
```

#### Wand Step
**TODO** - Since this needs implementation alongside the Magic plugin.


#### Example Recipe
```json
{
  "name": "Forgetfulness Potion",
  "description": "Causes an unknown degree of memory loss in the drinker. The recipe for this potion can be found in Magical Drafts and Potions.",
  "hex": "#fca849",
  "steps": [
    {
      "type": "INGREDIENT_STEP",
      "ingredient": "Lethe River Water",
      "count": 2
    },
    {
      "type": "HEAT_STEP",
      "heat": "HIGH"
    },
    {
      "type": "INGREDIENT_STEP",
      "ingredient": "Standard Ingredient",
      "count": 2
    },
    {
      "type": "HEAT_STEP",
      "heat": "LOW"
    },
    {
      "type": "INGREDIENT_STEP",
      "ingredient": "Powdered Mistletoe Berries",
      "count": 2
    }
  ]
}
```

## Contributing
Feel free to contribute to this project by submitting bug reports, feature requests, or pull requests on the [GitHub repository](https://github.com/hello-andrew-yan/paper-potions).

## License
This project is licensed under the [MIT License](LICENSE).

---

<p align="right"><a target="_blank" href="https://icons8.com/icon/auVXOEbHs0KH/potion">Potion</a> icon by <a target="_blank" href="https://icons8.com">Icons8</a></p>
