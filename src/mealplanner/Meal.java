package mealplanner;

import java.util.*;

public class Meal implements Comparator<Meal> {

    private static final Scanner SCANNER = new Scanner(System.in);
    private static int last_id = 1;
    private int meal_id;
    private String category;
    private String mealName;
    private List<String> ingredients = new ArrayList<>();
    private static final List<String> allowedCategories = List.of("breakfast", "lunch", "dinner");

    public Meal() {
    }


    private Meal(int meal_id, String category, String mealName, List<String> ingredients) {
        this.meal_id = meal_id;
        this.category = category;
        this.mealName = mealName;
        this.ingredients = ingredients;
    }

    public Meal createDBMeal(int meal_id, String category, String mealName, List<String> ingredients) {
        if (checkCategory(category)) {
            if (checkInput(mealName)) {
                var sb = new StringBuilder();

                for (var i : ingredients) {
                    var index = ingredients.indexOf(i);
                    if (index == (ingredients.size() - 1)) {
                        sb.append(i);
                        break;
                    }
                    sb.append(i);
                    sb.append(", ");
                }

                if (checkIngredients(sb.toString())) {
                    return new Meal(meal_id, category, mealName, ingredients);
                }
            }
        }
        return null;
    }

    public int getMealId() {
        return meal_id;
    }

    public void setMeal_Id() {
        this.meal_id = ++last_id;
    }

    public static void setLastMealId(int last_id) {
        Meal.last_id = last_id;
    }

    public String getMealName() {
        return mealName;
    }

    public String getCategory() {
        return category;
    }

    public static List<String> getAllowedCategories() {
        return allowedCategories;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public boolean createMeal() {
        System.out.println("Which meal do you want to add (breakfast, lunch, dinner)?");
        category = SCANNER.nextLine().toLowerCase();
        if (checkCategory(category)) {
            System.out.println("Input the meal's name:");

            do {
                mealName = SCANNER.nextLine().toLowerCase();
            } while (!checkInput(mealName));

            System.out.println("Input the ingredients:");
            String inputIngredients = SCANNER.nextLine();

            while (!checkIngredients(inputIngredients)) {
                inputIngredients = SCANNER.nextLine();
            }

            ingredients = Arrays.stream(inputIngredients.split(","))
                    .map(String::trim)
                    .toList();
            return true;
        }
        return false;
    }

    private boolean checkCategory(String category) {
        if (!allowedCategories.contains(category)) {
            System.out.println("Wrong meal category! Choose from: breakfast, lunch, dinner.");
            this.category = SCANNER.nextLine().toLowerCase();
            checkCategory(this.category);
        }
        return true;
    }

    private boolean checkInput(String input) {
        String format = "^[a-zA-Z][a-z\\s]*$";

        if (!input.matches(format)) {
            System.out.println("Wrong format. Use letters only!");
        }

        return input.matches(format);
    }

    private boolean checkIngredients(String input) {
        var arr = input.split(",");
        for (var e : arr) {
            if (!checkInput(e.trim())) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Name: ").append(mealName).append("\n");
        sb.append("Ingredients: ").append("\n");
        for (var e : ingredients) {
            sb.append(e).append("\n");
        }
        return sb.toString();
    }


    @Override
    public int compare(Meal o1, Meal o2) {
        String o1Name = o1.getMealName();
        String o2Name = o2.getMealName();
        return o1Name.compareTo(o2Name);
    }
}
