package algorithm.sequence;

/**
 * Title: 冒泡排序
 * Description:
 * Company:
 * Project: JavaSE
 *
 * @Author: jianghaotian
 * Create Time: 2020-08-05 19:53
 */

import java.util.Arrays;

/**
 * 链接：https://www.pdai.tech/md/algorithm/alg-sort-x-bubble.html
 * <p>
 * 它是一种较简单的排序算法。它会遍历若干次要排序的数列，每次遍历时，它都会从前往后依次的比较相邻两个数的大小；
 * 如果前者比后者大，则交换它们的位置。这样，一次遍历之后，最大的元素就在数列的末尾！
 * 采用相同的方法再次遍历时，第二大的元素就被排列在最大元素之前。重复此操作，直到整个数列都有序为止！
 * <p>
 * <p>
 * 时间复杂度：O(n²)：冒泡排序的时间复杂度是O(N2)。 假设被排序的数列中有N个数。遍历一趟的时间复杂度是O(N)，需要遍历多少次呢? N-1次！因此，冒泡排序的时间复杂度是O(N2)。
 * 空间复杂度：O(1)：没有用到辅助存储
 */
public class BubbleSort {
    public static void main(String[] args) {
        int[] array = new int[]{2, 4, 3, 1, 6, 5};
        System.out.println("排序前:" + Arrays.toString(array));

        //数组长度
        int length = array.length;


        /**
         * 我们先分析第1趟排序 当i=5,j=0时，a[0]<a[1]。此时，不做任何处理！
         * 当i=5,j=1时，a[1]>a[2]。此时，交换a[1]和a[2]的值；交换之后，a[1]=30，a[2]=40。
         * 当i=5,j=2时，a[2]>a[3]。此时，交换a[2]和a[3]的值；交换之后，a[2]=10，a[3]=40。
         * 当i=5,j=3时，a[3]<a[4]。此时，不做任何处理！
         * 当i=5,j=4时，a[4]>a[5]。此时，交换a[4]和a[5]的值；交换之后，a[4]=50，a[3]=60。
         * 于是，第1趟排序完之后，数列{20,40,30,10,60,50}变成了{20,30,10,40,50,60}。
         * 此时，数列末尾的值最大。
         *
         * 根据这种方法:
         * 第2趟排序完之后，数列中a[5...6]是有序的。
         * 第3趟排序完之后，数列中a[4...6]是有序的。
         * 第4趟排序完之后，数列中a[3...6]是有序的。
         * 第5趟排序完之后，数列中a[1...6]是有序的。
         * 整个数列也就是有序的了。
         */

        //如果在某一次循环中，一次交换操作都没有，那证明数据已经排好序了，就不用在做无用功了，直接跳出，设置一个标志位标记本次循环是否一次交换都没有
        boolean flag = false;
        //这里要循环n-1此
        for (int i = length - 1; i > 0; i--) {
            for (int j = 0; j < i; j++) {
                if (array[j] > array[j + 1]) {
                    int temp = array[j];
                    array[j] = array[j + 1];
                    array[j + 1] = temp;
                    flag = true;
                }
            }
            if (!flag) {
                break;
            }
        }


        System.out.println("排序后:" + Arrays.toString(array));
    }
}
