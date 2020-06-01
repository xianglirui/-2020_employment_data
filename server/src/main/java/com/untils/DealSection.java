package com.untils;

import com.pojo.Employment;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class DealSection {

    public int[] section(List<Employment> list){
        int[] arr = {0,0,0,0,0};
        for(Employment key : list){
            //去除字符串的"-"
            String[] str = key.getMoney().split("-");
            //将字符串数组转换为一个Stream，用于调用map函数
            Stream<String> stream = Arrays.stream(str);
            //将字符串数组 ["8k","12k"]中的k去掉
            Stream<Integer> stream1 = stream.map(v -> Integer.parseInt(v.substring(0,v.lastIndexOf('k'))));
            //将stream输出为List
            List<Integer> lists = stream1.collect(Collectors.toList());

            arr[0]+=compare(lists)[0];
            arr[1]+=compare(lists)[1];
            arr[2]+=compare(lists)[2];
            arr[3]+=compare(lists)[3];
            arr[4]+=compare(lists)[4];
        }
        return arr;
    }

    public int[] compare(List<Integer> list){
        int[] num = {0,0,0,0,0};
        int s = list.get(0);
        int b = list.get(1);
        if(s<5||b<5){
            num[0]++;
        }
        if(s>=5&&s<10||b>=5&&b<10){
            num[1]++;
        }
        if(s>=10&&s<15||b>=10&&b<15){
            num[2]++;
        }
        if(s>=15&&s<20||b>=15&&b<20){
            num[3]++;
        }
        if(s>=20||b>=20){
            num[4]++;
        }
        if(s<5&&b>=10&&b<15){
            num[1]++;
        }
        if(s<5&&b>=15&&b<20){
            num[1]++;
            num[2]++;
        }
        if(s<5&&b>=20){
            num[1]++;
            num[2]++;
            num[3]++;
        }
        if(s<10&&b>=15&&b<20){
            num[2]++;
        }
        if(s<10&&b>=20){
            num[2]++;
            num[3]++;
        }
        if(s<15&&b>=20){
            num[3]++;
        }
        return num;
    }

    public String city_indexTo_name(int index){
        String city_name = "";
        switch (index){
            case 0:
                city_name = "北京";break;
            case 1:
                city_name = "上海";break;
            case 2:
                city_name = "杭州";break;
            case 3:
                city_name = "广州";break;
            case 4:
                city_name = "深圳";break;
            case 5:
                city_name = "成都";break;
            case 6:
                city_name = "江苏";break;
            case 7:
                city_name = "武汉";break;
        }
        return city_name;
    }

    /**
     * 提取非中文字符，大写转小写
     * @param key
     * @return
     */
    public String drawEn(String key){
        String regEx="[^\u4e00-\u9fa5]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(key.toLowerCase());
        StringBuilder sb = new StringBuilder();
        while(m.find()){
            sb.append(m.group());
        }
        return sb.toString();
    }
}
