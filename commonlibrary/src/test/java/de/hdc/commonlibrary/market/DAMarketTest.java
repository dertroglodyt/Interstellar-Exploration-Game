/*
 *  Created by DerTroglodyt on 2016-11-23 08:59
 *  Email dertroglodyt@gmail.com
 *  Copyright by HDC, Germany
 */

package de.hdc.commonlibrary.market;

import org.jscience.economics.money.Currency;
import org.jscience.economics.money.Money;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;

import javax.measure.quantity.Dimensionless;
import javax.measure.quantity.Duration;

import de.hdc.commonlibrary.data.atom.DADateTime;
import de.hdc.commonlibrary.data.atom.DAText;
import de.hdc.commonlibrary.data.atom.DAUniqueID;
import de.hdc.commonlibrary.data.atom.DAValue;
import de.hdc.commonlibrary.data.quantity.Pieces;
import de.hdc.commonlibrary.module.DAShipDummy;

import static junit.framework.Assert.assertTrue;

public class DAMarketTest {
    @Test
    public void addOrder() throws Exception {

    }

    @Test
    public void toStream() throws Exception {
        DAWareTypeTreeNode wt = DAWareTypeTreeBootstrap.create();
        DAWareClass wc = wt.findWareClass(DAUniqueID.parse("00000000000000000000000000000002"));

        ByteArrayOutputStream bos = new ByteArrayOutputStream(1024);
        DAMarket expected = DAMarket.create(
                new DAShipDummy(), DAUniqueID.createRandom(), DAText.create("Neumarkt")
                , DAValue.< Dimensionless>create("1 %"));
        expected.toStream(new DataOutputStream(bos));
        bos.close();

        DataInputStream dis = new DataInputStream(new ByteArrayInputStream(bos.toByteArray()));
        DAMarket b = new DAMarket().fromStream(dis);
        assertTrue(expected.toString() + "###" + b.toString(), expected.equals(b));

        bos = new ByteArrayOutputStream(1024);
        expected = DAMarket.create(
                new DAShipDummy(), DAUniqueID.createRandom(), DAText.create("Neumarkt")
                , DAValue.< Dimensionless>create("1 %"));

        expected.addOrder(DAOrder.create(DAOrder.Type.BUY_ORDER
                , DAWare.create(wc, DAValue.create("1 pcs"))
                , DAValue.<Money>create(1, Currency.EUR), DADateTime.now(), DAUniqueID.createRandom()
                , DAUniqueID.createRandom(), DAText.create("A Description of this oerder.")
                , DAValue.<Pieces>create("0")
                , DAValue.<Duration>create("0 s"), DADateTime.now()));
        expected.toStream(new DataOutputStream(bos));
        bos.close();

        dis = new DataInputStream(new ByteArrayInputStream(bos.toByteArray()));
        b = new DAMarket().fromStream(dis);
        assertTrue(expected.toString() + "###" + b.toString(), expected.equals(b));
    }
}
