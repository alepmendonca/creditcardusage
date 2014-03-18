package br.com.alepmendonca.creditcardusage.model.test;

import de.greenrobot.dao.test.AbstractDaoTestLongPk;

import br.com.alepmendonca.creditcardusage.model.CardExtractToReceipts;
import br.com.alepmendonca.creditcardusage.dao.CardExtractToReceiptsDao;

public class CardExtractToReceiptsTest extends AbstractDaoTestLongPk<CardExtractToReceiptsDao, CardExtractToReceipts> {

    public CardExtractToReceiptsTest() {
        super(CardExtractToReceiptsDao.class);
    }

    @Override
    protected CardExtractToReceipts createEntity(Long key) {
        CardExtractToReceipts entity = new CardExtractToReceipts();
        entity.setExtractId();
        entity.setReceiptId();
        return entity;
    }

}
