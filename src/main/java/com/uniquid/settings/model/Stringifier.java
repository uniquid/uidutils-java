package com.uniquid.settings.model;

import com.uniquid.settings.exception.StringifyException;

public interface Stringifier {

    String stringify(Setting setting) throws StringifyException;

}
