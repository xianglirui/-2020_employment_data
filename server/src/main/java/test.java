import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class test {
    @Test
    public void textPython(Boolean update){
        try {
            String city="武汉";
            String key="java";
            String programmerPath="C:\\Users\\xianglirui\\Desktop\\Travel\\reptile\\text.py";
            String resolverPath="C:\\Users\\xianglirui\\AppData\\Local\\Programs\\Python\\Python38\\python.exe";

            String[] args = new String[]{resolverPath, programmerPath,city,key};
            Process pr = Runtime.getRuntime().exec(args);

            BufferedReader in = new BufferedReader(new InputStreamReader(pr.getInputStream()));
            String line;
            while ((line = in.readLine())!=null){
                System.out.println(line);
            }
            in.close();
            pr.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testOther(){
        String s = "8k-12k ";
        //去除字符串的"-"
        String[] str = s.split("-");
        //将字符串数组转换为一个Stream，用于调用map函数
        Stream<String> stream = Arrays.stream(str);
        //将字符串数组 ["8k","12k"]中的k去掉
        Stream<Integer> stream1 = stream.map(v -> Integer.parseInt(v.substring(0,v.lastIndexOf('k'))));
        //将stream输出为List
        List<Integer> list = stream1.collect(Collectors.toList());
        System.out.println(list.get(0));

    }

    @Test
    public void testTwo(){
        String str = "C++开发";
        String regEx="[^\u4e00-\u9fa5]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str.toLowerCase());
        StringBuilder sb = new StringBuilder();
        while(m.find()){
            sb.append(m.group());
        }
        System.out.println(sb.toString());
    }
}
