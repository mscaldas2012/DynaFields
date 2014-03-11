package com.msc.customFields.dataAccess;

import com.msc.customFields.CustomFieldDefList;
import com.msc.customFields.manager.CustomFieldDefinitionManager;
import com.msc.customFields.CustomFieldDefinition;
import com.msc.customFields.FieldType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.logging.Logger;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * This code was written by Marcelo Caldas.
 * e-Mail: mscaldas@gmail.com
 * <p/>
 * \* Project: DynaFields
 * <p/>
 * Date: 3/6/14
 * <p/>
 * Enjoy the details of life.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:customFields-context.xml")
public class CustomFieldDefinitionTest {
    private static final Logger logger = Logger.getLogger(CustomFieldDefinitionTest.class.getName());

    @Autowired
    CustomFieldDefinitionManager customFieldDefinitionManager;
    //CustomFieldDefinitionDAO customFieldDefinitionDAO;


    @Test
    public void testFindByOwner() throws Exception {
      fail("Develop Test!");
    }

    @Test
    public void testFindByOwnerAndClass() throws Exception {
        fail("Develop Test!");
    }

    @Test
    public void testFindByOnwerClassAndScope() throws Exception {
        fail("Develop Test!");
    }


    @Test
    public void testFindByPrimaryKey() throws Exception {
        CustomFieldDefinition def = customFieldDefinitionManager.findByPrimaryKey(1l);
        System.out.println("def = " + def);
        def = customFieldDefinitionManager.findByPrimaryKey(2l);
        def = customFieldDefinitionManager.findByPrimaryKey(2l);
    }
    @Test
    public void testCRUDCFDef() throws Exception {
        CustomFieldDefinition def = createCustomFieldDefinition() ;
        def.setFieldType(FieldType.STRING);
        def.setRegExpression("^[A-Za-z0-9]*$");

        def = customFieldDefinitionManager.save(def);
        System.out.println("def.getPk() = " + def.getPk());
        def.setDescription("Modified Description");
        CustomFieldDefinition updated = customFieldDefinitionManager.save(def);
        System.out.println("updated.getDescription() = " + updated.getDescription());
        assertTrue("Modified Description".equals(updated.getDescription()));
        customFieldDefinitionManager.delete(updated);
        CustomFieldDefinition invalid = customFieldDefinitionManager.findByPrimaryKey(def.getPk());
        assertTrue(invalid == null);
    }

    @Test
    public void testListValues() {
        CustomFieldDefinition def = createCustomFieldDefinition();

        def.addListValue(new CustomFieldDefList("A", "A", "field a", 1l));
        def.addListValue(new CustomFieldDefList("B", "B", "field b", 2l));
        def.addListValue(new CustomFieldDefList("C", "C", "field c", 3l));
        def = customFieldDefinitionManager.save(def);
        System.out.println("def.getPk() = " + def.getPk());
    }
    @Test
    public void testAddListValue() {
        CustomFieldDefinition def = createCustomFieldDefinition();

        def.addListValue(new CustomFieldDefList("A", "A", "field a", 1l));
        def = customFieldDefinitionManager.save(def);
        assertTrue(def.getListValues().size() == 1);
        def.addListValue(new CustomFieldDefList("B", "B", "field b", 2l));
        CustomFieldDefinition newDef = customFieldDefinitionManager.save(def);
        assertTrue(newDef.getListValues().size() == 2);
        CustomFieldDefList aVal = newDef.getListValues().iterator().next();
        aVal.setItem("D");
        aVal.setCode("D");
        aVal.setDescription("new field D");
        newDef.addListValue(new CustomFieldDefList("C", "C", "field c", 3l));
        CustomFieldDefinition updated = customFieldDefinitionManager.save(newDef);
        assertTrue(updated.getListValues().size() == 3);
    }

    @Test
    public void testDeleteListValue() throws Exception {
        CustomFieldDefinition def = createCustomFieldDefinition();

        def.addListValue(new CustomFieldDefList("A", "A", "field a", 1l));
        def.addListValue(new CustomFieldDefList("B", "B", "field b", 2l));
        def.addListValue(new CustomFieldDefList("C", "C", "field c", 3l));
        def = customFieldDefinitionManager.save(def);
        assertTrue(def.getListValues().size() == 3);
        for (CustomFieldDefList aVal: def.getListValues()) {
            if (aVal.getCode().equals("B")) {
               aVal.setDeleted(true);
                break;
            }
        }
        def = customFieldDefinitionManager.save(def);
        CustomFieldDefinition saved = customFieldDefinitionManager.findByPrimaryKey(def.getPk());
        assertTrue(saved.getListValues().size() == 2);
    }

    @Test
    public void testFakeAddListValue() throws Exception {
        CustomFieldDefinition def = createCustomFieldDefinition();

        def.addListValue(new CustomFieldDefList("A", "A", "field a", 1l));
        CustomFieldDefList notWanted = new CustomFieldDefList("B", "B", "field b", 2l);
        notWanted.setDeleted(true);
        def.addListValue(notWanted);
        def = customFieldDefinitionManager.save(def);
        assertTrue(def.getListValues().size() == 1);
    }

    @Test
    public void testGroupedDefinitions() {
        CustomFieldDefinition def = createCustomFieldDefinition();
        def.setFieldType(FieldType.GROUP);
        CustomFieldDefinition g1 = createCustomFieldDefinition();
        CustomFieldDefinition g2 = createCustomFieldDefinition();

        def.addGroupColumn(g1);
        def.addGroupColumn(g2);

        CustomFieldDefinition saved = customFieldDefinitionManager.save(def);
        assertTrue(saved.getGroupColumns().size() == 2);
    }

    private CustomFieldDefinition createCustomFieldDefinition() {
        CustomFieldDefinition def = new CustomFieldDefinition();
        def.setOwnerId(123l);
        def.setClazz("TEST");
        def.setScope("Category=xyz&SubCategory=abc");
        def.setName("Test List Field");
        def.setDescription("Test field");
        def.setFieldType(FieldType.LIST);
        def.setMaxAllowed(1l); //single value
        def.setMinRequired(1l); //mandatory
        def.setSequence(1l);
        return def;
    }
}
