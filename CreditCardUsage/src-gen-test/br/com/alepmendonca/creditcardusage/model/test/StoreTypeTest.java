package br.com.alepmendonca.creditcardusage.model.test;

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

}
