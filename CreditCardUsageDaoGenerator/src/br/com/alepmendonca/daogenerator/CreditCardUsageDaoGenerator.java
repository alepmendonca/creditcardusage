/*
 * Copyright (C) 2011 Markus Junginger, greenrobot (http://greenrobot.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package br.com.alepmendonca.daogenerator;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;

/**
 * Mainly copied from DaoExampleDaoGenerator, from greendao project.
 * 
 * Run it as a Java application (not Android).
 * 
 * @author Markus
 */
public class CreditCardUsageDaoGenerator {

	private static Entity storeType, store, cc;

    public static void main(String[] args) throws Exception {
        Schema schema = new Schema(1, "br.com.alepmendonca.creditcardusage.model");
        schema.setDefaultJavaPackageDao("br.com.alepmendonca.creditcardusage.dao");
        schema.setDefaultJavaPackageTest("br.com.alepmendonca.creditcardusage.model.test");
        schema.enableKeepSectionsByDefault();
        schema.enableActiveEntitiesByDefault();

        addStoreType(schema);
        addStore(schema);
        addCreditCard(schema);
        addCardReceipt(schema);

        new DaoGenerator().generateAll(schema, "../../CreditCardUsage/CreditCardUsage/src-gen", "../../CreditCardUsage/CreditCardUsageTest/src-gen");
    }

    private static void addStoreType(Schema schema) {
        storeType = schema.addEntity("StoreType");
        storeType.addIdProperty();
        storeType.addStringProperty("name");
        Property parent = storeType.addLongProperty("parentTypeId").getProperty();
        storeType.addToOne(storeType, parent, "parentType");
        storeType.addToMany(storeType, parent, "subtypes");
    }

    private static void addStore(Schema schema) {
    	store = schema.addEntity("Store");
    	store.addIdProperty();
        store.addStringProperty("originalName").notNull();
        store.addStringProperty("userDefinedName");
        Property type = store.addLongProperty("storeTypeId").getProperty();
        store.addToOne(storeType, type, "storeType");
    }
    private static void addCreditCard(Schema schema) {
        cc = schema.addEntity("CreditCard");
        cc.addIdProperty();
        cc.addIntProperty("finalNumbers").notNull();
        cc.addStringProperty("issuer");
        cc.addStringProperty("owner");
    }
    private static void addCardReceipt(Schema schema) {
    	Entity cardReceipt = schema.addEntity("CardReceipt");
    	cardReceipt.addIdProperty();
    	Property cardId = cardReceipt.addLongProperty("creditCardId").notNull().getProperty();
    	cardReceipt.addToOne(cc, cardId);
    	Property storeId = cardReceipt.addLongProperty("storeId").notNull().getProperty();
    	cardReceipt.addToOne(store, storeId);
    	cardReceipt.addDoubleProperty("value").notNull();
    	cardReceipt.addStringProperty("currency").notNull();
    	cardReceipt.addLongProperty("transaction").notNull();
    	cardReceipt.addDateProperty("authorizationDate").notNull();
    }
}
