package my.recipebox;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

// for xml stuff
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
/**
 *
 * @author davidjansing
 */
public class RecipeBoxUI extends javax.swing.JFrame {
    private ArrayList<Ingredient> ingredients;
    private String directions = "";
    private String substitutions;
    private int servings;
    private final String dataPath;
    private final String[] units = {"tsp","teaspoon","tbsp", "tablespoon",
        "cup","pint", "pt","quart","qt","gallon","gal",
        "dl","cl","liter","l","ml",
        "oz","ounce","lb","pound","g","gram","kg","kilo","kilogram"};
 
    /**
     * Creates new form recipeBoxUI
     */
    public RecipeBoxUI() {
        initComponents();
        ingredients = new ArrayList();
        dataPath = "./xml";
        File recipeFiles = new File(dataPath);
        String[] filenames = recipeFiles.list();
        recipeList.setSize(270,recipeList.getHeight());
        recipeList.addItem("Find a recipe");
        for (int i=0; i<filenames.length; i++)
        {
            recipeList.addItem(filenames[i]);
        }
        recipeList.setSelectedIndex(0);   
    }
    
    public void readXmlFile(String fileLocation)
    {
        try 
        {
            ingredients.clear();
            File xmlFile = new File(fileLocation);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            
            // Get the ingredient list
            NodeList ingList = doc.getElementsByTagName("ingredient");
            for (int i=0; i<ingList.getLength(); i++)
            {
                Ingredient ing = new Ingredient();
                Node ingNode = ingList.item(i);
                Element ingElem = (Element) ingNode.getChildNodes();
                Node qNode = ingElem.getElementsByTagName("quantity").item(0);
                ing.setQuantity(Double.parseDouble(qNode.getTextContent()));
                Node uNode = ingElem.getElementsByTagName("unit").item(0);
                ing.setUnit(uNode.getTextContent());
                Node iNode = ingElem.getElementsByTagName("item").item(0);
                ing.setItem(iNode.getTextContent());
                ingredients.add(ing);
            }  
            
            // Get the servings
            Node servNode = doc.getElementsByTagName("servings").item(0);
            servings = Integer.parseInt(servNode.getTextContent());
            
            // Get the directions
            Node dirNode = doc.getElementsByTagName("directions").item(0);
            directions = dirNode.getTextContent();
        }
        catch (ParserConfigurationException | SAXException | IOException ex) 
        {
            Logger.getLogger(RecipeBoxUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public boolean isUnit(String u)
    {
        // Strip off plural if it exists
        String uu = "";
        if (u.lastIndexOf("s") != u.length()-1)
        {
            uu = u.substring(0, u.length()-1);
        }
        else
        {
            uu = u;
        }
        
        for(String unitString: units)
        {
            if (uu.equals(unitString))
            {
            return true;
            }
        }
        return false;
    }
    
    private void updateIngredients(String ing) 
    {
     // Convert fractional quantities to decimal for storage
        String[] lines = ing.split("\n");
        ingredients.clear();
        for (String line: lines)
        {
            Ingredient in = new Ingredient();
            if (!line.equals("") && !line.equals("\n"))
            {
                String[] part = line.split(" ", 4);
                
                if (part[1].contains("/"))
                {
                    // combine parts 0 and 1 and convert
                    String quant = part[0] + "." + convertToDecimal(part[1]);
                    in.setQuantity(Double.parseDouble(quant)); 
                    if (isUnit(part[2]))
                    {
                        in.setUnit(part[2]);          
                    }
                    else
                    {
                        in.setItem(part[2]);
                    }
                }
                else if (part[0].contains("/"))
                {
                    // send the first part only to the converter
                    String quant = "0." + convertToDecimal(part[0]);
                    in.setQuantity(Double.parseDouble(quant)); 
                    if (isUnit(part[1]))
                    {
                        in.setUnit(part[1]);
                        in.setItem(part[2]);
                    }
                    else
                    {
                        if (part.length > 2)
                        {
                            in.setItem(part[1] + " " + part[2]);
                        }
                        else
                        {
                            in.setItem(part[1]);
                        }
                    }
                }
                else
                {
                    // quantity is a whole number
                    in.setQuantity(Double.parseDouble(part[0]));
                    // Unit is of the form "2 ounces vodka"
                    if (isUnit(part[1]))
                    {
                        in.setUnit(part[1]);
                        in.setItem(part[2]);
                    }
                    // ingredient is of the form "2 whole chickens"
                    else if (part.length > 2)
                    {
                        in.setItem(part[1] + " " + part[2]);
                    }
                    // ingredient is of the form "2 eggs"
                    else
                    {
                        in.setItem(part[1]);
                    }
                }
                
                // There might be another part here, or not
                // if so, append it.
                // ex.  "2 cups wheat flour"
                if (part.length > 3)
                {
                    in.setItem(in.getItem() + " " + part[3]);
                }
            ingredients.add(in);  
            }
        }
    }
    
    private void createXml(String filename) 
    {
        try 
        {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
 
            // root elements
            Document doc = docBuilder.newDocument();
            Element recipe = doc.createElement("recipe");
            recipe.setAttribute("title", "someRecipe");
            doc.appendChild(recipe);
            Element ingredientList = doc.createElement("ingredientList");
            recipe.appendChild(ingredientList);

            int i=0;

            for(Ingredient ingred : ingredients)
            {
                // ingredient elements
                Element ingredientElem = doc.createElement("ingredient");
                ingredientList.appendChild(ingredientElem);
 
                // quantity elements
                Element quantity = doc.createElement("quantity");
                String quant = "";
                quant += ingred.getQuantity();
                quantity.appendChild(doc.createTextNode(quant));
                ingredientElem.appendChild(quantity);
 
                // unit elements
                Element unitElem = doc.createElement("unit");
                unitElem.appendChild(doc.createTextNode(ingred.getUnit()));
                ingredientElem.appendChild(unitElem);
 
                // item elements
                Element itemElem = doc.createElement("item");
                itemElem.appendChild(doc.createTextNode(ingred.getItem()));
                ingredientElem.appendChild(itemElem);
 
                i++;
            }
            
            Element servingsElem = doc.createElement("servings");
            String svg = "";
            svg += servings;
            servingsElem.appendChild(doc.createTextNode(svg));
            recipe.appendChild(servingsElem);
            
            Element directionsElem = doc.createElement("directions");
            directionsElem.appendChild(doc.createTextNode(directions));
            recipe.appendChild(directionsElem);
                
            // write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(filename));

            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty
                    ("{http://xml.apache.org/xslt}indent-amount","3");
            transformer.transform(source, result);
 
            System.out.println("File saved to " + filename);
 
	} catch (Exception e) {
		e.printStackTrace();
	  }
    }

    // Setters and getters
    public void setServings(int s)
    {
        this.servings = s;
        servingsBox.setText(Integer.toString(servings));
    }

    public int getServings()
    {
        return this.servings;
    }  
    
    public void setDirections(String s)
    {
        this.directions = s;
        directionsBox.setText(directions);
    }

    
    public String getDirections()
    {
        return this.directions;
    }  
    
    public void setIngredients()
    {
        setIngredients(ingredients);
    }
   
    public void setIngredients(ArrayList<Ingredient> ing)
    {
        String ingredientList = "";
        this.ingredients = ing;
        for(Ingredient i : ing)
        {
            ingredientList += convertToFractional(i.getQuantity());
            if (i.getUnit().length() > 0)
            {
                ingredientList += " " + i.getUnit();
            }
            ingredientList += " " + i.getItem() + "\n";
        }
        ingredientListBox.setText(ingredientList);
    }

        public void addItemSafely(String item)
    {
        boolean itemExists = false;
        for (int i=0; i < recipeList.getItemCount(); i++)
        {
            if (recipeList.getItemAt(i).equals(item))
            {
                itemExists = true;
                break;
            }
        }
        if (!itemExists)
        {
            recipeList.addItem(item);
        }
    }
    
    public ArrayList<Ingredient> getIngredients()
    {
        return this.ingredients;
    }  
    
    public String convertSmallerUnit(double d)
    {
        return "tablespoons?";
    }
    
    public String convertToFractional(double dec)
    {
        double d = dec - (int)dec;
        String retval = "";
        if ((int)dec > 0)
        {
            if (d > 0.0)
            {
                // if there's a fraction, allow a space for it
                retval += (int)dec + " ";
            }
            else
            {
                // otherwise, leave it out
                retval += (int)dec;
            }
        }   
        
        if (d >= 0.0 && d < 0.0625 && (int)dec == 0)
        {
            retval += "0.0";     
        }
        else if (d >= 0.0625 && d < 0.1875)
        {
             retval += "1/8";
        }
        else if (d >= 0.1875 && d < 0.3125)
        {
             retval += "1/4";
        }
        else if (d >= 0.3125 && d < 0.4375)
        {
             retval += "3/8";
        }
        else if (d >= 0.4375 && d < 0.5625)
        {
             retval += "1/2";
        }
        else if (d >= 0.5625 && d < 0.6875)
        {
             retval += "5/8";
        }
        else if (d >= 0.6875 && d < 0.8125)
        {
             retval += "3/4";
        }
        else if (d >= 0.8125 && d < 0.9375)
        {
             retval += "7/8";
        }
        else if (d >= 0.9375) //close enough to 1 to just increment the value
        {
            int nextVal = (int)dec + 1;
            retval = "";
            retval += nextVal;
        }
        return retval;
    }
    
    public String convertToDecimal(String fraction)
    {
        double decimalFraction = 0.0;
        String retval = "";
        System.out.println(fraction);
        String[] numbers = fraction.split("/");
        double numerator= Double.parseDouble(numbers[0]);
        double denominator = Double.parseDouble(numbers[1]);
        decimalFraction = numerator/denominator;
        retval += decimalFraction;
        return retval.substring(retval.indexOf(".")+1); 
    }
    
    private Object makeObj(final String str) {
        return new Object() {
        @Override
        public String toString() {
            return str;}};
        }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        recipeList = new javax.swing.JComboBox();
        getRecipeButton = new javax.swing.JButton();
        editRecipeButton = new javax.swing.JButton();
        deleteRecipeButton = new javax.swing.JButton();
        directionsPane = new javax.swing.JScrollPane();
        directionsBox = new javax.swing.JTextArea();
        directionsLabel = new javax.swing.JLabel();
        ingredientListPane = new javax.swing.JScrollPane();
        ingredientListBox = new javax.swing.JTextArea();
        ingredientListLabel = new javax.swing.JLabel();
        servingsBox = new javax.swing.JTextField();
        servingsButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Dave's Recipe Box");

        recipeList.setEditable(true);
        recipeList.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                recipeListActionPerformed(evt);
            }
        });

