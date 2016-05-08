/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package my.recipebox;

import java.util.ArrayList;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
/**
 *
 * @author davidjansing
 */
public class RecipeBoxUITest {
    protected static RecipeBoxUI instance;
    @Rule
    public ExpectedException thrown= ExpectedException.none();
    
    public RecipeBoxUITest() {
        instance = new RecipeBoxUI();
    }
    
    @BeforeClass
    public static void setUpClass() {
         RecipeBoxUITest uiTest = new RecipeBoxUITest();
    }
    
    @AfterClass
    public static void tearDownClass() {
        instance.dispose();
    }

    /**
     * Test of readXmlFile method, of class RecipeBoxUI.
     */
    @Test
    public void testReadXmlFile() {
        System.out.println("readXmlFile");
        String fileLocation = "xml/bobs_burgers";
        instance.readXmlFile(fileLocation);
        // why is this struck through?
        assertEquals(30.0,instance.ingredients.get(0).getQuantity(),0.0);
        assertTrue(30.0 == instance.ingredients.get(0).getQuantity());
        // included to prove TestNG assertions are not more flexible than JUnit
        assertTrue("".equals(instance.ingredients.get(0).getUnit()));
        assertEquals("",instance.ingredients.get(0).getUnit());
        assertEquals("dollars cash",instance.ingredients.get(0).getItem());
        assertEquals(3,instance.getServings());
        assertEquals("go to Bob's Burgers.  \n" +
                     "Order the burger of the day.\n" +
                     "Be sure to comment on how funny the name is.\n" +
                     "Ignore Gene's gross noises.",instance.getDirections());
    }

    /**
     * Test of isUnit method, of class RecipeBoxUI.
     */
    @Test
    public void testIsUnit() {
        System.out.println("isUnit");
        boolean expResult = true;
        boolean result = instance.isUnit("cups");
        assertEquals(expResult, result);
        result = instance.isUnit("tablespoons");
        assertEquals(expResult, result);
        result = instance.isUnit("gallons");
        assertEquals(expResult, result);
        result = instance.isUnit("quarts");
        assertEquals(expResult, result);
        result = instance.isUnit("ounces");
        assertEquals(expResult, result);
        result = instance.isUnit("pounds");
        assertEquals(expResult, result);
        result = instance.isUnit("teaspoons");
        assertEquals(expResult, result);
        // test expected failure
        expResult = false;
        result = instance.isUnit("barleycorns");
        assertEquals(expResult, result);
    }

    /**
     * Test of setServings method, of class RecipeBoxUI.
     */
    @Test
    public void testSetGetServings() {
        System.out.println("getServings");
        instance.getRecipeList().addItem("item");
        instance.getRecipeList().setSelectedItem("item");
        // instead of hitting the method directly,
        // I'm going to let the UI handle it.
        instance.getServingsBox().setText("5");
        instance.getEditRecipeButton().doClick();
        int result = instance.getServings();
        assertEquals(result, 5);
        // I'm going to expect that the UI will just revert
        // to what was already there if I put in a non-numeric string
        // The NumberFormatException should get handled
        thrown.expect(NumberFormatException.class);
        instance.getServingsBox().setText("meatballs");
        instance.getEditRecipeButton().doClick();
        result = instance.getServings();
        assertEquals(result, 5);
        // It should also handle the case 
        // where a real number is put in
        instance.getServingsBox().setText("4.5");
        instance.getEditRecipeButton().doClick();
        result = instance.getServings();
        assertEquals(result, 4);
        // get rid of the test recipe
        instance.getDeleteRecipeButton().doClick();
    }
    
    @Test
    public void testGetSetImage() {
        System.out.println("getImage");
        instance.getImageList().setSelectedItem("Pasta.jpg");
        String expResult = "Pasta.jpg";
        String result = instance.getImage();
        assertEquals(result, expResult);
    }

    /**
     * Test of setDirections method, of class RecipeBoxUI.
     */
    @Test
    public void testSetGetDirections() {
        System.out.println("setDirections");
        String s = "directions";
        //RecipeBoxUI instance = new RecipeBoxUI();
        instance.setDirections(s);
        assertEquals("directions",instance.getDirections());
        //instance.dispose();
    }

    /**
     * Test of setIngredients method, of class RecipeBoxUI.
     * Method calls setIngredients method that takes an ArrayList argument
     * This test includes quantities and units that should convert during processing
     */
    @Test
    public void testSetIngredients() {
        System.out.println("setIngredients");
        instance.ingredients.add(new Ingredient(3.5,"cups","water"));
        instance.ingredients.add(new Ingredient(0.0625,"tablespoons","sea salt"));
        instance.ingredients.add(new Ingredient(2.9375,"ounces","cocoa powder"));
        instance.setIngredients();
        assertEquals("3 1/2 cups water\n" + 
                     "1/4 teaspoons sea salt\n" + 
                     "3 ounces cocoa powder\n",
                     instance.getIngredientListBox().getText());
    }

