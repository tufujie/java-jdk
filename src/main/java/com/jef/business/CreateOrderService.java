package com.jef.business;

import com.jef.entity.OrderInfo;
import com.jef.entity.OrderProduct;
import com.jef.entity.Product;
import org.hibernate.resource.transaction.spi.TransactionStatus;

import java.util.Date;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 创建订单服务
 * 模拟10个用户抢购2个商品
 *
 * @author tufujie
 * @date 2023/12/7
 */
public class CreateOrderService {

    // 总库存数量
    private Integer PRODUCT_COUNT = 2;

    /**
     * 错误案例一：数据库update相互覆盖
     * 直接在内存中判断是否有库存，计算扣减之后的值更新数据库，并发的情况下会导致相互覆盖发生：
     *
     * @param purchaseProductId  购买的商品id
     * @param purchaseProductNum 购买数量
     * @return
     * @throws Exception
     */
    public Long createOrderErrorV1(Long purchaseProductId, Integer purchaseProductNum, Long userId) throws Exception {
//        Product product = productMapper.selectByPrimaryKey(purchaseProductId);
        Product product = new Product(purchaseProductId, PRODUCT_COUNT);
        // ... 忽略校验逻辑

        // 商品当前库存
        Integer currentCount = product.getCount();
        // 校验库存
        if (purchaseProductNum > currentCount) {
            throw new Exception("当前用户" + userId + "商品" + purchaseProductId + "仅剩" + currentCount + "件，无法购买");
        }
        // 计算剩余库存
        Integer leftCount = currentCount - purchaseProductNum;
        // 更新库存
        product.setCount(leftCount);
        product.setGmtModified(new Date());

        // 更新库存数量
//        productMapper.updateByPrimaryKeySelective(product);
        PRODUCT_COUNT = product.getCount();

        OrderInfo order = new OrderInfo();
        // ... 省略 Set
//        orderMapper.insertSelective(order);
        System.out.println("当前用户" + userId + "成功创建商品" + purchaseProductId + "的订单");

        OrderProduct orderItem = new OrderProduct();
        orderItem.setOrderId(order.getId());
        // ... 省略 Set
        return order.getId();
    }

    /**
     * 错误案例二：扣减串行执行，但是库存被扣减为负数
     * 在 SQL 中加入运算避免值的相互覆盖，但是库存的数量变为负数，因为校验库存是否足够还是在内存中执行的，并发情况下都会读到有库存：
     *
     * @param purchaseProductId  购买的商品id
     * @param purchaseProductNum 购买数量
     * @return
     * @throws Exception
     */
    public Long createOrderErrorV2(Long purchaseProductId, Integer purchaseProductNum, Long userId) throws Exception {
//        Product product = productMapper.selectByPrimaryKey(purchaseProductId);
        Product product = new Product(purchaseProductId, PRODUCT_COUNT);
        // ... 忽略校验逻辑

        // 商品当前库存
        Integer currentCount = product.getCount();
        // 校验库存
        if (purchaseProductNum > currentCount) {
            throw new Exception("当前用户" + userId + "商品" + purchaseProductId + "仅剩" + currentCount + "件，无法购买");
        }
        // 更新库存数量
        // 使用 set count =  count - #{purchaseProductNum,jdbcType=INTEGER}, 更新库存
//        productMapper.updateProductCount(purchaseProductNum, new Date(), product.getId());
        PRODUCT_COUNT = product.getCount() - purchaseProductNum;

        OrderInfo order = new OrderInfo();
        // ... 省略 Set
//        orderMapper.insertSelective(order);
        System.out.println("当前用户" + userId + "成功创建商品" + purchaseProductId + "的订单");

        OrderProduct orderItem = new OrderProduct();
        orderItem.setOrderId(order.getId());
        // ... 省略 Set
        return order.getId();
    }

