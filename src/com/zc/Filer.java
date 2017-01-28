package com.zc;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Filer {
    public static void write(int transNumber) {
        try {
            File fac = new File(".\\JavaNotifier.txt");
            if (!fac.exists()) {
                fac.createNewFile();
            }
            int r = 1;
            FileWriter write = new FileWriter(fac);
            write.write(new Integer(transNumber).toString());
            write.flush();
            write.close();
        }

        catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static int read() {
        int result = 0;
        try {
            Scanner scanner = new Scanner(new File(".\\JavaNotifier.txt"));
            result = scanner.nextInt();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return result;
    }
}
