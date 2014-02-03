package br.com.alepmendonca.creditcardusage.dao;

import de.greenrobot.dao.test.AbstractDaoTestLongPk;

import br.com.alepmendonca.creditcardusage.StoreType;
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
