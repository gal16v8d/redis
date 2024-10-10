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
public class LocationDto implements Serializable {

  @Serial private static final long serialVersionUID = 6907545373081939224L;
  private String address;
  private String city;
}
