package org.example.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ListLRU {

    private List<String> list = new ArrayList<>();


    public void put(String element){
        list.remove(element);
        list.add(element);
    }

    public <T> String getHashLatestElement(Set<Map.Entry<String,T>> entrySet){
        int minId = list.size()-1;
        String minCache = list.get(minId);

        for (Map.Entry<String, T> el :
                entrySet) {
            int elId = list.indexOf(el.getKey());
            if(elId<minId) {
                minId=elId;
                minCache=el.getKey();
            }
        }
        list.remove(minCache);
        return minCache;
    }

}
