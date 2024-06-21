package com.manga;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try (Scanner input = new Scanner(System.in)) {
            String query;
            while (true) {
                System.out.print("manga> ");
                query = input.nextLine();
                if (query.equals(".exit")) {
                    System.exit(0);
                } else {
                    System.out.println("Unknown Command!");
                    System.out.flush();
                }
            }
        }
    }
}
