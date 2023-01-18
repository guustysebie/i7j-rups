package com.itextpdf.rups.io.cli;

import org.junit.Test;

import static org.junit.Assert.*;

public class RupsCliArgumentsTest {


    @Test
    public void noArgs() {
        RupsCliArguments args = new RupsCliArguments(new String[]{});
        assertFalse(args.attachToRunningInstance());
        assertFalse(args.shouldPrintHelp());
        assertFalse(args.shouldPrintVersion());
        assertEquals(0, args.getPdfFiles().size());
    }

    @Test
    public void argsAreNull() {
        RupsCliArguments args = new RupsCliArguments(null);
        assertFalse(args.attachToRunningInstance());
        assertFalse(args.shouldPrintHelp());
        assertFalse(args.shouldPrintVersion());
        assertEquals(0, args.getPdfFiles().size());
    }

    @Test
    public void help() {
        RupsCliArguments args = new RupsCliArguments(new String[]{"-h"});
        assertFalse(args.attachToRunningInstance());
        assertTrue(args.shouldPrintHelp());
        assertFalse(args.shouldPrintVersion());
        assertEquals(0, args.getPdfFiles().size());
    }

    @Test
    public void version() {
        RupsCliArguments args = new RupsCliArguments(new String[]{"-v"});
        assertFalse(args.attachToRunningInstance());
        assertFalse(args.shouldPrintHelp());
        assertTrue(args.shouldPrintVersion());
        assertEquals(0, args.getPdfFiles().size());
    }

    @Test
    public void attach() {
        RupsCliArguments args = new RupsCliArguments(new String[]{"-a"});
        assertTrue(args.attachToRunningInstance());
        assertFalse(args.shouldPrintHelp());
        assertFalse(args.shouldPrintVersion());
        assertEquals(0, args.getPdfFiles().size());
    }

    @Test
    public void oneFileAsParameter() {
        RupsCliArguments args = new RupsCliArguments(new String[]{"file.pdf"});
        assertFalse(args.attachToRunningInstance());
        assertFalse(args.shouldPrintHelp());
        assertFalse(args.shouldPrintVersion());
        assertEquals(1, args.getPdfFiles().size());
    }

    @Test
    public  void twoFilesAsParameter(){
        RupsCliArguments args = new RupsCliArguments(new String[]{"file1.pdf", "file2.pdf"});
        assertFalse(args.attachToRunningInstance());
        assertFalse(args.shouldPrintHelp());
        assertFalse(args.shouldPrintVersion());
        assertEquals(2, args.getPdfFiles().size());
    }

    @Test
    public void oneFileAsParameterWithHelp() {
        RupsCliArguments args = new RupsCliArguments(new String[]{"-h", "file.pdf"});
        assertFalse(args.attachToRunningInstance());
        assertTrue(args.shouldPrintHelp());
        assertFalse(args.shouldPrintVersion());
        assertEquals(1, args.getPdfFiles().size());
    }

    @Test
    public void twoFilesAsParameterWithHelp(){
        RupsCliArguments args = new RupsCliArguments(new String[]{"-h", "file1.pdf", "file2.pdf"});
        assertFalse(args.attachToRunningInstance());
        assertTrue(args.shouldPrintHelp());
        assertFalse(args.shouldPrintVersion());
        assertEquals(2, args.getPdfFiles().size());
    }

}