package com.investor.backofficeinvestor.controller;


import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import jdk.nashorn.internal.ir.ObjectNode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;


@RestController
public class InvestorController {

    @PostMapping("/test")
    public String Concept (@RequestBody  String ConceptRequest){
       return ConceptRequest;
    }

    @PostMapping("/test1")
    public Map<String, Object> Concept1 (@RequestBody String conceptRequest){

//        Gson gson = new Gson();
//        JsonParser parser = new JsonParser();
//        String json = gson.toJson(conceptRequest);
//
//
//        JsonObject o = parser.parse("{\"a\": \"A\"}").getAsJsonObject();
//        Map<String,Object> result =
//                new ObjectMapp().readValue(o, HashMap.class);

//
//        Gson gson = new Gson();
//        String json = gson.toJson(conceptRequest);
//        Map<String,Object> map = new HashMap<String,Object>();
//        map = (Map<String,Object>) gson.fromJson(json, map.getClass());

//        String jsonString = "{'employee.name':'Bob','employee.salary':10000}";
        Gson gson = new Gson();
//        System.out.println(conceptRequest);
        Map map = gson.fromJson(conceptRequest, Map.class);

        String type = map.get("type").toString();
        Map<String,Object> data = (Map<String, Object>) map.get("data");
        Map<String,Object> objectT = (Map)data.get("object");
        String id = objectT.get("id").toString();
        String object = objectT.get("object").toString();
        if (object.equals("charge"))
        {
            System.out.println(object);
        }else
            System.out.println("no es charge");
        System.out.println(type);
        System.out.println(id);
        return  gson.fromJson(conceptRequest, Map.class);

//        return map;

    }
    @PostMapping("/test2")
    public Map<String, String> sayHello() {
        HashMap<String, String> map = new HashMap<>();
        map.put("key", "value");
        map.put("foo", "bar");
        map.put("aa", "bb");
        return map;
    }

}
