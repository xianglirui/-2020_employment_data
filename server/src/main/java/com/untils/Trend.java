package com.untils;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@Configuration      //1.主要用于标记配置类，兼备Component的效果。
@EnableScheduling   // 2.开启定时任务
public class Trend {
    @Autowired
    MongoTemplate mongoTemplate;


    public ArrayList<String> getDate(){
        ArrayList<String> list = new ArrayList<>();
        for(int i=0;i<5;i++){
            Calendar calendar1 = Calendar.getInstance();
            calendar1.set(Calendar.DAY_OF_YEAR, calendar1.get(Calendar.DAY_OF_YEAR) - i);
            Date today = calendar1.getTime();
            SimpleDateFormat format = new SimpleDateFormat("MM-dd");
            String result = format.format(today);
            list.add(result);
        }
        Collections.reverse(list);

        return list;
    }

    public void trendNum(String key){
        com.pojo.Trend trend = new com.pojo.Trend();

        ArrayList<Integer> list1 = new ArrayList<>();
        ArrayList<Integer> list2 = new ArrayList<>();
        ArrayList<Integer> list3 = new ArrayList<>();


        for(int i=0;i<5;i++){
            int r = new Random().nextInt(600) + 600;
            list1.add(r);
        }

        for(int i=0;i<5;i++){
            int r = new Random().nextInt(550) + 480;
            list2.add(r);
        }

        for(int i=0;i<5;i++){
            int r = new Random().nextInt(660) + 475;
            list3.add(r);
        }

        trend.set_360(list1);
        trend.setBaidu(list2);
        trend.setSougou(list3);
        trend.setKey(key);

        mongoTemplate.save(trend,"trend");
    }

    /**
     * 每天0点执行一次，将第一个元素删除，在集合最后添加一个元素
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void add(){
        List<com.pojo.Trend> trendList = mongoTemplate.findAll(com.pojo.Trend.class);
        for(com.pojo.Trend trend :trendList){
            trend.get_360().remove(0);
            trend.get_360().add(new Random().nextInt(600) + 600);

            trend.getBaidu().remove(0);
            trend.getBaidu().add(new Random().nextInt(550) + 480);

            trend.getSougou().remove(0);
            trend.getSougou().add(new Random().nextInt(660) + 475);

            mongoTemplate.updateFirst(Query.query(Criteria.where("key").is(trend.getKey())),
                    Update.update("baidu",trend.getBaidu()).set("sougou",trend.getSougou()).set("_360",trend.get_360()), com.pojo.Trend.class);
        }

    }



}
