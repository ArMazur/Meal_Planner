package mealplanner;

import java.util.*;

public class ShoppingList {

    private List<String> ingredients;
    private Map<String, Integer> ingredientAmountMap;

    public ShoppingList(List<String> ingredients) {
        this.ingredients = ingredients;
        buildIngredientAmountMap();
    }

    private void buildIngredientAmountMap() {
        ingredientAmountMap = new HashMap<>();

        for (var i : ingredients) {
            if (!ingredientAmountMap.containsKey(i)) {
                ingredientAmountMap.put(i, 1);
            } else {
                int currAmount = ingredientAmountMap.get(i);
                ingredientAmountMap.replace(i, currAmount, currAmount + 1);
            }
        }
    }

    public List<String> buildShoppingList() {
        List<String> shoppingList = new ArrayList<>();
        ingredientAmountMap.forEach((ingredient, amount) -> {
            String line = amount == 1 ? ingredient : ingredient + " x" + amount;
            shoppingList.add(line);
        });
        var sortedList = shoppingList.stream().sorted().toList();
        return sortedList;
    }


}
