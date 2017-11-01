package ru.geekbrains.java1.dz.dz4.RuslanGafurov;

import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

// Игра Крестики-Нолики.
//
// Прочитать перед запуском!!! Выбор размера поля осуществляется в меню(3*3 или 5*5). Реализовано 2 варианта ИИ с
//  логикой. Для поля 3*3 используется старая версия ИИ. Для 5*5 новая версия ИИ с универсальной логикой.
// Крестики всегда ходят первыми. В меню игры можно выбрать режимы:

// -Одиночная игра (игра против ИИ) + можно выбрать сторону на поле 3*3 или игру за крестики на поле 5*5
// -Игра для двоих (человек против человека)
// -Демо (ИИ против ИИ)
// -Демо2 (рандом против ИИ+)



public class XOXOGame {

    // Внимание!!!! Перенос данных переменных приведет к полной неработоспосбности игры.
    // Вынос сделан с целью уменьшения кол-ва передаваемых параметров в методы

    private static int MAP_SIZE;
    private static int SIZE_WIN_COMB;
    private static final char DOT_X = 'X';  // Обозначение для X
    private static final char DOT_O = 'O';  //  Обозначение для 0
    private static final char DOT_EMPTY = '*'; //  Обозначение для пустой ячейки
    private static char[][] map;
    private static int[][] hashmap;


    public static void main(String[] args) {


        do {
            String playerX;
            String playerO;
            mainBoard();
            int sl = selectMenu();
            if (sl == 0) {
                break;
            } else {
                String[] player = selectPlayer(sl);
                playerX = player[0];
                playerO = player[1];
            }
            if (sl != 6) {
                MAP_SIZE = 3;
                SIZE_WIN_COMB = 3;
            }
            do {
                System.out.println();
                initMap();
                printMap();

                while (true) {
                    inputPlayer(playerX, DOT_X);
                    if (hasWin(DOT_X)) {
                        printWin(playerX, DOT_X);
                        break;
                    }
                    if (isMapFull()) {
                        break;
                    }
                    printMap();
                    inputPlayer(playerO, DOT_O);
                    if (hasWin(DOT_O)) {
                        printWin(playerO, DOT_O);
                        break;
                    }
                    if (isMapFull()) {
                        break;
                    }
                    printMap();
                }
            } while (repeatGame());
        } while (true);
        System.out.println("\nGood Bye!");

    }

    //Вывод титульной таблички
    private static void mainBoard() {
        System.out.println(
                "\n-------------------------" +
                        "\n|          Игра         |" +
                        "\n|   Крестики - Нолики   |" +
                        "\n|       XOXO GAME       |" +
                        "\n-------------------------");
    }

    //Задание параметров игроков и поля
    private static String[] selectPlayer(int select) {
        String[] typePlayer = new String[2];
        MAP_SIZE =3;
        SIZE_WIN_COMB=3;
        switch (select) {
            case 1:
                typePlayer[0] = "human";
                typePlayer[1] = "ai3";
                break;
            case 2:
                typePlayer[0] = "ai3";
                typePlayer[1] = "human";
                break;
            case 3:
                typePlayer[0] = "human";
                typePlayer[1] = "human";
                break;
            case 4:
                typePlayer[0] = "ai3";
                typePlayer[1] = "ai3";
                break;
            case 5:
                typePlayer[0] = "ai";
                typePlayer[1] = "ai3";
                break;
            case 6:
                typePlayer[0] = "human";
                typePlayer[1] = "ai+";
                MAP_SIZE = 5;
                SIZE_WIN_COMB = 4;
        }
        return typePlayer;
    }

