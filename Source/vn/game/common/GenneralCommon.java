/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.game.common;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author vipgame10
 * @param <E>
 */
public class GenneralCommon<E> implements Serializable {

    private final static Gson GSON;

    static {
        GSON = new Gson();
    }

    public List<E> parseFromJson(String json) {
        List<E> result = GSON.fromJson(json, new TypeToken< List<E>>() {
        }.getType());
        return result;
    }

    public static void main(String[] args) {
//        List<String> parseFromJson = new GenneralCommon<String>().parseFromJson("[\"asdf\",2,3,4]");
        System.out.println((List<Integer>) new GenneralCommon<Integer>() {
        }.parseFromJson("[1,2,3,4]"));
//        int a= 1;
//        List<Integer> test = new GenneralCommon<Integer>() .parseFromJson("[1,2,3,4]");
        List<Integer> test = new Gson().fromJson("[1,2,3,4]", new TypeToken< List<Integer>>() {
        }.getType());
//        
        System.out.println(test);
    }
}
