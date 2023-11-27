package com.jef.db;

import org.apache.commons.lang.math.NumberUtils;
import org.junit.Test;

/**
 * @author tufujie
 * @date 2023/11/27
 */
public class ShardingTest {
    private static final Long SHARDING_COLUMN = 92311142749459264L;

    private static final String DB_NAME = "db";

    private static final String TABLE_NAME = "user";

    @Test
    public void testCouponShardingDBAndTable() {

        String value1 = String.valueOf(SHARDING_COLUMN);
        long value2 = Math.abs(NumberUtils.toLong(value1, value1.hashCode()));
        long dbShard = value2 % 8;
        // 特殊，不从0开始，从1开始
        dbShard++;

        String value3 = String.valueOf(SHARDING_COLUMN);
        long value4 = Math.abs(NumberUtils.toLong(value3, value3.hashCode()));
        long tableShard = value4 / 8 % 64;
        System.out.println(DB_NAME + "_" + dbShard + "." + TABLE_NAME + "_" + tableShard);
    }

}