    //Главное меню
    private static int selectMenu() {
        Scanner sc = new Scanner(System.in);
        int input = 0, input2 = -1;
        do {
            System.out.println("\n   Меню:\n1.Одиночная игра\n2.Игра на двоих\n3.Демо(ИИ vs ИИ)\n4.Демо2(Рандом vs ИИ)\n5.Выход\n");
            try {
                System.out.print("Введите Ваш Выбор: ");
                input = sc.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Некорректный ввод. Повторите.");
                sc.nextLine();
            }
            switch (input) {
                case 1:
                    do {
                        System.out.println("\nВыбор стороны:\n1.Крестики\n2.Нолики\n3.Крестики (Поле 5x5 - 4 в ряд)\n0.Назад");
                        try {
                            System.out.print("\nВведите Ваш выбор: ");
                            input2 = sc.nextInt();
                        } catch (InputMismatchException e) {
                            System.out.println("Некорректный ввод. Повторите.");
                            sc.nextLine();
                        }
                        if (input2 == 1) {
                            return 1;
                        } else if (input2 == 2) {
                            return 2;
                        } else if (input2 == 3) {
                            return 6;
                        } else if (input2 == 0){
                            break;
                        }
                        input2=-1;
                    }while(true);
                    break;
                case 2:
                    return 3;
                case 3:
                    return 4;
                case 4:
                    return 5;
                case 5:
                    return 0;
            }
            input=0;
        }while(true);

    }

    //Создание игрового поля
    private static void initMap() {
        map = new char[MAP_SIZE][MAP_SIZE];
        for (int i = 0; i < MAP_SIZE; i++) {
            for (int j = 0; j < MAP_SIZE; j++) {
                map[i][j] = DOT_EMPTY;
            }
        }
    } //Status: Complete

    //Вывод игрового поля
    private static void printMap() {
        char[][] printablemap = createPrintMap();
        System.out.println();
        for (int i = 0; i < printablemap.length; i++) {
            for (int j = 0; j < printablemap[i].length; j++) {
                System.out.print(" " + printablemap[i][j] + " ");
                if (j < printablemap[i].length - 1) {
                    System.out.print("|");
                }
            }
            System.out.println();
            if (i < printablemap.length - 1) {
                for (int j = 0; j < printablemap[i].length; j++) {
                    System.out.print("---");
                    if (j < printablemap[i].length - 1) {
                        System.out.print("+");
                    }
                }
            }
            System.out.println();
        }
    } //Status: Complete

    //Создания поля для печати
    private static char[][] createPrintMap() {
        char[][] printmap = new char[MAP_SIZE + 1][MAP_SIZE + 1];
        for (int i = 0; i < printmap.length; i++) {
            for (int j = 0; j < printmap.length; j++) {
                if (i == 0) {
                    printmap[i][j] = (char) (j + 48);
                } else if (j == 0) {
                    printmap[i][j] = (char) (i + 48);

                } else {
                    printmap[i][j] = map[i - 1][j - 1];
                }
            }
        }
        return printmap;
    }// Status: Complete

    //Проверка горизонталей на выйгрыш
    private static boolean checkHorizon(char symbol) {
        for (int i = 0; i < MAP_SIZE; i++) {
            for (int j = 0; j < MAP_SIZE - SIZE_WIN_COMB + 1; j++) {
                int comb = 0;
                for (int p = j; p < j + SIZE_WIN_COMB; p++) {
                    if (map[i][p] == symbol) {
                        comb++;
                    }
                    if (comb == SIZE_WIN_COMB) {
                        return true;
                    }

                }
            }
        }

        return false;
    } //Status: Complete

    //Проверка вертикалей на выйгрыш
    private static boolean checkVertical(char symbol) {
        for (int i = 0; i < MAP_SIZE; i++) {
            for (int j = 0; j < MAP_SIZE - SIZE_WIN_COMB + 1; j++) {
                int comb = 0;
                for (int p = j; p < j + SIZE_WIN_COMB; p++) {

                    if (map[p][i] == symbol) {
                        comb++;
                    }
                    if (comb == SIZE_WIN_COMB) {
                        return true;
                    }

                }
            }
        }
        return false;
    } //Status: Complete

