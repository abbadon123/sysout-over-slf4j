/*
 * Copyright (c) 2009-2012 Robert Elliot
 * All rights reserved.
 *
 * Permission is hereby granted, free  of charge, to any person obtaining
 * a  copy  of this  software  and  associated  documentation files  (the
 * "Software"), to  deal in  the Software without  restriction, including
 * without limitation  the rights to  use, copy, modify,  merge, publish,
 * distribute,  sublicense, and/or sell  copies of  the Software,  and to
 * permit persons to whom the Software  is furnished to do so, subject to
 * the following conditions:
 *
 * The  above  copyright  notice  and  this permission  notice  shall  be
 * included in all copies or substantial portions of the Software.
 *
 * THE  SOFTWARE IS  PROVIDED  "AS  IS", WITHOUT  WARRANTY  OF ANY  KIND,
 * EXPRESS OR  IMPLIED, INCLUDING  BUT NOT LIMITED  TO THE  WARRANTIES OF
 * MERCHANTABILITY,    FITNESS    FOR    A   PARTICULAR    PURPOSE    AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE,  ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package uk.org.lidalia.sysoutslf4j.context.log4j;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Collection;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.SimpleLayout;
import org.junit.Test;

import uk.org.lidalia.slf4jtest.LoggingEvent;
import uk.org.lidalia.slf4jtest.TestLoggerFactory;
import uk.org.lidalia.sysoutslf4j.SysOutOverSLF4JTestCase;
import uk.org.lidalia.sysoutslf4j.context.LoggingMessages;
import uk.org.lidalia.sysoutslf4j.context.SysOutOverSLF4J;
import uk.org.lidalia.sysoutslf4j.system.SystemOutput;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsCollectionContaining.hasItem;
import static uk.org.lidalia.slf4jtest.LoggingEvent.warn;

public class ConsoleAppenderTests extends SysOutOverSLF4JTestCase {

    @Test
    public void appenderStillPrintsToSystemOut() {

        ByteArrayOutputStream outputStreamBytes = systemOutOutputStream();

        SysOutOverSLF4J.sendSystemOutAndErrToSLF4J();

        Logger log = Logger.getRootLogger();
        log.setLevel(Level.INFO);
        log.removeAllAppenders();
        log.addAppender(new ConsoleAppender(new SimpleLayout()));

        log.info("some log text");

        String outString = new String(outputStreamBytes.toByteArray());
        assertThat(outString, containsString("some log text"));

        Collection<LoggingEvent> allLoggingEvents = TestLoggerFactory.getAllLoggingEvents();
        assertThat(allLoggingEvents, not(hasItem(equalTo(warn(LoggingMessages.PERFORMANCE_WARNING)))));
    }

    ByteArrayOutputStream systemOutOutputStream() {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        PrintStream newSystemOut = new PrintStream(bytes, true);
        SystemOutput.OUT.set(newSystemOut);
        return bytes;
    }
}
