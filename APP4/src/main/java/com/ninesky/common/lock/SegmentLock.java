package com.ninesky.common.lock;

import java.util.HashMap;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 分段锁，系统提供一定数量的原始锁，根据传入对象的哈希值获取对应的锁并加锁
 * 注意：要锁的对象的哈希值如果发生改变，有可能导致锁无法成功释放!!!
 */
public class SegmentLock<T> {
    private Integer segments = 16;//默认分段数量
    private final HashMap<Integer, ReentrantLock> lockMap = new HashMap<>();

    public SegmentLock() {
        init(null, false);
    }

    public SegmentLock(Integer counts, boolean fair) {
        init(counts, fair);
    }

    private void init(Integer counts, boolean fair) {
        if (counts != null) {
            segments = counts;
        }
        for (int i = 0; i < segments; i++) {
            lockMap.put(i, new ReentrantLock(fair));
        }
    }

    public void lock(T key) {
        ReentrantLock lock = lockMap.get((key.hashCode()>>>1) % segments);
        lock.lock();
    }

    public void unlock(T key) {
        ReentrantLock lock = lockMap.get((key.hashCode()>>>1) % segments);
        lock.unlock();
    }

    public static void Test(){
        System.out.println("=============算术右移 >> ===========");
        int i=0xC0000000;
        System.out.println("移位前：i= "+i+" = "+Integer.toBinaryString(i)+"(B)");
        i=i>>28;
        System.out.println("移位后：i= "+i+" = "+Integer.toBinaryString(i)+"(B)");

        System.out.println("---------------------------------");

        int j=0x0C000000;
        System.out.println("移位前：j= "+j+" = "+Integer.toBinaryString(j)+"(B)");
        j=j>>24;
        System.out.println("移位后：j= "+j+" = "+Integer.toBinaryString(j)+"(B)");

        System.out.println("\n");
        System.out.println("==============逻辑右移 >>> =============");
        int m=0xC0000000;
        System.out.println("移位前：m= "+m+" = "+Integer.toBinaryString(m)+"(B)");
        m=m >>> 28;
        System.out.println("移位后：m= "+m+" = "+Integer.toBinaryString(m)+"(B)");

        System.out.println("---------------------------------");

        int n=0x0C000000;
        System.out.println("移位前：n= "+n+" = "+Integer.toBinaryString(n)+"(B)");
        n=n>>24;
        System.out.println("移位后：n= "+n+" = "+Integer.toBinaryString(n)+"(B)");

        System.out.println("\n");
        System.out.println("==============移位符号的取模===============");
        int a=0xCC000000;
        System.out.println("移位前：a= "+a+" = "+Integer.toBinaryString(a)+"(B)");
        System.out.println("算术右移32：a="+(a>>32)+" = "+Integer.toBinaryString(a>>32)+"(B)");
        System.out.println("逻辑右移32：a="+(a>>>32)+" = "+Integer.toBinaryString(a>>>32)+"(B)");
    }

    public static void main(String[] args){
        Test();
    }
}
