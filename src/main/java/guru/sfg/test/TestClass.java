package guru.sfg.test;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.util.Pair;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TestClass {

    public static void main(String[] args) throws IOException {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        InputStream is = classloader.getResourceAsStream("mandatoryConfig.json");

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        Map<String, MandatoryModel> stringMandatoryModelMap = mapper.readValue(is, new TypeReference<Map<String, MandatoryModel>>() {
        });

        List<String> readLine = new ArrayList<>();
        readLine.add("");
        readLine.add("Y");


        Map<String, Map<String, String>> mapOfStrings = new HashMap<>();
        mapOfStrings.put("key1", Map.of("key2", "value2", "key5", "Value5"));
        mapOfStrings.put("key3", Map.of("key4", "value4"));


        Map<String, Integer> mapOfKeyIndexes = new HashMap<>();
        mapOfKeyIndexes.put("key1", 1);
        mapOfKeyIndexes.put("key2", 2);
        mapOfKeyIndexes.put("key3", 3);
        mapOfKeyIndexes.put("key4", 4);
        mapOfKeyIndexes.put("key5", 5);

        Map<Integer, Map<Integer, String>> newMap = new HashMap<>();

        for (Map.Entry<String, Map<String, String>> entry :
                mapOfStrings.entrySet()) {
            Map<Integer, String> newValue = entry.getValue().entrySet().stream()
                    .map(stringStringEntry -> Pair.of(mapOfKeyIndexes.get(stringStringEntry.getKey()), stringStringEntry.getValue()))
                    .collect(Collectors.toMap(Pair::getFirst, Pair::getSecond));

            Integer newKey = mapOfKeyIndexes.get(entry.getKey());
            newMap.put(newKey, newValue);
        }

        System.out.println(newMap);


/*
        for (Map.Entry<String, MandatoryModel> entry : stringMandatoryModelMap.entrySet()) {
            MandatoryModel entryValue = entry.getValue();
            if (entryValue.isNeedsAdditionalCheck()) {
                List<AdditinalFields> additionalParams = entryValue.getAdditionalParams();
                boolean additionalCheckFailed = additionalParams.stream().anyMatch(additinalFields -> !checkValue(additinalFields, readLine, stringMandatoryModelMap));
                //Failed means additional parameter does not valid, field is not mandatory
                if (additionalCheckFailed) {
                    System.out.println(entry.getKey() + " is not mandatory");
                } else {
                    boolean mandatoryEntryValid = isEntryValid(readLine, entryValue);
                    System.out.println(entry.getKey() + " is valid: " + mandatoryEntryValid);
                }
            } else if (entryValue.isMandatory()) {
                isEntryValid(readLine, entryValue);
                System.out.println(entry.getKey() + " is valid: " + isEntryValid(readLine, entryValue));
            }
        }*/
    }

    private static boolean isEntryValid(List<String> readLine, MandatoryModel entryValue) {
        return Objects.nonNull(readLine.get(entryValue.getIndex())) && StringUtils.hasText(readLine.get(entryValue.getIndex()));
    }

    private static boolean checkValue(AdditinalFields addField, List<String> readLine, Map<String, MandatoryModel> stringMandatoryModelMap) {
        Integer fieldIndex = stringMandatoryModelMap.get(addField.getName()).getIndex();
        return addField.getValue().equals(readLine.get(fieldIndex));
    }
}
