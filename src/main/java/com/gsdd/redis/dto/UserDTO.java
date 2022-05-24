package com.gsdd.redis.dto;

import java.io.Serializable;
import lombok.Data;
import lombok.Generated;

@Generated
@Data
public class UserDTO implements Serializable {

  private static final long serialVersionUID = -552202647735887057L;
  private String name;
  private String lastName;
  private LocationDTO location;
}
