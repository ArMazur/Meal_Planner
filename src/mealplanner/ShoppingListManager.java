package mealplanner;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class ShoppingListManager {

    private List<Meal> meals;
    private List<DayPlan> plans;
    private ShoppingList shoppingList;

    public ShoppingListManager(List<Meal> meals, List<DayPlan> plans) {
        this.meals = meals;
        this.plans = plans;
        createShoppingList();
    }

    public void createShoppingList() {

        List<String> ingredients = new ArrayList<>();

        var plannedMealNames = plans.stream()
                .map(DayPlan::mealName)
                .toList();

        var chosenMeals = meals.stream()
                .filter(meal -> plannedMealNames.contains(meal.getMealName()))
                .toList();

        chosenMeals.forEach(m -> {
            plannedMealNames.forEach(mealName -> {
                if (m.getMealName().equals(mealName)) {
                    ingredients.addAll(m.getIngredients());
                }
            });
        });

        shoppingList = new ShoppingList(ingredients);
    }

    public void saveToFile(String fileName) {

        File file = new File(fileName);
        var list = shoppingList.buildShoppingList();

        try (PrintWriter printWriter = new PrintWriter(fileName)) {

            list.forEach(printWriter::println);

        } catch (IOException e) {
            System.out.println("Error in when writing to file");
            System.out.println(e.getMessage());
        }
        System.out.println("Saved!");

//        try {
//            Files.deleteIfExists(file.toPath());
//            Files.write(file.toPath(), list, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
//
//        } catch (IOException e) {
//            System.out.println("Error when saving to file!");
//            System.out.println(e.getMessage());
//        }
//        System.out.println("Saved!");

    }


}
