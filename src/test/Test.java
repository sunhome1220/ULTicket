package test;

import java.util.ArrayList;
import java.util.HashMap;
import util.DBUtil;

/**
 *
 * @author Sean_wang@syscom.com.tw
 */
public class Test {
    public static void main(String[] args){
        String sql= "SELECT taginc,evid,event,team,procman,procaddr,tickid,tickname,ticktel,tickmemo FROM proctick";
        ArrayList<HashMap> list = DBUtil.getInstance(2).executeQuery(sql);
        list.forEach((o) -> {
            System.out.println(o.toString());
        });
    }
}
