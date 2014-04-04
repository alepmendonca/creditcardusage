package br.com.alepmendonca.creditcardusage.model.test;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.GregorianCalendar;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import android.database.SQLException;
import br.com.alepmendonca.creditcardusage.dao.CardReceiptDao;
import br.com.alepmendonca.creditcardusage.dao.DaoSession;
import br.com.alepmendonca.creditcardusage.model.CardReceipt;
import br.com.alepmendonca.creditcardusage.model.CreditCard;
import br.com.alepmendonca.creditcardusage.model.Store;

import com.honorables.beckfowler.CreditCardUsageApplication;

import de.greenrobot.dao.DaoException;

@RunWith(RobolectricTestRunner.class)
public class CardReceiptTest extends AbstractDaoTest<CardReceiptDao, CardReceipt> {

	DaoSession d;
	
    public CardReceiptTest() {
        super(CardReceiptDao.class);
    }

    @Before
    public void fillVariables() {
    	 d = ((CreditCardUsageApplication) Robolectric.application).getDAO();
    }
    
    @Override
    protected CardReceipt createEntity(Long key) {
    	DaoSession d = ((CreditCardUsageApplication) Robolectric.application).getDAO();
    	CreditCard cc = new CreditCardTest().createEntity(null);
    	d.getCreditCardDao().insert(cc);
    	Store s = new StoreTest().createEntity(null);
    	d.getStoreDao().insert(s);

        CardReceipt entity = new CardReceipt(cc, s, 
        		new BigDecimal(430), Currency.getInstance("BRL"),
        		new GregorianCalendar(2014, 3, 25, 16, 45, 02),
        		103554L);
        if (key != null) entity.setId(key);
        return entity;
    }

    @Test
    public void cardReceiptVerifyFields() {
    	CardReceipt cr = createEntity(null);
    	dao.insert(cr);
    	cr = dao.load(cr.getId());
    	
    	assertEquals(1234, cr.getCreditCard().getFinalNumbers());
    	assertEquals("ORIGINAL NAME", cr.getStore().getOriginalName());
    	assertEquals(Currency.getInstance("BRL"), cr.getCurrency());
    	assertEquals(430d, cr.getValue(), 0.01);
    	assertEquals(new GregorianCalendar(2014, 3, 25, 16, 45, 02).getTime(), cr.getAuthorizationDate());
    	assertEquals(103554L, cr.getTransaction());
    	assertEquals("Cartão 1234 creditado R$ 430,00 em ORIGINAL NAME", cr.toString());
    }
    
    @Test(expected=DaoException.class)
    public void cardReceiptFailWithoutCreditCard() {
    	CardReceipt cr = createEntity(null);
    	try {
    		cr.setCreditCard(null);
    	} catch (DaoException dex) {
    		assertEquals("To-one property 'creditCardId' has not-null constraint; cannot set to-one to null", dex.getMessage());
    		throw dex;
    	}
    }

    @Test(expected=DaoException.class)
    public void cardReceiptFailWithoutCreditCardId() {
    	CardReceipt cr = createEntity(null);
    	GregorianCalendar calendar = new GregorianCalendar();
    	calendar.setTime(cr.getAuthorizationDate());

    	try {
        	new CardReceipt(
        			null, 
        			d.getStoreDao().load(cr.getStoreId()), 
        			new BigDecimal(cr.getValue()), 
        			cr.getCurrency(), 
        			calendar, 
        			cr.getTransaction());
    	} catch (DaoException dex) {
    		assertEquals("To-one property 'creditCardId' has not-null constraint; cannot set to-one to null", dex.getMessage());
    		throw dex;
    	}
    }
 
    @Test(expected=DaoException.class)
    public void cardReceiptFailWithoutStore() {
    	CardReceipt cr = createEntity(null);
    	try {
    		cr.setStore(null);
    	} catch (DaoException dex) {
    		assertEquals("To-one property 'storeId' has not-null constraint; cannot set to-one to null", dex.getMessage());
    		throw dex;
    	}
    }

