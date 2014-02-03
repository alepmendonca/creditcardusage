package br.com.alepmendonca.creditcardusage.dao;

import android.database.sqlite.SQLiteDatabase;

import java.util.Map;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.AbstractDaoSession;
import de.greenrobot.dao.identityscope.IdentityScopeType;
import de.greenrobot.dao.internal.DaoConfig;

import br.com.alepmendonca.creditcardusage.StoreType;
import br.com.alepmendonca.creditcardusage.Store;
import br.com.alepmendonca.creditcardusage.CreditCard;
import br.com.alepmendonca.creditcardusage.CardReceipt;

import br.com.alepmendonca.creditcardusage.dao.StoreTypeDao;
import br.com.alepmendonca.creditcardusage.dao.StoreDao;
import br.com.alepmendonca.creditcardusage.dao.CreditCardDao;
import br.com.alepmendonca.creditcardusage.dao.CardReceiptDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see de.greenrobot.dao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig storeTypeDaoConfig;
    private final DaoConfig storeDaoConfig;
    private final DaoConfig creditCardDaoConfig;
    private final DaoConfig cardReceiptDaoConfig;

    private final StoreTypeDao storeTypeDao;
    private final StoreDao storeDao;
    private final CreditCardDao creditCardDao;
    private final CardReceiptDao cardReceiptDao;

    public DaoSession(SQLiteDatabase db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        storeTypeDaoConfig = daoConfigMap.get(StoreTypeDao.class).clone();
        storeTypeDaoConfig.initIdentityScope(type);

        storeDaoConfig = daoConfigMap.get(StoreDao.class).clone();
        storeDaoConfig.initIdentityScope(type);

        creditCardDaoConfig = daoConfigMap.get(CreditCardDao.class).clone();
        creditCardDaoConfig.initIdentityScope(type);

        cardReceiptDaoConfig = daoConfigMap.get(CardReceiptDao.class).clone();
        cardReceiptDaoConfig.initIdentityScope(type);

        storeTypeDao = new StoreTypeDao(storeTypeDaoConfig, this);
        storeDao = new StoreDao(storeDaoConfig, this);
        creditCardDao = new CreditCardDao(creditCardDaoConfig, this);
        cardReceiptDao = new CardReceiptDao(cardReceiptDaoConfig, this);

        registerDao(StoreType.class, storeTypeDao);
        registerDao(Store.class, storeDao);
        registerDao(CreditCard.class, creditCardDao);
        registerDao(CardReceipt.class, cardReceiptDao);
    }
    
    public void clear() {
        storeTypeDaoConfig.getIdentityScope().clear();
        storeDaoConfig.getIdentityScope().clear();
        creditCardDaoConfig.getIdentityScope().clear();
        cardReceiptDaoConfig.getIdentityScope().clear();
    }

    public StoreTypeDao getStoreTypeDao() {
        return storeTypeDao;
    }

    public StoreDao getStoreDao() {
        return storeDao;
    }

    public CreditCardDao getCreditCardDao() {
        return creditCardDao;
    }

    public CardReceiptDao getCardReceiptDao() {
        return cardReceiptDao;
    }

}
