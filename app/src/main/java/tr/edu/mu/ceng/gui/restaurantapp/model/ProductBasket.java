package tr.edu.mu.ceng.gui.restaurantapp.model;

public class ProductBasket {

    private int productId, piece;

    public ProductBasket(int productId, int piece) {
        this.productId = productId;
        this.piece = piece;
    }

    public ProductBasket() {
    }

    public ProductBasket(Object productId, Object piece) {
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getPiece() {
        return piece;
    }

    public void setPiece(int piece) {
        this.piece = piece;
    }
}