    //Проверка побочных диагоналей на выйгрыш
    private static boolean cheakSecondaryDiag(char symbol) {

        for (int i = 0; i < MAP_SIZE - SIZE_WIN_COMB + 1; i++) {
            for (int j = MAP_SIZE - 1; j >= SIZE_WIN_COMB - 1; j--) {
                int comb = 0;
                for (int p = 0; p < SIZE_WIN_COMB; p++) {
                    if (map[i + p][j - p] == symbol) {
                        comb++;
                    }
                    if (comb == SIZE_WIN_COMB) {
                        return true;
                    }
                }
            }
        }
        return false;
    } // Status: Complete

    //Проверка главных диагоналей на выйгрыш
    private static boolean cheakMainDiag(char symbol) {
        for (int i = 0; i < MAP_SIZE - SIZE_WIN_COMB + 1; i++) {
            for (int j = 0; j < MAP_SIZE - SIZE_WIN_COMB + 1; j++) {
                int comb = 0;
                for (int p = 0; p < SIZE_WIN_COMB; p++) {

                    if (map[i + p][j + p] == symbol) {
                        comb++;
                    }
                    if (comb == SIZE_WIN_COMB) {
                        return true;
                    }
                }
            }
        }
        return false;
    } //Status: Complete

    //Проверка выйгрыша по символу
    private static boolean hasWin(char symbol) {
        return (cheakSecondaryDiag(symbol) || cheakMainDiag(symbol) || checkHorizon(symbol) || checkVertical(symbol));
    } //Status: Complete

    //Выбор источника ввода: ИИ или Человек
    private static void inputPlayer(String typePlayer, char symbol) {

        switch (typePlayer) {
            case "ai+":
                advAITurn(symbol);
                break;
            case "ai3":
                if (SIZE_WIN_COMB == 3 && MAP_SIZE == 3) {
                    aiTurn(symbol);
                    break;
                }
            case "ai":
                randomTurn(symbol);
                break;
            default:
                humanTurn(symbol);
        }

    }

    //Ввод человеком
    private static void humanTurn(char symbol) {
        int x = -1, y = -1;
        Scanner sc = new Scanner(System.in);
        do {
            try {
                System.out.print("Введите позицию для игрока " + symbol + " в формате (X Y): ");
                x = sc.nextInt() - 1;
                y = sc.nextInt() - 1;
            } catch (InputMismatchException e) {
                System.out.println("Не корректный ввод. Повторите попытку.");
                sc.nextLine();
            }
        } while (!isCellEmpty(x, y));
        map[y][x] = symbol;
    }

    //Проверка координат на пустоту
    private static boolean isCellEmpty(int x, int y) {
        if (x < 0 || x > MAP_SIZE - 1 || y < 0 || y > MAP_SIZE - 1) {
            return false;
        }
        if (map[y][x] == DOT_EMPTY) {
            return true;
        } else {
            System.out.println("Позиция уже занята. Попробуйте другую.");
            return false;
        }
    } //Status: Complete

    //Вывод поздравлений
    private static void printWin(String player, char symbol) {
        if ("ai".equals(player) || "ai3".equals(player) || "ai+".equals(player)) {
            printMap();
            System.out.println("\nПобедил компьютер, игравший за " + symbol);
        }
        if ("human".equals(player)) {
            printMap();
            System.out.println("\nПобедил человек, игравший за " + symbol);
        }

    } //Status:Complete

    //Проверка поля на заполнения
    private static boolean isMapFull() {
        for (int i = 0; i < MAP_SIZE; i++) {
            for (int j = 0; j < MAP_SIZE; j++) {
                if (map[i][j] == DOT_EMPTY) return false;
            }
        }
        printMap();
        System.out.println("Карта заполнена. Ничья.");
        return true;
    }

    //Запрос на повтор игры с текущими настройками
    private static boolean repeatGame() {
        int input = -1;
        //String trash;
        boolean exit = false;
        Scanner scanner = new Scanner(System.in);
        do {
            System.out.print("Повторить игру еще раз? 1-Да/0-Нет: ");
            try{
                input = scanner.nextInt();
            } catch (InputMismatchException e){
                System.out.println("Упс... Я Вас не понял.");
                scanner.nextLine();
            }
            if (input == 1 || input == 0) {
                exit = true;
            } else {
                System.out.println("Вводите 0 или 1.");
            }
        } while (!exit);
        return (input == 1);
    }

