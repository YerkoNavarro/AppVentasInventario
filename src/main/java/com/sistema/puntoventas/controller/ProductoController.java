package com.sistema.puntoventas.controller;

import com.sistema.puntoventas.modelo.Categoria;
import com.sistema.puntoventas.modelo.Producto;
import com.sistema.puntoventas.modelo.TipoProducto;
import com.sistema.puntoventas.modelo.UnidadMedida;
import com.sistema.puntoventas.service.ProductoService;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;

import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.util.StringConverter;

public class ProductoController {

        @FXML
        private Button btnRegistrar;

        @FXML
        private Label lblEstado;

        @FXML
        private ComboBox<Categoria> cmbCategoria;

        @FXML
        private TextField txtFechaVenc;

        @FXML
        private TextField txtImagen;

        @FXML
        private TextField txtNombre;

        @FXML
        private TextField txtPrecioCompra;

        @FXML
        private TextField txtPrecioVenta;

        @FXML
        private TextField txtStockActual;

        @FXML
        private TextField txtStockMinimo;

        @FXML
        private ComboBox<UnidadMedida> cmbUnidadMedida;

        @FXML
        private ComboBox<TipoProducto> cmbTipoProducto;


        private ProductoService productoService;

        @FXML
        public void initialize() throws Exception {
            productoService = new ProductoService();
            cmbUnidadMedida.getItems().setAll(UnidadMedida.values());
            cmbTipoProducto.getItems().setAll(
                    TipoProducto.PLATILLO,
                    TipoProducto.DIRECTO,
                    TipoProducto.SOLO_INVENTARIO
            );
            cmbCategoria.getItems().setAll(productoService.obtenerCategorias());

            cmbUnidadMedida.setConverter(new StringConverter<>() {
                @Override
                public String toString(UnidadMedida unidadMedida) {
                    return formatearUnidadMedida(unidadMedida);
                }

                @Override
                public UnidadMedida fromString(String string) {
                    return null;
                }
            });
            cmbUnidadMedida.setButtonCell(new ListCell<>() {
                @Override
                protected void updateItem(UnidadMedida item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty ? null : formatearUnidadMedida(item));
                }
            });
            cmbUnidadMedida.setCellFactory(listView -> new ListCell<>() {
                @Override
                protected void updateItem(UnidadMedida item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty ? null : formatearUnidadMedida(item));
                }
            });

                cmbTipoProducto.setConverter(new StringConverter<>() {
                    @Override
                    public String toString(TipoProducto tipoProducto) {
                        return formatearTipoProducto(tipoProducto);
                    }

                    @Override
                    public TipoProducto fromString(String string) {
                        return null;
                    }
                });

                cmbCategoria.setConverter(new StringConverter<Categoria>() {
                    @Override
                    public String toString(Categoria categoria) {
                        return categoria != null ? categoria.getNombreCategoria() : "";
                    }

                    @Override
                    public Categoria fromString(String string) {
                        return null;
                    }
                });
        }

        @FXML
        void registrarProducto(ActionEvent event) {
            try{
                double precioCompra = Double.parseDouble(txtPrecioCompra.getText());
                double precioVenta = Double.parseDouble(txtPrecioVenta.getText());
                int stockActual = Integer.parseInt(txtStockActual.getText());
                int stockMinimo = Integer.parseInt(txtStockMinimo.getText());

                Producto nuevoProducto = new Producto();
                nuevoProducto.setNombre(txtNombre.getText());
                nuevoProducto.setPrecioCompra(precioCompra);
                nuevoProducto.setPrecioVenta(precioVenta);
                nuevoProducto.setStockActual(stockActual);
                nuevoProducto.setStockMinimo(stockMinimo);
                Categoria categoriaObj = new Categoria();
                categoriaObj.setNombreCategoria(cmbCategoria.getValue().getNombreCategoria());
                nuevoProducto.setCategoria(categoriaObj);
                nuevoProducto.setFechaVenc(txtFechaVenc.getText());
                nuevoProducto.setImagen(txtImagen.getText());
                nuevoProducto.setUnidadMedida(cmbUnidadMedida.getValue());
                nuevoProducto.setTipoProducto(cmbTipoProducto.getValue());



                //lo enviamos al service para hacer validaciones necesarias
                productoService.registrarProducto(nuevoProducto);
                lblEstado.setText("¡Producto registrado con éxito!");
                lblEstado.setTextFill(Color.GREEN); // Pintamos el texto de verde
                limpiarFormulario();

            } catch (NumberFormatException e) {
                lblEstado.setText("Error: Los precios y el stock deben ser números válidos.");
                lblEstado.setTextFill(Color.RED);
            } catch (Exception e) {

                lblEstado.setText(e.getMessage());
                lblEstado.setTextFill(Color.RED);
            }

        }

    private void limpiarFormulario() {
        txtNombre.clear();
        txtPrecioCompra.clear();
        txtPrecioVenta.clear();
        cmbCategoria.getSelectionModel().clearSelection();
        txtFechaVenc.clear();
        txtStockActual.clear();
        txtStockMinimo.clear();
        txtImagen.clear();
        cmbUnidadMedida.getSelectionModel().clearSelection();
        cmbTipoProducto.getSelectionModel().clearSelection();
    }

    private String formatearUnidadMedida(UnidadMedida unidadMedida) {
        if (unidadMedida == null) {
            return "";
        }

        String texto = unidadMedida.name().toLowerCase();
        return Character.toUpperCase(texto.charAt(0)) + texto.substring(1);
    }

    private String formatearTipoProducto(TipoProducto tipoProducto){
            if(tipoProducto == null){
                return "";
            }

            String texto = tipoProducto.name().toLowerCase();
            return Character.toUpperCase(texto.charAt(0)) + texto.substring(1);
    }



}
