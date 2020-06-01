package com.controller;

import com.annotation.PassToken;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pojo.Employment;
import com.pojo.Session;
import com.pojo.Technology;
import com.pojo.Timing;
import com.untils.DealSection;
import com.untils.MakeScript;
import com.untils.Trend;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
public class HomeController {
    @Autowired
    MongoTemplate mongoTemplate;
    @Autowired
    DealSection dealSection;
    @Autowired
    MakeScript makeScript;
    @Autowired
    Trend trend;

    /**
     * 条件筛选请求
     * @param choice
     * @return
     * @throws JsonProcessingException
     */
    @PostMapping("/totalData")
    public String totalData(@RequestBody ArrayList<Object> choice,@RequestHeader int city) throws JsonProcessingException {
        System.out.println("总查询"+choice);
        HashMap<String,Object> hs = new HashMap<>();
        Object key = choice.get(4)==""?"java": dealSection.drawEn((String) choice.get(4));
        String city_name = dealSection.city_indexTo_name(city);
        System.out.println(city_name);

//        //判断该条件（关键词、城市）是不是正在爬取
//        Session session = mongoTemplate.findOne(Query.query(Criteria.where("city").is(city_name).and("key").is(key)),Session.class);
//        //没有爬
//        if(session==null){
//            if(mongoTemplate.findOne(Query.query(Criteria.where("city").is(city_name).and("key").is(key)),Employment.class)==null){
//                hs.put("None","None");
//                Timing timing = mongoTemplate.findOne(Query.query(Criteria.where("date").is(makeScript.day())),Timing.class);
//                assert timing != null;
//                //判断当前调用爬虫的时间是不是在指定的区间内，且对应的pm，am为false
//                if(makeScript.time()<=8&&makeScript.time()>=12&& !timing.getAm()||(makeScript.time()<=18&&makeScript.time()>=14&& !timing.getPm())){
//                    //调用爬虫时间过长，前端设置了超时时间，所以这里采用异步任务
//                    makeScript.PythonReptile(city_name, key+"",false);
//                }else {
//                    hs.put("once","once");
//                }
//
//            }else {
                /*
                    将符合四个条件的职位查出来，然后取出对应的月薪，放入5个合适的月薪区间（对应直角坐标系的x轴）
                 */
                Query query = new Query();
                Criteria criteria = Criteria.where("grade").is(choice.get(1));
                criteria.and("city").is(city_name);
                criteria.and("exper").is(choice.get(0));
                criteria.and("rongzi").is(choice.get(3));
                criteria.and("key").is(key);

                if(choice.get(2) instanceof ArrayList){
                    criteria.orOperator(Criteria.where("guimo").is("15-50人"),Criteria.where("guimo").is("50-150人"));
                }else {
                    criteria.and("guimo").is(choice.get(2));
                }
                query.addCriteria(criteria);
                List<Employment> employmentList = mongoTemplate.find(query,Employment.class);
                if(employmentList.size()!=0){
                    int[] arr = dealSection.section(employmentList);
                    hs.put("section",arr);
                }else {
                    hs.put("null","null");
                }

                /*
                    查询符合单个条件的职位查出来，将单个职位的月薪放入5个月薪区间
                 */
                List<Employment> jingyang = mongoTemplate.find(Query.query(Criteria.where("exper").is(choice.get(0)).and("city").is(city_name).and("key").is(key)),Employment.class);
                List<Employment> rongzi = mongoTemplate.find(Query.query(Criteria.where("rongzi").is(choice.get(3)).and("city").is(city_name).and("key").is(key)),Employment.class);
                List<Employment> xueli = mongoTemplate.find(Query.query(Criteria.where("grade").is(choice.get(1)).and("city").is(city_name).and("key").is(key)),Employment.class);


                Criteria criteria1 = new Criteria();
                Criteria c1 = Criteria.where("guimo").is("15-50人");
                Criteria c2 = Criteria.where("guimo").is("50-150人");
                criteria1 = choice.get(2) instanceof ArrayList?criteria1.orOperator(c1,c2):criteria1.and("guimo").is(choice.get(2));
                criteria1.and("city").is(dealSection.city_indexTo_name(city));
                List<Employment> guimo = mongoTemplate.find(Query.query(criteria1),Employment.class);

                if(jingyang.size()!=0||rongzi.size()!=0||xueli.size()!=0||guimo.size()!=0){
                    int[] jy = dealSection.section(jingyang);
                    int[] rz = dealSection.section(rongzi);
                    int[] xl = dealSection.section(xueli);
                    int[] gm = dealSection.section(guimo);

                    hs.put("jingyan",jy);
                    hs.put("xueli",xl);
                    hs.put("rongzi",rz);
                    hs.put("guimo",gm);
                }else {
                    hs.put("err","err");
                }

//        System.out.println("jingyan:"+ Arrays.toString(jy));
//        System.out.println("xueli:"+ Arrays.toString(xl));
//        System.out.println("rongzi:"+ Arrays.toString(rz));
//        System.out.println("guimo:"+ Arrays.toString(gm));
//            }
//        }else {
//            hs.put("exist","exist");
//        }
        System.out.println(hs);
        return new ObjectMapper().writeValueAsString(hs);
    }

