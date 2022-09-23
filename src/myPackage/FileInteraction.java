package myPackage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class FileInteraction {

    private Dictionary d;

    FileInteraction() {
        d = new Dictionary("null");
    }

    void setDictionary(Dictionary d) {
        this.d = d;
    }

    public Dictionary fileParser(String filePath) {
        try {
            File myFile = new File(filePath);
            Scanner myReader = new Scanner(myFile);
            int line = 0;
            while (myReader.hasNextLine()) {
                if (line == 0) {
                    d.setName(myReader.nextLine());
                }
                ArrayList<String> en = new ArrayList<String>();
                ArrayList<String> ru = new ArrayList<String>();
                String data = myReader.nextLine();
                String delims = "[;]";
                String[] res = data.split(delims);


                if (res[1] != null) {
                    if (res[1].contains(",")) {
                        String[] individualWords = res[1].split(",");
                        for (String i : individualWords) {
                            ru.add(i);
                        }
                    } else {
                        ru.add(res[1]);
                    }
                }
                en.add(res[0]);

                d.addTranslation(en, ru);
                line++;
            }
            myReader.close();
            return d;
        } catch (FileNotFoundException e) {
            System.out.println("Файл не найден!");
            e.printStackTrace();
            return null;
        }
    }

    public static boolean wordInDictionary(String word, Scanner dictionary, String language) {

        dictionary.nextLine();
        for (int i = 0; dictionary.hasNextLine() != false; i++) {
            String delims = "[;]";
            String[] temp = dictionary.nextLine().split(delims);

            if (language.equals("en")) {
                if (temp[0].equals(word)) {
                    return true;
                }
            }
            if (language.equals("ru")) {
                if (temp[1].contains(",")) {
                    String[] individualWords = temp[1].split(",");
                    for (String j : individualWords) {
                        if (j.equals(word)) {
                            return true;
                        }
                    }
                } else {
                    if (temp[1].equals(word)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static void replaceLine(String word, String filler) {

        FileInteraction rf = new FileInteraction();
        String filePath = "src/myPackage/dictionary_en_ru.txt";
        rf.d = rf.fileParser(filePath);
        try {
            BufferedReader file = new BufferedReader(new FileReader(filePath));
            StringBuffer inputBuffer = new StringBuffer();
            String line;

            while ((line = file.readLine()) != null) {
                inputBuffer.append(line + "\n");
            }
            file.close();
            String inputStr = inputBuffer.toString();
            inputStr = inputStr.substring(0, inputStr.length() - 1);

            ArrayList<String> temp = new ArrayList<String>();
            temp.add(word);
            temp.addAll(rf.d.getTranslation(temp));

            String toReplace = temp.get(0) + ";" + temp.get(1) + "," + filler;
            inputStr = inputStr.replace(temp.get(0) + ";" + temp.get(1), toReplace);

            FileOutputStream fileOut = new FileOutputStream(filePath);
            fileOut.write(inputStr.getBytes());
            fileOut.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Проблема чтения файла!");
        }
    }

    public static void main(String[] args) throws FileNotFoundException {

        Scanner input = new Scanner(System.in);
        int choice = 0;
        do {
            FileInteraction rf = new FileInteraction();
            String filePath = "src/myPackage/dictionary_en_ru.txt";
            rf.d = rf.fileParser(filePath);
            Scanner myScanner = new Scanner(new File(filePath));

            System.out.println("Выберите:\n1. Перевести слово\n2. Добавить перевод\n3. Посмотреть словарь\n4. Выйти");
            choice = input.nextInt();
            if (choice < 0 || choice > 4) {
                System.out.println("Выберите опцию");
                choice = input.nextInt();
            }
            System.out.println(choice);

            if (choice == 1) {
                System.out.println("1. Английский->русский\n2. Русский->Английский");
                int languageChoice = input.nextInt();
                input.nextLine();
                while (languageChoice < 1 || languageChoice > 2) {
                    System.out.println("Выберите опцию");
                    languageChoice = input.nextInt();
                    input.nextLine();
                }


                if (languageChoice == 1) {
                    System.out.println("Введите слово на английском для перевода:");
                    ArrayList<String> wordToTranslate = new ArrayList<String>(1);
                    String word = input.nextLine();
                    wordToTranslate.add(word);
                    if (wordInDictionary(word, myScanner, "en")) {
                        System.out.println("[" + word + "] на русском " + rf.d.getTranslation(wordToTranslate));
                    } else {
                        System.out.println("[" + word + "] нет в словаре!");
                    }
                    continue;
                }

                if (languageChoice == 2) {
                    System.out.println("Введите слово на русском для перевода:");
                    ArrayList<String> wordToTranslate = new ArrayList<String>(1);
                    String word = input.nextLine();
                    wordToTranslate.add(word);
                    if (wordInDictionary(word, myScanner, "ru")) {
                        System.out.println("[" + word + "] на английском " + rf.d.getTranslation(wordToTranslate));
                    } else {
                        System.out.println("[" + word + "] нет в словаре!");
                    }
                }
            }

            if (choice == 2) {
                input.nextLine();
                System.out.println("Введите слово на английском:");
                String wordEN = input.nextLine();
                System.out.println("Введите слово на русском:");
                String wordRU = input.nextLine();
                ArrayList<String> ru = new ArrayList<String>(1);
                ArrayList<String> en = new ArrayList<String>(1);
                ru.add(wordRU);
                en.add(wordEN);
                rf.d.addTranslation(en, ru);
                if (!wordInDictionary(wordRU, myScanner, "ru")) {
                    try {
                        FileWriter fw = new FileWriter(filePath, true);
                        Scanner myScanner1 = new Scanner(new File(filePath));
                        if (wordInDictionary(wordEN, myScanner1, "en")) {
                            replaceLine(wordEN, wordRU);
                        } else {
                            fw.write("\n" + wordEN + ";" + wordRU);
                        }
                        fw.close();
                    } catch (IOException e) {
                        System.out.println("Ошибка!");
                        e.printStackTrace();
                    }
                    System.out.println("Перевод: " + wordEN + " -> " + wordRU + " был добавлен в словарь.");
                } else {
                    System.out.println("В словаре уже есть данный перевод!");
                }
            }

            if (choice == 3) {
                rf.d.showHashMap();
            }

            if (choice == 4) {
                System.out.println("Вы вышли из программы.");
            }
        } while (choice != 4);
        input.close();
    }
}