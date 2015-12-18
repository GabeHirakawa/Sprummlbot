package ga.codesplash.scrumplex.sprummlbot.tools;

import ga.codesplash.scrumplex.sprummlbot.plugins.Plugin;
import ga.codesplash.scrumplex.sprummlbot.plugins.SprummlPlugin;
import ga.codesplash.scrumplex.sprummlbot.plugins.Sprummlbot;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * This class handles Esceptions.
 * It will write them to a file named lasterror.log
 */
public class Exceptions {

    /**
     * Handles Exeptions
     *
     * @param e     Thrown exception
     * @param CAUSE Custom CAUSE message
     */
    public static void handle(Exception e, String CAUSE) {
        handle(e, CAUSE, true);
    }

    /**
     * Handles Exeptions
     *
     * @param exception Thrown exception
     * @param cause     Custom CAUSE message
     * @param shutdown  Defines if Bot should shutdown.
     */
    public static void handle(Exception exception, String cause, boolean shutdown) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("d_M_Y__HH_mm_ss");
        File directory = new File("logs");
        directory.mkdir();
        File file = new File(directory, "error_" + sdf.format(cal.getTime()) + ".log");
        int i = 1;
        while (file.exists()) {
            file = new File(directory, "error_" + sdf.format(cal.getTime()) + "." + i + ".log");
            i++;
        }
        try {
            file.createNewFile();
        } catch (IOException e) {
            System.err.println("Couldn't write to " + file.getName());
            e.printStackTrace();
            System.err.println("Printing main error...");
            exception.printStackTrace();
        }

        System.err.println(cause + " More information in " + file.getAbsolutePath());

        StringBuilder contents = new StringBuilder();
        contents.append("Error Log from ").append(sdf.format(cal.getTime())).append(".\n");
        contents.append("Custom message: ").append(cause).append("\n");
        contents.append(EasyMethods.convertExceptionToString(exception));
        contents.append("\n\nPlease contact support!");
        try {
            EasyMethods.writeToFile(file, contents.toString());
        } catch (IOException e) {
            System.err.println("Couldn't write to " + file.getName());
            e.printStackTrace();
            System.err.println("Printing main error...");
            exception.printStackTrace();
        }

        if (shutdown) {
            System.exit(1);
        }
    }

    public static void handlePluginError(Exception exception, SprummlPlugin plugin) {
        handlePluginError(exception, Sprummlbot.getPluginManager().getPluginBySprummlPlugin(plugin));
    }

    public static void handlePluginError(Exception exception, Plugin plugin) {
        handlePluginError(exception, plugin.getFile());
    }

    public static void handlePluginError(Exception exception, File jarFile) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("d_M_Y__HH_mm_ss");
        File directory = new File("logs", "plugins");
        directory.mkdirs();
        File file = new File(directory, jarFile.getName() + "_error_" + sdf.format(cal.getTime()) + ".log");
        int i = 1;
        while (file.exists()) {
            file = new File(directory, jarFile.getName() + "_error_" + sdf.format(cal.getTime()) + "." + i + ".log");
            i++;
        }
        try {
            file.createNewFile();
        } catch (IOException e) {
            System.err.println("Couldn't write to " + file.getName());
            e.printStackTrace();
            System.err.println("Printing main error...");
            exception.printStackTrace();
        }
        System.err.println("[Plugins] ERROR! More information in " + file.getAbsolutePath());

        StringBuilder contents = new StringBuilder();
        contents.append("Error Log from ").append(sdf.format(cal.getTime())).append(".\n");
        contents.append(EasyMethods.convertExceptionToString(exception));
        contents.append("\n\nPlease contact support!");

        try {
            EasyMethods.writeToFile(file, contents.toString());
        } catch (IOException e) {
            System.err.println("Couldn't write to " + file.getName());
            e.printStackTrace();
            System.err.println("Printing main error...");
            exception.printStackTrace();
        }
    }


}