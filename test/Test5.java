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
public class Test5 {
    
    protected static RecipeBoxUI rb;
    
    private void setUpClass() {
        rb = new RecipeBoxUI();
    }

    private void tearDownClass() {
       rb.dispose();
    }
    
    // When the decimals-to-fractions functionality is implemented,
    // this test may fail and require maintenance.
    public void testDeleteRecipe()
    {
        // Create a minimal recipe
        rb.recipeList.addItem("bathtub_gin.xml");
        rb.recipeList.setSelectedItem("bathtub_gin.xml");
        rb.ingredientListBox.setText("50 gallons alcoholic stuff");
        rb.directionsBox.setText("mix in bathtub");
        rb.servingsBox.setText("1");
        rb.editRecipeButton.doClick();
        // Select another recipe
        rb.recipeList.setSelectedItem("bobs_burgers.xml");
        rb.getRecipeButton.doClick();
        // Select the new recipe
        rb.recipeList.setSelectedItem("bathtub_gin.xml");
        rb.getRecipeButton.doClick();
        // Click "delete"
        rb.deleteRecipeButton.doClick();
        // Is it gone?  If not, fail the test.
        File file = new File("./xml/bathtub_gin.xml");
        if (file.exists())
            fail("File deletion failed.");
    }
    
    /**
     * Test of main method, of class recipeBoxUI.
     */
    @Test
    public void testMain() {
        System.out.println("Test1::testMain");
        setUpClass();
        testDeleteRecipe();
        tearDownClass();
    }
}
