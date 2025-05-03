package com.inmyhand.refrigerator.common;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class ColoredBannerApplicationRunner implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments args) {
        String GREEN = "\u001B[32m";
        String BLUE = "\u001B[34m";
        String YELLOW = "\u001B[33m";
        String RED = "\u001B[31m";
        String RESET = "\u001B[0m";

        System.out.println("\n" + GREEN + "애플리케이션 시작 완료!" + RESET + "\n");

        System.out.println(GREEN + "프로그래머의 하루:" + RESET);
        System.out.println();
        System.out.println(BLUE + "  (•_•)");
        System.out.println("  <)   )╯\"버그를 찾자!\"");
        System.out.println("   /    \\" + RESET);
        System.out.println();
        System.out.println(YELLOW + "  (•_•)");
        System.out.println("  (   (> \"버그 발견!\"");
        System.out.println("  /    \\" + RESET);
        System.out.println();
        System.out.println(RED + "  (•_•)");
        System.out.println("  <)   )> \"하지만 새 버그가...\"");
        System.out.println("   /    \\" + RESET);
        System.out.println();
        System.out.println(GREEN + "오늘도 즐거운 코딩 ~~" + RESET);
        System.out.println();

    }
}
