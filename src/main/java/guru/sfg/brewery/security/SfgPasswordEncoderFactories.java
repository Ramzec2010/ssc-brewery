package guru.sfg.brewery.security;

import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SfgPasswordEncoderFactories {

    private SfgPasswordEncoderFactories(){}

    public static PasswordEncoder createDelegatingPasswordEncoder() {
        String encodingId = "bcrypt15";
        Map<String, PasswordEncoder> encoders = new HashMap<>();
        encoders.put("bcrypt", new BCryptPasswordEncoder());
        encoders.put(encodingId, new BCryptPasswordEncoder(10));
        encoders.put("ldap", new org.springframework.security.crypto.password.LdapShaPasswordEncoder());
        encoders.put("noop", org.springframework.security.crypto.password.NoOpPasswordEncoder.getInstance());
        encoders.put("sha256", new org.springframework.security.crypto.password.StandardPasswordEncoder());

        return new DelegatingPasswordEncoder(encodingId, encoders);
    }


    public static void main(String[] args) {
        String headersProvided = "PRICING_SEQ_NO,PROGRAM_CODE,PROGRAM_TIER,SKU,COLOUR_CODE,MODEL,MANF_CD,HARDWARE_MODEL,MSRP,MSRP_CREDIT,TERM,MONTHLY_CHARGE_AMT,FRANCHISE_TYPE,PROVINCE,ACTIVITY,DF_IND,UPC,MODEL_CATEGORY,DESCRIPTION,DESCRIPTION_F,FINANCING_ELIG_IND,SKU_DESC,SKU_DESC_F";

        String hedersRequired = "SKU,PRICING_SEQ_NO,PROGRAM_CODE,PROGRAM_TIER,MSRP,TERM,MONTHLY_CHARGE_AMT,DF_IND,UPC,FRANCHISE_TYPE,FINANCING_ELIG_IND";

        List<String> headersProvidedList = Arrays.asList(headersProvided.split(","));
        List<String> headersRequiredList = Arrays.asList(hedersRequired.split(","));

        boolean b = headersProvidedList.containsAll(headersRequiredList);
        System.out.println(b);

        String[] strings = Arrays.stream(headersProvided.split(","))
                .map(String::strip)
                .toArray(String[]::new);
        for (String s   : strings
             ) {
            System.out.println(s);
        }
    }




}
