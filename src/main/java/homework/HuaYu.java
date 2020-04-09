package homework;


/**
 * Title: 计算50000的阶乘
 * Description: 作业内容：计算50000的阶乘（50000!）并把各位数的和相加
 * Copyright: 2019
 * Company: 华宇
 * Project: JavaSE
 *
 * @Author: jianghaotian
 * Create Time: 2019-11-27 16:54
 */
public class HuaYu {

    public static void main(String[] args) {
        int j=0;
        for(int i=0;i<1000;i++) {
            j=++j;
        }
        System.out.println(j);
//        doBigFactorial(50000);
    }

    private static void doBigFactorial(int bigInteger) {
        long timeBegin = System.currentTimeMillis();
        bigFactorial(bigInteger);
        long timeFinish = System.currentTimeMillis();
        System.out.println("计算耗时: " + (timeFinish - timeBegin) + "毫秒");
    }

    /**
     * 大整数阶乘
     *
     * @param bigInteger 所计算的大整数
     */
    private static void bigFactorial(int bigInteger) {
        //阶乘位数
        int digit = getDigit(bigInteger);

        //初始化一个数组,长度是刚算出来的digit
        int[] array = new int[digit];
        //设个位为1,方便计算
        array[0] = 1;

        //用来记录最后一个需要相乘的数的下标(有效下标)
        int lastIndex = 0;
        //循环2至50000，依次与数组中表示的数相乘
        for (int i = 2; i <= bigInteger; i++) {

            //因为数组是从左往右记录数字，所以偏大的数组下标中的元素处于0的时间比较长，而0乘以任何数都是0
            //没有必要运算，且浪费性能，所以找到最后一个有效下标
            //即从右往左起，第一个不是0的数的下标
            for (int j = digit - 1; j >= 0; j--) {
                if (array[j] != 0) {
                    lastIndex = j;
                    break;
                }
            }

            //从数组的下标0开始到最后一个需要相乘的数的下标结束，依次乘以i
            for (int k = 0; k <= lastIndex; k++) {
                array[k] *= i;
            }

            //乘完后数组中的元素绝大部分都不止1位数，所以要进行拆分(进位)
            //满足每一个元素都是1位数
            calculateCarry(array, lastIndex);
        }

        //退出上一个循环，代表50000的阶乘已经完成，但是最后一个有效下标是进位之前得到的
        //所以为了保持一致，要再循环一次，更新成进位之后的下标
        for (int x = digit - 1; x >= 0; x--) {
            if (array[x] != 0) {
                lastIndex = x;
                break;
            }
        }
        //打印结果
        printResult(bigInteger, lastIndex, array);
    }

    /**
     * 计算阶乘的位数
     * @param bigInteger
     * @return
     */
    private static int getDigit(int bigInteger) {
        //计算50000的阶乘位数
        //x = 10^n + y
        //x为正整数 y为余数 则n+1是x的位数
        //所以要使用log10依次求出1-50000的位数，再加起来，向下取整，再+1(要算上最高位这一位)，就是50000!的位数
        double sum = 0;
        for (int i = 1; i <= bigInteger; i++) {
            sum += Math.log10(i);
        }
        return (int) sum + 1;
    }

    /**
     * 计算进位
     *
     * @param array 数组
     * @param lastIndex 最后一个有效下标
     */
    private static void calculateCarry(int[] array, int lastIndex) {
        //从0-lastIndex每一位需要进位的数，累加起来
        int carry = 0;
        //从0到lastIndex逐位检查是否需要进位
        for (int i = 0; i <= lastIndex; i++) {
            //累加进位
            array[i] = array[i] + carry;
            //小于9不进位
            if (array[i] <= 9) {
                carry = 0;
            } else if (array[i] > 9 && i < lastIndex) {
                //大于9但不是最后一个有效下标

                //除10的商准备进位给下一个元素
                carry = array[i] / 10;
                //除10的余数赋给当前元素
                array[i] = array[i] % 10;
            } else if (array[i] > 9 && i >= lastIndex) {
                //大于9且是最后一个有效下标

                //从lastIndex开始往后进位，直到每一个元素都是1位数
                while (array[i] > 9) {
                    //除10的商准备进位给下一个元素
                    carry = array[i] / 10;
                    //除10的余数赋给当前元素
                    array[i] = array[i] % 10;
                    //继续进位
                    i++;
                    //把累加的进位数给下一个元素，如果不是1位数(array[i] > 9)，则继续循环
                    array[i] = carry;
                }
            }

        }
    }

    /**
     * 打印结果
     *
     * @param bigInteger
     * @param lastIndex
     * @param array
     */
    private static void printResult(int bigInteger, int lastIndex, int[] array) {
        //统计输出位数
        int m = 0;
        //统计输出行数
        int n = 0;
        //每个位数累加
        int sum = 0;
        System.out.println(bigInteger + "阶乘结果为：");
        //输出计算结果,倒序输出
        for (int a = lastIndex; a >= 0; a--) {
            System.out.print(array[a]);
            sum += array[a];
            m++;
            if (m % 5 == 0) {
                System.out.print(" ");
            }
            if (40 == m) {
                System.out.println();
                m = 0;
                n++;
                if (10 == n) {
                    System.out.print("\n");
                    n = 0;
                }
            }
        }
        System.out.println("\n");
        System.out.println("阶乘各位数相加: " + sum);
        System.out.println("阶乘共有: " + (lastIndex + 1) + "位");
    }
}
