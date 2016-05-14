/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package my.recipebox;

import java.io.File;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author davidjansing
 */
public class Test2 {
    
    protected static RecipeBoxUI rb;
    
    private void setUpClass() {
        rb = new RecipeBoxUI();
    }

    private void tearDownClass() {
       File file = new File("./data/concrete_patio");
       file.delete();
       rb.dispose();
    }
    
    public void testCreateRecipe()
    {
        rb.recipeList.addItem("concrete_patio");
        rb.recipeList.setSelectedItem("concrete_patio");
        String ingredients = "1 bag concrete mix\n" +
                             "5 gallons water";
        String directions  = "Mix well in wheelbarrow.\n" +
                             "Pour in pre-constructed form.";
        rb.ingredientListBox.setText(ingredients);
        rb.directionsBox.setText(directions);
        rb.servingsBox.setText("1");
        rb.editRecipeButton.doClick();
        rb.recipeList.setSelectedItem("bobs_burgers");
        rb.getRecipeButton.doClick();
        rb.recipeList.setSelectedItem("concrete_patio");
        rb.getRecipeButton.doClick();
        assertEquals("1 bag concrete mix\n" +
                     "5 gallons water\n",
                     rb.ingredientListBox.getText());
        assertEquals("Mix well in wheelbarrow.\n" +
                     "Pour in pre-constructed form.",
                     rb.directionsBox.getText());
        assertEquals("1",rb.servingsBox.getText());
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
