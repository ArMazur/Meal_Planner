# Meal Planner Application (Java)
October 2024
### Goal
The goal for creating this application was to learn and practice working with 
JDBC and database. Practice CRUD operations and have hands-on experience with PostgreSQL.

### About application
This Meal Planner App allows you to create meal plans for the week and builds shopping list 
automatically for you! 

You can make a database of categorized meals and set the menu for the week. 
This app will also help create and store shopping lists based on the meals 
so that no ingredient is missing.

#### Application allows you to:
1. Create meal and save it to the database with its properties - category, name, 
necessary ingredients
2. Create meal plan for the week from the saved meals and save this plan to the database
3. Show you saved meals even after you close and reopen the app
4. Show you saved weekly plan (if it was created)
5. Generate a shopping list containing all the required ingredients for the planned meals
and save this list to the `.txt` file

### How to start the application:

- **To start the application you need to install PostgreSQL on your machine**

    By default:
    >username=postgres
    > 
    > password=1111
  
- After installing PostgreSQL, create **meals_db** database.
- You can start application with using **.jar** file or using your preffered IDE
  - To start a **.jar** file with terminal use:
```
      git clone https://github.com/ArMazur/Meal_Planner.git
      cd Meal_Planner
      java -jar MealPlanner.jar
```

- After this program will start and prompt you with:

      What would you like to do (add, show, plan, list plan, save, exit)?

- Follow the instructions and **enjoy!**


### Links
Here's the link to the project: https://hyperskill.org/projects/318

Check out my hyperskill profile: https://hyperskill.org/profile/614203111




