package br.com.alepmendonca.creditcardusage.model.test;

import de.greenrobot.dao.test.AbstractDaoTestLongPk;

import br.com.alepmendonca.creditcardusage.model.CardExtract;
import br.com.alepmendonca.creditcardusage.dao.CardExtractDao;

public class CardExtractTest extends AbstractDaoTestLongPk<CardExtractDao, CardExtract> {

    public CardExtractTest() {
        super(CardExtractDao.class);
    }

    @Override
    protected CardExtract createEntity(Long key) {
        CardExtract entity = new CardExtract();
        entity.setId(key);
        entity.setCreditCardId();
        entity.setExtractMonth();
        return entity;
    }

}
