package homework;

import java.math.BigInteger;
import java.util.stream.IntStream;

/**
 * Title:
 * Description:
 * Copyright: 2019
 * Company: 滴普科技
 * Project: JavaSE
 *
 * @Author: jianghaotian
 * Create Time: 2019-11-27 18:08
 */
public class HuaYu2 {
    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        int number = 50000;
        BigInteger bigInteger = IntStream.range(1, number + 1)
                .mapToObj(BigInteger::valueOf)
                .parallel()
                .reduce(BigInteger::multiply)
                .get();
        long finish = System.currentTimeMillis();
        System.out.println(bigInteger.toString());
        System.out.println(finish - start);
    }
}
