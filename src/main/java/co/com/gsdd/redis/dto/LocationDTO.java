package co.com.gsdd.redis.dto;

import java.io.Serializable;

import lombok.Data;
import lombok.Generated;

@Generated
@Data
public class LocationDTO implements Serializable {

    private static final long serialVersionUID = 6907545373081939224L;
    private String address;
    private String city;

}