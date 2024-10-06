package mealplanner;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public final class Database {

    private static final String URL = "jdbc:postgresql:meals_db";
    private static final String USER = System.getenv("USER");
    private static final String PASS = System.getenv("PASS");
    private int ingredient_id = 1;

    public Database() {
        createCategoryTable();
        createIngredientTable();
        createPlanTable(false);
    }

    public static void createCategoryTable() {

        try (Connection connection = DriverManager.getConnection(URL, USER, PASS);
             Statement statement = connection.createStatement()) {

            String createQuery = "CREATE TABLE IF NOT EXISTS meals (" +
                    "meal_id INTEGER PRIMARY KEY," +
                    "category VARCHAR(30)," +
                    "meal VARCHAR(30))";

            statement.executeUpdate(createQuery);

        } catch (SQLException e) {
            System.err.println("Error in creating 'category' table");
            System.out.println(e.getMessage());
        }

    }

    public static void createIngredientTable() {

        try (Connection connection = DriverManager.getConnection(URL, USER, PASS);
             Statement statement = connection.createStatement()) {

            String createQuery = "CREATE TABLE IF NOT EXISTS ingredients (" +
                    "ingredient_id INTEGER," +
                    "ingredient VARCHAR(30)," +
                    "meal_id INT NOT NULL)";

            statement.executeUpdate(createQuery);

        } catch (SQLException e) {
            System.err.println("Error in creating 'ingredient' table");
            System.out.println(e.getMessage());
        }
    }

    public void addMeal(Meal meal) {

        String insertInCategory = "INSERT INTO meals VALUES (?, ?, ?)";
        String insertInIngredients = "INSERT INTO ingredients (ingredient_id, ingredient, meal_id) VALUES (?, ?, ?)";

        int mealId = meal.getMealId();
        String category = meal.getCategory();
        String mealName = meal.getMealName();
        var ingredients = meal.getIngredients();

        try (Connection connection = DriverManager.getConnection(URL, USER, PASS);
             var psCategory = connection.prepareStatement(insertInCategory);
             var psIngredient = connection.prepareStatement(insertInIngredients)) {

            psCategory.setInt(1, mealId);
            psCategory.setString(2, category);
            psCategory.setString(3, mealName);

            for (var i : ingredients) {
                psIngredient.setInt(1, ingredient_id++);
                psIngredient.setString(2, i);
                psIngredient.setInt(3, mealId);
                psIngredient.executeUpdate();
            }

            psCategory.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public List<Meal> readDBMeals() {

        List<Meal> dbMeals = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(URL, USER, PASS);
             Statement statement = connection.createStatement();
             PreparedStatement ps = connection.prepareStatement("SELECT * FROM ingredients WHERE meal_id = ?")) {

            String queryMeals = "SELECT * FROM meals";

            var rs = statement.executeQuery(queryMeals);

            while (rs.next()) {

                int meal_id = rs.getInt(1);
                String category = rs.getString("category");
                String mealName = rs.getString("meal");

                List<String> ingredients = new ArrayList<>();

                ps.setInt(1, meal_id);
                var ingredientsRS = ps.executeQuery();

                while (ingredientsRS.next()) {
                    String ingredient = ingredientsRS.getString("ingredient");
                    int last_ingredient_id = ingredientsRS.getInt("ingredient_id");
                    ingredient_id = ++last_ingredient_id;
                    ingredients.add(ingredient);
                }

                Meal meal = new Meal().createDBMeal(meal_id, category, mealName, ingredients);
                Meal.setLastMealId(meal_id);
                dbMeals.add(meal);
            }

        } catch (SQLException e) {
            System.err.println("Error in readDBData method in Database class");
            System.out.println(e.getMessage());
        }
        return dbMeals;
    }

    public void createPlanTable(boolean clearContents) {

        try (Connection connection = DriverManager.getConnection(URL, USER, PASS);
             Statement statement = connection.createStatement()) {

            if (clearContents) {
                statement.executeUpdate("DROP TABLE IF EXISTS plan");
            }

            String query = "CREATE TABLE IF NOT EXISTS plan (" +
                    "meal_option VARCHAR(30)," +
                    "meal_category VARCHAR(30)," +
                    "meal_id INT NOT NULL)";

            statement.executeUpdate(query);


        } catch (SQLException e) {
            System.out.println("Error in creating 'plan' table");
            System.out.println(e.getMessage());
        }
    }

    public void addPlanToDB(DayPlan dayPlan) {
        String meal_option = dayPlan.mealName();
        String meal_category = dayPlan.category();
        int meal_id = dayPlan.mealId();
        String addQuery = "INSERT INTO plan VALUES (?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement ps = connection.prepareStatement(addQuery)) {

            ps.setString(1, meal_option);
            ps.setString(2, meal_category);
            ps.setInt(3, meal_id);
            ps.executeUpdate();


        } catch (SQLException e) {
            System.out.println("Error in addPlanToDB");
            System.out.println(e.getMessage());
        }
    }

    public List<DayPlan> readPlans() {

        List<DayPlan> plans = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(URL, USER, PASS);
             Statement statement = connection.createStatement()) {


            String selectQuery = "SELECT * FROM plan";

            var rs = statement.executeQuery(selectQuery);

            while (rs.next()) {
                String mealName = rs.getString("meal_option");
                String category = rs.getString("meal_category");
                int mealId = rs.getInt("meal_id");
                DayPlan dayPlan = new DayPlan(mealName, category, mealId);
                plans.add(dayPlan);
            }

        } catch (SQLException e) {
            System.out.println("Error in readPlans");
            System.out.println(e.getMessage());
        }
        return plans;
    }

}
