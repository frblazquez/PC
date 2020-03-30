package pc.practice4.part2.schema;

import java.util.List;

public interface Warehouse {

    /**
     * Stores certain products in the Warehouse, if there is no space enough in the
     * Warehouse buffer the process executing this instruction gets blocked until
     * more space is available.
     */
    public void store(List<Product> product);

    /**
     * Extracts a certain number of products from the Warehouse. If there is not
     * enough products available the process executing this instruction gets blocked
     * until more products are added.
     */
    public List<Product> extract(int n);

}
