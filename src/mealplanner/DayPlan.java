package mealplanner;

public record DayPlan(String mealName, String category, int mealId) {

    @Override
    public String toString() {
        // capitalizing first letter
        var firstLetter = category.substring(0, 1).toUpperCase();
        var mealCategory = firstLetter + category.substring(1);
        return String.format("%s: %s", mealCategory, mealName);
    }
}
