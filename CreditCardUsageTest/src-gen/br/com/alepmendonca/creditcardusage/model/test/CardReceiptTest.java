package br.com.alepmendonca.creditcardusage.model.test;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Currency;

import de.greenrobot.dao.test.AbstractDaoTestLongPk;

import br.com.alepmendonca.creditcardusage.model.CardReceipt;
import br.com.alepmendonca.creditcardusage.model.CreditCard;
import br.com.alepmendonca.creditcardusage.model.Store;
import br.com.alepmendonca.creditcardusage.model.StoreType;
import br.com.alepmendonca.creditcardusage.dao.CardReceiptDao;

public class CardReceiptTest extends AbstractDaoTestLongPk<CardReceiptDao, CardReceipt> {

    public CardReceiptTest() {
        super(CardReceiptDao.class);
    }

    @Override
    protected CardReceipt createEntity(Long key) {
    	CreditCard card = new CreditCard(1L, 7010, "VISA", "John Doe");
    	StoreType storeType = new StoreType(3L, "Eletronicos", null);
    	Store store = new Store(2L, "LOJA CEM", null, storeType.getId()); 
        CardReceipt entity = new CardReceipt(card, store, new BigDecimal(10),
        		Currency.getInstance("R$"), Calendar.getInstance(), 829389L);
        return entity;
    }

}
