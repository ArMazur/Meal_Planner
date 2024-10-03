package mealplanner;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MealPlanner {

    private static List<Meal> mealList = new ArrayList<>();
    private static final Database DB = new Database();
    private final Plan plan = new Plan();

    public MealPlanner() {
        updateMealList();
        menuAction();
    }

    public void menuAction() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("What would you like to do (add, show, plan, list plan, save, exit)?");
            String input = scanner.nextLine();
            if ("exit".equals(input)) break;

            switch (input) {
                case "add":
                    add();
                    break;
                case "show":
                    show();
                    break;
                case "plan":
                    plan();
                    break;
                case "list plan":
                    listPlan();
                    break;
                case "save":
                    saveToFile();
                    break;
            }
        }
        System.out.println("Bye!");
    }

    private void updateMealList() {
        mealList = DB.readDBMeals();
    }

    public static List<Meal> getMeals() {
        return mealList = DB.readDBMeals();
    }

    public void add() {
        Meal meal = new Meal();
        if (meal.createMeal()) {
            meal.setMeal_Id();
            DB.addMeal(meal);
            updateMealList();
            System.out.println("The meal has been added!");
        }
    }

    private boolean checkCategory(String category) {
        var allowedCategories = Meal.getAllowedCategories();
        if (!allowedCategories.contains(category)) {
            System.out.println("Wrong meal category! Choose from: breakfast, lunch, dinner.");
            return false;
        }
        return true;
    }

    public void show() {
        updateMealList();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Which category do you want to print (breakfast, lunch, dinner)?");
        String category;
        boolean allowed;
        do {
            category = scanner.nextLine();
            allowed = checkCategory(category);
        } while (!allowed);

        String c = category;
        var categoryList = mealList.stream()
                .filter(e -> e.getCategory().equals(c))
                .toList();

        if (categoryList.isEmpty()) {
            System.out.println("No meals found.");
        } else {
            System.out.println("Category: " + c + "\n");
            categoryList.forEach(System.out::println);
        }

    }

    private void plan() {
        plan.createNewPlan(mealList);
    }

    private void listPlan() {
        Plan.listPlan();
    }

    public static Database getDb() {
        return DB;
    }

    private void saveToFile() {

        var plans = plan.getPlans();

        if (plans.isEmpty()) {
            System.out.println("Unable to save. Plan your meals first.");
        } else {
            ShoppingListManager shoppingListManager = new ShoppingListManager(getMeals(), plans);
            Scanner scanner = new Scanner(System.in);
            System.out.println("Input a filename:");
            String fileName = scanner.nextLine();
            shoppingListManager.saveToFile(fileName);
        }

    }
}
