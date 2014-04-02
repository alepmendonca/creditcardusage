package br.com.alepmendonca.creditcardusage.model.test;

import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import br.com.alepmendonca.creditcardusage.dao.CardExtractDao;
import br.com.alepmendonca.creditcardusage.model.CardExtract;
import de.greenrobot.dao.test.AbstractDaoTestLongPk;

@RunWith(RobolectricTestRunner.class)
public class CardExtractTest extends AbstractDaoTestLongPk<CardExtractDao, CardExtract> {

    public CardExtractTest() {
        super(CardExtractDao.class);
    }

    @Override
    protected CardExtract createEntity(Long key) {
        CardExtract entity = new CardExtract();
//        entity.setId(key);
//        entity.setCreditCardId();
//        entity.setExtractMonth();
        return entity;
    }

}
