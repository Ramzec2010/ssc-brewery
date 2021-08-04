package guru.sfg.test;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.validator.GenericValidator;
import org.hibernate.validator.internal.util.Contracts;
import org.springframework.data.util.Pair;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TestClass {

    //public static void main(String[] args) throws IOException, ParseException {
   /*     ClassLoader classloader = Thread.currentThread().getContextClassLoader();
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

        System.out.println(newMap);*/


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

 //       checkTime();
  //  }


    public static void checkTime() throws ParseException {
        System.out.println(GenericValidator.isDate("12/12/2000", "MM/dd/yyy", true));
        Date date = DateUtils.parseDate("12-12-2000", "MM-dd-yyyy");
        String format = DateFormatUtils.format(date, "yyyy-dd-MM HH:mm:ss");
        System.out.println(format);
    }

    private static boolean isEntryValid(List<String> readLine, MandatoryModel entryValue) {
        return Objects.nonNull(readLine.get(entryValue.getIndex())) && StringUtils.hasText(readLine.get(entryValue.getIndex()));
    }

    private static boolean checkValue(AdditinalFields addField, List<String> readLine, Map<String, MandatoryModel> stringMandatoryModelMap) {
        Integer fieldIndex = stringMandatoryModelMap.get(addField.getName()).getIndex();
        return addField.getValue().equals(readLine.get(fieldIndex));
    }


    interface Dao {

        int [] insertV21(List<String> strings);

        int [] insertV178r(List<String> strings);
    }

    Dao dao = new Dao() {
        @Override
        public int[] insertV21(List<String> strings) {
            System.out.println(11111);
            return new int[0];
        }

        @Override
        public int[] insertV178r(List<String> strings) {
            System.out.println(22222);
            return new int[0];
        }
    };

    void testMethod() {
        List<String> strings = new ArrayList<>();
        List<String> string2 = new ArrayList<>();
        string2.add("");
        saveAccessory(strings, (parameter -> dao.insertV178r(parameter)));
        saveAccessory(string2, (parameter-> dao.insertV21(parameter)));

    }

    public void saveAccessory(List<String> strings, Function<List<String>, int[]> function) {
        if (CollectionUtils.isEmpty(strings)) {
            System.out.println("00000000000");

        }

        int[] messages = call(() -> function.apply(strings), "Message");

    }
    public static <T> T call(Callable<T> callable, String exseption) {
        try {
            return callable.call();
        } catch (Exception ex) {
            throw new RuntimeException(exseption);
        }
    }
    public static void main(String[] args) {
        TestClass testClass = new TestClass();
        testClass.testMethod();
    }
}
