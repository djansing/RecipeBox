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
public class Test2 {
    
    private static RecipeBoxUI rb;
        // components under test
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
       recipeList.setSelectedItem("concrete_patio");
       rb.getDeleteRecipeButton().doClick();
       rb.dispose();
    }
    
    public void testCreateRecipe()
    {
        recipeList.addItem("concrete_patio");
        recipeList.setSelectedItem("concrete_patio");
        String ingredients = "1 bag concrete mix\n" +
                             "5 gallons water";
        String directions  = "Mix well in wheelbarrow.\n" +
                             "Pour in pre-constructed form.";
        ingredientListBox.setText(ingredients);
        directionsBox.setText(directions);
        servingsBox.setText("1");
        editRecipeButton.doClick();
        recipeList.setSelectedItem("bobs_burgers");
        getRecipeButton.doClick();
        recipeList.setSelectedItem("concrete_patio");
        getRecipeButton.doClick();
        assertEquals("1 bag concrete mix\n" +
                     "5 gallons water\n",
                     ingredientListBox.getText());
        assertEquals("Mix well in wheelbarrow.\n" +
                     "Pour in pre-constructed form.",
                     directionsBox.getText());
        assertEquals("1", servingsBox.getText());
    }
    
    /**
     * Test of main method, of class recipeBoxUI.
     */
    @Test
    public void testMain() {
        System.out.println("Test2::testMain");
        setUpClass();
        testCreateRecipe();
        tearDownClass();
    }
}
