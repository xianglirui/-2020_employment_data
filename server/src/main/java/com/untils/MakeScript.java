package com.untils;

import com.DemoApplication;
import com.pojo.Session;
import com.pojo.Timing;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

@Configuration      //1.主要用于标记配置类，兼备Component的效果。
@EnableScheduling   // 2.开启定时任务
public class MakeScript {
    @Autowired
    MongoTemplate mongoTemplate;

    /**
     * 调用爬虫爬取数据，该方法一旦调用就会往数据库中存入一个标志，如果在爬数据期间，前段再次传入同条件的请求则不会响应，避免反复执行
     * 爬虫执行完后，删除数据库中的标志，并调用提取关键词的方法
     * @param city
     * @param key
     * @param timing
     */
    @Async
    public void PythonReptile(String city,String key,Boolean timing){

        time_interval();

        Session session = new Session();
        session.setCity(city);
        session.setKey(key);
        mongoTemplate.save(session,"session");
        try {
//            String programmerPath="C:\\Users\\xianglirui\\Desktop\\Travel\\reptile\\拉勾网.py";
            String programmerPath=readFileByChars().get(1);
//            String programmerPath="/usr/local/2020employment/re.py";
//            String resolverPath="C:\\Users\\xianglirui\\AppData\\Local\\Programs\\Python\\Python38\\python.exe";
            String resolverPath="python";
            String[] args = new String[]{resolverPath, programmerPath,city,key,0+""};
            if(timing){
                args[4] = 1+"";
            }
            Process pr = Runtime.getRuntime().exec(args);

            BufferedReader in = new BufferedReader(new InputStreamReader(pr.getInputStream(), "GBK"));
            String line;
            while ((line = in.readLine())!=null){
                System.out.println(line);
                //如果爬虫抛出异常：例如遇到了登录之类的反爬技术，就将数据库中的标志删除
                if(line.equals("fail")){
                    mongoTemplate.remove(Query.query(Criteria.where("city").is(city).and("key").is(key)),Session.class);
                }
            }
            in.close();
            pr.waitFor();
            mongoTemplate.remove(Query.query(Criteria.where("city").is(city).and("key").is(key)),Session.class);
            PythonKeyExtract(city,key,false);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }


    public void PythonKeyExtract(String city,String key,Boolean update){
        try {
//            String programmerPath="C:\\Users\\xianglirui\\Desktop\\Travel\\reptile\\十大热门技术.py";
            String programmerPath=readFileByChars().get(3);
//            String programmerPath="/usr/local/2020employment/tech.py";
            String resolverPath="python";
            String keys = key.equals("") ?"java":key;
            String[] args = new String[]{resolverPath, programmerPath,city,keys,"false"};
            if(update){
                args[4] = "true";
            }

            Process pr = Runtime.getRuntime().exec(args);

            BufferedReader in = new BufferedReader(new InputStreamReader(pr.getInputStream(), "GBK"));
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

    /**
     * 获取当前小时，判断时间段存入数据库，调用爬虫脚本时，判断是不是在8-12,14-18区间内
     * 如果在时间区间，查看数据库对应的am，pm是否为true，为true表示上下午已经执行一次，就不能调用爬虫
     */
    public void time_interval(){
        int hour = time();
        String day = day()+"";
        System.out.println(hour);

        if(hour>=14&&hour<=18){
            mongoTemplate.updateFirst(Query.query(Criteria.where("date").is(day)), Update.update("pm",true),Timing.class);
        }
        if(hour>=8&&hour<=12){
            mongoTemplate.updateFirst(Query.query(Criteria.where("date").is(day)), Update.update("am",true),Timing.class);
        }

    }

    /**
     * 零点存入删除上一天的数据，新增今天数据
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void remove_time_flag(){
        mongoTemplate.remove(Timing.class);
        Timing timing = new Timing();
        timing.setDate(day()+"");
        timing.setAm(false);
        timing.setPm(false);
        mongoTemplate.save(timing,"timing");
    }

    public int time(){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        String data = formatter.format(calendar.getTime());

        return Integer.parseInt(data.split(":")[0]);
    }

    public int day(){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("MM-dd");
        String data = formatter.format(calendar.getTime());
//        System.out.println(Integer.parseInt(data.split("-")[1]));
        return Integer.parseInt(data.split("-")[1]);
    }



    public ArrayList<String> readFileByChars() {
        ArrayList<String> list = new ArrayList<>();
        ClassPathResource classPathResource = new ClassPathResource("file.txt");
        try {
            InputStream reader = classPathResource.getInputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(reader, StandardCharsets.UTF_8));
            String line;
            while ((line = in.readLine())!=null){
                list.add(line);
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