    //Случайный выбор позиции (простой ИИ)
    private static void randomTurn(char symbol) {

        Random rand = new Random();
        int x, y;
        do {
            x = rand.nextInt(MAP_SIZE);
            y = rand.nextInt(MAP_SIZE);
        } while (!isCellEmptyAI(x, y));
        map[y][x] = symbol;
        System.out.println("Компьютер " + symbol + " выбрал позицию: " + (x + 1) + " " + (y + 1));
    }


    //Логика ИИ - сложная логика только для стандартной игры (3*3).

    //Главная логика сложного ИИ (AI3)
    private static void aiTurn(char symbol) {
        int[][] weight = calcWeight(symbol);
        //System.out.println(Arrays.deepToString(weight));

        if (isWinStep(weight)) {
            //System.out.println("WinStep");
            winStep(weight, symbol);
            return;
        }
        if (isDefStep(weight)) {
            //System.out.println("DefStep");
            defStep(weight, symbol);
            return;
        }
        double rand = Math.random();
        //System.out.println(rand);

        if (isAngleDefStep(symbol) && rand > 0.25) {
            //System.out.println("AngleDef");
            angleDefStep(symbol);
            return;
        }

        if (map[1][1] == DOT_EMPTY && rand > 0.38) {
            map[1][1] = symbol;
            //System.out.println("CenterStep");
            System.out.println("Компьютер " + symbol + " выбрал позицию: " + 2 + " " + 2);
            return;
        }
        randomTurn(symbol);
    }

    //Проверка доступности ячейки для ИИ (не выводит сообщений о не доступности)
    private static boolean isCellEmptyAI(int x, int y) {
        if (x < 0 || x > MAP_SIZE - 1 || y < 0 || y > MAP_SIZE - 1) {
            return false;
        }
        return (map[y][x] == DOT_EMPTY);

    } //Status: Complete

    //Проверка наличия победного хода по весу комбинаций (только для AI3)
    private static boolean isWinStep(int[][] weight) {
        return (minWeight(weight) == -4);
    }

    //Проверка наличия хода блокировки победы соперника по весу комбинации (только для AI3)
    private static boolean isDefStep(int[][] weight) {
        return (maxWeight(weight) == 2);
    }

    //Проведение блокировки хода соперника (только для AI3)
    private static void defStep(int[][] weight, char mysymbol) {
        int[] position = findPosition(weight, 2);
        if (position[0] == 0) {
            for (int i = 0; i < MAP_SIZE; i++) {
                if (map[position[1]][i] == DOT_EMPTY) {
                    map[position[1]][i] = mysymbol;
                    System.out.println("Компьютер " + mysymbol + " выбрал позицию: " + (i + 1) + " " + (position[1] + 1));
                    return;
                }
            }
        }
        if (position[0] == 1) {
            for (int i = 0; i < MAP_SIZE; i++) {
                if (map[i][position[1]] == DOT_EMPTY) {
                    map[i][position[1]] = mysymbol;
                    System.out.println("Компьютер " + mysymbol + " выбрал позицию: " + (position[1] + 1) + " " + (i + 1));
                    return;
                }
            }

        }
        if (position[0] == 2) {
            for (int i = 0; i < MAP_SIZE; i++) {
                if (map[i][i] == DOT_EMPTY) {
                    map[i][i] = mysymbol;
                    System.out.println("Компьютер " + mysymbol + " выбрал позицию: " + (i + 1) + " " + (i + 1));
                    return;
                }
            }
        }
        if (position[0] == 3) {
            for (int i = 0; i < MAP_SIZE; i++) {
                if (map[i][MAP_SIZE - i - 1] == DOT_EMPTY) {
                    map[i][MAP_SIZE - 1 - i] = mysymbol;
                    System.out.println("Компьютер " + mysymbol + " выбрал позицию: " + (MAP_SIZE - i) + " " + (i + 1));
                    return;
                }
            }
        }


    }