    @Test(expected=DaoException.class)
    public void cardReceiptFailWithoutStoreId() {
    	CardReceipt cr = createEntity(null);
    	GregorianCalendar calendar = new GregorianCalendar();
    	calendar.setTime(cr.getAuthorizationDate());

    	try {
        	new CardReceipt(
        			d.getCreditCardDao().load(cr.getCreditCardId()), 
        			null, 
        			new BigDecimal(cr.getValue()), 
        			cr.getCurrency(), 
        			calendar, 
        			cr.getTransaction());
    	} catch (DaoException dex) {
    		assertEquals("To-one property 'storeId' has not-null constraint; cannot set to-one to null", dex.getMessage());
    		throw dex;
    	}
    }

    @Test(expected=IllegalArgumentException.class)
    public void cardReceiptFailWithoutValue() {
    	CardReceipt cr = createEntity(null);
    	try {
    		cr.setValue(0);
    	} catch (IllegalArgumentException ex) {
    		assertEquals("Property value cannot be zero", ex.getMessage());
    		throw ex;
    	}
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void cardReceiptFailWithoutCurrency() {
    	CardReceipt cr = createEntity(null);
    	try {
    		cr.setCurrency(null);
    	} catch (IllegalArgumentException ex) {
    		assertEquals("Property currency cannot be null", ex.getMessage());
    		throw ex;
    	}
    }

    @Test
    public void cardReceiptUseDefaultCurrency() {
    	CardReceipt cr = createEntity(null);
    	GregorianCalendar calendar = new GregorianCalendar();
    	calendar.setTime(cr.getAuthorizationDate());
    	CardReceipt cr2 = new CardReceipt(
    			d.getCreditCardDao().load(cr.getCreditCardId()), 
    			d.getStoreDao().load(cr.getStoreId()), 
    			new BigDecimal(cr.getValue()), 
    			null, 
    			calendar, 
    			cr.getTransaction());
    	dao.insert(cr2);
    	
    	assertEquals(NumberFormat.getInstance().getCurrency(), cr2.getCurrency());
    }

    @Test(expected=IllegalArgumentException.class)
    public void cardReceiptFailWithoutAuthorizationDate() {
    	CardReceipt cr = createEntity(null);
    	try {
        	new CardReceipt(
        			d.getCreditCardDao().load(cr.getCreditCardId()), 
        			d.getStoreDao().load(cr.getStoreId()), 
        			new BigDecimal(cr.getValue()), 
        			cr.getCurrency(), 
        			null, 
        			cr.getTransaction());
    	} catch (IllegalArgumentException ex) {
    		assertEquals("Property authorizationDate cannot be null", ex.getMessage());
    		throw ex;
    	}
    }

    @Test(expected=SQLException.class)
    public void cardReceiptFail2ReceiptsWithSameStoreTransactionAndCard() {
    	CardReceipt cr = createEntity(null);
    	cr.setCurrency(Currency.getInstance("EUR"));
    	dao.insert(cr);

    	CardReceipt cr2 = createEntity(null);
    	cr2.setCreditCard(cr.getCreditCard());
    	cr2.setStore(cr.getStore());

    	assertEquals(cr.getCreditCardId(), cr2.getCreditCard().getId().longValue());
    	assertEquals(cr.getStoreId(), cr2.getStore().getId().longValue());
    	assertEquals(cr.getTransaction(), cr2.getTransaction());
    	assertEquals(0, new CardReceipt.CardReceiptComparator().compare(cr, cr2));
    	
    	try {
    		dao.insert(cr2);
    	} catch (SQLException ex) {
    		assertEquals("[SQLITE_CONSTRAINT]  Abort due to constraint violation (columns CREDIT_CARD_ID, STORE_ID, TRANSACTION are not unique)", ex.getMessage());
    		throw ex;
    	}
    }
}
