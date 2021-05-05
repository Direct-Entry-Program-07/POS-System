package lk.ijse.pos.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.pos.db.Database;
import lk.ijse.pos.model.Customer;
import lk.ijse.pos.model.Item;
import lk.ijse.pos.views.TM.ItemTM;

import javax.xml.crypto.Data;
import java.io.IOException;

public class ItemFormController {
    public AnchorPane contextOfItemForm;
    public Button btnNewItem;
    public TextField txtItemCode;
    public TextField txtItemQty;
    public TextField txtItemPrice;
    public TextField txtItemDes;
    public Button btnSaveButton;
    public TextField txtSearch;
    public TableView<ItemTM> tblItem;
    public TableColumn colItemCode;
    public TableColumn colItemDes;
    public TableColumn colItemPrice;
    public TableColumn colItemQty;
    public TableColumn colItemOperate;

    ObservableList<ItemTM> obList = FXCollections.observableArrayList();

    public void initialize(){
        colItemCode.setCellValueFactory(new PropertyValueFactory<>("id"));
        colItemDes.setCellValueFactory(new PropertyValueFactory<>("description"));
        colItemQty.setCellValueFactory(new PropertyValueFactory<>("qtyOnHand"));
        colItemPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colItemOperate.setCellValueFactory(new PropertyValueFactory<>("btn"));

        loadAllItems("");

        //-----------------------------
        tblItem.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        setData(newValue);
                    }

                });
        //-----------------------------

    }

    public void setData(ItemTM value){
        txtItemCode.setText(value.getId());
        txtItemDes.setText(value.getDescription());
        txtItemQty.setText(String.valueOf(value.getQtyOnHand()));
        txtItemPrice.setText(String.valueOf(value.getUnitPrice()));

        btnSaveButton.setText("Update Item");

    }

    public void loadAllItems(String searchText){
        obList.clear();
        for (Item i:Database.itemsList
        ) {

            Button btn = new Button("Delete");

            if (i.getId().contains(searchText) || i.getDescription().contains(searchText)){
                obList.add(new ItemTM(i.getId(),i.getDescription(),i.getQtyOnHand(),i.getUnitPrice(), btn));

                btn.setOnAction(e->{
                    if (Database.itemsList.remove(i)){
                        new Alert(Alert.AlertType.CONFIRMATION, "Deleted").show();
                        loadAllItems("");
                    }else {
                        new Alert(Alert.AlertType.WARNING, "Try Again..").show();

                    }
                });
            }

        }

        tblItem.setItems(obList);
        /*ObservableList<ItemTM> observableList
                = FXCollections.observableArrayList();
        for (Item i: Database.itemsList){

            Button btn = new Button("Delete");

            observableList.add(new ItemTM(i.getId(), i.getDescription(), i.getQtyOnHand(), i.getUnitPrice(), btn));

            btn.setOnAction(e -> {
                if (Database.itemsList.remove(i)){
                    loadAllItems();
                }
            });
        }

        tblItem.setItems(observableList);*/
    }

    public void BackToHome(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) contextOfItemForm.
                getScene().getWindow();
        stage.setScene(new Scene(
                FXMLLoader.load(getClass()
                        .getResource
                                ("../views/DashBoardForm.fxml"))));
    }

    public void NewItemOnAction(ActionEvent actionEvent) {
        btnSaveButton.setText("Save Item");
        txtItemCode.clear();
        txtItemDes.clear();
        txtItemQty.clear();
        txtItemPrice.clear();

    }

    public void SaveItemOnAction(ActionEvent actionEvent) {

        Item item1 = new Item(
                txtItemCode.getText(),
                txtItemDes.getText(),
                Integer.parseInt(txtItemQty.getText()),
                Double.parseDouble(txtItemPrice.getText())
        );

        if(btnSaveButton.getText().equals("Save Item")){
            if (Database.itemsList.add(item1)){
                new Alert(Alert.AlertType.CONFIRMATION,"Saved", ButtonType.OK).show();
                loadAllItems("");
            }else {
                new Alert(Alert.AlertType.WARNING,"Try again...", ButtonType.CLOSE).show();
            }
        }else {
            int counter=0;
            for (Item i: Database.itemsList){
                if (txtItemCode.getText().equals(i.getId())){
                    Database.itemsList.get(counter).setDescription(txtItemDes.getText());
                    Database.itemsList.get(counter).setQtyOnHand(Integer.parseInt(txtItemQty.getText()));
                    Database.itemsList.get(counter).setUnitPrice(Double.parseDouble(txtItemPrice.getText()));
                    loadAllItems("");
                    break;
                }
            }

           /* for (int i = 0; i < Database.itemsList.size(); i++) {
                if (txtItemCode.getText().equals(Database.itemsList.get(i).getId())) {
                    Database.itemsList.remove(i);
                    if (Database.itemsList.add(item1)){
                        new Alert(Alert.AlertType.CONFIRMATION,
                                "Updated.", ButtonType.OK).show();
                        //loadAllItems();
                        break;
                    } else {
                        new Alert(Alert.AlertType.WARNING,
                                "Try Again.", ButtonType.CLOSE).show();
                    }
                    *//*break;*//*
                }

            }*/

        }
    }

    //String tempSearchText = "";
    public void Search(KeyEvent keyEvent) {

        //tempSearchText = tempSearchText + "" + keyEvent.getText();
        loadAllItems(txtSearch.getText());

        /*String searchText = "";
        searchText = txtSearch.getText();

        ObservableList<ItemTM> searchTm = FXCollections.observableArrayList();

        for (Item i1 : Database.itemsList
        ) {
            if (
                    i1.getId().contains(searchText) ||
                            i1.getDescription().contains(searchText)
                            //i1.getAddress().contains(searchText)
            ) {
                Button btn = new Button("Delete");
                searchTm.add(new ItemTM(
                        i1.getId(),
                        i1.getDescription(),
                        i1.getQtyOnHand(),
                        i1.getUnitPrice(), btn));
            }
        }
        tblItem.setItems(searchTm);*/
    }
}
