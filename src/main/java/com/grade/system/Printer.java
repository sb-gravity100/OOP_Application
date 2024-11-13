package com.grade.system;

import java.io.Console;

@SuppressWarnings("all")
public class Printer {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";
    public static final String ANSI_BLACK_BACKGROUND = "\u001B[40m";
    public static final String ANSI_RED_BACKGROUND = "\u001B[41m";
    public static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
    public static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
    public static final String ANSI_BLUE_BACKGROUND = "\u001B[44m";
    public static final String ANSI_PURPLE_BACKGROUND = "\u001B[45m";
    public static final String ANSI_CYAN_BACKGROUND = "\u001B[46m";
    public static final String ANSI_WHITE_BACKGROUND = "\u001B[47m";
    public static final String _line = "=============== ";
    public static final Console c = System.console();

    public void print(String args) {
        System.out.print(args);
    }

    protected void line() {
        System.out.print(_line);
    }

    protected void line(String args) {
        System.out.print(_line);
        print(args);
    }

    protected void newLine() {
        System.out.println();
    }

    protected void clear() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    protected void printExit() {
        line("[\u001B[31m0\u001B[0m] " + ANSI_RED + "EXIT" + ANSI_RESET);
        newLine();
    }

    protected String prompt(String arg) {
        line();
        return c.readLine(arg);
    }

    protected int prompt() {
        line();
        int res = 0;
        try {
            res = Integer.parseInt(c.readLine(ANSI_CYAN + "-> " + ANSI_RESET));
        } catch (Exception e) {
        }
        return res;
    }

    protected int prompt(Runnable failcallback) {
        line();
        int res = 0;
        try {
            res = Integer.parseInt(c.readLine(ANSI_CYAN + "-> " + ANSI_RESET));
        } catch (Exception e) {
            failcallback.run();
        }
        return res;
    }

    protected void cont() {
        line("PRESS ENTER TO CONTINUE...");
        try {
            System.in.read(new byte[2]);
        } catch (Exception e) {
        }
    }

    protected void choices(String[] choices) {
        int i = 1;
        for (String string : choices) {
            line("[" + ANSI_YELLOW + i + ANSI_RESET + "] " + ANSI_GREEN + string + ANSI_RESET);
            newLine();
            i++;
        }
    }
}
