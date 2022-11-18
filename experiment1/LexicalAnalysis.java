package experiment1;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.ArrayList;

/**
 * @author 李天翔
 * @date 2022/05/11
 **/
@SuppressWarnings({"all"})
public class LexicalAnalysis {
    private String[] k = {"do", "end", "for", "if", "printf", "scanf", "then", "while"};//关键字表
    private String[] s = {",", ";", "(", ")", "[", "]"};//分界符表
    private String[] a = {"+", "-", "*", "/"};//算数运算符表
    private String[] r = {"<", "<=", "=", ">", ">=", "<>"};//关系运算符表
    private ArrayList<WordInfo> wordInfos = new ArrayList<>();//保存处理好的单词信息
    private ArrayList<String> id = new ArrayList<>();//保存标识符表
    private ArrayList<String> ci = new ArrayList<>();//保存常数表
    //将符号表存到list中便于查找
    ArrayList<String> arrayListS = new ArrayList<>(Arrays.asList(s));
    ArrayList<String> arrayListA = new ArrayList<>(Arrays.asList(a));
    ArrayList<String> arrayListR = new ArrayList<>(Arrays.asList(r));
    ArrayList<String> arrayListK = new ArrayList<>(Arrays.asList(k));
    private char c;//保存读入的当前字符
    private String[] inString;//缓存一行的字符数组，以空格分割得到
    private int totalRows = 0;
    private int row = 1;//保存现在处理到第几行了
    private int index = 1;//记录读到哪个字符了
    private int inStringPointer = 1;//记录读到哪个String
    private int col = 1;//记录列
    private HashMap<Integer, String> hm = new HashMap<>();//保存行 对应的字符串

    public void startAnalysis() {
        getInputFile();
        while (getChar()) {
            if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')) {//判断字母
                String buffer = inString[inStringPointer - 1];
                StringBuilder sb = new StringBuilder();
                int j;
                for (j = index - 1; j < buffer.length(); j++) {
                    if ((buffer.charAt(j) >= 'a' && buffer.charAt(j) <= 'z') || (buffer.charAt(j) >= 'A' && buffer.charAt(j) <= 'Z') || (buffer.charAt(j) >= '0' && buffer.charAt(j) <= '9')) {
                        sb.append(buffer.charAt(j));
                    } else {
                        index = j + 1;
                        break;
                    }
                }
                if (j == buffer.length()) {
                    inStringPointer++;
                    index = 1;
                }
                int i = 0;
                String dif = sb.toString();
                if (arrayListK.contains(dif)) {
                    wordInfos.add(new WordInfo(row, col, dif, "关键字", 1));
                    col++;
                    continue;
                }
                if (!id.contains(dif)) {
                    id.add(dif);
                }
                wordInfos.add(new WordInfo(row, col, dif, "标识符", 6));
                col++;
                continue;
            } else if (c >= '0' && c <= '9') {//判断数字
                String buffer = inString[inStringPointer - 1];
                StringBuilder sb = new StringBuilder();
                int j;
                boolean isNum = true;
                for (j = index - 1; j < buffer.length(); j++) {
                    if ((buffer.charAt(j) >= '0' && buffer.charAt(j) <= '9')) {
                        sb.append(buffer.charAt(j));
                    } else if ((buffer.charAt(j) >= 'a' && buffer.charAt(j) <= 'z') || (buffer.charAt(j) >= 'A' && buffer.charAt(j) <= 'Z')) {
                        sb.append(buffer.charAt(j));
                        isNum = false;
                    } else {
                        index = j + 1;
                        break;
                    }
                }
                if (j == buffer.length()) {
                    inStringPointer++;
                    index = 1;
                }
                if (isNum) {
                    int i;
                    String dig = sb.toString();
                    if (!ci.contains(dig)) {
                        ci.add(dig);
                    }
                    wordInfos.add(new WordInfo(row, col, dig, "常数", 5));
                    col++;
                    continue;
                } else {
                    wordInfos.add(new WordInfo(row, col, sb.toString(), "Error"));
                    col++;
                    continue;
                }
            } else {//其他
                String buffer = inString[inStringPointer - 1];
                StringBuilder sb = new StringBuilder();
                if (arrayListS.contains(c + "")) {//分界符
                    wordInfos.add(new WordInfo(row, col, c + "", "分界符", 6));
                    index++;
                    col++;
                    continue;
                } else if (arrayListA.contains(c + "")) {//算数运算符
                    int j;
                    for (j = index - 1; j < buffer.length(); j++) {
                        if (arrayListA.contains(buffer.charAt(j) + "")) {
                            sb.append(buffer.charAt(j));
                        } else {
                            index = j + 1;
                            break;
                        }
                    }
                    if (j == buffer.length()) {
                        inStringPointer++;
                        index = 1;
                    }
                    if (arrayListA.contains(sb.toString())) {
                        wordInfos.add(new WordInfo(row, col, sb.toString(), "算术运算符", 3));
                    } else {
                        wordInfos.add(new WordInfo(row, col, sb.toString(), "Error"));
                    }
                    col++;
                    continue;
                } else if (arrayListR.contains(c + "")) {//关系运算符
                    int j;
                    for (j = index - 1; j < buffer.length(); j++) {
                        if (arrayListR.contains(buffer.charAt(j) + "")) {
                            sb.append(buffer.charAt(j));
                        } else {
                            index = j + 1;
                            break;
                        }
                    }
                    if (j == buffer.length()) {
                        inStringPointer++;
                        index = 1;
                    }
                    if (arrayListR.contains(sb.toString())) {
                        wordInfos.add(new WordInfo(row, col, sb.toString(), "关系运算符", 4));
                    } else {
                        wordInfos.add(new WordInfo(row, col, sb.toString(), "Error"));
                    }
                    col++;
                    continue;
                } else {//出错
                    wordInfos.add(new WordInfo(row, col, c + "", "Error"));
                    index++;
                    col++;
                    continue;
                }
            }
        }
        printWordInfos();
    }
    /*
    打印单词信息
     */
    public void printWordInfos() {
        System.out.println("单词" + "\t\t\t\t二元序列" + "\t\t\t\t类型" + "\t\t\t\t位置（行，列）");
        for (int i = 0; i < wordInfos.size(); i++) {
            System.out.println(wordInfos.get(i).toString());
        }
    }
    /*
    将程序全部读入到hashmap里，同时以行作为key
     */
    public void getInputFile() {
        try {
            BufferedReader br = new BufferedReader(new FileReader("src\\1.txt"));
            String content;
            while ((content = br.readLine()) != null) {
                totalRows++;
                hm.put(totalRows, content);
            }
            br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /*
    得到一个输入字符
     */
    public boolean getChar() {
        inString = hm.get(row).split(" ");
        while (true) {
            if (inStringPointer > inString.length) {
                row++;
                if (row <= totalRows) {
                    inString = hm.get(row).split(" ");
                    col = 1;
                    inStringPointer = 1;
                    index = 1;
                } else {
                    return false;
                }
            }
            if (inString[inStringPointer - 1].length() < index) {
                inStringPointer++;
                index = 1;
            } else {
                c = inString[inStringPointer - 1].charAt(index - 1);
                break;
            }
        }
        return true;
    }
}
