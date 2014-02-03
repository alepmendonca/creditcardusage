package br.com.alepmendonca.creditcardusage.model.test;

import de.greenrobot.dao.test.AbstractDaoTestLongPk;

import br.com.alepmendonca.creditcardusage.model.StoreType;
import br.com.alepmendonca.creditcardusage.dao.StoreTypeDao;

public class StoreTypeTest extends AbstractDaoTestLongPk<StoreTypeDao, StoreType> {

    public StoreTypeTest() {
        super(StoreTypeDao.class);
    }

    @Override
    protected StoreType createEntity(Long key) {
        StoreType entity = new StoreType();
        entity.setId(key);
        return entity;
    }

}
