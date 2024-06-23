package com.manga;

import java.util.Scanner;

import com.manga.sql.QueryExecutor;
import com.manga.sql.QueryParser;

public class Main {
    private static QueryParser parser;
    private static QueryExecutor executor;
    public static void main(String[] args) {
        try (Scanner input = new Scanner(System.in)) {
            String query;
            while (true) {
                System.out.print("manga> ");
                query = input.nextLine();
                parser = new QueryParser(query);
                // executor = new QueryExecutor(parser.parse());
                // executor.execute();

                System.out.println(parser.parse());
            }
        }
    }
}
