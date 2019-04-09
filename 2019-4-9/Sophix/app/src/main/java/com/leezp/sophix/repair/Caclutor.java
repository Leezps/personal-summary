package com.leezp.sophix.repair;

import com.leezp.sophix.Replace;

public class Caclutor {
    @Replace(clazz = "com.leezp.sophix.exception.Caclutor", method = "caculator")
    public int caculator() {
        return 10;
    }
}
