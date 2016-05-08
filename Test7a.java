/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package my.recipebox;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import org.junit.experimental.theories.DataPoint;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

/**
 *
 * @author davidjansing
 */
@RunWith(Theories.class)
public class Test7a {
    
    private static RecipeBoxUI rb;
    @DataPoint
    public static String[] tsp = {"1 teaspoons ingredient", "3 1/3 tablespoons"};
    @DataPoint
    public static String[] tbsp = {"1 tablespoons ingredient", "2/3 cups"};
    @DataPoint
    public static String[] cup = {"1 cups ingredient", "2 1/2 quarts"};
    @DataPoint
    public static String[] qt = { "1 quarts ingredient", "2 1/2 gallons"};
    @DataPoint
    public static String[] oz = { "1 ounces ingredient", "2/3 pounds"};
    
    // components under test
    private static javax.swing.JTextArea ingredientListBox;
    private static javax.swing.JTextArea directionsBox;
    private static javax.swing.JTextField servingsBox;
    private static javax.swing.JComboBox recipeList;
    private static javax.swing.JButton editRecipeButton;
    private static javax.swing.JButton servingsButton;
    
    private void setUpClass() {
        rb = new RecipeBoxUI();
        ingredientListBox = rb.getIngredientListBox();
        directionsBox = rb.getDirectionsBox();
        servingsBox = rb.getServingsBox();
        recipeList = rb.getRecipeList();
        editRecipeButton = rb.getEditRecipeButton();
        servingsButton = rb.getServingsButton();
    }

    private void tearDownClass() {
       recipeList.setSelectedItem("test cake");
       rb.getDeleteRecipeButton().doClick();
       rb.dispose();
    }
    
    @Theory
    public void testConvertUnits(String[] linePair)
    {
        assertThat(convertUnits(linePair), is(true));
        System.out.println(linePair[0] + " x10 equals " + linePair[1]);
    }
    
    // This is a "theoretical" version of the test from Test7.java
    // It's more general, but it costs more to run.
    public boolean convertUnits(String[] linePair)
    {
        boolean pass = true;
        setUpClass();
        // Create a nonsense recipe that tests all units
        recipeList.addItem("test cake");
        recipeList.setSelectedItem("test cake");
        ingredientListBox.setText(linePair[0]);
        directionsBox.setText("This is a test recipe.  It isn't supposed " + 
                                 "to make any sense");
        servingsBox.setText("1");
        editRecipeButton.doClick();
        
        servingsBox.setText("10");
        servingsButton.doClick();
        
        if (!ingredientListBox.getText().contains(linePair[1]))
        {
            pass = false;
        }
        servingsBox.setText("1");
        servingsButton.doClick();
        
        if (!ingredientListBox.getText().contains(linePair[0]))
        {
            pass = false;
        }
        tearDownClass();
        return pass;
    }

}
