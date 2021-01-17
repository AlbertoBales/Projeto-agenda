package com.alberto.pedro.william.controller;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import com.alberto.pedro.william.dao.ContatoDAO;
import com.alberto.pedro.william.entity.Contato;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MenuController implements Initializable {

	@FXML
	private AnchorPane pnlPrincipal;

	@FXML
	private SplitPane pnlDivisao;

	@FXML
	private AnchorPane pnlEsquerda;

	@FXML
	private TableView<Contato> tbvContatos;

	@FXML
	private TableColumn<Contato, Long> tbcCodigo;

	@FXML
	private TableColumn<Contato, String> tbcNome;

	@FXML
	private AnchorPane pnlDireita;

	@FXML
	private Label lblDetalhes;

	@FXML
	private GridPane pnlDetalhes;

	@FXML
	private Label lblNome;

	@FXML
	private Label lblNomeValor;

	@FXML
	private Label lblTelefone;

	@FXML
	private Label lblTelefoneValor;

	@FXML
	private Label lblEmail;

	@FXML
	private Label lblEmailValor;

	@FXML
	private ButtonBar barBotoes;

	@FXML
	private Button btnInclur;

	@FXML
	private Tooltip tlpIncluir;

	@FXML
	private Button btnEditar;

	@FXML
	private Tooltip tlpEditar;

	@FXML
	private Button btnExcluir;

	
	@FXML
	private Tooltip tlpExcluir;

	private List<Contato> listaContatos;
	private ObservableList<Contato> observableListaContatos = FXCollections.observableArrayList();
	private ContatoDAO contatoDAO;

	public static final String CONTATO_EDITAR = " - Editar";
	public static final String CONTATO_INCLUIR = " - Incluir";

	@FXML
	void onClickBtnEditar(ActionEvent event) {
		Contato contato = this.tbvContatos.getSelectionModel().getSelectedItem();

		if (contato != null) {
			boolean btnConfirmarClic = this.onShowTelaContatoEditar(contato, ContatoEditController.CONTATO_EDITAR);
			
			if (btnConfirmarClic) {
				this.getContatoDAO().update(contato, null);
				this.carregarTableViewContatos();
			}
			
			
		} else {
			Alert alerta = new Alert(Alert.AlertType.ERROR);
			alerta.setContentText("Por favor, escolha um contato na tabela!");
			alerta.show();
		}
				
	}

	@FXML
	void onClickBtnExcluir(ActionEvent event) {
		Contato contato = this.tbvContatos.getSelectionModel().getSelectedItem();

		if (contato != null) {

			Alert alerta = new Alert(AlertType.CONFIRMATION);
			alerta.setTitle("Pergunta");
			alerta.setHeaderText("Confirma a exclusao do contato?\n" + contato.getNome());

			ButtonType botaoNao = ButtonType.NO;
			ButtonType botaoSim = ButtonType.YES;
			alerta.getButtonTypes().setAll(botaoSim, botaoNao);
			Optional<ButtonType> resultado = alerta.showAndWait();

			if (resultado.get() == botaoSim) {
				this.getContatoDAO().delete(contato);
				this.carregarTableViewContatos();
			}
		} else {
			Alert alerta = new Alert(Alert.AlertType.ERROR);
			alerta.setContentText("Por favor, escolha um contato na tabela!");
			alerta.show();
		}
	}

	@FXML
	void onClickBtnIncluir(ActionEvent event) {
		Contato contato = new Contato();

		boolean btnConfirmarClic = this.onShowTelaContatoEditar(contato, ContatoEditController.CONTATO_INCLUIR);

		if (btnConfirmarClic) {
			this.getContatoDAO().save(contato);
			this.carregarTableViewContatos();
		}
		
		
		
	}

	public ContatoDAO getContatoDAO() {
		return contatoDAO;
	}

	public void setContatoDAO(ContatoDAO contatoDAO) {
		this.contatoDAO = contatoDAO;
	}

	public List<Contato> getListaContatos() {
		return listaContatos;
	}

	public void setListaContatos(List<Contato> listaContatos) {
		this.listaContatos = listaContatos;
	}

	public ObservableList<Contato> getObservableListaContatos() {
		return observableListaContatos;
	}

	public void setObservableListaContatos(ObservableList<Contato> observableListaContato) {
		this.observableListaContatos = observableListaContato;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.setContatoDAO(new ContatoDAO());
		this.carregarTableViewContatos();
		this.selecionarItemTableViewContatos(null);

		this.tbvContatos.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> selecionarItemTableViewContatos(newValue));
	}

	public void carregarTableViewContatos() {
		this.tbcCodigo.setCellValueFactory(new PropertyValueFactory<>("id"));
		this.tbcNome.setCellValueFactory(new PropertyValueFactory<>("nome"));

		this.setListaContatos(this.getContatoDAO().getAll());
		this.setObservableListaContatos(FXCollections.observableArrayList(this.getListaContatos()));
		this.tbvContatos.setItems(this.getObservableListaContatos());
	}

	public void selecionarItemTableViewContatos(Contato contato) {
		if (contato != null) {
			this. lblNomeValor.setText(contato.getNome());
			this. lblTelefoneValor.setText(contato.getTelefone());
			this. lblEmailValor.setText(contato.getEmail());

		} else {
			this.lblNomeValor.setText("");
			this.lblTelefoneValor.setText("");
			this.lblEmailValor.setText("");
		}
	}

	public boolean onCloseQuery() {
		Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
		alerta.setTitle("Pergunta");
		alerta.setHeaderText("Deseja sair do cadastro de contatos?");
		ButtonType buttonTypeNO = ButtonType.NO;
		ButtonType buttonTypeYES = ButtonType.YES;
		alerta.getButtonTypes().setAll(buttonTypeYES, buttonTypeNO);
		Optional<ButtonType> result = alerta.showAndWait();
		return result.get() == buttonTypeYES ? true : false;
	}

	public boolean onShowTelaContatoEditar(Contato contato, String operacao) {
		try {
			// carregando o loader
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/alberto/pedro/william/view/ContatoEdit.fxml"));
			Parent contatoEditXML = loader.load();

			// criando uma janela nova
			Stage janelaContatoEditar = new Stage();
			janelaContatoEditar.setTitle("Cadastro de contato" + operacao);
			janelaContatoEditar.initModality(Modality.APPLICATION_MODAL);
			janelaContatoEditar.resizableProperty().setValue(Boolean.FALSE);

			Scene contatoEditLayout = new Scene(contatoEditXML);
			janelaContatoEditar.setScene(contatoEditLayout);

			// carregando o controller e a scene
			ContatoEditController contatoEditController = loader.getController();
			contatoEditController.setJanelaContatoEdit(janelaContatoEditar);
			contatoEditController.populaTela(contato);

			// mostrando a tela
			janelaContatoEditar.showAndWait();

			return contatoEditController.isOkClick();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	public List<Contato> retornaListagemContato() {
		if (this.getContatoDAO() == null) {
			this.setContatoDAO(new ContatoDAO());
		}
		return this.getContatoDAO().getAll();
	}
}

