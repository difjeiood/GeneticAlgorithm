package 遗传算法测试.com.liguang.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
种群个体（染色体）
 */
public class Chromosome {
    private boolean[] gene; //基因序列
    private double score; //对应的函数得分

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public Chromosome() {
    }

    /*
        随机生成基因序列
         */
    public Chromosome(int size){
        if (size <= 0){
            return;
        }
        initGeneSize(size);
        for (int i = 0; i < size ; i++) {
            gene[i] = Math.random() >= 0.5;
        }
    }

    private void initGeneSize(int size) {
        if (size <= 0) { return; }
        gene = new boolean[size];
    }

    /**
     * 把基因转化为对应的值（位运算）
     * @return
     */
    public int getNum() {
        if (gene == null) return 0;

        int num = 0;
        for (boolean bool : gene) {
            num <<= 1;
            if (bool) {
                num += 1;
            }
        }
        return num;
    }


    /**
     * 基因变异，变异位置随机，变异原则1变0，0变1
     * @param num
     */
    public void mutation(int num) {
        int size = gene.length;
        for (int i = 0; i < num ; i++) {
            //寻找变异位置
            int at = ((int) (Math.random() * size)) % size;
            //变异后的值
            boolean bool = !gene[at];
            gene[at] = bool;
        }
    }

    /**
     * 克隆基因，用于产生下一代，将已存在的基因copy一份
     * @param c
     * @return
     */
    public static Chromosome clone(final Chromosome c){
        if (c == null || c.gene == null) return null;

        Chromosome copy = new Chromosome();
        copy.initGeneSize(c.gene.length);
        for (int i = 0; i < c.gene.length ; i++) {
            copy.gene[i] = c.gene[i];
        }
        return copy;
    }


    /**
     * 父母双方产生下一代，哪段基因产生交叉，完全随机
     * @param p1
     * @param p2
     * @return
     */
    public static List<Chromosome> genetic(Chromosome p1, Chromosome p2) {
        if (p1 == null || p2 == null) return null; //染色体有一个为空，不产生下一代
        if (p1.gene == null || p2.gene == null ) return null; //染色体有一个没有基因序列，不产生下一代
        if (p1.gene.length != p2.gene.length) return null; //染色体基因序列长度不同，不产生下一代

        Chromosome c1 = clone(p1);
        Chromosome c2 = clone(p2);

        //随机产生交叉互换位置
        int size = c1.gene.length;
        int a = ((int) (Math.random() * size)) % size;
        int b = ((int) (Math.random() * size)) % size;
        int min = a > b ? b : a;
        int max = a > b ? a : b;
        //对应位置上的基因进行交叉互换
        for (int i = min; i < max ; i++) {
            boolean temp = c1.gene[i];
            c1.gene[i] = c2.gene[i];
            c2.gene[i] = temp;
        }

        List<Chromosome> list = new ArrayList<Chromosome>();
        list.add(c1);
        list.add(c2);

        return list;
    }



    @Override
    public String toString() {
        return "Chromosome{" +
                "gene=" + Arrays.toString(gene) +
                ", score=" + score +
                '}';
    }

    public static void main(String[] args) {
        Chromosome chromosome = new Chromosome(5);
        System.out.println(chromosome.toString());
        System.out.println(chromosome.getNum());
    }
}
