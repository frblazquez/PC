package pc.practice3.schema;

public class IntProduct implements Product {

    private int value;

    public IntProduct(int value) {
	this.value = value;
    }

    @Override
    public String getValueToString() {
	return "" + value;
    }
}
