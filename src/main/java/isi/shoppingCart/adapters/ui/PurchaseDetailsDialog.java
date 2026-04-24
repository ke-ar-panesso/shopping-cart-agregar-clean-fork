package isi.shoppingCart.adapters.ui;

import isi.shoppingCart.entities.CartItem;
import isi.shoppingCart.entities.Purchase;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class PurchaseDetailsDialog {
    public static void show(Purchase purchase) {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Detalles de la Compra #" + purchase.getId());
        
        VBox content = new VBox(10);
        content.setPadding(new Insets(15));
        
        Label titleLabel = new Label("Compra #" + purchase.getId());
        titleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        
        VBox itemsBox = new VBox(5);
        double total = 0.0;
        
        for (CartItem item : purchase.getItems()) {
            HBox row = new HBox(10);
            
            Label nameLabel = new Label(item.getProduct().getName());
            nameLabel.setPrefWidth(200);
            
            Label quantityLabel = new Label("Cantidad: " + item.getQuantity());
            quantityLabel.setPrefWidth(100);
            
            Label subtotalLabel = new Label("Subtotal: $" + item.getSubtotal());
            subtotalLabel.setPrefWidth(120);
            
            row.getChildren().addAll(nameLabel, quantityLabel, subtotalLabel);
            row.setStyle("-fx-padding: 5; -fx-border-color: #DDDDDD; -fx-background-color: #F9F9F9;");
            
            itemsBox.getChildren().add(row);
            total += item.getSubtotal();
        }
        
        Label totalLabel = new Label("Total: $" + total);
        totalLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 10 0 0 0;");
        
        Button closeButton = new Button("Cerrar");
        closeButton.setOnAction(event -> dialog.close());
        closeButton.setPrefWidth(100);
        
        content.getChildren().addAll(titleLabel, itemsBox, totalLabel, closeButton);
        
        Scene scene = new Scene(content, 500, 400);
        dialog.setScene(scene);
        dialog.showAndWait();
    }
}
