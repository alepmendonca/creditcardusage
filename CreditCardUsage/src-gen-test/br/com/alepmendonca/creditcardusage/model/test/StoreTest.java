package br.com.alepmendonca.creditcardusage.model.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import br.com.alepmendonca.creditcardusage.dao.DaoSession;
import br.com.alepmendonca.creditcardusage.dao.StoreDao;
import br.com.alepmendonca.creditcardusage.model.Store;
import br.com.alepmendonca.creditcardusage.model.StoreType;

import com.honorables.beckfowler.CreditCardUsageApplication;

@RunWith(RobolectricTestRunner.class)
public class StoreTest extends AbstractDaoTest<StoreDao, Store> {

    public StoreTest() {
        super(StoreDao.class);
    }

    @Override
    protected Store createEntity(Long key) {
        return new Store(key, "ORIGINAL NAME", null, null);
    }

    @Test(expected=IllegalArgumentException.class)
    public void storeFailWithoutOriginalName() {
    	Store s = createEntity(null);
    	s.setOriginalName(null);
    	dao.insert(s);
    }

    @Test
    public void storeCreateAndUpdateAfterwards() {
    	Store s = createEntity(null);
    	dao.insert(s);
    	assertEquals("ORIGINAL NAME", s.toString());

    	DaoSession d = ((CreditCardUsageApplication) Robolectric.application).getDAO();
    	StoreTypeTest test = new StoreTypeTest();
    	StoreType st = test.createEntity(null);
    	s.setUserDefinedName("Assigned Name");
    	d.getStoreTypeDao().insert(st);
    	s.setStoreTypeId(st.getId());
    	dao.update(s);
    	
    	s = dao.load(s.getId());
    	assertEquals("Assigned Name", s.getUserDefinedName());
    	assertEquals(st.getId(), s.getStoreType().getId());
    	assertEquals("Test Store Type em Assigned Name", s.toString());
    }
}
