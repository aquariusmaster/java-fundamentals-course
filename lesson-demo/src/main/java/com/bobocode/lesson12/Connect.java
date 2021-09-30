package com.bobocode.lesson12;

import lombok.SneakyThrows;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Connect {
    @SneakyThrows
    public static void main(String[] args) {

        try (Socket socket = new Socket("93.175.204.87", 8899)) {
            var out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())));
            out.print("Test 1");
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

//        try (Socket socket = new Socket("93.175.204.87", 8899)) {
//            var out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())));
//            Scanner scanner = new Scanner(System.in);
//            System.out.println("Enter message: ");
//            while (scanner.hasNext()) {
//                var mes = scanner.nextLine();
//                out.println(mes);
//                out.flush();
//            }
//            scanner.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }
}
