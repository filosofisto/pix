package com.antifraud.api.constants;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Result {

    private String result;

    private static Result instance;

    private Result(String result) {
        this.result = result;
    }

    public static Result getInstance() {
        if (instance == null) {
            instance = new Result("POSITIVE");
        }

        return instance;
    }
}
