package com.jef.transaction;

/**
 * 两阶段提交
 *
 * @author Jef
 * @date 2023/10/30
 */
public interface TwoPhaseCommit {

    /**
     * 准备
     */
    void prepare();

    /**
     * 提交
     *
     * @return 是否提交成功
     */
    boolean commit();
}