    /**
     * 错误案例三：使用 synchronized 实现内存中串行校验，但是依旧扣减为负数
     * 因为我们使用的是事务的注解，synchronized加在方法上，方法执行结束的时候锁就会释放，此时的事务还没有提交，
     * 另一个线程拿到这把锁之后获取的可能是旧库存数，就会有一次扣减，导致负数。
     *
     * @param purchaseProductId  购买的商品id
     * @param purchaseProductNum 购买数量
     * @return
     * @throws Exception
     */

//    @Transactional(rollbackFor = Exception.class)
    public synchronized Long createOrderErrorV3(Long purchaseProductId, Integer purchaseProductNum, Long userId) throws Exception {
//        Product product = productMapper.selectByPrimaryKey(purchaseProductId);
        Product product = new Product(purchaseProductId, PRODUCT_COUNT);
        // ... 忽略校验逻辑

        // 商品当前库存
        Integer currentCount = product.getCount();
        // 校验库存
        if (purchaseProductNum > currentCount) {
            throw new Exception("当前用户" + userId + "商品" + purchaseProductId + "仅剩" + currentCount + "件，无法购买");
        }

        // 更新库存数量
        // 使用 set count =  count - #{purchaseProductNum,jdbcType=INTEGER}, 更新库存
//        productMapper.updateProductCount(purchaseProductNum, new Date(), product.getId());
        PRODUCT_COUNT = product.getCount() - purchaseProductNum;

        OrderInfo order = new OrderInfo();
        // ... 省略 Set
//        orderMapper.insertSelective(order);
        System.out.println("当前用户" + userId + "成功创建商品" + purchaseProductId + "的订单");

        OrderProduct orderItem = new OrderProduct();
        orderItem.setOrderId(order.getId());
        // ... 省略 Set
        return order.getId();
    }

    /**
     * 单体应用解决超卖的问题
     * 正确示例：将事务包含在锁的控制范围内
     * 通过手动提交的方式，保证在锁释放之前，事务已经提交。
     *
     * @param purchaseProductId  购买的商品id
     * @param purchaseProductNum 购买数量
     * @return
     * @throws Exception
     */
//    @Transactional(rollbackFor = Exception.class)
    public synchronized Long createOrderSingleRightV1(Long purchaseProductId, Integer purchaseProductNum, Long userId) throws Exception {
//        TransactionStatus transaction1 = PlatformTransactionManager.getTransaction(transactionDefinition);
        TransactionStatus transaction1 = TransactionStatus.COMMITTED;
//        Product product = productMapper.selectByPrimaryKey(purchaseProductId);
        Product product = new Product(purchaseProductId, PRODUCT_COUNT);
        // ... 忽略校验逻辑

        // 商品当前库存
        Integer currentCount = product.getCount();
        // 校验库存
        if (purchaseProductNum > currentCount) {
//            platformTransactionManager.rollback(transaction1);
            throw new Exception("当前用户" + userId + "商品" + purchaseProductId + "仅剩" + currentCount + "件，无法购买");
        }

        // 更新库存数量
        // 使用 set count =  count - #{purchaseProductNum,jdbcType=INTEGER}, 更新库存
//        productMapper.updateProductCount(purchaseProductNum, new Date(), product.getId());
        PRODUCT_COUNT = product.getCount() - purchaseProductNum;

        OrderInfo order = new OrderInfo();
        // ... 省略 Set
//        orderMapper.insertSelective(order);
        System.out.println("当前用户" + userId + "成功创建商品" + purchaseProductId + "的订单");

        OrderProduct orderItem = new OrderProduct();
        orderItem.setOrderId(order.getId());
        // ... 省略 Set
//        platformTransactionManager.commit(transaction1);
        return order.getId();
    }

    /**
     * 单体应用解决超卖的问题
     * 正确示例：使用synchronized的代码块
     *
     * @param purchaseProductId  购买的商品id
     * @param purchaseProductNum 购买数量
     * @return
     * @throws Exception
     */
    public Long createOrderSingleRightV2(Long purchaseProductId, Integer purchaseProductNum, Long userId) throws Exception {
        synchronized (CreateOrderService.class) {
            //        TransactionStatus transaction1 = PlatformTransactionManager.getTransaction(transactionDefinition);
            TransactionStatus transaction1 = TransactionStatus.COMMITTED;
//        Product product = productMapper.selectByPrimaryKey(purchaseProductId);
            Product product = new Product(purchaseProductId, PRODUCT_COUNT);
            // ... 忽略校验逻辑

            // 商品当前库存
            Integer currentCount = product.getCount();
            // 校验库存
            if (purchaseProductNum > currentCount) {
//            platformTransactionManager.rollback(transaction1);
                throw new Exception("当前用户" + userId + "商品" + purchaseProductId + "仅剩" + currentCount + "件，无法购买");
            }

            // 更新库存数量
            // 使用 set count =  count - #{purchaseProductNum,jdbcType=INTEGER}, 更新库存
//        productMapper.updateProductCount(purchaseProductNum, new Date(), product.getId());
            PRODUCT_COUNT = product.getCount() - purchaseProductNum;
//        platformTransactionManager.commit(transaction2);
        }

        TransactionStatus transaction2 = TransactionStatus.COMMITTED;
        OrderInfo order = new OrderInfo();
        // ... 省略 Set
//        orderMapper.insertSelective(order);
        System.out.println("当前用户" + userId + "成功创建商品" + purchaseProductId + "的订单");

        OrderProduct orderItem = new OrderProduct();
        orderItem.setOrderId(order.getId());
        // ... 省略 Set
//        platformTransactionManager.commit(transaction2);
        return order.getId();
    }


