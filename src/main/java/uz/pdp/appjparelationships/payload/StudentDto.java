package uz.pdp.appjparelationships.payload;

import lombok.Data;
import uz.pdp.appjparelationships.entity.Address;

import java.util.List;

@Data
public class StudentDto {
    private String firstName;

    private String lastName;

    private String city;//Toshkent

    private String district;//Mirobod

    private String street;//U.Nosir ko'chasi

    private Integer groupsId;
    private List<Integer> subject;

}
