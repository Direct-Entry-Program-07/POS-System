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
import lk.ijse.pos.views.Tm.CustomerTM;

import java.io.IOException;

public class CustomerFormController {
    public AnchorPane contextOfCustomerForm;
    public TextField txtCId;
    public TextField txtCName;
    public TextField txtCSalary;
    public TextField txtCAddress;
    public TableView<lk.ijse.pos.views.Tm.CustomerTM> tblCustomer;
    public TableColumn colCusId;
    public TableColumn colCusName;
    public TableColumn colCusAddress;
    public TableColumn colCusSalary;
    public TableColumn colCusOperate;
    public Button btnSave;
    public TextField txtSearch;

    public void initialize() {
        colCusId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colCusName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colCusAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colCusSalary.setCellValueFactory(new PropertyValueFactory<>("salary"));
        colCusOperate.setCellValueFactory(new PropertyValueFactory<>("btn"));
        loadAllCustomers();

        //-----------------------------
        tblCustomer.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        setData(newValue);
                    }

                });
        //-----------------------------


    }

    private void setData(CustomerTM value) {
        txtCId.setText(value.getId());
        txtCName.setText(value.getName());
        txtCAddress.setText(value.getAddress());
        /*txtCSalary.setText(value.getSalary()+"");*/
        txtCSalary.setText(String.valueOf(value.getSalary()));
        btnSave.setText("Update Customer");
    }

    private void loadAllCustomers() {
        ObservableList<CustomerTM> observableList
                = FXCollections.observableArrayList();
        for (Customer c : Database.customersList
        ) {

            Button btn = new Button("Delete");

            observableList.add(
                    new CustomerTM(c.getId(), c.getName(), c.getAddress(), c.getSalary(), btn)
            );

            btn.setOnAction(e -> {
                if (Database.customersList.remove(c)) {
                    loadAllCustomers();
                }
            });

        }
        tblCustomer.setItems(observableList);
    }

    public void backToHome(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) contextOfCustomerForm.
                getScene().getWindow();
        stage.setScene(new Scene(
                FXMLLoader.load(getClass()
                        .getResource
                                ("../views/DashBoardForm.fxml"))));
    }

    public void btnSaveOnAction(ActionEvent actionEvent) {
        Customer customer1 = new Customer(
                txtCId.getText(),
                txtCName.getText(),
                txtCAddress.getText(),
                Double.parseDouble(txtCSalary.getText())
        );

        if (btnSave.getText().equals("Save Customer")) {
            // save
            if (Database.customersList.add(customer1)) {
                new Alert(Alert.AlertType.CONFIRMATION,
                        "Done", ButtonType.OK).show();
                loadAllCustomers();
            } else {
                new Alert(Alert.AlertType.WARNING,
                        "Try Again.", ButtonType.CLOSE).show();
            }
        } else {

            for (int i = 0; i < Database.customersList.size(); i++) {
                if (txtCId.getText().equals(Database.customersList.get(i).getId())) {
                    Database.customersList.remove(i);
                    if (Database.customersList.add(customer1)) {
                        new Alert(Alert.AlertType.CONFIRMATION,
                                "Updated.", ButtonType.OK).show();
                        loadAllCustomers();
                        break;
                    } else {
                        new Alert(Alert.AlertType.WARNING,
                                "Try Again.", ButtonType.CLOSE).show();
                    }
                    /*break;*/
                }

            }

        }


    }

    public void newCustomerOnAction(ActionEvent actionEvent) {
        btnSave.setText("Save Customer");
        txtCId.clear();
        txtCName.clear();
        txtCAddress.clear();
        txtCSalary.clear();
    }

    public void search(KeyEvent keyEvent) {
       /* searchText=searchText+""+keyEvent.getText();
        System.out.println(searchText);*/
        String searchText = "";
        searchText = txtSearch.getText();

        ObservableList<CustomerTM> searchTm = FXCollections.observableArrayList();

        for (Customer c1 : Database.customersList
        ) {
            if (
                    c1.getId().contains(searchText) ||
                            c1.getName().contains(searchText) ||
                            c1.getAddress().contains(searchText)
            ) {
                Button btn = new Button("Delete");
                searchTm.add(new CustomerTM(
                        c1.getId(),
                        c1.getName(),
                        c1.getAddress(), c1.getSalary(), btn));
            }
        }
        tblCustomer.setItems(searchTm);

    }
}
