package mealplanner;

import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class Plan {

    private static final List<String> daysOfWeek = List.of("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday");
    private static final List<String> categories = List.of("breakfast", "lunch", "dinner");
    private List<Meal> meals;
    private static final Database DB = MealPlanner.getDb();
    private static List<DayPlan> plans = DB.readPlans();

    public Plan() {
    }

    public void createNewPlan(List<Meal> meals) {
        this.meals = meals;
        DB.createPlanTable(true);

        for (var day : daysOfWeek) {
            System.out.println(day);
            for (var category : categories) {
                chooseMeals(day, category, meals);
            }
            System.out.printf("Yeah! We planned the meals for %s.%n", day);
        }
        listPlan();

    }

    private static List<Meal> getMeals() {
        return MealPlanner.getMeals();
    }

    public List<DayPlan> getPlans() {
        return plans;
    }


    private void chooseMeals(String dayOfWeek, String category, List<Meal> meals) {
        Scanner scanner = new Scanner(System.in);
        var list = getMealsByCategory(category, meals);

        list.stream().map(Meal::getMealName).sorted().forEach(System.out::println);

        System.out.printf("Choose the %s for %s from the list above:%n", category, dayOfWeek);
        boolean wrongInput = true;

        do {
            String input = scanner.nextLine();
            for (var meal : list) {
                if (meal.getMealName().equals(input)) {
                    wrongInput = false;
                    var dayPlan = new DayPlan(meal.getMealName(), meal.getCategory(), meal.getMealId());
                    DB.addPlanToDB(dayPlan);
                    break;
                }
            }

            if (wrongInput) {
                System.out.println("This meal doesn’t exist. Choose a meal from the list above.");
            }

        } while (wrongInput);
    }

    private List<Meal> getMealsByCategory(String category, List<Meal> meals) {
        return meals.stream()
                .filter(m -> m.getCategory().equals(category))
                .sorted(Comparator.comparing(Meal::getMealName))
                .toList();
    }

    public static void listPlan() {
        plans = DB.readPlans();
        if (!plans.isEmpty()) {
            int start = 0;
            int end = 3;
            for (String day : daysOfWeek) {
                System.out.println();
                System.out.println(day);

                for (int i = start; i < end; i++) {
                    var plan = plans.get(i);
                    System.out.println(plan);
                    if (plan.category().equals("dinner")) {
                        start = end;
                        end += 3;
                        break;
                    }
                }

            }
        } else {
            System.out.println("Database does not contain any meal plans");
        }
    }


}
