package org.nabla;

import org.nabla.processes.StatisticMaker;
import org.nabla.processes.TextProcess;
import org.nabla.utills.FileLoader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;


public class Solution {
    public static void main(String[] args) throws IOException {
        FileLoader connector = FileLoader.getInstance();
        TextProcess maker = StatisticMaker.getInstance();
        Path p;

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("enter - Enter new web page;");
        System.out.println("show - Show word statistic and save to file;");
        System.out.println("exit - Exit;");
        String str;

        while((str = br.readLine()) != null) {
            switch(str) {
                case "enter" :
                    System.out.println("Your link: ");
                    try {
                        p = connector.downloadRawText(br.readLine());
                        maker.process(p);
                        System.out.println("Done!");
                    } catch (IOException ex) {
                        System.out.println("Try again!");
                    }
                    break;
                case "show" :
                    try {
                        System.out.println("Statistic: ");
                        maker.show();
                        maker.saveToFile();
                    } catch (IOException ex) {
                        System.out.println("Nothing to show. Try again.");
                    }
                    break;
                case "exit" :
                    System.exit(0);
            }
        }
    }
}
