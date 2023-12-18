import java.util.Scanner;

/**
 * @author tufujie
 * @date 2023/12/18
 */
public class ShardingGetMain {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("输入数据库名.表名.键值");
        String allValue = scanner.next();
        String[] arr = allValue.split("\\.");
        String dbName = arr[0], tableName = arr[1], shardingColumnValue = arr[2];
        long value2 = Long.valueOf(shardingColumnValue);
        long dbShard = value2 % 8;

        long value4 = Long.valueOf(shardingColumnValue);
        long tableShard = value4 / 8 % 64;
        System.out.println("所在库表如下");
        System.out.println(dbName + "_" + dbShard + "." + tableName + "_" + tableShard);
    }

}