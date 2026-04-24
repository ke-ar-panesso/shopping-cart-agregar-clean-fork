package isi.shoppingCart.usecases.services;

import java.util.ArrayList;
import java.util.List;

import isi.shoppingCart.entities.Cart;
import isi.shoppingCart.entities.CartItem;
import isi.shoppingCart.entities.Product;
import isi.shoppingCart.entities.Purchase;
import isi.shoppingCart.usecases.ports.CartRepository;
import isi.shoppingCart.usecases.ports.ProductRepository;
import isi.shoppingCart.usecases.ports.PurchaseRepository;


public class ConfirmarCompraUseCase {
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final PurchaseRepository purchaseRepository;
    private int numCompra = 0;

    public ConfirmarCompraUseCase(CartRepository cartRepository, ProductRepository
        productRepository, PurchaseRepository purchaseRepository) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.purchaseRepository = purchaseRepository;
    }

    public String execute() {
        Cart currentCart = cartRepository.getCart(); 
        List<CartItem> itemsInCart = currentCart.getItems();

        //Validar que el carrito no este vacio
        if (currentCart.getItems().isEmpty()) {
            return "Error, carrito vacío";
        }

        //Verificar que la cantidad de productos en el carrito este disponible
        for (CartItem item : itemsInCart) {
            Product product = productRepository.findById(item.getProduct().getId());
            if (product.getAvailableQuantity() < item.getQuantity()) {
                return "Error, Producto " + product.getName() + " no cuenta con la cantidad solicitada.";
            }
        }

        //Crear copia de los items del carrito para la compra (antes de descontar inventario)
        List<CartItem> purchaseItems = new ArrayList<>();
        for (CartItem item : itemsInCart) {
            purchaseItems.add(new CartItem(item.getProduct(), item.getQuantity()));
        }

        //Generar persistencia de la compra
        Purchase newPurchase = new Purchase(numCompra, purchaseItems);
        purchaseRepository.recordPurchase(newPurchase);
        numCompra++;
        
        //Descontar cantidades del inventario
        for (CartItem item : itemsInCart) {
            Product product = productRepository.findById(item.getProduct().getId());
            int i = 0;
            do {
                product.decreaseAvailableQuantity();
                i++;
            } while (i < item.getQuantity());
        }

        //Limpiar carrito
        currentCart.clear();

        return "Compra realizada exitosamente";
    }
}
