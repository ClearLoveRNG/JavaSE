package homework;

import java.util.Scanner;

/**
 * Title:
 * Description:
 * Copyright: 2019
 * Company:
 * Project: JavaSE
 * Author: jianghaotian
 * Create Time: 2019/5/1 00:53
 */
public class HomeWork {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("请输入汇编语言(请用*号结束):");
        sc.useDelimiter("\\*");
        String[] input = sc.next().split("\n");
        System.out.println("结果如下:");
        for (String s : input) {
            System.out.println(convert(s));
        }
    }

    //根据不同的类型转换为不同的二进制
    private static String convert(String input) {
        System.out.print(input + "用二进制表示为:");
        //去掉$符号,并获取指令类型
        input = input.replace("$", "").replace(",", " ");
        String inputType = input.split(" ")[0];
        switch (inputType) {
            case "or":
                return converOR(input);
            case "and":
                return converAND(input);
            case "add":
                return converADD(input);
            case "sub":
                return converSUB(input);
            case "sllv":
                return converSLLV(input);
            case "srlv":
                return converSRLV(input);
            case "srav":
                return converSRAV(input);
            case "slt":
                return converSLT(input);
            case "ori":
                return converORI(input);
            case "andi":
                return converANDI(input);
            case "addi":
                return converADDI(input);
            case "lw":
                return converLW(input);
            case "sw":
                return converSW(input);
            case "beq":
                return converBEQ(input);
            case "bgt":
                return converBGT(input);
            case "jump":
                return converJUMP(input);
            default:
                return "";
        }
    }

    //or命令
    private static String converOR(String input) {
        int rd = Integer.parseInt(input.split(" ")[1]);
        int rs = Integer.parseInt(input.split(" ")[2]);
        int rt = Integer.parseInt(input.split(" ")[3]);
        return toBinaryString(0, 4) + toBinaryString(rs, 2) + toBinaryString(rt, 2) + toBinaryString(rd, 2) + toBinaryString(0, 3) + toBinaryString(0, 3);
    }

    //and命令
    private static String converAND(String input) {
        String result = converOR(input);
        result = result.substring(0, result.length() - 3);
        return result + toBinaryString(1, 3);
    }

    //add命令
    private static String converADD(String input) {
        String result = converOR(input);
        result = result.substring(0, result.length() - 3);
        return result + toBinaryString(2, 3);
    }

    //sub命令
    private static String converSUB(String input) {
        String result = converOR(input);
        result = result.substring(0, result.length() - 3);
        return result + toBinaryString(3, 3);
    }

    //sllv命令
    private static String converSLLV(String input) {
        String result = converOR(input);
        result = result.substring(0, result.length() - 3);
        return result + toBinaryString(4, 3);
    }

    //srlv命令
    private static String converSRLV(String input) {
        String result = converOR(input);
        result = result.substring(0, result.length() - 3);
        return result + toBinaryString(5, 3);
    }

    //srav命令
    private static String converSRAV(String input) {
        String result = converOR(input);
        result = result.substring(0, result.length() - 3);
        return result + toBinaryString(6, 3);
    }

    //slt命令
    private static String converSLT(String input) {
        String result = converOR(input);
        result = result.substring(0, result.length() - 3);
        return result + toBinaryString(7, 3);
    }

    //ori命令
    private static String converORI(String input) {
        int rt = Integer.parseInt(input.split(" ")[1]);
        int rs = Integer.parseInt(input.split(" ")[2]);
        int imm = Integer.parseInt(input.split(" ")[3]);
        return toBinaryString(1, 4) + toBinaryString(rs, 2) + toBinaryString(rt, 2) + toBinaryString(imm, 8);
    }

    //andi命令
    private static String converANDI(String input) {
        String result = converORI(input);
        result = result.substring(0, 4);
        return toBinaryString(2, 4) + result;
    }

    //addi命令
    private static String converADDI(String input) {
        String result = converORI(input);
        result = result.substring(0, 4);
        return toBinaryString(3, 4) + result;
    }

    //lw命令
    private static String converLW(String input) {
        String result = converORI(input);
        result = result.substring(0, 4);
        return toBinaryString(4, 4) + result;
    }

    //sw命令
    private static String converSW(String input) {
        String result = converORI(input);
        result = result.substring(0, 4);
        return toBinaryString(5, 4) + result;
    }

    //beq命令
    private static String converBEQ(String input) {
        int rs = Integer.parseInt(input.split(" ")[1]);
        int rt = Integer.parseInt(input.split(" ")[2]);
        int offset = Integer.parseInt(input.split(" ")[3]);
        return toBinaryString(6, 4) + toBinaryString(rs, 2) + toBinaryString(rt, 2) + toBinaryString(offset, 8);
    }

    //bgt命令
    private static String converBGT(String input) {
        String result = converBEQ(input);
        result = result.substring(0, 4);
        return toBinaryString(7, 4) + result;
    }

    //jump命令
    private static String converJUMP(String input) {
        int address = Integer.parseInt(input.split(" ")[1]);
        return toBinaryString(8, 4) + toBinaryString(address, 12);
    }

    /**
     * 十进制转化为二进制数
     *
     * @param source 十进制数
     * @param digits 位数
     * @return 二进制
     */
    private static String toBinaryString(int source, int digits) {
        //获取十进制的二进制表示
        String result = Integer.toBinaryString(source);
        //需要的补0字符串
        String zero = "";
        //如果表示后的位数比要求的位数小，则用0补齐
        if (result.length() < digits) {
            int subResult = digits - result.length();
            for (int i = 0; i < subResult; i++) {
                zero += "0";
            }
            result = zero + result;
        }
        return result;
    }
}
