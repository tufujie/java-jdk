package com.jef.transaction;

/**
 * 三阶段提交
 *
 * @author Jef
 * @date 2023/10/30
 */
public interface ThreePhaseCommit {
    /**
     * 是否可提交
     *
     * @return 是否
     */
    boolean canCommit();

    /**
     * 准备提交
     */
    void preCommit();

    /**
     * 提交
     *
     * @return 是否提交提交
     */
    boolean doCommit();
}