package com.gsdd.redis.dto;

import java.io.Serial;
import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Generated;
import lombok.NoArgsConstructor;

@Generated
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto implements Serializable {

  @Serial private static final long serialVersionUID = -552202647735887057L;
  private String name;
  private String lastName;
  private LocationDto location;
}
