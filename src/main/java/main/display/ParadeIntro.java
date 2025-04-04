package main.display;

import main.helpers.ScreenUtils;

public class ParadeIntro {


    public static void printAsciiParadeZigZag() {

        
        StringBuilder openingMsg = new StringBuilder();
        openingMsg.append("██████╗ ██████╗ ██████╗  █████╗ ██████╗ ███████╗\n")
        .append("██╔══██╗██╔══██╗██╔══██╗ ██╔══██╗██╔══██╗██╔════╝\n")
        .append("██████╔╝███████║██████╔╝ ███████║██║  ██║█████╗ \n")
        .append("██╔═══╝ ██╔══██║██╔══██╝ ██╔══██║██║  ██║██╔══╝  \n")
        .append("██║     ██║  ██║██║  ██║ ██║  ██║██████╔╝███████╗\n")
        .append("╚═╝     ╚═╝  ╚═╝╚═╝  ╚═╝ ╚═╝  ╚═╝╚═════╝ ╚══════╝");
        System.out.println("-----------------Welcome To--------------------");
        ScreenUtils.pause(500);
        System.out.println();
        System.out.println(openingMsg);
        ScreenUtils.pause(500);
        System.out.println("-----------------------------------------------");
    }

}