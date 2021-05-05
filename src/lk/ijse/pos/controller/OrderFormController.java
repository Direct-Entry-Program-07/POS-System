package lk.ijse.pos.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.pos.db.Database;
import lk.ijse.pos.model.Customer;
import lk.ijse.pos.model.Item;
import lk.ijse.pos.model.ItemDetails;
import lk.ijse.pos.model.Order;
import lk.ijse.pos.views.TM.CartTM;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class OrderFormController {

    public AnchorPane contextOfOrderForm;
    public Label txtTime;
    public Label txtDate;
    public ComboBox<String> cmbCustomerId;
    public TextField txtCustName;
    public TextField txtCustAddress;
    public TextField txtCustSalary;
    public TextField txtItemDes;
    public TextField txtQtyOnHand;
    public TextField txtUnitPrice;
    public Button btnRemoveItem;
    public Button btnAddToCartItem;
    public TextField txtQty;
    public ComboBox<String> cmbItemCode;
    public TableView tblCart;
    public TableColumn colItemCode;
    public TableColumn colDescription;
    public TableColumn colQuantity;
    public TableColumn calUnitPrice;
    public TableColumn colTotal;
    public Label txtTotal;
    public Label txtOId;

    public void initialize() {

        colItemCode.setCellValueFactory((new PropertyValueFactory<>("itemCode")));
        colDescription.setCellValueFactory((new PropertyValueFactory<>("description")));
        colQuantity.setCellValueFactory((new PropertyValueFactory<>("qty")));
        calUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colTotal.setCellValueFactory((new PropertyValueFactory<>("total")));

        setDateAndTime();
        loadAllCustomerIds();
        loaAllItemIds();

        //-----------------------------

        cmbCustomerId.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            setCutomerData(newValue);
        });

        cmbItemCode.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            setItemData(newValue);
        });

        tblCart.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            tempCartTM= (CartTM) newValue;
        });
        //-----------------------------
    }

    private void setItemData(String id) {
        for (Item i : Database.itemsList
        ) {
            if (id.equals(i.getId())) {
                txtItemDes.setText(i.getDescription());
                txtQtyOnHand.setText(String.valueOf(i.getQtyOnHand()));
                txtUnitPrice.setText(String.valueOf(i.getUnitPrice()));
                break;
            }
        }
    }

    private void loaAllItemIds() {
        ObservableList<String> itemObList = FXCollections.observableArrayList();
        for (Item i : Database.itemsList
        ) {
            itemObList.add(i.getId());
        }
        cmbItemCode.setItems(itemObList);
    }

    private void setCutomerData(String id) {
        for (Customer c : Database.customersList
        ) {
            if (id.equals(c.getId())) {
                txtCustName.setText(c.getName());
                txtCustAddress.setText(c.getAddress());
                txtCustSalary.setText(String.valueOf(c.getSalary()));
                break;
            }
        }
    }

    private void loadAllCustomerIds() {
        ObservableList<String> customerObList = FXCollections.observableArrayList();
        for (Customer c : Database.customersList
        ) {
            customerObList.add(c.getId());
        }
        cmbCustomerId.setItems(customerObList);
    }

    private void setDateAndTime() {
        Date date = new Date();
        SimpleDateFormat f = new SimpleDateFormat("YYYY-MM-dd");
        txtDate.setText(f.format(date));
        txtTime.setText(new SimpleDateFormat("HH:mm:ss").format(date));

    }

    public void backToHome(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) contextOfOrderForm.getScene().getWindow();
        stage.setScene(new Scene(
                FXMLLoader.load(getClass()
                        .getResource
                                ("../views/DashBoardForm.fxml"))));
    }

    ObservableList<CartTM> cartObList = FXCollections.observableArrayList();

    public void AddToCartOnAction(ActionEvent actionEvent) {

        if (Integer.parseInt(txtQty.getText()) <= Integer.parseInt(txtQtyOnHand.getText())) {

            int qty = Integer.parseInt(txtQty.getText());
            double unitPrice = Double.parseDouble(txtUnitPrice.getText());
            double total = qty * unitPrice;
            //String itemId = cmbItemCode.getValue().toString();

            CartTM tm = new CartTM(
                    cmbItemCode.getValue().toString(),
                    txtItemDes.getText(),
                    qty,
                    unitPrice,
                    total
            );

            boolean isExists = checkIsExists(tm);

            if (isExists) {
                tblCart.refresh();
            } else {
                cartObList.add(tm);
                calculateTotalCost();
                tblCart.setItems(cartObList);
            }

            /////////////////////////////// JUST A TRY ///////////////////////////
       /* if(!cartObList.isEmpty()){
            for (int i = 0; i < cartObList.size(); i++) {
                if (cartObList.get(i).getItemCode() == itemId){
                    System.out.println(tm);
                     cartObList.get(i).setQty(tm.getQty() + qty);

                }
                else {
                    System.out.println("False");

                }

            } tblCart.refresh();
        }else{
                cartObList.add(tm);
                tblCart.setItems(cartObList);
            }*/

            /////////////////////////////// END OF - JUST A TRY ///////////////////////////

        } else {

            new Alert(Alert.AlertType.WARNING, "Invalid Quantity").show();

        }

    }

    private boolean checkIsExists(CartTM tm) {
        int counter = 0;
        for (CartTM temp : cartObList) {
            if (temp.getItemCode().equals(tm.getItemCode())) {

                if (cartObList.get(counter).getQty() >= Integer.parseInt(txtQty.getText())+ cartObList.get(counter).getQty()){
                    cartObList.get(counter).setQty(tm.getQty() + temp.getQty());
                    cartObList.get(counter).setTotal(tm.getTotal() + temp.getTotal());
                    calculateTotalCost();
                    return true;
                }else{
                    new Alert(Alert.AlertType.CONFIRMATION, "Invalid QTY").show();
                    return true;
                }
            }
            counter++;
        }
        return false;
    }

    CartTM tempCartTM = null;

    public void btnRemoveAction(ActionEvent actionEvent) {
        if (tempCartTM!=null){
            for (CartTM tm: cartObList){
                if (tm.getItemCode().equals(tempCartTM.getItemCode())){
                    cartObList.remove(tm);
                    calculateTotalCost();
                    tblCart.refresh();
                }
            }
        }else {
            new Alert(Alert.AlertType.WARNING,"Please select a Row.").show();
        }

    }

    void calculateTotalCost(){
        double total = 0.00;

         for (CartTM tm: cartObList){

             total += tm.getTotal();
         }

         txtTotal.setText(total+"/=");
    }

    public void PlaceOrderOnAction(ActionEvent actionEvent) {
        ArrayList<ItemDetails> details = new ArrayList<>();
        for (CartTM tm:cartObList){
            details.add(new ItemDetails(
                    tm.getItemCode(),
                    tm.getQty(),
                    tm.getUnitPrice()
                )
            );
        }

        Order order = new Order(
                        txtOId.getText(),
                        txtDate.getText(),
                        cmbCustomerId.getValue(),
                        Double.parseDouble(txtTotal.getText().split("/=")[0]),
                        details
        );

        if (Database.orderList.add(order)){
            new Alert(Alert.AlertType.CONFIRMATION, "placed. ").show();
            cartObList.clear();
            calculateTotalCost();
        }
    }
}
