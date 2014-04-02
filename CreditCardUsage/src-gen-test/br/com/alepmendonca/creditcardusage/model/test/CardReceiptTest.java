package br.com.alepmendonca.creditcardusage.model.test;

import org.junit.internal.runners.JUnit38ClassRunner;
import org.junit.runner.RunWith;

import br.com.alepmendonca.creditcardusage.dao.CardReceiptDao;
import br.com.alepmendonca.creditcardusage.model.CardReceipt;
import de.greenrobot.dao.test.AbstractDaoTestLongPk;

@RunWith(JUnit38ClassRunner.class)
public class CardReceiptTest extends AbstractDaoTestLongPk<CardReceiptDao, CardReceipt> {

    public CardReceiptTest() {
        super(CardReceiptDao.class);
    }

    @Override
    protected CardReceipt createEntity(Long key) {
        CardReceipt entity = new CardReceipt();
        entity.setId(key);
//        entity.setCreditCardId();
//        entity.setStoreId();
//        entity.setValue();
//        entity.setCurrency();
//        entity.setTransaction();
//        entity.setAuthorizationDate();
        return entity;
    }

}
