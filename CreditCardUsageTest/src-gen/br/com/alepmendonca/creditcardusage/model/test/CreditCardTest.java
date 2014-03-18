package br.com.alepmendonca.creditcardusage.model.test;

import br.com.alepmendonca.creditcardusage.dao.CreditCardDao;
import br.com.alepmendonca.creditcardusage.model.CreditCard;
import de.greenrobot.dao.test.AbstractDaoTestLongPk;

public class CreditCardTest extends AbstractDaoTestLongPk<CreditCardDao, CreditCard> {

    public CreditCardTest() {
        super(CreditCardDao.class);
    }

    @Override
    protected CreditCard createEntity(Long key) {
    	CreditCard entity = new CreditCard(1L, 7010, "VISA", "John Doe");
        return entity;
    }

}