    //Выйгрышный ход ИИ (только для AI3)
    private static void winStep(int[][] weight, char mysymbol) {
        int[] position = findPosition(weight, -4);
        if (position[0] == 0) {
            for (int i = 0; i < MAP_SIZE; i++) {
                if (map[position[1]][i] == DOT_EMPTY) {
                    map[position[1]][i] = mysymbol;
                    System.out.println("Компьютер " + mysymbol + " выбрал позицию: " + (i + 1) + " " + (position[1]+1));
                    return;
                }
            }
        }
        if (position[0] == 1) {
            for (int i = 0; i < MAP_SIZE; i++) {
                if (map[i][position[1]] == DOT_EMPTY) {
                    map[i][position[1]] = mysymbol;
                    System.out.println("Компьютер " + mysymbol + " выбрал позицию: " + (position[1]+1) + " " + (i + 1));
                    return;
                }
            }

        }
        if (position[0] == 2) {
            for (int i = 0; i < MAP_SIZE; i++) {
                if (map[i][i] == DOT_EMPTY) {
                    map[i][i] = mysymbol;
                    System.out.println("Компьютер " + mysymbol + " выбрал позицию: " + (i + 1) + " " + (i + 1));
                    return;
                }
            }
        }
        if (position[0] == 3) {
            for (int i = 0; i < MAP_SIZE; i++) {
                if (map[i][MAP_SIZE - i - 1] == DOT_EMPTY) {
                    map[i][MAP_SIZE - 1 - i] = mysymbol;
                    System.out.println("Компьютер " + mysymbol + " выбрал позицию: " + (MAP_SIZE - i) + " " + (i + 1));
                    return;
                }
            }
        }
    }

    //Поиск минимального значения веса комбинаций (только для AI3)
    private static int minWeight(int[][] weight) {
        int min = weight[0][0];
        for (int[] i : weight) {
            for (int j : i) {
                if (min > j) {
                    min = j;
                }
            }
        }
//        for (int i = 0; i < weight.length; i++) {
//            for (int j = 0; j < weight[i].length; j++) {
//                if (min > weight[i][j]) {
//                    min = weight[i][j];
//                }
//            }
//        }
        return min;
    }

    //Поиск максимального значения веса комбинаций (только для AI3)
    private static int maxWeight(int[][] weight) {
        int max = weight[0][0];
        for (int i = 0; i < weight.length; i++) {
            for (int j = 0; j < weight[i].length; j++) {
                if (max < weight[i][j]) {
                    max = weight[i][j];
                }
            }
        }
        return max;
    }

    //Вычисления таблицы веса комбинаций (только для AI3)
    private static int[][] calcWeight(char mysymbol) {
        int[][] weight = {{0, 0, 0}, {0, 0, 0}, {0}, {0}};
        char enemysymbol;
        if (mysymbol == DOT_O) {
            enemysymbol = DOT_X;
        } else {
            enemysymbol = DOT_O;
        }
        for (int i = 0; i < MAP_SIZE; i++) {
            for (int j = 0; j < MAP_SIZE; j++) {
                if (enemysymbol == map[i][j]) {
                    weight[0][i] += 1;
                }
                if (mysymbol == map[i][j]) {
                    weight[0][i] -= 2;
                }
                if (enemysymbol == map[j][i]) {
                    weight[1][i] += 1;
                }
                if (mysymbol == map[j][i]) {
                    weight[1][i] -= 2;
                }
            }
        }
        for (int i = 0; i < MAP_SIZE; i++) {
            if (enemysymbol == map[i][i]) {
                weight[2][0] += 1;
            }
            if (mysymbol == map[i][i]) {
                weight[2][0] -= 2;
            }
            if (enemysymbol == map[i][MAP_SIZE - i - 1]) {
                weight[3][0] += 1;
            }
            if (mysymbol == map[i][MAP_SIZE - i - 1]) {
                weight[3][0] -= 2;
            }
        }

        return weight;
    }

