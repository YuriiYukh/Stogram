package com.github.yuriiyukh.stogram.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JWTTokenSuccsessResponse {

    private boolean succsess;
    private String token;

}
