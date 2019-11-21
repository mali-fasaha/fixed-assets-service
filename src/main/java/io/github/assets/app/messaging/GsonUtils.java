package io.github.assets.app.messaging;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;

@Slf4j
public class GsonUtils<T> {

    public static String toJsonString(List stuff) {
        log.info("Converting to json-string a list of : {} items", stuff.size());
        return new Gson().toJson(stuff);
    }

    public static <T> List<T> stringToList(String s, Class<T[]> clazz) {
        log.debug("Converting json-string to list : {}", s);
        T[] arr = new Gson().fromJson(s, clazz);
        return Arrays.asList(arr); //or return Arrays.asList(new Gson().fromJson(s, clazz)); for a one-liner
    }
}
