package com.study.boardclient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

//json을 자바의 객체로 변환하는 코드를 별도로 정의해보자!!
public class BoardConverter<T> {

    public ArrayList<T> getConvertedData(String jsonString){
        //[{},{},{},...]
        ArrayList<T> list  = new ArrayList<T>();
        try {
            JSONArray jsonArray = new JSONArray(jsonString);
            for (int i = 0 ; i < jsonArray.length(); i++){
                JSONObject json = (JSONObject) jsonArray.get(i);
                Board board = new Board();
                board.setBoard_id(json.getInt("board_id"));
                board.setTitle(json.getString("title"));
                board.setWriter(json.getString("writer"));
                board.setContent(json.getString("content"));
                board.setRegdate(json.getString("regdate"));
                board.setHit(json.getInt("hit"));
                list.add((T)board); //T형으로 반환하여서 넣어야한다
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }
}
