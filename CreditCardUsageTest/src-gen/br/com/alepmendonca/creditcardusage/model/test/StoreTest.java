package br.com.alepmendonca.creditcardusage.model.test;

import de.greenrobot.dao.test.AbstractDaoTestLongPk;

import br.com.alepmendonca.creditcardusage.model.Store;
import br.com.alepmendonca.creditcardusage.model.StoreType;
import br.com.alepmendonca.creditcardusage.dao.StoreDao;

public class StoreTest extends AbstractDaoTestLongPk<StoreDao, Store> {

    public StoreTest() {
        super(StoreDao.class);
    }

    @Override
    protected Store createEntity(Long key) {
    	StoreType storeType = new StoreType(3L, "Eletronicos", null);
        Store entity = new Store(2L, "LOJA CEM", null, storeType.getId());
        return entity;
    }

}
