package com.lantu.woevent.models;

import lombok.Data;
import lombok.ToString;

@ToString
@Data
public class ResultInfo<T>
{
    private boolean success;

    private String message;

    private T entity;
}
