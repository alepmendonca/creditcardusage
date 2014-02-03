package br.com.alepmendonca.creditcardusage.model.test;

import de.greenrobot.dao.test.AbstractDaoTestLongPk;

import br.com.alepmendonca.creditcardusage.model.Store;
import br.com.alepmendonca.creditcardusage.dao.StoreDao;

public class StoreTest extends AbstractDaoTestLongPk<StoreDao, Store> {

    public StoreTest() {
        super(StoreDao.class);
    }

    @Override
    protected Store createEntity(Long key) {
        Store entity = new Store();
        entity.setId(key);
        entity.setOriginalName();
        return entity;
    }

}
