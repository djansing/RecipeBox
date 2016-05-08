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
public class Test3 {
    
    private static RecipeBoxUI rb;
    private static javax.swing.JTextArea ingredientListBox;
    private static javax.swing.JTextArea directionsBox;
    private static javax.swing.JTextField servingsBox;
    private static javax.swing.JComboBox recipeList;
    private static javax.swing.JButton editRecipeButton;
    private static javax.swing.JButton getRecipeButton;
    
    private void setUpClass() {
        rb = new RecipeBoxUI();
        ingredientListBox = rb.getIngredientListBox();
        directionsBox = rb.getDirectionsBox();
        servingsBox = rb.getServingsBox();
        recipeList = rb.getRecipeList();
        editRecipeButton = rb.getEditRecipeButton();
        getRecipeButton = rb.getGetRecipeButton();
    }

    private void tearDownClass() {
        recipeList.setSelectedItem("bobs_burgers");
        getRecipeButton.doClick();
        ingredientListBox.setText("30 dollars cash");
        directionsBox.setText("go to Bob's Burgers.  \n" +
                                "Order the burger of the day.\n" +
                                "Be sure to comment on how funny the name is.\n" +
                                "Ignore Gene's gross noises.");
        servingsBox.setText("3");
        editRecipeButton.doClick();
        rb.dispose();
    }
    

    public void testEditRecipe()
    {
        recipeList.setSelectedItem("bobs_burgers");
        getRecipeButton.doClick();
        assertEquals("bobs_burgers",
                    recipeList.getSelectedItem());
        ingredientListBox.setText("12 euros cash");
        directionsBox.setText("Dial 555-5555 and ask for Gene.");
        editRecipeButton.doClick();
        recipeList.setSelectedItem("Pasta e fagioli");
        getRecipeButton.doClick();
        recipeList.setSelectedItem("bobs_burgers");
        getRecipeButton.doClick();
        assertEquals("12 euros cash\n",
                    ingredientListBox.getText());
        assertEquals("Dial 555-5555 and ask for Gene.",
                    directionsBox.getText());
    }
    
    /**
     * Test of main method, of class recipeBoxUI.
     */
    @Test
    public void testMain() {
        System.out.println("Test3::testMain");
        setUpClass();
        testEditRecipe();
        tearDownClass();
    }
}
