package br.com.alepmendonca.creditcardusage.model.test;

import br.com.alepmendonca.creditcardusage.dao.StoreDao;
import br.com.alepmendonca.creditcardusage.model.Store;

public class StoreTest extends AbstractDaoTest<StoreDao, Store> {

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