    //Поиск позиции веса в таблицы веса (только для AI3)
    private static int[] findPosition(int[][] weight, int value) {
        int[] pos = new int[2];
        for (int i = 0; i < weight.length; i++) {
            for (int j = 0; j < weight[i].length; j++) {
                if (weight[i][j] == value) {
                    pos[0] = i;
                    pos[1] = j;
                    return pos;
                }
            }
        }
        return pos;
    }

    //Проверка необходимости блокировать углов (только для AI3)
    private static boolean isAngleDefStep(char mysymbol) {
        char enemysymbol;
        if (mysymbol == DOT_O) {
            enemysymbol = DOT_X;
        } else {
            enemysymbol = DOT_O;
        }
        if (map[0][0] == DOT_EMPTY && map[0][1] == enemysymbol && map[1][0] == enemysymbol) {
            return true;
        }
        if (map[0][2] == DOT_EMPTY && map[0][1] == enemysymbol && map[1][2] == enemysymbol) {
            return true;
        }
        if (map[2][0] == DOT_EMPTY && map[1][0] == enemysymbol && map[2][1] == enemysymbol) {
            return true;
        }
        if (map[2][2] == DOT_EMPTY && map[2][1] == enemysymbol && map[1][2] == enemysymbol) {
            return true;
        }
        return false;
    }

    //Блокировка угла (только для AI3)
    private static void angleDefStep(char mysymbol) {
        char enemysymbol;
        if (mysymbol == DOT_O) {
            enemysymbol = DOT_X;
        } else {
            enemysymbol = DOT_O;
        }
        if (map[0][0] == DOT_EMPTY && map[0][1] == enemysymbol && map[1][0] == enemysymbol) {
            map[0][0] = mysymbol;
            System.out.println("Компьютер " + mysymbol + " выбрал позицию: " + 1 + " " + 1);

            return;
        }
        if (map[0][2] == DOT_EMPTY && map[0][1] == enemysymbol && map[1][2] == enemysymbol) {
            map[0][2] = mysymbol;
            System.out.println("Компьютер " + mysymbol + " выбрал позицию: " + 3 + " " + 1);
            return;
        }
        if (map[2][0] == DOT_EMPTY && map[1][0] == enemysymbol && map[2][1] == enemysymbol) {
            map[2][0] = mysymbol;
            System.out.println("Компьютер " + mysymbol + " выбрал позицию: " + 1 + " " + 3);
            return;
        }
        if (map[2][2] == DOT_EMPTY && map[2][1] == enemysymbol && map[1][2] == enemysymbol) {
            map[2][2] = mysymbol;
            System.out.println("Компьютер " + mysymbol + " выбрал позицию: " + 3 + " " + 3);
        }
    }



    //Логика для универсального ИИ

