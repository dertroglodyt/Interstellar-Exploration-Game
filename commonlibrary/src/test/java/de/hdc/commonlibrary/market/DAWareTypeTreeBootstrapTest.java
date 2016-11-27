/*
 *  Created by DerTroglodyt on 2016-11-22 10:16
 *  Email dertroglodyt@gmail.com
 *  Copyright by HDC, Germany
 */

package de.hdc.commonlibrary.market;

import org.junit.Test;
import de.hdc.commonlibrary.data.atom.DAText;
import static junit.framework.Assert.assertTrue;

public class DAWareTypeTreeBootstrapTest {
    @Test
    public void create() throws Exception {
        DAWareTypeTreeNode root = DAWareTypeTreeBootstrap.create();
        System.out.println(root.findChildByName(DAText.create("Ship")));
        assertTrue(root.findChildByName(DAText.create("Ship")).getDisplayName().toString() == "Ship");
    }
}
