package pc.practice4.part1.schema;

public interface Warehouse {

    public void store(Product product);

    public Product extract(int idx);

}
