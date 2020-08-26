package algorithm.sequence;

/**
 * Title: 快读排序
 * Description:
 * Company:
 * Project: JavaSE
 *
 * @Author: jianghaotian
 * Create Time: 2020-08-05 20:31
 */

import java.util.Arrays;

/**
 * 链接：https://www.pdai.tech/md/algorithm/alg-sort-x-fast.html
 * <p>
 * 从数列中挑出一个基准值。 将所有比基准值小的摆放在基准前面，所有比基准值大的摆在基准的后面(相同的数可以到任一边)；
 * 在这个分区退出之后，该基准就处于数列的中间位置。
 * 递归地把"基准值前面的子数列"和"基准值后面的子数列"进行排序。
 * <p>
 * 时间复杂度：O(nlogn)
 * 这句话很好理解: 假设被排序的数列中有N个数。遍历一次的时间复杂度是O(N)，需要遍历多少次呢?
 * 至少lg(N+1)次，最多N次。
 * 为什么最少是lg(N+1)次?
 * 快速排序是采用的分治法进行遍历的，我们将它看作一棵二叉树，它需要遍历的次数就是二叉树的深度，而根据完全二叉树的定义，它的深度至少是lg(N+1)。
 * 因此，快速排序的遍历次数最少是lg(N+1)次。
 * 为什么最多是N次?
 * 这个应该非常简单，还是将快速排序看作一棵二叉树，它的深度最大是N。因此，快读排序的遍历次数最多是N次。
 * 空间复杂度：O(1)
 */
public class QuickSort {
    public static void main(String[] args) {
        int[] array = new int[]{2, 4, 3, 1, 6, 5};
        System.out.println("排序前:" + Arrays.toString(array));

        //数组长度
        int length = array.length;

        //快速排序
        quickSort(array, 0, length - 1);

        System.out.println("排序后:" + Arrays.toString(array));
    }

    /**
     * 在第1趟中，设置x=a[i]，即x=30。
     * 从"右 --> 左"查找小于x的数: 找到满足条件的数a[j]=20，此时j=4；然后将a[j]赋值a[i]，此时i=0；接着从左往右遍历。
     * 从"左 --> 右"查找大于x的数: 找到满足条件的数a[i]=40，此时i=1；然后将a[i]赋值a[j]，此时j=4；接着从右往左遍历。
     * 从"右 --> 左"查找小于x的数: 找到满足条件的数a[j]=10，此时j=3；然后将a[j]赋值a[i]，此时i=1；接着从左往右遍历。
     * 从"左 --> 右"查找大于x的数: 找到满足条件的数a[i]=60，此时i=2；然后将a[i]赋值a[j]，此时j=3；接着从右往左遍历。
     * 从"右 --> 左"查找小于x的数: 没有找到满足条件的数。当i>=j时，停止查找；然后将x赋值给a[i]。
     * 此趟遍历结束！ 按照同样的方法，对子数列进行递归遍历。最后得到有序数组！
     */
    private static void quickSort(int[] array, int start, int end) {
        if (start >= end) {
            return;
        }

        int i = start;
        int j = end;
        int x = array[start];

        while (i < j) {
            while (i < j && array[j] > x) {
                j--;
            }
            if (i < j) {
                array[i] = array[j];
                i++;
            }
            while (i < j && array[i] < x) {
                i++;
            }
            if (i < j) {
                array[j] = array[i];
                j--;
            }
        }
        array[i] = x;
        quickSort(array, start, i - 1);
        quickSort(array, i + 1, end);
    }
}
