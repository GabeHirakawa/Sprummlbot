package net.scrumplex.sprummlbot.tools;

import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class SprummlbotOutStream extends PrintStream {

    public SprummlbotOutStream() {
        super(System.out);
    }

    @Override
    public void println(String msg) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("d.M.Y HH:mm:ss");
        super.println("[" + sdf.format(cal.getTime()) + " | INFO] " + msg);
    }
}
