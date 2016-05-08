/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package my.recipebox;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author davidjansing
 */
public class Test7 {
    
    private static RecipeBoxUI rb;
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
    
    // I probably need a specially-made recipe to get
    // full coverage of the unit conversion bits
    public void testConvertUnits()
    {
        // Create a nonsense recipe that tests all units
        recipeList.addItem("test cake");
        recipeList.setSelectedItem("test cake");
        ingredientListBox.setText("1 teaspoon ingredient\n" +
                                     "1 tsp ingredient\n" +
                                     "1 tablespoon ingredient\n" +
                                     "1 tbsp ingredient\n" +
                                     "1 cup ingredient\n" + 
                                     "1 ounce ingredient\n" + 
                                     "1 quart ingredient");
        directionsBox.setText("This is a test recipe.  It isn't supposed " + 
                                 "to make any sense");
        servingsBox.setText("1");
        editRecipeButton.doClick();
        
        servingsBox.setText("10");
        servingsButton.doClick();
        
        // tsp goes to tbsp, tbsp goes to cups, cups goes to qts,
        // oz goes to lbs, and qts goes to gals.
        assertEquals("3 1/3 tablespoons ingredient\n" +
                     "3 1/3 tablespoons ingredient\n" + 
                     "2/3 cups ingredient\n" +
                     "2/3 cups ingredient\n" +
                     "2 1/2 quarts ingredient\n" +
                     "2/3 pounds ingredient\n" +
                     "2 1/2 gallons ingredient\n",
                    ingredientListBox.getText());
        servingsBox.setText("1");
        servingsButton.doClick();
        assertEquals("1 teaspoons ingredient\n" +
                     "1 teaspoons ingredient\n" +
                     "1 tablespoons ingredient\n" +
                     "1 tablespoons ingredient\n" +
                     "1 cups ingredient\n" + 
                     "1 ounces ingredient\n" + 
                     "1 quarts ingredient\n",
                    ingredientListBox.getText());
    }
    
    /**
     * Test of main method, of class recipeBoxUI.
     */
    @Test
    public void testMain() {
        System.out.println("Test7::testMain");
        setUpClass();
        testConvertUnits();
        tearDownClass();
    }
}
