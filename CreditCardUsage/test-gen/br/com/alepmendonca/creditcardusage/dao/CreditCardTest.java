package br.com.alepmendonca.creditcardusage.dao;

import de.greenrobot.dao.test.AbstractDaoTestLongPk;

import br.com.alepmendonca.creditcardusage.CreditCard;
import br.com.alepmendonca.creditcardusage.dao.CreditCardDao;

public class CreditCardTest extends AbstractDaoTestLongPk<CreditCardDao, CreditCard> {

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
