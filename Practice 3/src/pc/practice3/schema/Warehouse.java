package pc.practice3.schema;

public interface Warehouse {

    /**
     * Stores a product in the Warehouse, if there is no space the process executing
     * this instruction gets blocked until it's freed.
     */
    public void store(Product product);

    /**
     * Extracts first available product. If there is no product available the
     * process executing this instruction gets blocked until a product is added.
     */
    public Product extract();

}
