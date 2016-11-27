/*
 *  Created by DerTroglodyt on 2016-11-23 09:41
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

import javax.measure.quantity.Duration;

import de.hdc.commonlibrary.data.atom.DADateTime;
import de.hdc.commonlibrary.data.atom.DAText;
import de.hdc.commonlibrary.data.atom.DAUniqueID;
import de.hdc.commonlibrary.data.atom.DAValue;
import de.hdc.commonlibrary.data.quantity.Pieces;

import static junit.framework.Assert.assertTrue;

public class DAOrderTest {
    @Test
    public void toStream() throws Exception {
        DAWareTypeTree tree = DAWareTypeTree.create();
//        DAWareTypeTreeNode wt = DAWareTypeTreeBootstrap.create();
        DAWareClass wc = tree.getWareClass(DAUniqueID.parse("00000000000000000000000000000002"));
        DAWare w = DAWare.create(wc, DAValue.create("1"));
        w.init(tree);

        ByteArrayOutputStream bos = new ByteArrayOutputStream(1024);
        DAOrder expected = DAOrder.create(DAOrder.Type.BUY_ORDER
                , w
                , DAValue.<Money>create(1, Currency.EUR)
                , DADateTime.now(), DAUniqueID.createRandom()
                , DAUniqueID.createRandom(), DAText.create("A Description of this oerder.")
                , DAValue.<Pieces>create("0")
                , DAValue.<Duration>create("0 s"), DADateTime.now());
        expected.toStream(new DataOutputStream(bos));
        bos.close();

        DataInputStream dis = new DataInputStream(new ByteArrayInputStream(bos.toByteArray()));
        DAOrder b = new DAOrder().fromStream(dis);
        assertTrue(expected.toString() + "###" + b.toString(), expected.equals(b));
    }
}
