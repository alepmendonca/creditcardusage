package br.com.alepmendonca.creditcardusage.model.test;

import br.com.alepmendonca.creditcardusage.dao.CreditCardDao;
import br.com.alepmendonca.creditcardusage.model.CreditCard;

public class CreditCardTest extends AbstractDaoTest<CreditCardDao, CreditCard> {

    public CreditCardTest() {
        super(CreditCardDao.class);
    }

    @Override
    protected CreditCard createEntity(Long key) {
        CreditCard entity = new CreditCard();
        entity.setId(key);
        entity.setFinalNumbers();
        return entity;
    }

}