    /**
     * Test of addItemSafely method, of class RecipeBoxUI.
     * The point of the addItemSafely method 
     * is to avoid adding a recipe to the RecipeList
     * if a recipe of that title already exists.
     */
    @Test
    public void testAddItemSafely() {
        System.out.println("addItemSafely");
        int itemCount = instance.getRecipeList().getItemCount();
        String item = "new_item";
        instance.setServings(8);
        instance.setDirections("directions");
        ArrayList<Ingredient> ingList = new ArrayList();
        Ingredient ing = new Ingredient(1.0, "cup", "water");
        ingList.add(ing);
        instance.setIngredients(ingList);
        instance.addItemSafely(item);
        // add the same recipe again
        instance.addItemSafely(item);
        // recipe should only have been added once to the list.
        assertEquals(itemCount+1, instance.getRecipeList().getItemCount());
    }

    /**
     * Test of getIngredients method, of class RecipeBoxUI.
     */
    @Test
    public void testGetIngredients() {
        System.out.println("getIngredients");
        instance.getRecipeList().setSelectedItem("bobs_burgers");
        instance.getGetRecipeButton().doClick();
        ArrayList<Ingredient> expResult = new ArrayList();
        Ingredient ing = new Ingredient(30, "", "dollars cash");
        expResult.add(ing);
        ArrayList<Ingredient> result = instance.getIngredients();
        assertEquals(result.get(0).getQuantity(), 
                     expResult.get(0).getQuantity(),0.0);
        assertEquals(result.get(0).getUnit(), 
                     expResult.get(0).getUnit());
        assertEquals(result.get(0).getItem(), 
                     expResult.get(0).getItem());
    }

    /**
     * Test of convertLargerUnit method, of class RecipeBoxUI.
     */
    @Test
    public void testConvertLargerUnit() {
        System.out.println("convertLargerUnit");
        Ingredient i = new Ingredient();
        i.setQuantity(16);
        i.setUnit("tablespoons");
        i.setItem("some ingredient");
        instance.convertLargerUnit(i);
        assertEquals(1, i.getQuantity(),0.0);
        assertEquals("cups",i.getUnit());
        // There are 16 tablespoons in a cup,
        // but only four cups in a quart,
        // so test both.
        i.setQuantity(4);
        i.setUnit("cups");
        instance.convertLargerUnit(i);
        assertEquals(1, i.getQuantity(),0.0);
        assertEquals("quarts",i.getUnit());
    }

    /**
     * Test of convertSmallerUnit method, of class RecipeBoxUI.
     */
    @Test
    public void testConvertSmallerUnit() {
        System.out.println("convertSmallerUnit");
        Ingredient i = new Ingredient();
        i.setQuantity(0.125);
        i.setUnit("cups");
        i.setItem("some ingredient");
        instance.convertSmallerUnit(i);
        assertEquals(2,i.getQuantity(),0.0);
        assertEquals("tablespoons",i.getUnit());
        // Likewise, as with up-conversion, since tbsp-cups
        // and cups-quarts are different, an additional test is needed
        i.setQuantity(0.25);
        i.setUnit("quarts");
        instance.convertSmallerUnit(i);
        assertEquals(1,i.getQuantity(),0.0);
        assertEquals("cups",i.getUnit());
    }

    /**
     * Test of convertToFractional method, of class RecipeBoxUI.
     */
    @Test
    public void testConvertToFractional() {
        System.out.println("convertToFractional");
        double dec = 2.5;
        String expResult = "2 1/2";
        String result = instance.convertToFractional(dec);
        assertEquals(result, expResult);
        // Also test the instance where the quantity is less than one
        dec = 0.5;
        expResult = "1/2";
        result = instance.convertToFractional(dec);
        assertEquals(result, expResult);
    }

    /**
     * Test of convertToDecimal method, of class RecipeBoxUI.
     * convertToDecimal doesn't concern itself with the entire number, 
     * just the fractional part,returning an integer representing the quantity 
     * right of the decimal point.
     */
    @Test
    public void testConvertToDecimal() {
        System.out.println("convertToDecimal");
        String fraction = "1/2";
        String expResult = "5";
        String result = instance.convertToDecimal(fraction);
        assertEquals(result, expResult);
    }
    
}
