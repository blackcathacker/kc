package org.kuali.coeus.common.impl.unit;

import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.kuali.coeus.common.framework.unit.Unit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UnitServiceImplTest {

    private List<Unit> allUnits;
    private Unit one;
    private Unit two;
    private Unit three;
    private UnitServiceImpl unitService;

    @Before
    public void buildServiceToTest() {
        unitService = new UnitServiceImpl();
    }

    @Before
    public void buildAllUnits() {
        //unit hierarchy = one-->two-->three
        one = new Unit();
        one.setUnitNumber("ONE");

        two = new Unit();
        two.setUnitNumber("TWO");
        two.setParentUnitNumber("ONE");

        three = new Unit();
        three.setUnitNumber("THREE");
        three.setParentUnitNumber("TWO");

        allUnits = new ArrayList<Unit>() {{
            add(one);
            add(two);
            add(three);
        }};
    }

    @Test
    public void test_getUnitCaseInsensitive_blankUnitNumber() {
        Assert.assertNull(unitService.getUnitCaseInsensitive(""));
    }

    @Test
    public void test_getUnitCaseInsensitive_nullUnitNumber() {
        Assert.assertNull(unitService.getUnitCaseInsensitive(null));
    }

    @Test
    public void test_findUnitsWithDirectParent_blankUnitNumber() {
        Assert.assertEquals(Collections.emptyList(), unitService.findUnitsWithDirectParent(allUnits, ""));
    }

    @Test()
    public void test_findUnitsWithDirectParent_nullUnitNumber() {
        Assert.assertEquals(Collections.emptyList(), unitService.findUnitsWithDirectParent(allUnits, null));
    }

    @Test()
    public void test_findUnitsWithDirectParent_normal_top_UnitNumber() {
        final List<Unit> expects = new ArrayList<Unit>() {{
            add(two);
            add(three);
        }};

        Assert.assertEquals(expects, unitService.findUnitsWithDirectParent(allUnits, "ONE"));
    }

    @Test()
    public void test_findUnitsWithDirectParent_normal_middle_UnitNumber() {
        final List<Unit> expects = new ArrayList<Unit>() {{
            add(three);
        }};

        Assert.assertEquals(expects, unitService.findUnitsWithDirectParent(allUnits, "TWO"));
    }
}
