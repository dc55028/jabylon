/**
 * (C) Copyright 2013 Jabylon (http://www.jabylon.org) and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jabylon.review.standard.internal;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import org.jabylon.common.review.ReviewParticipant;
import org.jabylon.properties.Property;

public class MessageFormatCheckTest extends AbstractReviewTest{

    @Test
    public void testReview() {

        Property master = createProperty("key", "value {0}");
        Property slave = createProperty("value", "{0} wert");
        assertNull(getFixture().review(null, master, slave));
    }

    @Test
    public void testReviewNoHit() {

        Property master = createProperty("key", "value");
        Property slave = createProperty("value", "wert");
        assertNull(getFixture().review(null, master, slave));
    }

    @Test
    public void testReviewWrongNumber() {

        Property master = createProperty("key", "value {0}");
        Property slave = createProperty("value", "{1} wert");
        assertNotNull(getFixture().review(null, master, slave));
    }

    @Test
    public void testReviewMissingFormat() {

        Property master = createProperty("key", "value {0}");
        Property slave = createProperty("value", " wert");
        assertNotNull(getFixture().review(null, master, slave));
    }

    @Test
    public void testReviewExtraFormat() {

        Property master = createProperty("key", "value {0}");
        Property slave = createProperty("value", "{0} wert {2}");
        assertNotNull(getFixture().review(null, master, slave));
    }

    @Test
    public void testReviewDoubleUsageMaster() {

        Property master = createProperty("key", "{0} value {0}");
        Property slave = createProperty("value", "{0} wert");
        assertNull(getFixture().review(null, master, slave));
    }

    @Test
    public void testReviewDoubleUsageSlave() {

        Property master = createProperty("key", "value {0}");
        Property slave = createProperty("value", "{0} wert {0}");
        assertNull(getFixture().review(null, master, slave));
    }
    /**
     * @see http://github.com/jutzig/jabylon/issues/issue/65
     */
    @Test
    public void testReviewChoiceFormat() {

        Property master = createProperty("key", "value {0,choice,0#days|1#day|1<days}");
        Property slave = createProperty("value", "wert {0,choice,0#Tage|1#Tag|1<Tage}");
        assertNull("this is ok, because the 0 argument is present in both cases",getFixture().review(null, master, slave));
    }

    public void testReviewChoiceFormatWrongNumber() {

        Property master = createProperty("key", "value {0,choice,0#days|1#day|1<days}");
        Property slave = createProperty("value", "wert {1,choice,0#Tage|1#Tag|1<Tage}");
        assertNotNull("this is not ok, because the master uses 0 and the slave 1",getFixture().review(null, master, slave));
    }



    @Override
    public ReviewParticipant createFixture() {
        return new MessageFormatCheck();
    }

}
