package com;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: ruoyi
 * @description:
 * @author: ma zy
 * @date: 2022-11-01 19:32
 **/
public class TestUser {
    public static void main(String[] args) {
        List<Map> list = new ArrayList();
        Map<String,List<User>> map = new HashMap();
        List<User> userList = new ArrayList<>();
        User user = new User();
        user.setId("1");
        user.setName("aa");
        userList.add(user);
        User user1 = new User();
        user1.setId("2");
        user1.setName("bb");
        userList.add(user1);
        map.put("a",userList);

        list.add(map);


        list.stream().forEach(p->{
            List<User> maps = (List<User>) p.get("a");

        });


    }
}
class User{
    private String id;
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}