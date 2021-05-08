package guru.sfg.test;

import lombok.Data;

import java.util.List;

@Data
public class MandatoryModel {
    private String name;
    private Integer index;
    private boolean mandatory;
    private boolean needsAdditionalCheck;
    private List<AdditinalFields> additionalParams;
}