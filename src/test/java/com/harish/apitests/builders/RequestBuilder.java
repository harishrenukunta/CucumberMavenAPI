package com.harish.apitests.builders;

import java.io.FileNotFoundException;
import java.util.Map;

public interface RequestBuilder<T> {

    public T build(Map<String, String> requestParams);
    public T build() throws FileNotFoundException;
}
