package pc.practice3.schema;

public interface Warehouse {

    public void store(Product product);

    // Se accede por índice al producto
    public Product extract(int idx);

}
