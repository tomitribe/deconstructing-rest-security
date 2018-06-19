/* =====================================================================
 *
 * Copyright (c) 2011 David Blevins.  All rights reserved.
 *
 * =====================================================================
 */
package io.superbiz.sec;

import org.tomitribe.crest.cmds.processors.Commands;

import java.util.Arrays;
import java.util.Iterator;

public class Loader implements Commands.Loader {

    @Override
    public Iterator<Class<?>> iterator() {
        return Arrays.asList(new Class<?>[]{Hash.class}).iterator();
    }
}
