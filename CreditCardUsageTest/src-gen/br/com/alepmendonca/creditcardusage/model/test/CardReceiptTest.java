package br.com.alepmendonca.creditcardusage.model.test;

import de.greenrobot.dao.test.AbstractDaoTestLongPk;

import br.com.alepmendonca.creditcardusage.model.CardReceipt;
import br.com.alepmendonca.creditcardusage.dao.CardReceiptDao;

public class CardReceiptTest extends AbstractDaoTestLongPk<CardReceiptDao, CardReceipt> {

    public CardReceiptTest() {
        super(CardReceiptDao.class);
    }

    @Override
    protected CardReceipt createEntity(Long key) {
        CardReceipt entity = new CardReceipt();
        entity.setId(key);
        entity.setCreditCardId();
        entity.setStoreId();
        entity.setValue();
        entity.setCurrency();
        entity.setTransaction();
        entity.setAuthorizationDate();
        return entity;
    }

}
