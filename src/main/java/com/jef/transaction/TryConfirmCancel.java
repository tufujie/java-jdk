package com.jef.transaction;

/**
 * TCC
 */
public interface TryConfirmCancel {

    void tryCommit();

    void confirmCommit();

    void cancel();
}
