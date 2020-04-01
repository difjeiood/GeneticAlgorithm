package 遗传算法测试.com.liguang.test;

import java.util.ArrayList;
import java.util.List;

/*
对于遗传算法，我们需要有对应的种群以及我们需要设置的一些常量：种群数量、基因长度、基因突变个数、基因突变率等
 */
public abstract class GeneticAlgorithm {
    private List<Chromosome> population = new ArrayList<Chromosome>();//种群
    private int popSize = 100;//种群数量
    private int geneSize;//基因最大长度
    private int maxIterNum = 500;//最大迭代次数
    private double mutationRate = 0.01;//基因变异的概率
    private int maxMutationNum = 3;//最大变异步长

    private int generation = 1;//当前遗传到第几代

    private double bestScore;//最好得分
    private double worstScore;//最坏得分
    private double totalScore;//总得分
    private double averageScore;//平均得分

    private double x; //记录历史种群中最好的X值
    private double y; //记录历史种群中最好的Y值
    private int geneI;//x y所在代数

    public GeneticAlgorithm(int geneSize) {
        this.geneSize = geneSize;
    }

    /**
     * 初始化种群
     */
    private void init() {
        population = new ArrayList<Chromosome>();
        for (int i = 0; i < popSize; i++){
            Chromosome chro = new Chromosome(geneSize);
            population.add(chro);
        }
        caculteScore();
    }

    /**
     * 计算种群的适用度
     */
    protected  void caculteScore(){
        setChromosomeScore(population.get(0));
        bestScore = population.get(0).getScore();
        worstScore = population.get(0).getScore();
        totalScore = 0;
        for (Chromosome chromosome : population) {
            setChromosomeScore(chromosome);
            if (chromosome.getScore() > bestScore) {
                //返回好基因值
                bestScore = chromosome.getScore();
                if (y < bestScore){
                    x = changeX(chromosome);
                    y = bestScore;
                    geneI = generation;
                }
            }
            if (chromosome.getScore() < worstScore) {
                //设置最坏基因
                worstScore = chromosome.getScore();
            }
            totalScore += chromosome.getScore();
        }

        averageScore = totalScore / popSize;
        //因为精度问题导致的平均值大于最好值，将平均值设置成最好值
        averageScore = averageScore > bestScore ? bestScore : averageScore;
    }

    /**
     * 根据基因计算对应的Y值
     * @param chromosome
     */
    protected  void setChromosomeScore(Chromosome chromosome){
        if (chromosome == null) return;
        double x = changeX(chromosome);
        double y = caculateY(x);
        chromosome.setScore(y);
    }

    /**
     * @param chro
     * @return
     * @Description: 将二进制转化为对应的X
     */
    public abstract double changeX(Chromosome chro);


    /**
     * @param x
     * @return
     * @Description: 根据X计算Y值 Y=F(X)
     */
    public abstract double caculateY(double x);

    /**
     * 转盘赌法选取可以产生下一代的个体,
     * 只有个人的适应度不小于平均适应度才会长生下一代
     * @return
     */
    private Chromosome getParentChromosome() {
        double Slice = Math.random() * totalScore;
        double sum = 0;
        for (Chromosome chromosome : population) {
            sum += chromosome.getScore();
            //转到对应的位置并且适应度不小于平均适应度
            if (sum > Slice && chromosome.getScore() >= averageScore){
                return chromosome;
            }
        }
        return null;
    }

    /**
     * 产生下一代
     */
    private void evolve() {
        List<Chromosome> childPopulation = new ArrayList<Chromosome>();
        //生成下一代种群
        while (childPopulation.size() < popSize) {
            Chromosome p1 = getParentChromosome();
            Chromosome p2 = getParentChromosome();
            List<Chromosome> children = Chromosome.genetic(p1, p2);
            if (children != null) {
                for (Chromosome chro : children) {
                    childPopulation.add(chro);
                }
            }
        }
        //新种群替换旧种群
        List<Chromosome> t = population;
        population = childPopulation;
        t.clear();
        t = null;
        //基因突变
        mutation();
        //计算新种群的适应度
        caculteScore();
    }

    /**
     * 基因突变
     */
    private void mutation() {
        for (Chromosome chro : population) {
            if (Math.random() < mutationRate) { //发生基因突变
                int mutationNum = (int) (Math.random() * maxMutationNum);
                chro.mutation(mutationNum);
            }
        }
    }

    /**
     * 迭代
     */
    public void caculte() {
        //初始化种群
        generation = 1;
        init();
        while (generation < maxIterNum){
            //遗传
            evolve();
            print();
            generation++;
        }
    }

    /**
     * 输出结果
     */
    private void print() {
        System.out.println("--------------------------------");
        System.out.println("the generation is:" + generation);
        System.out.println("the best y is:" + bestScore);
        System.out.println("the worst fitness is:" + worstScore);
        System.out.println("the average fitness is:" + averageScore);
        System.out.println("the total fitness is:" + totalScore);
        System.out.println("geneI:" + geneI + "\tx:" + x + "\ty:" + y);
    }






}
