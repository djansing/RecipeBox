/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package my.recipebox;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author davidjansing
 */
public class Test1 {
    
    protected static RecipeBoxUI rb;
    
    private void setUpClass() {
        rb = new RecipeBoxUI();
    }

    private void tearDownClass() {
       rb.dispose();
    }
    
    // When the decimals-to-fractions functionality is implemented,
    // this test may fail and require maintenance.
    public void testSetIngredients()
    {
        rb.recipeList.setSelectedItem("Pasta_e_fagioli.xml");
        rb.getRecipeButton.doClick();
        assertEquals("2 slices bacon\n" + 
                    "1 cup frozen chopped mixed vegetables\n" + 
                    "1 can Hunt's Garlic & Herb Pasta Sauce\n" + 
                    "2 cups water\n" + 
                    "1 can red kidney beans\n" + 
                    "1/2 cup dry large elbow macaroni, uncooked\n" +
                    "4 tbsp grated Parmesan cheese\n",
                    rb.ingredientListBox.getText());
        assertEquals("6",rb.servingsBox.getText());
        assertEquals("Cook bacon in saucepan 5 minutes or until crisp\n"+
                    "Remove bacon, leaving drippings in pan.\n"+
                    "Add vegetables in pan, cook and stir 2 minutes or until tender.\n"+
                    "Add pasta sauce, water, beans, and macaroni.\n"+
                    "Bring to a boil, reduce heat and simmer 10 minutes or until macaroni is tender.\n"+
                    "Stir crumbled bacon into soup.  Top each serving with cheese.", 
                    rb.directionsBox.getText());                   
    }
    
    /**
     * Test of main method, of class recipeBoxUI.
     */
    @Test
    public void testMain() {
        System.out.println("Test1::testMain");
        setUpClass();
        testSetIngredients();
        tearDownClass();
    }
}