    /**
     * 接受搜索关键词请求，据条件判断是否调用爬虫
     * 1.数据库中必须没有该城市、关键字的session存在
     * 2.时间要在对应的区间，且时间对应上午下午标识为false
     * @param city
     * @param key
     * @return
     * @throws JsonProcessingException
     */
    @GetMapping("/onsearch")
    public String onSearch(@RequestHeader int city,@RequestParam String key) throws JsonProcessingException {
        System.out.println("搜索请求");
        HashMap<String,Object> hs = new HashMap<>();
        Object keys = key.equals("") ?"java": dealSection.drawEn(key);
        String city_name = dealSection.city_indexTo_name(city);
        System.out.println(city_name);
        //判断该条件（关键词、城市）是不是正在爬取
        Session session = mongoTemplate.findOne(Query.query(Criteria.where("city").is(city_name).and("key").is(keys)),Session.class);
        //没有爬
        if(session==null) {
            if (mongoTemplate.findOne(Query.query(Criteria.where("city").is(city_name).and("key").is(keys)), Employment.class) == null) {

                Timing timing = mongoTemplate.findOne(Query.query(Criteria.where("date").is(makeScript.day()+"")), Timing.class);
                assert timing != null;
                //判断当前调用爬虫的时间是不是在指定的区间内，且对应的pm，am为false
                if (makeScript.time() >= 8 && makeScript.time() <= 12 && !timing.getAm() || (makeScript.time() <= 18 && makeScript.time() >= 14 && !timing.getPm())) {
                    hs.put("None", "None");
                    //调用爬虫时间过长，前端设置了超时时间，所以这里采用异步任务
                    makeScript.PythonReptile(city_name, keys + "", false);
                } else {
                    hs.put("once", "once");
                }
            }
        }else {
            hs.put("exist","exist");
        }
        System.out.println(hs);
        return new ObjectMapper().writeValueAsString(hs);
    }

    /**
     * 地图查询请求
     * @return
     * @throws JsonProcessingException
     */
    @GetMapping("/returnArea")
    public String returnArea(@RequestHeader int city,@RequestParam String key) throws JsonProcessingException {
        System.out.println("地图查询"+key);
        Object keys = key.equals("") ?"java":key;
        List<Employment> list = mongoTemplate.find(Query.query(Criteria.where("city").is(dealSection.city_indexTo_name(city)).and("key").is(keys)),Employment.class);
        if(list.size()==0){
            return "err";
        }else {
            List<String> list1 = new ArrayList<>();
            for(Employment employment : list){
                LinkedHashMap<String,String> place = employment.getPlace();
                String region = place.get("region");
                if(region.equals("武汉经济技术开发区")){
                    region = "汉南区";
                }
                if(region.equals("东湖新技术开发区")){
                    region = "洪山区";
                }
                list1.add(region);
            }

            // 统计list里元素的个数
            Map<String, Integer> map = new HashMap<>();
            for (String temp : list1) {
                Integer count = map.get(temp);
                map.put(temp, (count == null) ? 1 : count + 1);
            }
            System.out.println(map);
            return new ObjectMapper().writeValueAsString(map);
        }
    }

    /**
     * 热门技术查询
     * @param key
     * @param city
     * @return
     * @throws JsonProcessingException
     */
    @GetMapping("/returnHotTech")
    public String returnHotTech(@RequestParam String key,@RequestHeader int city) throws JsonProcessingException {
        System.out.println("热门技术查询"+key);
        Object keys = key.equals("") ?"java":key;
        Technology technology = mongoTemplate.findOne(Query.query(Criteria.where("city").is(dealSection.city_indexTo_name(city)).and("key").is(keys)),Technology.class);
        Object back = technology==null?"err":technology;
        System.out.println(back);
        return new ObjectMapper().writeValueAsString(back);
    }

    /**
     * 接受参数为不合适的词，并将其添加到对应的停用词表
     * @param key
     * @param city
     * @param err
     * @return
     */
    @GetMapping("/noSuitable")
    public String noSuitable(@RequestParam String key,@RequestHeader int city,@RequestParam String err){
        System.out.println("停用词请求"+key);
        Object keys = key.equals("") ?"java":key;
        System.out.println(err);
        Query query = new Query(Criteria.where("city").is(dealSection.city_indexTo_name(city)).and("key").is(keys));
        Technology technology = mongoTemplate.findOne(query,Technology.class);
        assert technology != null;
        List<String> stop_list = technology.getStop_list();
        Update update = new Update();
        stop_list.add(err);
        update.set("stop_list", stop_list);
        mongoTemplate.updateFirst(query, update, Technology.class);
        makeScript.PythonKeyExtract(dealSection.city_indexTo_name(city),key,true);
        return "ok";
    }


    /**
     * 返回前五天的日期，和搜索次数
     * @param key
     * @return
     */
    @GetMapping("/returnTrend")
    private String returnTrend(@RequestParam String key) throws JsonProcessingException {
        System.out.println("日期");
        Object keys = key.equals("") ?"java":key;
        HashMap<String,Object> hs = new HashMap<>();
        List<Employment> employment = mongoTemplate.find(Query.query(Criteria.where("key").is(keys)),Employment.class);
        if(employment.size()!=0){
            if(mongoTemplate.findOne(Query.query(Criteria.where("key").is(keys)),com.pojo.Trend.class)==null){
                trend.trendNum(key);
            }
            com.pojo.Trend trends = mongoTemplate.findOne(Query.query(Criteria.where("key").is(keys)), com.pojo.Trend.class);
            hs.put("date", trend.getDate());
            assert trends != null;
            hs.put("baidu",trends.getBaidu());
            hs.put("sougou",trends.getSougou());
            hs.put("_360",trends.get_360());
        }else {
            hs.put("err","err");
        }
        System.out.println(hs);
        return new ObjectMapper().writeValueAsString(hs);
    }

}
