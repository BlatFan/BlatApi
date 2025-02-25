package ru.blatfan.blatapi.utils.random;



import net.minecraft.util.RandomSource;
import ru.blatfan.blatapi.utils.MathUtils;

import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

public class RandomUtils {

    public static Random RANDOM = new Random();


    public static boolean doWithChance(float chance) {
        return doWithChance(RANDOM,chance);
    }

    public static double random() {
        return Math.random();
    }


    public static int randomSign(){
        return randomElement(-1,1);
    }

    public static int randomInt(int min,int from,int to){
        int res = RandomUtils.randomInt(from,to);
        return (int) MathUtils.clamp(res,-min,min);
    }

    public static float randomFloat(float min,float from,float to){
        float res = RandomUtils.randomFloat(from,to);
        return (float) MathUtils.clamp(res,-min,min);
    }

    public static int randomInt(int border) {
        return randomInt(RANDOM,border);
    }

    public static int randomInt(int min, int max) {
        return randomInt(RANDOM,min,max);
    }

    public static float randomFloat(float min, float max) {
        return randomFloat(RANDOM,min,max);
    }


    public static boolean doWithChance(Random random,float chance) {
        double num = random.nextDouble() * 100;
        if (num <= chance) {
            return true;
        }
        return false;
    }

    public static int randomInt(Random random,int border) {
        return random.nextInt(border);
    }

    public static int randomInt(Random random,int min, int max) {
        return random.nextInt(max - min) + min;
    }

    public static float randomFloat(Random random,float min, float max) {
        return min + random.nextFloat() * (max - min);
    }

    public static boolean doWithChance(RandomSource random, float chance) {
        double num = random.nextDouble() * 100;
        if (num <= chance) {
            return true;
        }
        return false;
    }

    public static long randomLong(){
        return RANDOM.nextLong();
    }

    public static int randomInt(RandomSource random,int border) {
        return random.nextInt(border);
    }

    public static int randomInt(RandomSource random,int min, int max) {
        return random.nextInt(max - min) + min;
    }

    public static float randomFloat(RandomSource random,float min, float max) {
        return min + random.nextFloat() * (max - min);
    }


    public static <T> T randomElement(RandomSource random,T... array){
        return array[RandomUtils.randomInt(random,array.length)];
    }

    public static <T> T randomElement(RandomSource random, List<T> list){
        return list.get(RandomUtils.randomInt(random,list.size()));
    }

    public static <T> T randomElement(RandomSource random, Stream<T> stream){
        return randomElement(random,stream.toList());
    }


    public static <T> T randomElement(Random random,T... array){
        return array[RandomUtils.randomInt(random,array.length)];
    }

    public static <T> T randomElement(Random random, List<T> list){
        return list.get(RandomUtils.randomInt(random,list.size()));
    }

    public static <T> T randomElement(Random random, Stream<T> stream){
        return randomElement(random,stream.toList());
    }

    public static <T> T randomElement(T... array){
        return array[RandomUtils.randomInt(RANDOM,array.length)];
    }

    public static <T> T randomElement(List<T> list){
        return list.get(RandomUtils.randomInt(RANDOM,list.size()));
    }

    public static <T> T randomElement(Stream<T> stream){
        return randomElement(RANDOM,stream.toList());
    }
}

