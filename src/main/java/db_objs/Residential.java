package db_objs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class Residential {
    private final int id;
    private final int city_id;
    private final String street_name;
    private final String house_number;
}
