package controller;

import javafx.collections.ObservableList;
import javafx.fxml.Initializable;

import service.AbstractService;

import java.net.URL;
import java.util.ResourceBundle;

public class AbstractController<ID, E> implements Initializable {

    protected AbstractService<ID, E> service;
    protected ObservableList<E> model;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void setService(AbstractService<ID, E> service) {
        this.service = service;
    }
}
