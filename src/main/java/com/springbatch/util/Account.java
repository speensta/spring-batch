package com.springbatch.util;

import lombok.extern.slf4j.Slf4j;

import java.io.Closeable;
import java.io.IOException;
import java.math.BigDecimal;

@Slf4j
public class Account implements Closeable {

    public Account() {
    }

    public void transfer(BigDecimal amount, String to) {
      log.info("===========================amount:", amount, " to:",to);

    }

    @Override
    public void close() throws IOException {

    }
}