    private Lock lock = new ReentrantLock();

    /**
     * 单体应用解决超卖的问题
     * 正确示例：使用Lock
     *
     * @param purchaseProductId  购买的商品id
     * @param purchaseProductNum 购买数量
     * @return
     * @throws Exception
     */
    public Long createOrderSingleRightV3(Long purchaseProductId, Integer purchaseProductNum, Long userId) throws Exception {
        lock.lock();
        TransactionStatus transaction1 = TransactionStatus.COMMITTED;
        try {
//            product = productMapper.selectByPrimaryKey(purchaseProductId);
            Product product = new Product(purchaseProductId, PRODUCT_COUNT);

            //商品当前库存
            Integer currentCount = product.getCount();
            //校验库存
            if (purchaseProductNum > currentCount) {
                throw new Exception("当前用户" + userId + "商品" + purchaseProductId + "仅剩" + currentCount + "件，无法购买");
            }

//            productMapper.updateProductCount(purchaseProductNum, new Date(), product.getId());
            PRODUCT_COUNT = product.getCount() - purchaseProductNum;
//            platformTransactionManager.commit(transaction1);
        } catch (Exception e) {
//            platformTransactionManager.rollback(transaction1);
            throw new Exception(e);
        } finally {
            // 注意抛异常的时候锁释放不掉，分布式锁也一样，都要在这里删掉
            lock.unlock();
        }

        TransactionStatus transaction = TransactionStatus.COMMITTED;
        OrderInfo order = new OrderInfo();
        // ... 省略 Set
//        orderMapper.insertSelective(order);
        System.out.println("当前用户" + userId + "成功创建商品" + purchaseProductId + "的订单");

        OrderProduct orderItem = new OrderProduct();
        // ... 省略 Set
//        orderItemMapper.insertSelective(orderItem);
//        platformTransactionManager.commit(transaction);
        return order.getId();
    }

    /**
     * 分布式应用
     * 正确案例一：Redis setNx
     *
     * @param purchaseProductId  购买的商品id
     * @param purchaseProductNum 购买数量
     * @return
     * @throws Exception
     */
    public Long createOrderDistributedRightV1(Long purchaseProductId, Integer purchaseProductNum, Long userId) throws Exception {
//        Product product = productMapper.selectByPrimaryKey(purchaseProductId);
        Product product = new Product(purchaseProductId, PRODUCT_COUNT);
        // ... 忽略校验逻辑

        // 商品当前库存
        Integer currentCount = product.getCount();
        // 校验库存
        if (purchaseProductNum > currentCount) {
            throw new Exception("当前用户" + userId + "商品" + purchaseProductId + "仅剩" + currentCount + "件，无法购买");
        }
        // 计算剩余库存
        Integer leftCount = currentCount - purchaseProductNum;
        // 更新库存
        product.setCount(leftCount);
        product.setGmtModified(new Date());

        // 更新库存数量
//        productMapper.updateByPrimaryKeySelective(product);
        PRODUCT_COUNT = product.getCount();

        OrderInfo order = new OrderInfo();
        // ... 省略 Set
//        orderMapper.insertSelective(order);
        System.out.println("当前用户" + userId + "成功创建商品" + purchaseProductId + "的订单");

        OrderProduct orderItem = new OrderProduct();
        orderItem.setOrderId(order.getId());
        // ... 省略 Set
        return order.getId();
    }


}