    //Создание карты веса для текущего шага для каждого игрока
    private static void hashMapUpdate(char symbol, double correctionPlayer, double correctionComb) {
        int[][] hashstepmap = new int[MAP_SIZE][MAP_SIZE];

        for (int k = 2; k <= SIZE_WIN_COMB; k++) {
            //Вес для горизонтали
            for (int i = 0; i < MAP_SIZE; i++) {
                for (int j = 0; j < MAP_SIZE - k + 1; j++) {
                    int nosymb = 0, esymb = 0;
                    for (int p = j; p < j + k; p++) {
                        if (map[i][p] == DOT_EMPTY) {
                            nosymb++;
                        }
                        if (map[i][p] == symbol) {
                            esymb++;
                            break;
                        }
                    }
                    if (esymb == 0) {
                        for (int p = j; p < j + k; p++) {
                            hashstepmap[i][p] += Math.round(Math.pow(((k - nosymb) * 40 * correctionPlayer), (k - esymb - (nosymb * correctionComb))));
                        }
                    }
                }
            }
            //Вес для вертикали
            for (int i = 0; i < MAP_SIZE; i++) {
                for (int j = 0; j < MAP_SIZE - k + 1; j++) {
                    int nosymb = 0, esymb = 0;
                    for (int p = j; p < j + k; p++) {
                        if (map[p][i] == DOT_EMPTY) {
                            nosymb++;
                        }
                        if (map[p][i] == symbol) {
                            esymb++;
                            break;
                        }

                    }
                    if (esymb == 0) {
                        for (int p = j; p < j + k; p++) {
                            hashstepmap[p][i] += Math.round(Math.pow(((k - nosymb) * 40 * correctionPlayer), (k - esymb - (nosymb * correctionComb))));
                        }
                    }
                }
            }
            //Вес для побочных диагоналей
            for (int i = 0; i < MAP_SIZE - k + 1; i++) {
                for (int j = MAP_SIZE - 1; j >= k - 1; j--) {
                    int nosymb = 0, esymb = 0;
                    for (int p = 0; p < k; p++) {
                        if (map[i + p][j - p] == DOT_EMPTY) {
                            nosymb++;
                        }
                        if (map[i + p][j - p] == symbol) {
                            esymb++;
                            break;
                        }
                    }
                    if (esymb == 0) {
                        for (int p = 0; p < k; p++) {
                            hashstepmap[i + p][j - p] += Math.round(Math.pow(((k - nosymb) * 40 * correctionPlayer), (k - esymb - (nosymb * correctionComb))));
                        }
                    }

                }
            }
            //Вес для главных диагоналей
            for (int i = 0; i < MAP_SIZE - k + 1; i++) {
                for (int j = 0; j < MAP_SIZE - k + 1; j++) {
                    int nosymb = 0, esymb = 0;
                    for (int p = 0; p < k; p++) {
                        if (map[i + p][j + p] == DOT_EMPTY) {
                            nosymb++;
                        }
                        if (map[i + p][j + p] == symbol) {
                            esymb++;
                            break;
                        }
                    }
                    if (esymb == 0) {
                        for (int p = 0; p < k; p++) {
                            hashstepmap[i + p][j + p] += Math.round(Math.pow(((k - nosymb) * 40 * correctionPlayer), (k - esymb - (nosymb * correctionComb))));
                        }
                    }
                }
            }

        }
        concatHashMap(hashstepmap);
        cleanHashMap();

    }

    //Генерация карты веса позиций со случайным вестом, для реализации случайности хода
    private static void hashMapGenerater(int x) {
        Random rand = new Random();
        hashmap = new int[MAP_SIZE][MAP_SIZE];
        for (int i = 0; i < MAP_SIZE; i++) {
            for (int j = 0; j < MAP_SIZE; j++) {
                hashmap[i][j] = rand.nextInt(x);
            }
        }
    }

    //Сложение основной карты веса с картой веса шага
    private static void concatHashMap(int[][] hashstepmap) {
        for (int i = 0; i < MAP_SIZE; i++) {
            for (int j = 0; j < MAP_SIZE; j++) {
                hashmap[i][j] += hashstepmap[i][j];
            }
        }
    }

    //Удаление из карты веса занятых позиций
    private static void cleanHashMap() {
        for (int i = 0; i < MAP_SIZE; i++) {
            for (int j = 0; j < MAP_SIZE; j++) {
                if (map[i][j] != DOT_EMPTY) {
                    hashmap[i][j] = 0;
                }
            }
        }
    }

    //Выбор хода по максимальному весу
    private static void selectAIPosition(char mysymbol) {
        int max = hashmap[0][0];
        int x = 0, y = 0;
        for (int i = 0; i < MAP_SIZE; i++) {
            for (int j = 0; j < MAP_SIZE; j++) {
                if (hashmap[i][j] > max) {
                    max = hashmap[i][j];
                    x = j;
                    y = i;
                }
            }
        }
        map[y][x] = mysymbol;
        System.out.println("Компьютер за " + mysymbol + " сходил: " + (x + 1) + " " + (y + 1));
    }

    //Главная логика ИИ
    private static void advAITurn(char mysymbol) {
        hashMapGenerater(50);
        char enemysymbol;
        if (mysymbol == DOT_O) {
            enemysymbol = DOT_X;
        } else {
            enemysymbol = DOT_O;
        }
        hashMapUpdate(mysymbol, 1.2, 1);
        hashMapUpdate(enemysymbol, 1.1, 0.9);
        selectAIPosition(mysymbol);
    }
}