        getRecipeButton.setText("get recipe");
        getRecipeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                getRecipeButtonActionPerformed(evt);
            }
        });

        editRecipeButton.setText("save recipe");
        editRecipeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editRecipeButtonActionPerformed(evt);
            }
        });

        deleteRecipeButton.setText("delete recipe");
        deleteRecipeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteRecipeButtonActionPerformed(evt);
            }
        });

        directionsBox.setColumns(20);
        directionsBox.setRows(5);
        directionsPane.setViewportView(directionsBox);

        directionsLabel.setText("Directions");

        ingredientListBox.setColumns(20);
        ingredientListBox.setRows(5);
        ingredientListPane.setViewportView(ingredientListBox);

        ingredientListLabel.setText("Ingredient list");

        servingsBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                servingsBoxActionPerformed(evt);
            }
        });

        servingsButton.setText("change # of servings");
        servingsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                servingsButtonActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(12, 12, 12)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                            .add(directionsLabel)
                            .add(servingsBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 59, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(servingsButton)
                        .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .add(layout.createSequentialGroup()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                            .add(org.jdesktop.layout.GroupLayout.LEADING, directionsPane)
                            .add(org.jdesktop.layout.GroupLayout.LEADING, ingredientListPane)
                            .add(org.jdesktop.layout.GroupLayout.LEADING, layout.createSequentialGroup()
                                .add(recipeList, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 300, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 68, Short.MAX_VALUE)
                                .add(getRecipeButton))
                            .add(org.jdesktop.layout.GroupLayout.LEADING, layout.createSequentialGroup()
                                .add(ingredientListLabel)
                                .add(0, 386, Short.MAX_VALUE))
                            .add(layout.createSequentialGroup()
                                .add(0, 0, Short.MAX_VALUE)
                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                                    .add(deleteRecipeButton)
                                    .add(editRecipeButton))))
                        .add(12, 12, 12))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(recipeList, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(getRecipeButton))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(editRecipeButton)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(deleteRecipeButton)
                .add(1, 1, 1)
                .add(ingredientListLabel)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(ingredientListPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 138, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(18, 18, 18)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(servingsButton)
                    .add(servingsBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(directionsLabel)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(directionsPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 257, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void servingsBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_servingsBoxActionPerformed

    }//GEN-LAST:event_servingsBoxActionPerformed

    private void recipeListActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_recipeListActionPerformed

    }//GEN-LAST:event_recipeListActionPerformed

    private void servingsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_servingsButtonActionPerformed
        String servingsStr = (String) servingsBox.getText();
        // Shouldn't have zero servings of anything
        int newServings = 1;
        
        // Nor should we have half-servings of anything
        if (servingsStr.indexOf(".") != -1)
        {
            // drop the fractional part
            newServings = Integer.parseInt(servingsStr.substring(0, servingsStr.indexOf(".")));
        }
        // I said no zeroes, and I meant it.
        else if (Integer.parseInt(servingsStr) == 0)
        {
            newServings = servings;
        }
        else
        {
        newServings = Integer.parseInt(servingsStr);
        }
        for (Ingredient i : ingredients)
        {
            double newQuant = i.getQuantity();
            newQuant*=newServings;
            newQuant/=servings;
            i.setQuantity(newQuant);
        }
        setIngredients();

        setServings(newServings);
    }//GEN-LAST:event_servingsButtonActionPerformed

    private void getRecipeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_getRecipeButtonActionPerformed
        // Get the selected recipe from the database
        String filename = (String) recipeList.getSelectedItem();
        readXmlFile(dataPath + "/" + filename);
        setServings(servings);
        setIngredients(ingredients);
        setDirections(directions);
    }//GEN-LAST:event_getRecipeButtonActionPerformed

    private void editRecipeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editRecipeButtonActionPerformed
        String title = (String) recipeList.getSelectedItem();
        directions = directionsBox.getText();
        servings = Integer.parseInt(servingsBox.getText());
        String ing = ingredientListBox.getText();
        updateIngredients(ing);
        createXml(dataPath + "/" + title);
        addItemSafely(title);
    }//GEN-LAST:event_editRecipeButtonActionPerformed

    private void deleteRecipeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteRecipeButtonActionPerformed
       
        String title = (String) recipeList.getSelectedItem();
        File file = new File(dataPath + "/" + title);

        if(file.exists())
        {
            file.delete();
            recipeList.removeItem(title);
            recipeList.setSelectedIndex(0);
            ingredientListBox.setText("");
            servingsBox.setText("");
            directionsBox.setText("");
        }
    }//GEN-LAST:event_deleteRecipeButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    javax.swing.JButton deleteRecipeButton;
    javax.swing.JTextArea directionsBox;
    private javax.swing.JLabel directionsLabel;
    private javax.swing.JScrollPane directionsPane;
    javax.swing.JButton editRecipeButton;
    javax.swing.JButton getRecipeButton;
    javax.swing.JTextArea ingredientListBox;
    private javax.swing.JLabel ingredientListLabel;
    private javax.swing.JScrollPane ingredientListPane;
    javax.swing.JComboBox recipeList;
    javax.swing.JTextField servingsBox;
    javax.swing.JButton servingsButton;
    // End of variables declaration//GEN-END:variables

}
