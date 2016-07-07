package com.peter.action;

import com.peter.bean.Bihua;

import java.util.ArrayList;

/**
 * Created by jiangbin on 16/7/7.
 */
public class BihuaParser {

    public static Bihua parseBihua(String content,String encode) {
        Bihua bihua = new Bihua();

        bihua.encode = encode;
        String bihuaReal = content.split(";")[0];
//        System.out.println(bihuaReal);

        bihua.hanzi = bihuaReal.substring(content.indexOf("{") + 1, content.indexOf(":"));
//        System.out.println(bihua.hanzi);


        int firstIndex = bihuaReal.indexOf("[");
        int lastIndex = bihuaReal.lastIndexOf("]");
        String bihuaWithCountAndSteps = bihuaReal.substring(firstIndex + 1, lastIndex);
//        System.out.println(bihuaWithCountAndSteps);
        bihua.bihuaCount = Integer.parseInt(bihuaWithCountAndSteps.substring(0, bihuaWithCountAndSteps.indexOf(",")));
        //        System.out.println(bihuaEntity.bihuaCount);
        int count = bihuaWithCountAndSteps.length();
        bihua.pinyin = bihuaWithCountAndSteps.substring(bihuaWithCountAndSteps.lastIndexOf("'", count - 2) + 1, count - 1);
//        System.out.println(bihuaEntity.pinyin);

        String bihua2 = bihuaWithCountAndSteps.substring(bihuaWithCountAndSteps.indexOf(",") + 1, bihuaWithCountAndSteps.lastIndexOf(","));
//        System.out.println(bihua2);
        bihua.bihuaStep = bihua2.substring(bihua2.indexOf("'")+1,bihua2.indexOf("'",bihua2.indexOf("'")+1));
//        System.out.println(bihua.bihuaStep);
        String bihua3 = bihua2.substring(bihua2.indexOf(",") + 1);

//        System.out.println(bihua3);
        String bihua4 = bihua3.substring(1, bihua3.length() - 1);
//        System.out.println(bihua4);
        bihua.points = bihua4;
        return bihua;
    }
}
