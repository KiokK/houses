package ru.clevertec.cachestarter.util;

import java.util.UUID;

public class UserDtoTestData {

    public static final UUID USER_UUID_1 = UUID.randomUUID();
    public static final UUID USER_UUID_2 = UUID.randomUUID();
    public static final UUID USER_UUID_3 = UUID.randomUUID();

    public static UserDto getUserDto1() {
        return UserDto.builder()
                .id(USER_UUID_1)
                .build();
    }

    public static UserDto getUserDto2() {
        return UserDto.builder()
                .id(USER_UUID_2)
                .build();
    }

    public static UserDto getUserDto3() {
        return UserDto.builder()
                .id(USER_UUID_3)
                .build();
    }

}
