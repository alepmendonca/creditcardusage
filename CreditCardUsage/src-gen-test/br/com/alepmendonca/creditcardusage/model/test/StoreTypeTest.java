package br.com.alepmendonca.creditcardusage.model.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import br.com.alepmendonca.creditcardusage.dao.StoreTypeDao;
import br.com.alepmendonca.creditcardusage.model.StoreType;

@RunWith(RobolectricTestRunner.class)
public class StoreTypeTest extends AbstractDaoTest<StoreTypeDao, StoreType> {

    public StoreTypeTest() {
        super(StoreTypeDao.class);
    }

    @Override
    protected StoreType createEntity(Long key) {
        return new StoreType(key, "Test Store Type", null);
    }

    @Test(expected=IllegalArgumentException.class)
    public void storeTypeFailWithoutName() {
    	StoreType s = createEntity(null);
    	s.setName(null);
    	dao.insert(s);
    }

    @Test
    public void storeTypeWithoutParent() {
    	StoreType s = createEntity(null);
    	dao.insert(s);
    	assertNotNull(s.getId());
    	assertEquals("Test Store Type", s.getName());
    	assertNull(s.getParentType());
    	assertEquals(0, s.getSubtypes().size());
    	assertEquals("Test Store Type", s.toString());
    }

    @Test
    public void storeTypeWithParent() {
    	StoreType s1 = createEntity(null);
    	dao.insert(s1);
    	StoreType s2 = createEntity(null);
    	s2.setParentType(s1);
    	dao.insert(s2);
    	assertEquals(s1, s2.getParentType());
    	assertNull(s1.getParentType());
    	assertEquals(1, s1.getSubtypes().size());
    	assertEquals(s2.getId(), s1.getSubtypes().get(0).getId());
    }

    @Test
    public void storeTypeChangeParent() {
    	StoreType s1 = createEntity(null);
    	dao.insert(s1);
    	StoreType s2 = createEntity(null);
    	s2.setParentType(s1);
    	StoreType s3 = createEntity(null);
    	dao.insertInTx(s2, s3);
    	s2.setParentType(s3);
    	dao.updateInTx(s1, s2, s3);
    	assertEquals(s3, s2.getParentType());
    	assertEquals(0, s1.getSubtypes().size());
    	assertEquals(1, s3.getSubtypes().size());
    	assertEquals(s2.getId(), s3.getSubtypes().get(0).getId());
    }

}
