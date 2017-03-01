package chess;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * TODO threading AI move progress bar using opponent's clock perform deeper
 * analysis on selected moves? finish calculating capture lines? reuse old tree
 * cache allow user to promote into a rook enforce consistency between board /
 * piece position
 */
import chess.gui.AppWindow;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import javafx.application.Application;

/**
 * Main class and entry point for the app.
 *
 * @author Nick Houser
 */
public class ChessEngine {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        setupLogger();
        Application.launch(AppWindow.class, new String[0]);
    }

    /**
     * Method which sets up logging, including log files and custom format.
     */
    private static void setupLogger() {
        Logger rootLogger = Logger.getLogger("chess");
        try {
            Handler fileLog = new FileHandler("chess.log");
            fileLog.setFormatter(new CustomFormatter());
            fileLog.setLevel(Level.ALL);
            rootLogger.addHandler(fileLog);
        } catch (IOException | SecurityException e) {
            e.printStackTrace();
        }
        rootLogger.setLevel(Level.ALL);
    }

    /**
     * Implementation of Formatter that handles custom log formatting.
     */
    private static class CustomFormatter extends Formatter {

        //date formatter
        private static SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss.SSS");

        /**
         * Method which formats a LogRecord.
         *
         * @param record the record to format
         * @return the formatted string
         */
        @Override
        public String format(LogRecord record) {
            StringBuilder builder = new StringBuilder(1000);
            builder.append(df.format(new Date(record.getMillis())));
            builder.append(" - [");
            builder.append(record.getSourceClassName());
            builder.append(".");
            builder.append(record.getSourceMethodName());
            builder.append("] -");
            while (builder.length() < 70) {
                builder.append("-");
            }
            builder.append(" [");
            builder.append(record.getLevel());
            builder.append("] - ");
            builder.append(formatMessage(record));
            builder.append(System.lineSeparator());
            return builder.toString();
        }
    }
}
