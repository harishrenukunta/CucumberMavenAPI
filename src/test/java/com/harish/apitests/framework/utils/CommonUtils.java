package com.harish.apitests.framework.utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

public class CommonUtils {

    public static Map<String, String> createMapFromLists(final List<String> keys, final List<String> values){
        final Map<String, String> map = new HashMap<>();
        for(int i = 0; i < keys.size(); i++){
            map.put(keys.get(i).trim(),values.get(i).trim());
        }
        return map;
    }

    public static Map<String, List<String>> getMapFromStringContainingEntryKeyValueDelimeter(final String incomingStr, final String mapEntrySeparator, final String keyValueSeparator, final String valueSeparator){
        final Function<String, List<String>> createListOfMapValuesFromEntry = (entryItem) -> {
            final String commaSeparatedStr = entryItem.split(keyValueSeparator)[1];
            return Arrays.stream(commaSeparatedStr.split(","))
                    .collect(toList());
        };
            return Arrays.stream(incomingStr.split(mapEntrySeparator))
                    .collect(Collectors.toMap(entryItem -> entryItem.split(keyValueSeparator)[0], createListOfMapValuesFromEntry ));
    }

    public static Map<String, String> getMapFromStringContainingEntryKeyValueDelimiter(final String incomingStr, final String mapEntrySeparator, final String keyValueSeparator){
        return Arrays.stream(incomingStr.split(mapEntrySeparator))
                .collect(Collectors.toMap(entryItem -> entryItem.split(keyValueSeparator)[0], entryItem -> entryItem.split(keyValueSeparator)[1]));
    }
}
