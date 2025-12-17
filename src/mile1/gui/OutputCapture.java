package mile1.gui;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class OutputCapture {

    public static String runAndCapture(Runnable action) {
        PrintStream oldOut = System.out;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        System.setOut(ps);

        try {
            action.run();
        } finally {
            System.out.flush();
            System.setOut(oldOut);
        }

        return baos.toString();
    }
}
